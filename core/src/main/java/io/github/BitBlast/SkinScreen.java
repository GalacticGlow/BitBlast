package io.github.BitBlast;

import Helper.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SkinScreen implements Screen {

    private final Main game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage stage;
    private Texture background;

    private BitmapFont font;

    private ImageButton skin1Button;
    private ImageButton skin2Button;
    private ImageButton skin3Button;
    private ImageButton skin4Button;
    private ImageButton skin5Button;
    private ImageButton skin6Button;
    private ImageButton skin7Button;

    private Texture skin1;
    private Texture skin2;
    private Texture skin3;
    private Texture skin4;
    private Texture skin5;
    private Texture skin6;
    private Texture skin7;
    private Texture selectedTexture;
    private Sprite selectedSprite;
    private Texture lockedTexture;
    private ImageButton locker2;
    private Sprite locker3;
    private Sprite locker4;
    private Sprite locker5;
    private Sprite locker6;
    private Sprite locker7;

    private boolean skin2Unlocked = false;
    private boolean skin3Unlocked = false;
    private boolean skin4Unlocked = false;
    private boolean skin5Unlocked = false;
    private boolean skin6Unlocked = false;
    private boolean skin7Unlocked = false;

    private final int SKIN_BUTTON_SIZE = 170;

    public int playerKeys = 0;

    Json json = new Json();
    FileHandle file = Gdx.files.local(Constants.playerDataPath);
    JsonReader jsonReader = new JsonReader();
    JsonValue base = jsonReader.parse(file);
    private int currentIcon = base.get("current_icon").asInt();

    public SkinScreen(Main game, OrthographicCamera camera) {
        this.camera = camera;
        this.game = game;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport());
    }

    public void loadKeys(){//Дмитре, використай цей метод щоб отримати кількість ключів які гравець має зараз, і в залежності скільки їх будуть доступні різні іконки. Використай те число куди треба
        JsonValue ud_keys = base.get("ud_keys");
        JsonValue ed_keys = base.get("ed_keys");
        JsonValue ca_keys = base.get("ca_keys");

        for (JsonValue obj : ud_keys) {
            for (JsonValue boolValue : obj) {
                if (boolValue.asBoolean()) {
                    playerKeys++;
                }
            }
        }

        for (JsonValue obj : ed_keys) {
            for (JsonValue boolValue : obj) {
                if (boolValue.asBoolean()) {
                    playerKeys++;
                }
            }
        }

        for (JsonValue obj : ca_keys) {
            for (JsonValue boolValue : obj) {
                if (boolValue.asBoolean()) {
                    playerKeys++;
                }
            }
        }
    }

    @Override
    public void show() {

        updateUnlockedSkins();

        Gdx.input.setInputProcessor(stage);
        background = new Texture(Gdx.files.internal(Constants.IconSelectorBackgroundPath));

        float gap = SKIN_BUTTON_SIZE * 0.55f;
        float totalWidth = SKIN_BUTTON_SIZE * 7 + gap * 6;
        float startX = (Gdx.graphics.getWidth() - totalWidth) / 2;
        float y = (float) ((Gdx.graphics.getHeight() / 2 - SKIN_BUTTON_SIZE / 2) / 1.15);

        selectedTexture = new Texture(Gdx.files.internal(Constants.selectedPath));
        selectedSprite = new Sprite(selectedTexture);
        selectedSprite.setSize(SKIN_BUTTON_SIZE + 40, SKIN_BUTTON_SIZE + 40);

        ImageButton.ImageButtonStyle skin1Style = new ImageButton.ImageButtonStyle();
        skin1 = new Texture(Gdx.files.internal(Constants.playerSkin1Path));
        skin1Style.up = new TextureRegionDrawable(new TextureRegion(skin1));
        skin1Style.down = new TextureRegionDrawable(new TextureRegion(skin1));
        skin1Button = new ImageButton(skin1Style);
        skin1Button.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
        skin1Button.setPosition(startX + (SKIN_BUTTON_SIZE + gap) * 0, y);
        skin1Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedSprite.setPosition(skin1Button.getX() - 20, skin1Button.getY() - 20);

                base.get("current_icon").set(String.valueOf(1));
                file.writeString(base.prettyPrint(JsonWriter.OutputType.json, 0), false);
                updateCurrentIcon(1);
                currentIcon = 1;
            }
        });

        ImageButton.ImageButtonStyle skin2Style = new ImageButton.ImageButtonStyle();
        skin2 = new Texture(Gdx.files.internal(Constants.playerSkin2Path));
        skin2Style.up = new TextureRegionDrawable(new TextureRegion(skin2));
        skin2Style.down = new TextureRegionDrawable(new TextureRegion(skin2));
        skin2Button = new ImageButton(skin2Style);
        skin2Button.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
        skin2Button.setPosition(startX + (SKIN_BUTTON_SIZE + gap) * 1, y);
        skin2Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedSprite.setPosition(skin2Button.getX() - 20, skin2Button.getY() - 20);

                base.get("current_icon").set(String.valueOf(2));
                file.writeString(base.prettyPrint(JsonWriter.OutputType.json, 0), false);
                updateCurrentIcon(2);
                currentIcon = 2;
            }
        });

        ImageButton.ImageButtonStyle skin3Style = new ImageButton.ImageButtonStyle();
        skin3 = new Texture(Gdx.files.internal(Constants.playerSkin3Path));
        skin3Style.up = new TextureRegionDrawable(new TextureRegion(skin3));
        skin3Style.down = new TextureRegionDrawable(new TextureRegion(skin3));
        skin3Button = new ImageButton(skin3Style);
        skin3Button.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
        skin3Button.setPosition(startX + (SKIN_BUTTON_SIZE + gap) * 2, y);
        skin3Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedSprite.setPosition(skin3Button.getX() - 20, skin3Button.getY() - 20);

                base.get("current_icon").set(String.valueOf(3));
                file.writeString(base.prettyPrint(JsonWriter.OutputType.json, 0), false);
                updateCurrentIcon(3);
                currentIcon = 3;
            }
        });

        ImageButton.ImageButtonStyle skin4Style = new ImageButton.ImageButtonStyle();
        skin4 = new Texture(Gdx.files.internal(Constants.playerSkin4Path));
        skin4Style.up = new TextureRegionDrawable(new TextureRegion(skin4));
        skin4Style.down = new TextureRegionDrawable(new TextureRegion(skin4));
        skin4Button = new ImageButton(skin4Style);
        skin4Button.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
        skin4Button.setPosition(startX + (SKIN_BUTTON_SIZE + gap) * 3, y);
        skin4Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedSprite.setPosition(skin4Button.getX() - 20, skin4Button.getY() - 20);

                base.get("current_icon").set(String.valueOf(4));
                file.writeString(base.prettyPrint(JsonWriter.OutputType.json, 0), false);
                updateCurrentIcon(4);
                currentIcon = 4;
            }
        });

        ImageButton.ImageButtonStyle skin5Style = new ImageButton.ImageButtonStyle();
        skin5 = new Texture(Gdx.files.internal(Constants.playerSkin5Path));
        skin5Style.up = new TextureRegionDrawable(new TextureRegion(skin5));
        skin5Style.down = new TextureRegionDrawable(new TextureRegion(skin5));
        skin5Button = new ImageButton(skin5Style);
        skin5Button.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
        skin5Button.setPosition(startX + (SKIN_BUTTON_SIZE + gap) * 4, y);
        skin5Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedSprite.setPosition(skin5Button.getX() - 20, skin5Button.getY() - 20);

                base.get("current_icon").set(String.valueOf(5));
                file.writeString(base.prettyPrint(JsonWriter.OutputType.json, 0), false);
                updateCurrentIcon(5);
                currentIcon = 5;
            }
        });

        ImageButton.ImageButtonStyle skin6Style = new ImageButton.ImageButtonStyle();
        skin6 = new Texture(Gdx.files.internal(Constants.playerSkin6Path));
        skin6Style.up = new TextureRegionDrawable(new TextureRegion(skin6));
        skin6Style.down = new TextureRegionDrawable(new TextureRegion(skin6));
        skin6Button = new ImageButton(skin6Style);
        skin6Button.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
        skin6Button.setPosition(startX + (SKIN_BUTTON_SIZE + gap) * 5, y);
        skin6Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedSprite.setPosition(skin6Button.getX() - 20, skin6Button.getY() - 20);

                base.get("current_icon").set(String.valueOf(6));
                file.writeString(base.prettyPrint(JsonWriter.OutputType.json, 0), false);
                updateCurrentIcon(6);
                currentIcon = 6;
            }
        });

        ImageButton.ImageButtonStyle skin7Style = new ImageButton.ImageButtonStyle();
        skin7 = new Texture(Gdx.files.internal(Constants.playerSkin7Path));
        skin7Style.up = new TextureRegionDrawable(new TextureRegion(skin7));
        skin7Style.down = new TextureRegionDrawable(new TextureRegion(skin7));
        skin7Button = new ImageButton(skin7Style);
        skin7Button.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
        skin7Button.setPosition(startX + (SKIN_BUTTON_SIZE + gap) * 6, y);
        skin7Button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                selectedSprite.setPosition(skin7Button.getX() - 20, skin7Button.getY() - 20);

                base.get("current_icon").set(String.valueOf(7));
                file.writeString(base.prettyPrint(JsonWriter.OutputType.json, 0), false);
                updateCurrentIcon(7);
                currentIcon = 7;
            }
        });

        updateCurrentIcon(currentIcon);

        stage.addActor(skin1Button);
        skin1Button.toBack();
        stage.addActor(skin2Button);
        skin2Button.toBack();
        stage.addActor(skin3Button);
        skin3Button.toBack();
        stage.addActor(skin4Button);
        skin4Button.toBack();
        stage.addActor(skin5Button);
        skin5Button.toBack();
        stage.addActor(skin6Button);
        skin6Button.toBack();
        stage.addActor(skin7Button);
        skin7Button.toBack();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80;
        parameter.color = com.badlogic.gdx.graphics.Color.WHITE;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();

        showSkinLockers();
    }

    private void showSkinLockers() {
        lockedTexture = new Texture(Constants.lockerPath);
        ImageButton.ImageButtonStyle lockerStyle = new ImageButton.ImageButtonStyle();
        lockerStyle.up = new TextureRegionDrawable(new TextureRegion(lockedTexture));
        lockerStyle.down = new TextureRegionDrawable(new TextureRegion(lockedTexture));

        if (!skin2Unlocked) {
            Button lockerButton = new Button(lockerStyle);
            lockerButton.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
            lockerButton.setPosition(skin2Button.getX(), skin2Button.getY());

            stage.addActor(lockerButton);
            lockerButton.toFront();
        }
        if (!skin3Unlocked) {
            Button lockerButton = new Button(lockerStyle);
            lockerButton.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
            lockerButton.setPosition(skin3Button.getX(), skin3Button.getY());

            stage.addActor(lockerButton);
            lockerButton.toFront();
        }
        if (!skin4Unlocked) {
            ImageButton lockerButton = new ImageButton(lockerStyle);
            lockerButton.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
            lockerButton.setPosition(skin4Button.getX(), skin4Button.getY());

            stage.addActor(lockerButton);
            lockerButton.toFront();
        }
        if (!skin5Unlocked) {
            ImageButton lockerButton = new ImageButton(lockerStyle);
            lockerButton.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
            lockerButton.setPosition(skin5Button.getX(), skin5Button.getY());

            stage.addActor(lockerButton);
            lockerButton.toFront();
        }
        if (!skin6Unlocked) {
            ImageButton lockerButton = new ImageButton(lockerStyle);
            lockerButton.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
            lockerButton.setPosition(skin6Button.getX(), skin6Button.getY());

            stage.addActor(lockerButton);
            lockerButton.toFront();
        }
        if (!skin7Unlocked) {
            ImageButton lockerButton = new ImageButton(lockerStyle);
            lockerButton.setSize(SKIN_BUTTON_SIZE, SKIN_BUTTON_SIZE);
            lockerButton.setPosition(skin7Button.getX(), skin7Button.getY());

            stage.addActor(lockerButton);
            lockerButton.toFront();
        }
    }

    private void updateUnlockedSkins() {
        loadKeys();
        skin2Unlocked = playerKeys >= 1;
        skin3Unlocked = playerKeys >= 2;
        skin4Unlocked = playerKeys >= 3;
        skin5Unlocked = playerKeys >= 4;
        skin6Unlocked = playerKeys >= 6;
        skin7Unlocked = playerKeys >= 9;
    }

    private void updateCurrentIcon(int currentIcon) {
        switch (currentIcon) {
            case 1:
                selectedSprite.setPosition(skin1Button.getX() - 20, skin1Button.getY() - 20);
                break;
            case 2:
                selectedSprite.setPosition(skin2Button.getX() - 20, skin2Button.getY() - 20);
                break;
            case 3:
                selectedSprite.setPosition(skin3Button.getX() - 20, skin3Button.getY() - 20);
                break;
            case 4:
                selectedSprite.setPosition(skin4Button.getX() - 20, skin4Button.getY() - 20);
                break;
            case 5:
                selectedSprite.setPosition(skin5Button.getX() - 20, skin5Button.getY() - 20);
                break;
            case 6:
                selectedSprite.setPosition(skin6Button.getX() - 20, skin6Button.getY() - 20);
                break;
            case 7:
                selectedSprite.setPosition(skin7Button.getX() - 20, skin7Button.getY() - 20);
                break;
            default:
                selectedSprite.setPosition(skin1Button.getX() - 20, skin1Button.getY() - 20);
                break;
        }
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
