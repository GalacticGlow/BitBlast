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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.math.Vector3;

import java.io.*;
import java.math.BigDecimal;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.util.List;
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

    private int inputIgnoreFrames = 0;

    private int musicIgnoreFrames = 15;
    private boolean musicStarted = false;

    public float baseY;

    private boolean redFlashActive = false;
    private float redFlashTimer = 0.7f;
    private BitmapFont font;
    private BitmapFont fontPercentage;

    public Map allColors = Map.of("BLUE", Color.BLUE, "YELLOW", Color.YELLOW,
        "GREEN", Color.GREEN, "ORANGE", Color.ORANGE,
        "RED", Color.RED, "WHITE", Color.WHITE,
        "BLACK", Color.BLACK, "MAGENTA", Color.MAGENTA, "PINK", Color.PINK, "PURPLE", Color.PURPLE);

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

    private Stage victoryWindowStage;
    private boolean showVictoryWindow = false;
    private Image victoryWindow;
    private BitmapFont levelCompleteLabel;
    private BitmapFont pauseLabelFont;
    private Texture keyTexture;
    private Texture keyGreyedTexture;
    private Image key1Complete;
    private Image key2Complete;
    private Image key3Complete;
    private boolean[] json_keys;

    private boolean levelGenerated = false;
    private boolean savedComplete = false;

    public float maxX = 0;
    public float curPercentage = 0f;

    public FirstScreen(Main game, OrthographicCamera camera, String curLevel) {
        this.game = game;
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.curLevel = curLevel;
        this.curKeys = new ArrayList();
        this.allKeys = new ArrayList();
    }

    public void saveKeysWithGson() throws Exception {
        try {
            System.out.println(curLevel);
            System.out.println(curKeys);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // 1. Read existing JSON file as JsonObject
            Reader reader = new FileReader(Constants.playerDataPath);
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            // 2. Get the array for the current level's keys, e.g., "ed_keys"
            String keysKey = curLevel + "_keys";  // e.g., "ed_keys"
            JsonArray keysArray = root.getAsJsonArray(keysKey);

            // 3. Update the keys' boolean values based on collected state
            for (int i = 0; i < keysArray.size(); i++) {
                JsonObject keyObj = keysArray.get(i).getAsJsonObject();

                // There should be exactly one key per object, so get the key name and value
                for (Map.Entry<String, JsonElement> entry : keyObj.entrySet()) {
                    boolean previouslyCollected = entry.getValue().getAsBoolean();
                    boolean collectedNow = curKeys.get(i) == null;

                    System.out.println("KEY #" + i + " (" + entry.getKey() + "): previously=" + previouslyCollected + ", now=" + collectedNow);

                    // Update the value to true if previously or now collected
                    keyObj.addProperty(entry.getKey(), previouslyCollected || collectedNow);
                }
            }

            // 4. Write the updated JSON back to file (overwrite)
            Writer writer = new FileWriter(Constants.playerDataPath);
            gson.toJson(root, writer);
            writer.flush();
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateLevel(String fileName) {
        JsonReader jsonReader = new JsonReader();
        JsonValue base = jsonReader.parse(Gdx.files.absolute(fileName));

        JsonValue layout = base.get("layout");
        JsonValue colorsArray = base.get("colors");
        triggerInterval = base.get("color_trigger_interval").asInt();

        for (JsonValue color : colorsArray) {
            colors.add(color.asString());
        }
        levelColors = colors.toArray(new String[colors.size()]);
        currentColor = new Color((Color) allColors.get(levelColors[colorIndex])); // make a copy
        targetColor = new Color((Color) allColors.get(levelColors[colorIndex]));

        maxX = 0;

        for (JsonValue item = layout.child; item != null; item = item.next) {
            String type = item.getString("type");
            float x = item.getFloat("x")*Constants.oneBlockHeight + Constants.editorTestOffsetX*Constants.oneBlockHeight;
            float y = item.getFloat("y")*Constants.oneBlockHeight + Constants.startY + Constants.editorTestOffsetY;

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
                    decorationList.add(new Decoration(Constants.block4SkinPath, x, y, Constants.oneBlockWidth, Constants.oneBlockHeight));
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
            if (x > maxX){
                maxX = x;
            }
        }
    }

    @Override
    public void show() {
        FileHandle file = Gdx.files.absolute(Constants.playerDataPath);
        JsonReader jsonReader = new JsonReader();
        JsonValue base = jsonReader.parse(file);

        int currIcon = base.get("current_icon").asInt();
        String iconPath = "Sprites/Icons/Cubes/Cube-" + currIcon + ".png";

        Gdx.input.setInputProcessor(null);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (player == null) {
            this.player = new Player(iconPath, Constants.startX, Constants.startY);
        } else {
            this.player = null;
            this.player = new Player(iconPath, Constants.startX, Constants.startY);
        }
        baseY = Constants.startY;
        font = new BitmapFont(Gdx.files.internal("font.fnt"), false);
        font.setColor(Color.WHITE);
        font.getData().setScale(3);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        parameter.color = Color.WHITE;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.BLACK;
        fontPercentage = generator.generateFont(parameter);

        backgroundTexture = new Texture(Constants.backdropPath); // Replace with your background texture path
        groundTexture = new Texture(Constants.groundPath);

        if (!levelGenerated) {
            switch (curLevel) {
                case "ud":
                    generateLevel(System.getProperty("user.dir") + "\\assets\\Sprites\\UltimateDestruction.json");
                    break;
                case "ed":
                    generateLevel(System.getProperty("user.dir") + "\\assets\\Sprites\\Eurodancer.json");
                    break;
                case "ca":
                    generateLevel(System.getProperty("user.dir") + "\\assets\\Sprites\\ChaozAirflow.json");
                    break;
            }
            levelGenerated = true;
        }

        initVictoryWindow();

        MusicManager.stop();

        if (curLevel.equals("ud")) {
            MusicManager.load(MusicManager.jojoMusicEnabled ? Constants.jojoUDLevelMusicPath : Constants.ultimateDestructionPath, true);
        } else if (curLevel.equals("ed")) {
            MusicManager.load(MusicManager.jojoMusicEnabled ? Constants.jojoEDLevelMusicPath : Constants.eurodancerPath, true);
        } else if (curLevel.equals("ca")) {
            MusicManager.load(MusicManager.jojoMusicEnabled ? Constants.jojoCALevelMusicPath : Constants.chaozAirflowPath, true);
        }
        MusicManager.rewind();
        MusicManager.play();
    }

    private void initVictoryWindow() {
        updateKeys();
        victoryWindowStage = new Stage(new ScreenViewport());

        Texture victoryWindowTexture = new Texture(Constants.levelCompleteOverlayPath);
        victoryWindow = new Image(victoryWindowTexture);
        victoryWindow.setSize((float) (victoryWindow.getWidth() * 5.5), (float) (victoryWindow.getHeight() * 5.5));
        victoryWindow.setPosition(
            (Gdx.graphics.getWidth() - victoryWindow.getWidth()) / 2,
            (Gdx.graphics.getHeight() - victoryWindow.getHeight()) / 2
        );

        Texture exitButtonTexture = new Texture(Constants.menuButtonPath);
        Texture exitButtonGreyedTexture = new Texture(Constants.menuButtonGreyedPath);
        Button.ButtonStyle exitStyle = new Button.ButtonStyle();
        exitStyle.up = new TextureRegionDrawable(new TextureRegion(exitButtonTexture));
        exitStyle.down = new TextureRegionDrawable(new TextureRegion(exitButtonGreyedTexture));
        Button exitButton = new Button(exitStyle);
        exitButton.setSize((float) (exitButton.getWidth() * 5.5), (float) (exitButton.getHeight() * 5.5));
        exitButton.setPosition(
            (float) (victoryWindow.getX() + (victoryWindow.getWidth() * 0.75 - exitButton.getWidth()/2)),
            victoryWindow.getY() - exitButton.getHeight()/4
        );
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (curLevel.equals("ud")) {
                    ScreenManager.getInstance().setScreenWithFade(ScreenType.LEVEL1_SELECT, FirstScreen.this, 2f);
                } else if (curLevel.equals("ed")) {
                    ScreenManager.getInstance().setScreenWithFade(ScreenType.LEVEL2_SELECT, FirstScreen.this, 2f);
                } else if (curLevel.equals("ca")) {
                    ScreenManager.getInstance().setScreenWithFade(ScreenType.LEVEL3_SELECT, FirstScreen.this, 2f);
                }
            }
        });

        Texture replayButtonTexture = new Texture(Constants.replayButtonPath);
        Texture replayButtonGreyedTexture = new Texture(Constants.replayButtonGreyedPath);
        Button.ButtonStyle replayStyle = new Button.ButtonStyle();
        replayStyle.up = new TextureRegionDrawable(new TextureRegion(replayButtonTexture));
        replayStyle.down = new TextureRegionDrawable(new TextureRegion(replayButtonGreyedTexture));
        Button replayButton = new Button(replayStyle);
        replayButton.setSize((float) (replayButton.getWidth() * 5.5), (float) (replayButton.getHeight() * 5.5));
        replayButton.setPosition(
            victoryWindow.getX() + (victoryWindow.getWidth()/4 - replayButton.getWidth()/2),
            victoryWindow.getY() - replayButton.getHeight()/4
        );
        replayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO
            }
        });

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 75;
        parameter.color = Color.WHITE;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.BLACK;
        levelCompleteLabel = generator.generateFont(parameter);

        parameter.size = 105;
        parameter.color = Color.WHITE;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.BLACK;
        pauseLabelFont = generator.generateFont(parameter);

        generator.dispose();

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = levelCompleteLabel;
        style.fontColor = Color.WHITE;
        Label victoryLabel = new Label("Level complete!", style);
        victoryLabel.setPosition(
            victoryWindow.getX() + (victoryWindow.getWidth() - victoryLabel.getWidth())/2,
            (float) (victoryWindow.getY() + victoryWindow.getHeight() - victoryWindow.getHeight() / 2.7)
        );

        keyTexture = new Texture(Constants.keyPath);
        keyGreyedTexture = new Texture(Constants.keyGreyedPath);

        key1Complete = new Image(json_keys[0] ? keyTexture : keyGreyedTexture);
        key2Complete = new Image(json_keys[1] ? keyTexture : keyGreyedTexture);
        key3Complete = new Image(json_keys[2] ? keyTexture : keyGreyedTexture);

        key1Complete.setSize((float) (keyGreyedTexture.getWidth() * 10), (float) (keyGreyedTexture.getHeight() * 10));
        key1Complete.setPosition(
            (float) ((victoryWindow.getX() + (victoryWindow.getWidth() - key1Complete.getWidth())/2) - key1Complete.getWidth()*1.5),
            (float) (victoryWindow.getY() + victoryWindow.getHeight() - victoryWindow.getHeight() / 1.4)
        );
        key2Complete.setSize((float) (keyGreyedTexture.getWidth() * 10), (float) (keyGreyedTexture.getHeight() * 10));
        key2Complete.setPosition(
            victoryWindow.getX() + (victoryWindow.getWidth() - key2Complete.getWidth())/2,
            (float) (victoryWindow.getY() + victoryWindow.getHeight() - victoryWindow.getHeight() / 1.4)
        );
        key3Complete.setSize((float) (keyGreyedTexture.getWidth() * 10), (float) (keyGreyedTexture.getHeight() * 10));
        key3Complete.setPosition(
            (float) ((victoryWindow.getX() + (victoryWindow.getWidth() - key3Complete.getWidth())/2) + key3Complete.getWidth()*1.5),
            (float) (victoryWindow.getY() + victoryWindow.getHeight() - victoryWindow.getHeight() / 1.4)
        );

        victoryWindowStage.addActor(victoryWindow);
        victoryWindowStage.addActor(replayButton);
        victoryWindowStage.addActor(exitButton);
        victoryWindowStage.addActor(victoryLabel);
        victoryWindowStage.addActor(key1Complete);
        victoryWindowStage.addActor(key2Complete);
        victoryWindowStage.addActor(key3Complete);

        paused = false;
    }

    public void markLevelAsCompletedWithGson(String filePath) {
        try {
            // Use LibGDX to get the file handle for reading from internal assets
            FileHandle file = Gdx.files.absolute(filePath);

            // Read the JSON using InputStreamReader from LibGDX file handle
            InputStreamReader reader = new InputStreamReader(file.read());
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            // Check and update "completed" if false
            if (root.has("completed") && !root.get("completed").getAsBoolean()) {
                root.addProperty("completed", true);

                // For writing, use local file (writable storage)
                FileHandle writableFile = Gdx.files.absolute(filePath);

                // Write the updated JSON back to writable file
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Writer writer = new OutputStreamWriter(writableFile.write(false));
                gson.toJson(root, writer);
                writer.flush();
                writer.close();

                System.out.println("Level marked as completed.");
            } else {
                System.out.println("Level is already completed or missing 'completed' field.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateKeys(){
        JsonReader jsonReader = new JsonReader();
        JsonValue base = jsonReader.parse(Gdx.files.absolute(Constants.playerDataPath));

        JsonValue keysArray = base.get(curLevel + "_keys");
        json_keys = new boolean[3];

        for (int i = 0; i < keysArray.size; i++) {
            JsonValue keyObj = keysArray.get(i);
            json_keys[i] = keyObj.child().asBoolean();
        }
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

        //showVictoryWindow = true;


        if (delta > 0.25f) delta = 0.25f;
        accumulator += delta;

        while (accumulator >= UPDATE_DELTA) {
            if (!player.isAlive()) {
                deathCameraPosition.set(camera.position);
                die();
            }

            if (inputIgnoreFrames > 0) {
                inputIgnoreFrames--;
               return;
            }

            update(UPDATE_DELTA);
            checkForCollisions();

            accumulator -= UPDATE_DELTA;
        }

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


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !paused) {
            paused = true;
            MusicManager.pause();
        } else if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && paused) {
            paused = false;
            MusicManager.play();
        }

        if (paused) {
            drawPauseScreen(delta);
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

        float thresholdX = camera.viewportWidth * 0.3f;
        float percentX = 9*Constants.oneBlockHeight;

        if (player.getX() > thresholdX) {
            percentX = player.getX() + 3*Constants.oneBlockWidth;
        }

        Vector3 screenPos = new Vector3(0, 50, 0);
        camera.unproject(screenPos);
        float percentY = screenPos.y;

        batch.begin();
        GlyphLayout layout = new GlyphLayout(fontPercentage, String.valueOf(curPercentage) + '%');
        fontPercentage.draw(batch, layout, percentX, percentY);
        batch.end();
        curPercentage = (player.getX()/maxX) * 100 > 0 ? new BigDecimal((player.getX()/maxX) * 100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue() : 0;

        if (curPercentage >= 100 && !showVictoryWindow) {
            showVictoryWindow = true;
            Gdx.input.setInputProcessor(victoryWindowStage);
        }
        if (curPercentage >= 100) {
            try {
                saveKeysWithGson();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            updateKeys();
            victoryWindowStage.act(delta);
            victoryWindowStage.draw();

            if (!savedComplete) {
                switch (curLevel) {
                    case "ud":
                        markLevelAsCompletedWithGson(System.getProperty("user.dir") + "\\assets\\Sprites\\UltimateDestruction.json");
                        break;
                    case "ed":
                        markLevelAsCompletedWithGson(System.getProperty("user.dir") + "\\assets\\Sprites\\Eurodancer.json");
                        break;
                    case "ca":
                        markLevelAsCompletedWithGson(System.getProperty("user.dir") + "\\assets\\Sprites\\ChaozAirflow.json");
                        break;
                }
                savedComplete = true;
            }
        }

        if (showVictoryWindow) {
            updateKeys();
            victoryWindowStage.act(delta);
            victoryWindowStage.draw();

            if (!savedComplete) {
                switch (curLevel) {
                    case "ud":
                        markLevelAsCompletedWithGson(System.getProperty("user.dir") + "\\assets\\Sprites\\UltimateDestruction.json");
                        break;
                    case "ed":
                        markLevelAsCompletedWithGson(System.getProperty("user.dir") + "\\assets\\Sprites\\Eurodancer.json");
                        break;
                    case "ca":
                        markLevelAsCompletedWithGson(System.getProperty("user.dir") + "\\assets\\Sprites\\ChaozAirflow.json");
                        break;
                }
                savedComplete = true;
            }
        }

        if (redFlashActive) {
            redFlashTimer -= delta;

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 0, 0, 1); // Red with full opacity
            shapeRenderer.rect(0, 0, camera.viewportWidth, camera.viewportHeight);
            shapeRenderer.end();

            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            font.setColor(Color.WHITE);
            GlyphLayout layoutDead = new GlyphLayout(font, "YOU ARE DEAD!");
            font.draw(batch, layoutDead,
                (Gdx.graphics.getWidth() - layoutDead.width) / 2,
                (Gdx.graphics.getHeight() + layoutDead.height) / 2
            );
            batch.end();

            if (redFlashTimer <= 0) {
                redFlashActive = false;
                player.alive = true;
            }
        }
    }

    private void drawPauseScreen(float delta) {
        Stage pauseStage = new Stage(new ScreenViewport());

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = pauseLabelFont;
        style.fontColor = Color.WHITE;
        Label pauseLabel = new Label("PAUSE", style);
        pauseLabel.setPosition(
            (Gdx.graphics.getWidth() - pauseLabel.getWidth()) / 2,
            (Gdx.graphics.getHeight() + pauseLabel.getHeight()) / 2
        );

        pauseStage.addActor(pauseLabel);

        pauseStage.act(delta);
        pauseStage.draw();
    }

    public void update(float delta) {
        if (paused) return;

        batch.setProjectionMatrix(camera.combined);
        cameraUpdate();

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
