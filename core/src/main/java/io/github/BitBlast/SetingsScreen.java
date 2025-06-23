package io.github.BitBlast;

import Helper.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SetingsScreen implements Screen {

    private final Main game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage stage;

    private Texture background;
    private Music music; // Або Sound, залежно від потреб
    private Skin skin;
    private Slider volumeSlider;

    private ImageButton secretButton;

    private BitmapFont font;

    public SetingsScreen(Main game, OrthographicCamera camera) {
        this.camera = camera;
        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        background = new Texture(Gdx.files.internal(Constants.backdropPath));
        Gdx.input.setInputProcessor(stage); // Додай це, якщо не було

        Skin skin = new Skin(Gdx.files.internal("uiskin.json")); // або свій шлях до skin

        // Створення повзунка
        if (volumeSlider == null) {
            volumeSlider = new Slider(0f, 1f, 0.01f, false, skin);
        }
        volumeSlider.setValue(MusicManager.getVolume()); // встановити поточне значення збереженої гучності
        volumeSlider.setSize(1400, 40);
        volumeSlider.setPosition(
            (Gdx.graphics.getWidth() - volumeSlider.getWidth()) / 2,
            Gdx.graphics.getHeight() / 3
        );

        // Обробка зміни значення
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MusicManager.setVolume(volumeSlider.getValue());
            }
        });

        stage.addActor(volumeSlider);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80;
        parameter.color = com.badlogic.gdx.graphics.Color.WHITE;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = new TextureRegionDrawable(new Texture("Sprites/Menu Objects/jojoSecretButton.png"));
        style.down = new TextureRegionDrawable(new Texture("Sprites/Menu Objects/jojoSecretButton.png"));
        secretButton = new ImageButton(style);
        secretButton.setSize(50, 50);
        secretButton.setPosition(
            Gdx.graphics.getWidth() - secretButton.getWidth(),
            0
        );
        secretButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                // TODO JOJOOOOOOOOOOOOOO
            }
        });

        stage.addActor(secretButton);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 4, 0, 1);

        batch.begin();

        batch.setColor(0.5f, 0.25f, 0.71f, 1.0f);
        for (int i = 0; i < 2; i++) {
            batch.draw(background, i * Gdx.graphics.getHeight(), 0,
                Gdx.graphics.getHeight(), Gdx.graphics.getHeight()
            );
        }

        GlyphLayout layout = new GlyphLayout(font, "Volume setting");
        font.draw(batch, layout,
                Gdx.graphics.getWidth() / 2 - layout.width/2, Gdx.graphics.getHeight() / 2 + layout.height
            );
        batch.setColor(Color.WHITE);

        batch.end();

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ScreenManager.getInstance().setScreenWithFade(ScreenType.MENU, this, 1.5f);
        }
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        background.dispose();
        stage.dispose();
        batch.dispose();
        skin.dispose();
        music.dispose();
    }
}
