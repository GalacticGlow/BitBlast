package io.github.BitBlast;

import Helper.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.awt.image.Kernel;

public class SetingsScreen implements Screen {

    private final Main game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage stage;

    private Texture background;

    public SetingsScreen(Main game, OrthographicCamera camera) {
        this.camera = camera;
        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage();
    }

    @Override
    public void show() {
        background = new Texture(Gdx.files.internal(Constants.backdropPath));
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 4, 0, 1);

        stage.act(v);
        stage.draw();

        batch.begin();
        batch.setColor(0f, 4.7f, 1.0f, 1.0f);
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(Color.WHITE);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ScreenManager.getInstance().setScreenWithFade(ScreenType.START, SetingsScreen.this, 1.5f);
        }
    }


    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
