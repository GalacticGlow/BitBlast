package io.github.BitBlast;

import Helper.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class StartScreen implements Screen {
    private final Main game;
    private OrthographicCamera camera;
    private Stage stage;
    private SpriteBatch batch;
    private BitmapFont fontLogo;
    private BitmapFont fontMini;
    private Texture logoTexture;
    private Sprite logoSprite;
    private Texture backgroundTexture;
    private TextureRegion backgroundTextureRegion;
    private BitmapFont font;

    private final float PLAY_BUTTON_SIZE = 300;
    private final float SECONDARY_BUTTON_SIZE = (float) (PLAY_BUTTON_SIZE * 0.7);
    private final int LOGO_WIGHT = 700;
    private final int LOGO_HEIGHT = 400;

    private float time = 0;

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

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.color = Color.WHITE;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();

        backgroundTexture = new Texture(Gdx.files.internal(Constants.backdropPath));

        Texture upTexturePlayButton = new Texture(Gdx.files.internal(Constants.playButtonPath));
        Texture downTexturePlayButton = new Texture(Gdx.files.internal(Constants.greyedPlayButtonPath));

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = new TextureRegionDrawable(new TextureRegion(upTexturePlayButton));
        style.down = new TextureRegionDrawable(new TextureRegion(downTexturePlayButton));

        Button startButton = new Button(style);
        startButton.setSize(PLAY_BUTTON_SIZE, PLAY_BUTTON_SIZE);
        startButton.setPosition(Gdx.graphics.getWidth() / 2 - PLAY_BUTTON_SIZE / 2,
            (float) (Gdx.graphics.getHeight() / 3.25 - PLAY_BUTTON_SIZE / 2));
        startButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreenWithFade(ScreenType.LEVEL1_SELECT, StartScreen.this, 1.5f);
            }
        });

        Texture upTextureCubeButton = new Texture(Gdx.files.internal(Constants.cubeButtonPath));
        Texture downTextureCubeButton = new Texture(Gdx.files.internal(Constants.greyedCubeButtonPath));

        Button.ButtonStyle cubeButtonStyle = new Button.ButtonStyle();
        cubeButtonStyle.up = new TextureRegionDrawable(new TextureRegion(upTextureCubeButton));
        cubeButtonStyle.down = new TextureRegionDrawable(new TextureRegion(downTextureCubeButton));

        Button cubeButton = new Button(cubeButtonStyle);
        cubeButton.setSize(SECONDARY_BUTTON_SIZE, SECONDARY_BUTTON_SIZE);
        cubeButton.setPosition((float) ((Gdx.graphics.getWidth() - PLAY_BUTTON_SIZE) / 3.66),
                                (float) (Gdx.graphics.getHeight() / 2.8 - PLAY_BUTTON_SIZE / 2));
        cubeButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreenWithFade(ScreenType.SKIN, StartScreen.this, 1.5f);
            }
        });

        Texture upTextureCogButton = new Texture(Gdx.files.internal(Constants.CogButtonPath));
        Texture downTextureCogButton = new Texture(Gdx.files.internal(Constants.greyedCogButtonPath));

        Button.ButtonStyle cogButtonStyle = new Button.ButtonStyle();
        cogButtonStyle.up = new TextureRegionDrawable(new TextureRegion(upTextureCogButton));
        cogButtonStyle.down = new TextureRegionDrawable(new TextureRegion(downTextureCogButton));

        Button cogButton = new Button(cogButtonStyle);
        cogButton.setSize(SECONDARY_BUTTON_SIZE, SECONDARY_BUTTON_SIZE);
        cogButton.setPosition((float) ((Gdx.graphics.getWidth() - PLAY_BUTTON_SIZE) / 1.28),
                              (float) (Gdx.graphics.getHeight() / 2.8 - PLAY_BUTTON_SIZE / 2));
        cogButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreenWithFade(ScreenType.SETTINGS, StartScreen.this, 1.5f);
            }
        });

        logoTexture = new Texture(Gdx.files.internal(Constants.logoPath));
        logoSprite = new Sprite(logoTexture);
        logoSprite.setSize(LOGO_WIGHT, LOGO_HEIGHT);
        logoSprite.setPosition(
            (Gdx.graphics.getWidth() - LOGO_WIGHT) / 2,
               (float) ((Gdx.graphics.getHeight() - LOGO_HEIGHT) / 1.1)
        );

        stage.addActor(startButton);
        stage.addActor(cubeButton);
        stage.addActor(cogButton);

        fontMini = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        fontMini.setColor(Color.WHITE);
        fontMini.getData().setScale(1);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 2, 5, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        time += v/3;
        float r = 0.5f + 0.5f * (float)Math.sin(time + 5);
        float g = 0.5f + 0.5f * (float)Math.sin(time + 4);
        float b = 0.5f + 0.5f * (float)Math.sin(time + 2);

        batch.begin();

        batch.setColor(r, g, b, 1.0f);
        for (int i = 0; i < 2; i++) {
            batch.draw(backgroundTexture, i * Gdx.graphics.getHeight(), 0,
                Gdx.graphics.getHeight(), Gdx.graphics.getHeight()
            );
        }
        batch.setColor(Color.WHITE);

        logoSprite.draw(batch);
        font.draw(batch, "By BitCrusaders", 5, 20);

        batch.end();

        stage.act(v);
        stage.draw();
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

    }

    @Override
    public void dispose() {
        //stage.dispose();
        logoTexture.dispose();
    }
}
