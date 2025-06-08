package io.github.BitBlast;

import Helper.Block;
import Helper.Constants;
import Helper.Player;
import Helper.Spike;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.Input;

import java.util.ArrayList;

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

    ArrayList<Spike> spikeList = new ArrayList<>();

    private boolean redFlashActive = false;
    private float redFlashTimer = 0.7f;
    private BitmapFont font;

    public ArrayList<Block> blockList = new ArrayList<>();

    private boolean jumpPressedLastFrame = false;

    public FirstScreen(OrthographicCamera camera) {
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        this.player = new Player(Constants.playerSkinPath, Constants.startX, Constants.startY);
        baseY = Constants.startY;
        font = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        font.setColor(Color.WHITE);
        font.getData().setScale(3);
    }

    @Override
    public void render(float delta) {
        Texture[] rainbowBlocks = {
            redBlock, orangeBlock, yellowBlock, greenBlock,
            blueBlock, indigoBlock, violetBlock
        };

        update(delta);
        checkForSpikeCollisions();
        //player.checkGroundCollision(blockList);

        Gdx.gl.glClearColor(255, 255, 255, 1);
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

        int spikeStartBlock = (int)Math.floor((camera.position.x - camera.viewportWidth / 2) / Constants.oneBlockWidth);
        int spikeEndBlock = (int)Math.ceil((camera.position.x + camera.viewportWidth / 2) / Constants.oneBlockWidth);

        blockList.clear();
        for (int i = spikeStartBlock; i <= spikeEndBlock; i++) {
            if ((i + 11) % 10 == 0) {
                float x = i * Constants.oneBlockWidth;
                float y = baseY;

                // You can adjust spike dimensions here
                float spikeWidth = Constants.oneBlockWidth;
                float spikeHeight = Constants.oneBlockHeight;

                // Check if this spike already exists at this position
                boolean exists = false;
                for (Spike s : spikeList) {
                    if (s.getX() == x && s.getY() == y) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    Spike spike = new Spike(Constants.spike1SkinPath, x, y, spikeWidth, spikeHeight);
                    spikeList.add(spike);
                }
            }
        }

        shapeRenderer.end();

        for (int i = spikeStartBlock; i < spikeEndBlock; i++) {
            if ((i + 8) % 10 == 0 || (i + 9) % 10 == 0) {
                float x = i * Constants.oneBlockWidth;
                blockList.add(new Block(Constants.blockSkinPath, x, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
            }
        }

        batch.begin();
        player.getSprite().draw(batch);
        for (Spike spike : spikeList) {
            spike.draw(batch);
        }
        for (Block block : blockList) {
            block.draw(batch);
        }
        batch.end();

        if (redFlashActive) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 0, 0, 1); // Red with full opacity
            shapeRenderer.rect(0, 0, camera.viewportWidth, camera.viewportHeight);
            shapeRenderer.end();

            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            font.setColor(Color.WHITE);
            GlyphLayout layout = new GlyphLayout(font, "YOU ARE GAY!");
            font.draw(batch, layout,
                (Gdx.graphics.getWidth() - layout.width) / 2,
                (Gdx.graphics.getHeight() + layout.height) / 2
            );
            batch.end();

            redFlashTimer -= delta;
            if (redFlashTimer <= 0) {
                redFlashActive = false;
            }
        }
    }

    public void update(float delta) {
        batch.setProjectionMatrix(camera.combined);
        cameraUpdate();
        if (!redFlashActive) {
            player.update(delta);
            boolean spacePressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);
            //player.handleJumpInput(spacePressed);
        }
    }

    public void checkForSpikeCollisions() {
        for (Spike spike : spikeList) {
            if (player.getHitBox().overlaps(spike.getHitBox())) {
                die();
            }
        }
    }

    public void die(){
        redFlashActive = true;
        redFlashTimer = 0.7f;
        player.updatePosition(Constants.startX, Constants.startY);
        player.onGround = true;
        player.curYSpeed = 0;
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
