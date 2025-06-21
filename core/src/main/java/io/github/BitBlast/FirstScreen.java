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
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirstScreen implements Screen {
    private final Main game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Player player;
    private String curLevel;

    private Texture backgroundTexture;
    private Texture groundTexture;

    private int inputIgnoreFrames = 10;

    public float baseY;

    private boolean redFlashActive = false;
    private float redFlashTimer = 0.7f;
    private BitmapFont font;

    public Map allColors = Map.of("BLUE", Color.BLUE, "YELLOW", Color.YELLOW,
        "GREEN", Color.GREEN, "ORANGE", Color.ORANGE,
        "RED", Color.RED, "WHITE", Color.WHITE,
        "BLACK", Color.BLACK, "PINK", new Color(252 ,19, 192, 1));

    public ArrayList<Spike> spikeList = new ArrayList<>();
    public ArrayList<Block> blockList = new ArrayList<>();
    public ArrayList<JumpPad> jumpPadList = new ArrayList<>();
    public ArrayList<Orb> orbList = new ArrayList<>();
    public ArrayList<Decoration> decorationList = new ArrayList<>();

    ArrayList<String> colors = new ArrayList<>();
    String[] levelColors;
    public String[] levelColorList;
    public int triggerInterval;
    private Color currentColor = new Color(Color.WHITE);
    private Color targetColor = new Color(Color.WHITE);
    private float transitionTimer = 0f;
    private float transitionDuration = 1f; // seconds
    private int colorIndex = 0;

    private boolean paused = false;

    private float accumulator = 0f;
    private static final float UPDATE_DELTA = 1f / 240f; // 60 updates per second

    public ArrayList<Key> curKeys;
    public ArrayList<Key> allKeys;

    public Vector3 deathCameraPosition = new Vector3();

    private int lastColorIndex = -1;

    public FirstScreen(Main game, OrthographicCamera camera, String curLevel) {
        this.game = game;
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.curLevel = curLevel;
        this.curKeys = new ArrayList();
        this.allKeys = new ArrayList();
    }

        FileHandle file = Gdx.files.internal(Constants.playerDataPath);
        JsonReader jsonReader = new JsonReader();
        JsonValue base = jsonReader.parse(file);

    public void saveKeys(){
        JsonValue keysArray = base.get(curLevel + "_keys");
        boolean[] json_keys = new boolean[3];

        for (int i = 0; i < keysArray.size; i++) {
            JsonValue keyObj = keysArray.get(i);
            json_keys[i] = keyObj.child().asBoolean();
        }

        for (int i = 0; i < keysArray.size; i++) {
            JsonValue keyObj = keysArray.get(i);
            JsonValue keyEntry = keyObj.child();

            boolean previouslyCollected = keyEntry.asBoolean();
            boolean collectedNow = curKeys.get(i) == null;

            keyEntry.set(Boolean.toString(previouslyCollected || collectedNow));
        }

        Json json = new Json();
        String newContent = json.prettyPrint(base);
        file.writeString(newContent, false);
    }

    public void generateLevel(String fileName) {
        JsonReader jsonReader = new JsonReader();
        JsonValue base = jsonReader.parse(Gdx.files.internal(fileName));

        JsonValue layout = base.get("layout");
        JsonValue colorsArray = base.get("colors");
        triggerInterval = base.get("color_trigger_interval").asInt();

        for (JsonValue color : colorsArray) {
            colors.add(color.asString());
        }
        levelColors = colors.toArray(new String[colors.size()]);
        currentColor = new Color((Color) allColors.get(levelColors[colorIndex])); // make a copy
        targetColor = new Color((Color) allColors.get(levelColors[colorIndex]));

        for (JsonValue item = layout.child; item != null; item = item.next) {
            String type = item.getString("type");
            float x = item.getFloat("x")*Constants.oneBlockHeight;
            float y = item.getFloat("y")*Constants.oneBlockHeight + Constants.startY;

            switch (type){
                case "spike":
                    JsonValue rotation = item.get("rotation");
                    if (rotation !=null) {
                        spikeList.add(new Spike(Constants.spike1SkinPath, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight, rotation.asFloat()));
                    }
                    else {
                        spikeList.add(new Spike(Constants.spike1SkinPath, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    }
                    break;
                case "halfspike":
                    rotation = item.get("rotation");
                    if (rotation != null) {
                        Spike spike = new Spike(Constants.spike2SkinPath, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight/2, rotation.asFloat());
                        spike.hitBox.height = spike.getHitBox().height/2;
                        spike.hitBox.y = spike.getHitBox().y - 15;
                        spikeList.add(spike);
                    }
                    else {
                        Spike spike = new Spike(Constants.spike2SkinPath, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight/2);
                        spike.hitBox.height = spike.getHitBox().height/2;
                        spike.hitBox.y = spike.getHitBox().y - 15;
                        spikeList.add(spike);
                    }
                    break;
                case "spikefloor":
                    Spike spike = new Spike(Constants.spike3SkinPath, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight);
                    spike.hitBox.width = Constants.oneBlockWidth;
                    spike.hitBox.height = spike.getHitBox().height/2;
                    spike.hitBox.x = x;
                    spike.hitBox.y = spike.getHitBox().y - 25;
                    spikeList.add(spike);
                    break;
                case "block":
                    blockList.add(new Block(Constants.blockSkinPath, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
                case "segmented-block1":
                    blockList.add(new Block(Constants.block1SkinPath, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
                case "segmented-block2":
                    blockList.add(new Block(Constants.block2SkinPath, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
                case "segmented-block3":
                    blockList.add(new Block(Constants.block3SkinPath, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
                case "segmented-block4":
                    blockList.add(new Block(Constants.block4SkinPath, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
                case "segmented-block5":
                    blockList.add(new Block(Constants.block5SkinPath, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
                case "segmented-block6":
                    blockList.add(new Block(Constants.block6SkinPath, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
                case "slab":
                    blockList.add(new Block(Constants.slabSkinPath, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight/2));
                    break;
                case "jumppad":
                    jumpPadList.add(new JumpPad(x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
                case "orb":
                    orbList.add(new Orb(x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
                case "chain":
                    decorationList.add(new Decoration(Constants.chainDecoPath, x, y, Constants.oneBlockWidth, 2*Constants.oneBlockHeight));
                    break;
                case "spikedeco1":
                    decorationList.add(new Decoration(Constants.spikeDeco1Path, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
                case "spikedeco2":
                    decorationList.add(new Decoration(Constants.spikeDeco2Path, x, y, 2*Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
                case "spikedeco3":
                    decorationList.add(new Decoration(Constants.spikeDeco3Path, x, y, 3*Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
                case "torch":
                    decorationList.add(new Decoration(Constants.torchDecoPath, x, y, Constants.oneBlockWidth, 2*Constants.oneBlockHeight));
                    break;
                case "key":
                    curKeys.add(new Key(x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    allKeys.add(new Key(x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
                    break;
            }
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (player == null) {
            this.player = new Player(Constants.playerSkin3Path, Constants.startX, Constants.startY);
        }
        baseY = Constants.startY;
        font = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        font.setColor(Color.WHITE);
        font.getData().setScale(3);

        backgroundTexture = new Texture(Constants.backdropPath); // Replace with your background texture path
        groundTexture = new Texture(Constants.groundPath);
        generateLevel("Sprites/UltimateDestruction.json");

        MusicManager.rewind();
        MusicManager.load(Constants.chaozAirflowPath, false);
        MusicManager.setVolume(1.0f);
        MusicManager.play();
    }

    private void updateBackgroundColor(float delta) {
        if (!player.isAlive()) return;

        transitionTimer += delta;

        float progress = Math.min(transitionTimer / transitionDuration, 1f);
        currentColor.lerp(targetColor, progress);

        if (progress >= 1f) {
            colorIndex = (colorIndex + 1) % levelColors.length;
            transitionTimer = 0f;

            targetColor.set((Color) allColors.get(levelColors[colorIndex]));

            currentColor.set(currentColor);
        }
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

        batch.setColor(currentColor);
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

        batch.setColor(currentColor);
        for (int x = startTileX; x <= endTileX; x++) {
            float drawX = x*groundWidth;
            batch.draw(groundTexture, drawX, groundBottom, groundWidth, totalGroundHeight);
        }
        batch.setColor(Color.WHITE);
    }

    public Color cycleColors(){
        int index = (int)((player.getX()/Constants.oneBlockWidth / triggerInterval) % levelColors.length);
        return (Color) allColors.get(levelColors[index]);
    }

    private void updateCurrentColor() {
        int currentIndex = (int)((player.getX() / Constants.oneBlockWidth) / triggerInterval) % levelColors.length;

        if (currentIndex != lastColorIndex) {
            lastColorIndex = currentIndex;
            currentColor = new Color((Color) allColors.get(levelColors[currentIndex]));
        }
    }

    @Override
    public void render(float delta) {
        if (player == null || backgroundTexture == null || groundTexture == null) return;

        if (delta > 0.25f) delta = 0.25f;
        accumulator += delta;

        while (accumulator >= UPDATE_DELTA) {
            if (!player.isAlive()) {
                deathCameraPosition.set(camera.position);
                die();
            }

            update(UPDATE_DELTA);
            checkForCollisions();

            accumulator -= UPDATE_DELTA;
        }
        /*
        if (paused && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { //я тут трохи похімічив Дмитре, і тепер коли я виходжу з рівня і заходжу назад зявляється лише екран паузи, сорі
            ScreenType screen;
            switch (curLevel){
                case "ud":
                    screen = ScreenType.LEVEL1_SELECT;
                    break;
                case "ed":
                    screen = ScreenType.LEVEL2_SELECT;
                    break;
                case "ca":
                    screen = ScreenType.LEVEL3_SELECT;
                    break;
                default:
                    screen = ScreenType.LEVEL1_SELECT;
            }
            ScreenManager.getInstance().setScreenWithFade(screen, FirstScreen.this, 1f);
        }
         */

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !paused) {
            paused = true;
            MusicManager.pause();
        } else if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && paused) {
            paused = false;
            MusicManager.play();
        }

        if (paused) {
            drawPauseScreen();
            return;
        }

        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(camera.combined);

        int startBlock = (int)(camera.position.x - camera.viewportWidth / 2) / Constants.oneBlockWidth;
        int endBlock = (int)(camera.position.x + camera.viewportWidth / 2) / Constants.oneBlockWidth;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (player.isAlive()) {
            updateCurrentColor();
            drawRepeatingBackground();
            drawRepeatingGround();
        }

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
        for (Decoration decoration : decorationList) {
            decoration.draw(batch);
        }
        for (Key key : curKeys) {
            if (key != null) {
                key.draw(batch);
            }
        }
        batch.end();

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
            (player.getX() + layout.width) / 2,
            (player.getY() + layout.height)
        );
        batch.end();
    }

    public void update(float delta) {
        batch.setProjectionMatrix(camera.combined);
        cameraUpdate();

        if (inputIgnoreFrames > 0) {
            inputIgnoreFrames--;
            return;
        }

        if (!redFlashActive && !paused) {
            player.update(delta, blockList, jumpPadList, orbList, curKeys);
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
        transitionTimer = 0f;
        colorIndex = 0;
        curKeys = new ArrayList(allKeys);
        currentColor = new Color((Color) allColors.get(levelColors[colorIndex]));
        targetColor = new Color((Color) allColors.get(levelColors[colorIndex]));

        MusicManager.rewind();
        MusicManager.play();
        MusicManager.setVolume(1.0f);
    }

    private void cameraUpdate() {
        if (paused) return;

        float playerX = player.getX();
        float playerY = player.getY();

        float thresholdX = camera.viewportWidth * 0.3f;
        float targetX = camera.viewportWidth / 2f;
        float targetY = camera.viewportHeight / 2f;

        // Target X
        if (playerX > thresholdX) {
            targetX = playerX - thresholdX + camera.viewportWidth / 2f;
        }

        // Target Y
        if (playerY > Constants.startY + Constants.cameraFollowThresholdY) {
            targetY = playerY - Constants.cameraFollowThresholdY + camera.viewportHeight / 2f;
        }

        float smoothingY = 0.02f; // vertical follows more slowly

        if (!player.isAlive()) {
            camera.position.set(deathCameraPosition);
        }
        else {
            camera.position.x = targetX;
            camera.position.y += (targetY - camera.position.y) * smoothingY;
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
