package Helper;

import com.badlogic.gdx.Gdx;

public class Constants {
    public static String playerDataPath = System.getProperty("user.dir") + "\\assets\\Sprites\\player_data.json";

    public static String playerSkin1Path = "Sprites/Icons/Cubes/Cube-1.png";
    public static String playerSkin2Path = "Sprites/Icons/Cubes/Cube-2.png";
    public static String playerSkin3Path = "Sprites/Icons/Cubes/Cube-3.png";
    public static String playerSkin4Path = "Sprites/Icons/Cubes/Cube-4.png";
    public static String playerSkin5Path = "Sprites/Icons/Cubes/Cube-5.png";
    public static String playerSkin6Path = "Sprites/Icons/Cubes/Cube-6.png";
    public static String playerSkin7Path = "Sprites/Icons/Cubes/Cube-7.png";
    public static String selectedPath = "Sprites/Menu Objects/Icon Selector/Selected.png";

    public static String levelSelectBackgroundPath = "Sprites/Menu Objects/Level Selector/Level-Select-BG.png";
    public static String hardFacePath = "Sprites/Menu Objects/Level Selector/Hard-Face.png";
    public static String harderFacePath = "Sprites/Menu Objects/Level Selector/Harder-Face.png";
    public static String insaneFacePath = "Sprites/Menu Objects/Level Selector/Insane-Face.png";
    public static String arrowPath = "Sprites/Menu Objects/Level Selector/Arrow.png";
    public static String checkmarkPath = "Sprites/Menu Objects/Level Selector/Checkmark.png";
    public static String checkmarkGreyedPath = "Sprites/Menu Objects/Level Selector/Checkmark-Greyed-out.png";
    public static String keyPath = "Sprites/Interactables/Key-1.png";
    public static String keyGreyedPath = "Sprites/Menu Objects/Level Selector/Key-Greyed-out.png";

    public static String spike1SkinPath = "Sprites/Spikes/Spike-1.png";
    public static String spike2SkinPath = "Sprites/Spikes/Spike-2.png";
    public static String spike3SkinPath = "Sprites/Spikes/Spikefloor-1.png";

    public static String blockSkinPath = "Sprites/Blocks/Block-1.png";
    public static String block1SkinPath = "Sprites/Blocks/Block-Segmented-1.png";
    public static String block2SkinPath = "Sprites/Blocks/Block-Segmented-2.png";
    public static String block3SkinPath = "Sprites/Blocks/Block-Segmented-3.png";
    public static String block4SkinPath = "Sprites/Blocks/Block-Segmented-4.png";
    public static String block5SkinPath = "Sprites/Blocks/Block-Segmented-5.png";
    public static String block6SkinPath = "Sprites/Blocks/Block-Segmented-6.png";
    public static String slabSkinPath = "Sprites/Blocks/Slab-1.png";

    public static String jumpOrbPath = "Sprites/Interactables/Orb-1.png";

    public static String backdropPath = "Sprites/Background/Backdrop-1.png";
    public static String groundPath = "Sprites/Background/GroundBlock-1.png";

    public static String jumpPadPath = "Sprites/Interactables/Pad-1.png";

    public static String logoPath = "Sprites/Menu Objects/Logo.png";
    public static String playButtonPath = "Sprites/Menu Objects/Play-Button.png";
    public static String greyedPlayButtonPath = "Sprites/Menu Objects/Play-Button-Greyed.png";
    public static String cubeButtonPath = "Sprites/Menu Objects/Cube-Button.png";
    public static String greyedCubeButtonPath = "Sprites/Menu Objects/Cube-Button-Greyed.png";
    public static String CogButtonPath = "Sprites/Menu Objects/Cog-Button.png";
    public static String greyedCogButtonPath = "Sprites/Menu Objects/Cog-Button-Greyed.png";

    public static String IconSelectorBackgroundPath = "Sprites/Menu Objects/Icon Selector/Background.png";
    public static String lockerPath = "Sprites/Menu Objects/Icon Selector/Lock.png";

    public static String chainDecoPath = "Sprites/Deco/Chain-1.png";
    public static String spikeDeco1Path = "Sprites/Deco/Spikedeco-1.png";
    public static String spikeDeco2Path = "Sprites/Deco/Spikedeco-2.png";
    public static String spikeDeco3Path = "Sprites/Deco/Spikedeco-3.png";
    public static String torchDecoPath = "Sprites/Deco/Torch-1.png";

    public static String backgroundMusicPath = "Sounds/Menu/MenuMusic.mp3";
    public static String jojoBackgroundMusicPath = "Sounds/Menu/Pillarmen theme main menu.mp3";
    public static String ultimateDestructionPath = "Sounds/Music/1-ultimate-destruction.mp3";
    public static String jojoUDMenuMusicPath = "Sounds/Music/Josuke Theme Menu.mp3";
    public static String jojoUDLevelMusicPath = "Sounds/Music/Josuke Theme Level.mp3";
    public static String eurodancerPath = "Sounds/Music/2-eurodancer.mp3";
    public static String jojoEDMenuMusicPath = "Sounds/Music/Il Vento Doro Menu.mp3";
    public static String jojoEDLevelMusicPath = "Sounds/Music/Il Vento Doro Level.mp3";
    public static String chaozAirflowPath = "Sounds/Music/3-chaoz-airflow.mp3";
    public static String jojoCAMenuMusicPath = "Sounds/Music/Jolyne Theme Menu.mp3";
    public static String jojoCALevelMusicPath = "Sounds/Music/Jolyne Theme Level.mp3";

    public static String levelCompleteOverlayPath = "Sprites/Menu Objects/Level Complete/Level-Complete-Overlay.png";
    public static String menuButtonPath = "Sprites/Menu Objects/Level Complete/Menu-Button.png";
    public static String menuButtonGreyedPath = "Sprites/Menu Objects/Level Complete/Menu-Button-Greyed-out.png";
    public static String replayButtonPath = "Sprites/Menu Objects/Level Complete/Replay-Button.png";
    public static String replayButtonGreyedPath = "Sprites/Menu Objects/Level Complete/Replay-Button-Greyed-out.png";

    public static final int worldWidth = 20;
    public static final int worldHeight = 12;

    public static int oneBlockWidth;
    public static int oneBlockHeight;

    public static final float acceleration = 0.876f;
    public static final float xSpeed = 5f;
    public static final float ySpeedJump = 1.94f;
    public static final float ySpeedMax = 2.6f;

    public static final int startX = -200;

    public static float maxJumpHeight;
    public static int startY;
    public static float maxHeight;
    public static float jumpVelocity;

    public static float cameraFollowThresholdX;
    public static float cameraFollowThresholdY;

    public static float jumpHorizontalSpeed;

    public static int difficultyFaceSize = 150;

    public static int editorTestOffsetX = 0;
    public static int editorTestOffsetY = 0;


    public static void init(){
        oneBlockHeight = Gdx.graphics.getWidth()/worldWidth;
        oneBlockWidth = oneBlockHeight;

        maxJumpHeight = 2.148f*oneBlockHeight;
        startY = (int)3.5*oneBlockHeight;
        maxHeight = 1.5f * Constants.oneBlockHeight;
        jumpVelocity = (float)Math.sqrt(2 * -acceleration * maxHeight);

        cameraFollowThresholdX = Constants.oneBlockWidth * 4; // After 4 blocks
        cameraFollowThresholdY = Constants.oneBlockHeight * 6;

        jumpHorizontalSpeed = Constants.xSpeed * Constants.oneBlockWidth; // or whatever speed you want
    }
}
