package io.github.BitBlast;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class FadeTransitionScreen implements Screen {

    private final Game game;
    private final Screen fromScreen;
    private final Screen toScreen;

    private FrameBuffer fromFbo;
    private FrameBuffer toFbo;
    private Texture fromTexture;
    private Texture toTexture;

    private final SpriteBatch batch = new SpriteBatch();
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private final float duration;
    private float time = 0f;
    private boolean prepared = false;

    public FadeTransitionScreen(Game game, Screen fromScreen, Screen toScreen, float duration) {
        this.game = game;
        this.fromScreen = fromScreen;
        this.toScreen = toScreen;
        this.duration = duration;
    }

    private void prepareFromScreen() {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        fromScreen.show();
        fromScreen.resize(w, h);

        fromFbo = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, false);
        fromFbo.begin();
        fromScreen.render(0);
        fromFbo.end();
        fromTexture = fromFbo.getColorBufferTexture();

        fromScreen.hide(); // відключаємо input
    }

    private void prepareToScreen() {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        toScreen.show();
        toScreen.resize(w, h);

        toFbo = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, false);
        toFbo.begin();
        toScreen.render(0);
        toFbo.end();
        toTexture = toFbo.getColorBufferTexture();
    }


    @Override
    public void render(float delta) {
        if (!prepared) {
            prepareFromScreen();
            prepared = true;
        }

        time += delta;
        float halfDuration = duration / 2;
        float alpha;

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.setColor(Color.WHITE);

        if (time < halfDuration) {
            // Показуємо fromScreen fade-out
            batch.draw(fromTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1, 1);
            alpha = time / halfDuration;
        } else {
            // Після половини часу — готуємо toScreen (один раз)
            if (toTexture == null) {
                prepareToScreen();
            }

            // Показуємо toScreen fade-in
            batch.draw(toTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0, 0, 1, 1);
            alpha = 1f - ((time - halfDuration) / halfDuration);
        }

        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, MathUtils.clamp(alpha, 0, 1));
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        if (time >= duration) {
            game.setScreen(toScreen); // тепер безпосередньо активуємо екран
            dispose();
        }
    }


    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        if (fromFbo != null) {
            fromFbo.dispose();
        }
        if (toFbo != null) {
            toFbo.dispose();
        }
    }
}
