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

public class Level3SelectorScreen implements Screen {

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

    public Level3SelectorScreen(Main game, OrthographicCamera camera) {
        this.game = game;
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.stage = new Stage();
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
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


        // Clickable zone to start game
        clickableZone = new Rectangle(205, 390, 1515, 430); // x, y, width, height
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                float realY = Gdx.graphics.getHeight() - screenY;
                if (clickableZone.contains(screenX, realY)) {
                    System.out.println("Zone was clicked");
                    ScreenManager.getInstance().setScreenWithFade(ScreenType.FIRST_LEVEL, Level3SelectorScreen.this, 2f);
                }
                return false;
            }
        });

        Gdx.input.setInputProcessor(multiplexer);

        difficultFace = new Texture(Constants.insaneFacePath);
        checkmarkTexture = new Texture(Constants.checkmarkPath);
        checkmarkGreyedTexture = new Texture(Constants.checkmarkGreyedPath);
        keyTexture = new Texture(Constants.keyPath);
        keyGreyedTexture = new Texture(Constants.keyGreyedPath);

        checkmark = new Sprite(checkmarkGreyedTexture);
        key1 = new Sprite(keyGreyedTexture);
        key2 = new Sprite(keyGreyedTexture);
        key3 = new Sprite(keyGreyedTexture);

        int checkmarkSize = 120;
        checkmark.setSize(checkmarkSize, checkmarkSize);
        checkmark.setPosition(clickableZone.x + clickableZone.width - checkmarkSize-30, clickableZone.y + clickableZone.height - checkmarkSize-30);

        key1.setSize(100, 100);
        key2.setSize(100, 100);
        key3.setSize(100, 100);

        key1.setPosition(clickableZone.x + clickableZone.width - 380, clickableZone.y + 30);
        key2.setPosition(clickableZone.x + clickableZone.width - 250, clickableZone.y + 30);
        key3.setPosition(clickableZone.x + clickableZone.width - 120, clickableZone.y + 30);

        arrowTexture = new Texture(Constants.arrowPath);
        Button.ButtonStyle arrowLeftStyle = new Button.ButtonStyle();
        arrowLeftStyle.up = new TextureRegionDrawable(arrowTexture);
        arrowLeftStyle.down = new TextureRegionDrawable(arrowTexture);
        Button arrowLeft = new Button(arrowLeftStyle);
        arrowLeft.setSize(165, 300);
        arrowLeft.setPosition(clickableZone.x - 195, clickableZone.y + 70);
        arrowLeft.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getInstance().setScreenWithFade(ScreenType.LEVEL2_SELECT, Level3SelectorScreen.this, 1f);
            }
        });

        stage.addActor(arrowLeft);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ScreenManager.getInstance().setScreenWithFade(ScreenType.START, this, 1.5f);
        }

        batch.begin();
        batch.setColor(1f, 0.2f, 0.2f, 1f);
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(Color.WHITE);

        float difficultFaceX = clickableZone.x + 50;
        float difficultFaceY = (clickableZone.x + clickableZone.height) - Constants.difficultyFaceSize/2;
        batch.draw(difficultFace, difficultFaceX, difficultFaceY, Constants.difficultyFaceSize, Constants.difficultyFaceSize);
        GlyphLayout layout = new GlyphLayout(difficFont, "Insane");
        difficFont.draw(batch, layout, difficultFaceX - 30, difficultFaceY - 30);

        GlyphLayout layoutName = new GlyphLayout(nameFont, "Chaoz Airflow");
        nameFont.draw(batch, layoutName, difficultFaceX + 200, difficultFaceY + Constants.difficultyFaceSize/2 + layoutName.height/2);

        checkmark.draw(batch);
        key1.draw(batch);
        key2.draw(batch);
        key3.draw(batch);

        batch.end();

        stage.act(delta);
        stage.draw();

        // Малювання клікабельної зони як рамки (виділення)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(clickableZone.x, clickableZone.y, clickableZone.width, clickableZone.height);
        shapeRenderer.end();
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
        shapeRenderer.dispose();
    }
}

