package io.github.BitBlast;

import Helper.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class StartScreen implements Screen {
    private final Main game;
    private final OrthographicCamera camera;
    private Stage stage;
    private Texture upTexture;
    private Texture downTexture;
    private SpriteBatch batch;
    private BitmapFont fontLogo;
    private BitmapFont fontMini;

    private final int BUTTON_SIZE = 200;

    public StartScreen(Main game, OrthographicCamera camera) {
        this.game = game;
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.fontLogo = new BitmapFont();
        this.fontMini = new BitmapFont();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Texture upTexture = new Texture(Gdx.files.internal(Constants.jumpOrbPath));
        Texture downTexture = new Texture(Gdx.files.internal(Constants.blockSkinPath));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(upTexture));
        style.down = new TextureRegionDrawable(new TextureRegion(downTexture));

        Button startButton = new Button(style);
        startButton.setSize(BUTTON_SIZE, BUTTON_SIZE);
        startButton.setPosition(Gdx.graphics.getWidth() / 2 - BUTTON_SIZE / 2, Gdx.graphics.getHeight() / 2 - BUTTON_SIZE / 2);
        startButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Game is starting!");
                game.setScreen(new FirstScreen(camera));
            }
        });

        stage.addActor(startButton);

        fontLogo = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        fontLogo.setColor(Color.WHITE);
        fontLogo.getData().setScale(4);
        fontMini = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        fontMini.setColor(Color.WHITE);
        fontMini.getData().setScale(1);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 2, 5, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(v);
        stage.draw();

        batch.begin();
        fontLogo.setColor(Color.BLACK);
        GlyphLayout layout = new GlyphLayout(fontLogo, "BitBlast");
        fontLogo.draw(batch, layout,
            (Gdx.graphics.getWidth() - layout.width) / 2,
            (float) ((Gdx.graphics.getHeight() + layout.height) / 1.3)
        );
        GlyphLayout layout1 = new GlyphLayout(fontMini, "*Prototype");
        fontMini.draw(batch, layout1,
            15,
            (Gdx.graphics.getHeight() + layout1.height) / 15
        );
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
