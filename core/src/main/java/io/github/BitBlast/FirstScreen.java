package io.github.BitBlast;

import Helper.Constants;
import Helper.Player;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Input;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Player player;

    private Texture redBlock;
    private Texture orangeBlock;
    private Texture yellowBlock;
    private Texture greenBlock;
    private Texture blueBlock;
    private Texture indigoBlock;
    private Texture violetBlock;

    public float baseY;

    public FirstScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        this.player = new Player(Constants.playerSkinPath, Constants.startX, Constants.startY);
        baseY = Constants.startY;
    }

    @Override
    public void render(float delta) {
        Texture[] rainbowBlocks = {
            redBlock, orangeBlock, yellowBlock, greenBlock,
            blueBlock, indigoBlock, violetBlock
        };

        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        shapeRenderer.rect(this.player.getX(), this.player.getY(), this.player.getWidth(), this.player.getHeight());

        shapeRenderer.end();

        Color[] rainbowColors = {
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.GREEN,
            Color.BLUE,
            new Color(75 / 255f, 0, 130 / 255f, 1), // Indigo
            new Color(143 / 255f, 0, 255 / 255f, 1) // Violet
        };

        int startBlock = (int)(camera.position.x - camera.viewportWidth / 2) / Constants.oneBlockWidth;
        int endBlock = (int)(camera.position.x + camera.viewportWidth / 2) / Constants.oneBlockWidth;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = startBlock; i <= endBlock; i++) {
            Color color = rainbowColors[Math.floorMod(i, rainbowColors.length)];
            shapeRenderer.setColor(color);
            float x = i * Constants.oneBlockWidth;
            shapeRenderer.rect(x, 0, Constants.oneBlockWidth, Constants.oneBlockHeight);
        }

        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        int triangleSpacing = 3 * Constants.oneBlockWidth;

        float triangleWidth = Constants.oneBlockWidth;

        float triangleHeight = player.getHeight();

        int spikeStartBlock = (int)Math.floor((camera.position.x - camera.viewportWidth / 2) / Constants.oneBlockWidth);
        int spikeEndBlock = (int)Math.ceil((camera.position.x + camera.viewportWidth / 2) / Constants.oneBlockWidth);

        for (int i = spikeStartBlock; i <= spikeEndBlock; i ++) {
            if (i % 2 == 0) {// every 4 blocks
                float x = i * Constants.oneBlockWidth;
                float baseLeft = x;
                float baseRight = x + Constants.oneBlockWidth;
                float tipX = x + Constants.oneBlockWidth / 2f;
                float tipY = baseY + player.getHeight(); // Same height as player

                shapeRenderer.setColor(Color.RED);
                shapeRenderer.triangle(baseLeft, baseY, baseRight, baseY, tipX, tipY);
            }
        }

        shapeRenderer.end();

        batch.begin();
        player.getSprite().draw(batch);
        batch.end();
    }

    public void update(float delta) {
        batch.setProjectionMatrix(camera.combined);
        cameraUpdate();
        player.update(delta);
        boolean spacePressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        player.handleJumpInput(spacePressed);
    }

    private void cameraUpdate(){
        float playerX = player.getX();

        // Only move the camera if player has moved past the threshold
        float thresholdX = camera.viewportWidth / 3f;

        if (playerX > thresholdX) {
            camera.position.x = playerX - thresholdX + camera.viewportWidth / 2f;
        } else {
            camera.position.x = camera.viewportWidth / 2f;
        }

        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }
}
