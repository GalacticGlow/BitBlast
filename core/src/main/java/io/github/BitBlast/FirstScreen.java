package io.github.BitBlast;

import Helper.*;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;

public class FirstScreen implements Screen {

    //    private Main game;
    private final Main game;
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

    private Texture backgroundTexture;
    private Texture groundTexture;


    public float baseY;

    ArrayList<Spike> spikeList = new ArrayList<>();

    private boolean redFlashActive = false;
    private float redFlashTimer = 0.7f;
    private BitmapFont font;

    public ArrayList<Block> blockList = new ArrayList<>();
    public ArrayList<JumpPad> jumpPadList = new ArrayList<>();
    public ArrayList<Orb> orbList = new ArrayList<>();

    private boolean paused = false;

    public FirstScreen(Main game, OrthographicCamera camera) {
        this.game = game;
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
    }

    public void generateLevel(String fileName) {
        /*
        spikeList.add(new Spike(Constants.spike1SkinPath, 7*Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
        spikeList.add(new Spike(Constants.spike1SkinPath,  8*Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
        spikeList.add(new Spike(Constants.spike1SkinPath,  9*Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));

        for (int i = 20; i < 400; i += 30) {
            float x = i * Constants.oneBlockWidth;

            // Spikes
            spikeList.add(new Spike(Constants.spike1SkinPath, x - Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
            spikeList.add(new Spike(Constants.spike1SkinPath, x - 2 * Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));

            // Pillars
            blockList.add(new Block(Constants.blockSkinPath, x, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
            blockList.add(new Block(Constants.blockSkinPath, x + 3 * Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
            blockList.add(new Block(Constants.blockSkinPath, x + 3 * Constants.oneBlockHeight, Constants.startY + Constants.oneBlockHeight, Constants.oneBlockWidth, Constants.oneBlockHeight));
            blockList.add(new Block(Constants.blockSkinPath, x + 7 * Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
            blockList.add(new Block(Constants.blockSkinPath, x + 7 * Constants.oneBlockHeight, Constants.startY + Constants.oneBlockHeight, Constants.oneBlockWidth, Constants.oneBlockHeight));
            blockList.add(new Block(Constants.blockSkinPath, x + 7 * Constants.oneBlockHeight, Constants.startY + 2 * Constants.oneBlockHeight, Constants.oneBlockWidth, Constants.oneBlockHeight));

            jumpPadList.add(new JumpPad(x + 11 * Constants.oneBlockWidth, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));

            spikeList.add(new Spike(Constants.spike1SkinPath, x + 12 * Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
            spikeList.add(new Spike(Constants.spike1SkinPath, x + 13 * Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
            spikeList.add(new Spike(Constants.spike1SkinPath, x + 14 * Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
            spikeList.add(new Spike(Constants.spike1SkinPath, x + 15 * Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
            spikeList.add(new Spike(Constants.spike1SkinPath, x + 16 * Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
            spikeList.add(new Spike(Constants.spike1SkinPath, x + 17 * Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
            spikeList.add(new Spike(Constants.spike1SkinPath, x + 18 * Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
            spikeList.add(new Spike(Constants.spike1SkinPath, x + 19 * Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
            spikeList.add(new Spike(Constants.spike1SkinPath, x + 20 * Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
            spikeList.add(new Spike(Constants.spike1SkinPath, x + 21 * Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
            spikeList.add(new Spike(Constants.spike1SkinPath, x + 22 * Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));
            spikeList.add(new Spike(Constants.spike1SkinPath, x + 23 * Constants.oneBlockHeight, Constants.startY, Constants.oneBlockWidth, Constants.oneBlockHeight));

            orbList.add(new Orb(x + 16 * Constants.oneBlockWidth, Constants.startY + 2 * Constants.oneBlockWidth, Constants.oneBlockHeight, Constants.oneBlockHeight));
            orbList.add(new Orb(x + 20 * Constants.oneBlockWidth, Constants.startY + 2 * Constants.oneBlockWidth, Constants.oneBlockHeight, Constants.oneBlockHeight));
        }
        */
        JsonReader jsonReader = new JsonReader();
        JsonValue base = jsonReader.parse(Gdx.files.internal(fileName));

        JsonValue layout = base.get("layout");
        System.out.println("layout = " + layout);

        for (JsonValue item = layout.child; item != null; item = item.next) {
            String type = item.getString("type");
            float x = item.getFloat("x")*Constants.oneBlockHeight;
            float y = item.getFloat("y")*Constants.oneBlockHeight + Constants.startY;

            switch (type){
                case "spike":
                    spikeList.add(new Spike(Constants.spike1SkinPath, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
                case "block":
                    blockList.add(new Block(Constants.blockSkinPath, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
                case "jumppad":
                    jumpPadList.add(new JumpPad(x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
                case "orb":
                    orbList.add(new Orb(x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
            }
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (player == null) {
            this.player = new Player(Constants.playerSkinPath, Constants.startX, Constants.startY);
        }
        baseY = Constants.startY;
        font = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        font.setColor(Color.WHITE);
        font.getData().setScale(3);

        backgroundTexture = new Texture(Constants.backdropPath); // Replace with your background texture path
        groundTexture = new Texture(Constants.groundPath);
        generateLevel("Sprites/UltimateDestruction.json");
    }

    private void drawRepeatingBackground() {
        float bgWidth = 24 * Constants.oneBlockWidth;
        float bgHeight = 24 * Constants.oneBlockWidth;

        float cameraLeft = camera.position.x - camera.viewportWidth / 2;
        float cameraRight = camera.position.x + camera.viewportWidth / 2;
        float cameraTop = camera.position.y + camera.viewportHeight / 2;
        float cameraBottom = camera.position.y - camera.viewportHeight / 2;

        int startTileX = (int) Math.floor(cameraLeft / bgWidth);
        int endTileX = (int) Math.ceil(cameraRight / bgWidth);
        int startTileY = (int) Math.floor(cameraBottom / bgHeight);
        int endTileY = (int) Math.ceil(cameraTop / bgHeight);


        batch.setColor(0f, 0f, 1.0f, 1.0f);
        for (int x = startTileX; x <= endTileX; x++) {
            for (int y = startTileY; y <= endTileY; y++) {
                float drawX = x * bgWidth;
                float drawY = y * bgHeight;
                batch.draw(backgroundTexture, drawX, drawY, bgWidth, bgHeight);
            }
        }
        batch.setColor(Color.WHITE);
    }

    private void drawRepeatingGround() {
        float groundWidth = Constants.oneBlockWidth*3;

        float cameraLeft = camera.position.x - camera.viewportWidth / 2;
        float cameraRight = camera.position.x + camera.viewportWidth / 2;

        float groundTop = Constants.startY;
        float groundBottom = camera.position.y - camera.viewportHeight / 2;
        float totalGroundHeight = groundTop - groundBottom;

        int startTileX = (int) Math.floor(cameraLeft / groundWidth);
        int endTileX = (int) Math.ceil(cameraRight / groundWidth);


        batch.setColor(0f, 0f, 1.0f, 1.0f);
        for (int x = startTileX; x <= endTileX; x++) {
            float drawX = x*groundWidth;
            batch.draw(groundTexture, drawX, groundBottom, groundWidth, totalGroundHeight);
        }
        batch.setColor(Color.WHITE);
    }

    @Override
    public void render(float delta) {
        System.out.println("Game is starting!");
        if (player == null || backgroundTexture == null || groundTexture == null) return;
        Texture[] rainbowBlocks = {
            redBlock, orangeBlock, yellowBlock, greenBlock,
            blueBlock, indigoBlock, violetBlock
        };

        update(delta);
        checkForCollisions();
        if (!player.isAlive()){
            die();
        }

//        if (paused && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
//            game.setScreen(new StartScreen(game, camera));
//        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !paused) {
            paused = true;
        } else if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && paused) {
            paused = false;
        }

        if (paused) {
            drawPauseScreen();
            return;
        }


        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);

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

        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        drawRepeatingBackground();
        drawRepeatingGround();

        player.getSprite().draw(batch);
        for (Spike spike : spikeList) {
            spike.draw(batch);
        }
        for (Block block : blockList) {
            block.draw(batch);
        }
        for (JumpPad jumpPad : jumpPadList) {
            jumpPad.draw(batch);
        }
        for (Orb orb : orbList) {
            orb.draw(batch);
        }
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Spike spike : spikeList) {
            shapeRenderer.rect(spike.hitBoxData()[0], spike.hitBoxData()[1], spike.hitBoxData()[2], spike.hitBoxData()[3]);
        }
        shapeRenderer.end();

        if (redFlashActive) {
            redFlashTimer -= delta;

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 0, 0, 1); // Red with full opacity
            shapeRenderer.rect(0, 0, camera.viewportWidth, camera.viewportHeight);
            shapeRenderer.end();

            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            font.setColor(Color.WHITE);
            GlyphLayout layout = new GlyphLayout(font, "YOU ARE DEAD!");
            font.draw(batch, layout,
                (Gdx.graphics.getWidth() - layout.width) / 2,
                (Gdx.graphics.getHeight() + layout.height) / 2
            );
            batch.end();

            if (redFlashTimer <= 0) {
                redFlashActive = false;
                player.alive = true;
            }
        }
    }

    private void drawPauseScreen() {
        batch.begin();
        font.setColor(Color.BLACK);
        GlyphLayout layout = new GlyphLayout(font, "PAUSE");
        font.draw(batch, layout,
            (Gdx.graphics.getWidth() - layout.width) / 2,
            (Gdx.graphics.getHeight() + layout.height) / 2
        );
        batch.end();
    }

    public void update(float delta) {
        batch.setProjectionMatrix(camera.combined);
        cameraUpdate();



        if (!redFlashActive && !paused) {
            player.update(delta, blockList, jumpPadList, orbList);
        }
    }


    public void checkForCollisions() {
        for (Spike spike : spikeList) {
            if (player.getHitBox().overlaps(spike.getHitBox())) {
                player.alive = false;
            }
        }
    }

    public void die(){
        redFlashActive = true;
        redFlashTimer = 0.7f;
        player.updatePosition(Constants.startX, Constants.startY);
        player.onGround = true;
        player.curYSpeed = 0;
        player.alive = true;
        player.getSprite().setRotation(0);
    }

    private void cameraUpdate(){
        if(paused) {
            return;
        }

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
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (groundTexture != null) groundTexture.dispose();
    }
}
