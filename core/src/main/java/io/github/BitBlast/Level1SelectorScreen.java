package io.github.BitBlast;

import Helper.Constants;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Level1SelectorScreen implements Screen {

    private Main game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Stage stage;

    private Texture background;
    private Texture difficultFace;
    private Texture keyTexture;
    private Texture keyGreyedTexture;
    private Texture checkmarkTexture;
    private Texture checkmarkGreyedTexture;
    private Texture arrowTexture;

    private Sprite key1;
    private Sprite key2;
    private Sprite key3;
    private Sprite checkmark;

    private BitmapFont difficFont;
    private BitmapFont nameFont;

    private Rectangle clickableZone;
    private ShapeRenderer shapeRenderer;

    public boolean[] ud_keys;
    public boolean completed;

    public Level1SelectorScreen(Main game, OrthographicCamera camera) {
        this.game = game;
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport());
        this.shapeRenderer = new ShapeRenderer();
    }

    public void updateKeys(){
        JsonReader jsonReader = new JsonReader();
        JsonValue base = jsonReader.parse(Gdx.files.absolute(Constants.playerDataPath));

        JsonValue keysArray = base.get("ud_keys");
        ud_keys = new boolean[3];

        for (int i = 0; i < keysArray.size; i++) {
            JsonValue keyObj = keysArray.get(i);
            ud_keys[i] = keyObj.child().asBoolean();
        }
    }

    @Override
    public void show() {
        updateKeys();
        background = new Texture(Gdx.files.internal(Constants.levelSelectBackgroundPath));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        parameter.color = Color.WHITE;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.BLACK;
        difficFont = generator.generateFont(parameter);

        parameter.size = 55;
        parameter.color = Color.WHITE;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.BLACK;
        nameFont = generator.generateFont(parameter);
        generator.dispose();

        clickableZone = new Rectangle(
            (float) (Gdx.graphics.getWidth() / 9.5),
            (float) (Gdx.graphics.getHeight() / 2.6),
            (float) (Gdx.graphics.getWidth() / 1.267),
            (float) (Gdx.graphics.getHeight() / 2.37)); // x, y, width, height
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                float realY = Gdx.graphics.getHeight() - screenY;
                if (clickableZone.contains(screenX, realY)) {
                    System.out.println("Zone was clicked");
                    ScreenManager.getInstance().setScreenWithFadeLevel(ScreenType.FIRST_LEVEL, Level1SelectorScreen.this, 2f, "ud");
                }
                return false;
            }
        });

        Gdx.input.setInputProcessor(multiplexer);

        difficultFace = new Texture(Constants.hardFacePath);
        checkmarkTexture = new Texture(Constants.checkmarkPath);
        checkmarkGreyedTexture = new Texture(Constants.checkmarkGreyedPath);
        keyTexture = new Texture(Constants.keyPath);
        keyGreyedTexture = new Texture(Constants.keyGreyedPath);

        JsonReader jsonReader = new JsonReader();
        JsonValue base = jsonReader.parse(Gdx.files.absolute(System.getProperty("user.dir") + "\\assets\\Sprites\\UltimateDestruction.json"));
        System.out.println(base);
        completed = base.get("completed").asBoolean();

        System.out.println(completed);

        checkmark = new Sprite(completed ? checkmarkTexture : checkmarkGreyedTexture);
        key1 = new Sprite(ud_keys[0] ? keyTexture : keyGreyedTexture);
        key2 = new Sprite(ud_keys[1] ? keyTexture : keyGreyedTexture);
        key3 = new Sprite(ud_keys[2] ? keyTexture : keyGreyedTexture);

        int checkmarkSize = 120;
        checkmark.setSize(checkmarkSize, checkmarkSize);
        checkmark.setPosition(clickableZone.x + clickableZone.width - checkmarkSize-30, clickableZone.y + clickableZone.height - checkmarkSize-30);

        key1.setSize(100, 100);
        key2.setSize(100, 100);
        key3.setSize(100, 100);

        key1.setPosition(clickableZone.x + clickableZone.width - 380, clickableZone.y + 30);
        key2.setPosition(clickableZone.x + clickableZone.width - 250, clickableZone.y + 30);
        key3.setPosition(clickableZone.x + clickableZone.width - 120, clickableZone.y + 30);

        Texture arrowTexture = new Texture(Constants.arrowPath);
        TextureRegion mirroredTexture = new TextureRegion(arrowTexture);
        mirroredTexture.flip(true, false);
        Button.ButtonStyle arrowRightStyle = new Button.ButtonStyle();
        arrowRightStyle.up = new TextureRegionDrawable(mirroredTexture);
        arrowRightStyle.down = new TextureRegionDrawable(mirroredTexture);
        Button arrowRight = new Button(arrowRightStyle);
        arrowRight.setSize(165, 300);
        arrowRight.setPosition(clickableZone.x + clickableZone.width + 30, clickableZone.y + 70);
        arrowRight.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreenWithFade(ScreenType.LEVEL2_SELECT, Level1SelectorScreen.this, 0.5f);
            }
        });
        stage.addActor(arrowRight);

        MusicManager.load(MusicManager.jojoMusicEnabled ? Constants.jojoUDMenuMusicPath : Constants.ultimateDestructionPath, true);
        MusicManager.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ScreenManager.getInstance().setScreenWithFade(ScreenType.MENU, this, 1f);
        }

        batch.begin();
        batch.setColor(0.14f, 0.64f, 1f, 1f);
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(Color.WHITE);

        float difficultFaceX = clickableZone.x + 50;
        float difficultFaceY = (clickableZone.x + clickableZone.height) - Constants.difficultyFaceSize/2;
        batch.draw(difficultFace, difficultFaceX, difficultFaceY, Constants.difficultyFaceSize, Constants.difficultyFaceSize);
        GlyphLayout layout = new GlyphLayout(difficFont, "Hard");
        difficFont.draw(batch, layout, difficultFaceX, difficultFaceY - 30);

        GlyphLayout layoutName = new GlyphLayout(nameFont, "Ultimate Destruction");
        nameFont.draw(batch, layoutName, difficultFaceX + 200, difficultFaceY + Constants.difficultyFaceSize/2 + layoutName.height/2);

        checkmark.draw(batch);
        key1.draw(batch);
        key2.draw(batch);
        key3.draw(batch);

        batch.end();

        stage.act(delta);
        stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            ScreenManager.getInstance().setScreenWithFade(ScreenType.LEVEL2_SELECT, this, 0.5f);
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        background.dispose();
        batch.dispose();
    }
}
