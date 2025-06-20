package io.github.BitBlast;

import Helper.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SkinScreen implements Screen {

    private final Main game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage stage;
    private Texture background;

    private BitmapFont font;

    private Texture skin1;
    private Texture skin2;
    private Texture skin3;
    private Texture skin4;
    private Texture skin5;
    private Texture skin6;
    private Texture skin7;
    private Texture selectedTexture;
    private Sprite selectedSprite;

    private final int SKIN_BUTTON_SIZE = 170;

    public int playerKeys = 0;

    public static boolean skin1Unlocked = true;
    public static boolean skin2Unlocked = false;
    public static boolean skin3Unlocked = false;
    public static boolean skin4Unlocked = false;
    public static boolean skin5Unlocked = false;
    public static boolean skin6Unlocked = false;
    public static boolean skin7Unlocked = false;

    public SkinScreen(Main game, OrthographicCamera camera) {
        this.camera = camera;
        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport());
    }

    public void loadKeys(){//Дмитре, використай цей метод щоб отримати кількість ключів які гравець має зараз, і в залежності скільки їх будуть доступні різні іконки. Використай те число куди треба
        JsonReader jsonReader = new JsonReader();
        JsonValue base = jsonReader.parse(Gdx.files.internal(Constants.playerDataPath));

        boolean[] ud_keys = base.get("ud_keys").asBooleanArray();
        boolean[] ed_keys = base.get("ed_keys").asBooleanArray();
        boolean[] ca_keys = base.get("ca_keys").asBooleanArray();

        for (boolean ud_key : ud_keys) {
            if (ud_key) {
                playerKeys++;
            }
        }

        for (boolean ed_key : ed_keys) {
            if (ed_key) {
                playerKeys++;
            }
        }

        for (boolean ca_key : ca_keys) {
            if (ca_key) {
                playerKeys++;
            }
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        background = new Texture(Gdx.files.internal(Constants.IconSelectorBackgroundPath));

        float gap = SKIN_BUTTON_SIZE * 0.55f;
        float totalWidth = SKIN_BUTTON_SIZE * 7 + gap * 6;
        float startX = (Gdx.graphics.getWidth() - totalWidth) / 2;
        float y = (float) ((Gdx.graphics.getHeight() / 2 - SKIN_BUTTON_SIZE / 2) / 1.15);

        selectedTexture = new Texture(Gdx.files.internal(Constants.selectedPath));
        selectedSprite = new Sprite(selectedTexture);
        selectedSprite.setSize(SKIN_BUTTON_SIZE + 40, SKIN_BUTTON_SIZE + 40);

        Button.ButtonStyle skin1Style = new Button.ButtonStyle();
        skin1 = new Texture(Gdx.files.internal(Constants.playerSkin1Path));
        skin1Style.up = new TextureRegionDrawable(new TextureRegion(skin1));
        skin1Style.down = new TextureRegionDrawable(new TextureRegion(skin1));
        Button skin1Button = new Button(skin1Style);
        skin1Button.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
        skin1Button.setPosition(startX + (SKIN_BUTTON_SIZE + gap) * 0, y);
        skin1Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedSprite.setPosition(skin1Button.getX() - 20, skin1Button.getY() - 20);
            }
        });

        Button.ButtonStyle skin2Style = new Button.ButtonStyle();
        skin2 = new Texture(Gdx.files.internal(Constants.playerSkin2Path));
        skin2Style.up = new TextureRegionDrawable(new TextureRegion(skin2));
        skin2Style.down = new TextureRegionDrawable(new TextureRegion(skin2));
        Button skin2Button = new Button(skin2Style);
        skin2Button.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
        skin2Button.setPosition(startX + (SKIN_BUTTON_SIZE + gap) * 1, y);
        skin2Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedSprite.setPosition(skin2Button.getX() - 20, skin2Button.getY() - 20);
            }
        });

        Button.ButtonStyle skin3Style = new Button.ButtonStyle();
        skin3 = new Texture(Gdx.files.internal(Constants.playerSkin3Path));
        skin3Style.up = new TextureRegionDrawable(new TextureRegion(skin3));
        skin3Style.down = new TextureRegionDrawable(new TextureRegion(skin3));
        Button skin3Button = new Button(skin3Style);
        skin3Button.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
        skin3Button.setPosition(startX + (SKIN_BUTTON_SIZE + gap) * 2, y);
        skin3Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedSprite.setPosition(skin3Button.getX() - 20, skin3Button.getY() - 20);
            }
        });

        Button.ButtonStyle skin4Style = new Button.ButtonStyle();
        skin4 = new Texture(Gdx.files.internal(Constants.playerSkin4Path));
        skin4Style.up = new TextureRegionDrawable(new TextureRegion(skin4));
        skin4Style.down = new TextureRegionDrawable(new TextureRegion(skin4));
        Button skin4Button = new Button(skin4Style);
        skin4Button.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
        skin4Button.setPosition(startX + (SKIN_BUTTON_SIZE + gap) * 3, y);
        skin4Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedSprite.setPosition(skin4Button.getX() - 20, skin4Button.getY() - 20);
            }
        });

        Button.ButtonStyle skin5Style = new Button.ButtonStyle();
        skin5 = new Texture(Gdx.files.internal(Constants.playerSkin5Path));
        skin5Style.up = new TextureRegionDrawable(new TextureRegion(skin5));
        skin5Style.down = new TextureRegionDrawable(new TextureRegion(skin5));
        Button skin5Button = new Button(skin5Style);
        skin5Button.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
        skin5Button.setPosition(startX + (SKIN_BUTTON_SIZE + gap) * 4, y);
        skin5Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedSprite.setPosition(skin5Button.getX() - 20, skin5Button.getY() - 20);
            }
        });

        Button.ButtonStyle skin6Style = new Button.ButtonStyle();
        skin6 = new Texture(Gdx.files.internal(Constants.playerSkin6Path));
        skin6Style.up = new TextureRegionDrawable(new TextureRegion(skin6));
        skin6Style.down = new TextureRegionDrawable(new TextureRegion(skin6));
        Button skin6Button = new Button(skin6Style);
        skin6Button.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
        skin6Button.setPosition(startX + (SKIN_BUTTON_SIZE + gap) * 5, y);
        skin6Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedSprite.setPosition(skin6Button.getX() - 20, skin6Button.getY() - 20);
            }
        });

        Button.ButtonStyle skin7Style = new Button.ButtonStyle();
        skin7 = new Texture(Gdx.files.internal(Constants.playerSkin7Path));
        skin7Style.up = new TextureRegionDrawable(new TextureRegion(skin7));
        skin7Style.down = new TextureRegionDrawable(new TextureRegion(skin7));
        Button skin7Button = new Button(skin7Style);
        skin7Button.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
        skin7Button.setPosition(startX + (SKIN_BUTTON_SIZE + gap) * 6, y);
        skin7Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedSprite.setPosition(skin7Button.getX() - 20, skin7Button.getY() - 20);
            }
        });
        selectedSprite.setPosition(skin1Button.getX() - 20, skin1Button.getY() - 20);

        stage.addActor(skin1Button);
        stage.addActor(skin2Button);
        stage.addActor(skin3Button);
        stage.addActor(skin4Button);
        stage.addActor(skin5Button);
        stage.addActor(skin6Button);
        stage.addActor(skin7Button);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80;
        parameter.color = com.badlogic.gdx.graphics.Color.WHITE;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 1f, 0, 1f);

        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        selectedSprite.draw(batch);

        GlyphLayout layout = new GlyphLayout(font, "Choose your character!");
        font.draw(batch, layout, (Gdx.graphics.getWidth() - layout.width) / 2f, Gdx.graphics.getHeight() * 3f / 4f);

        batch.end();

        stage.act(v);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ScreenManager.getInstance().setScreenWithFade(ScreenType.MENU, this, 1.5f);
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
        background.dispose();
        stage.dispose();
        batch.dispose();
    }
}
