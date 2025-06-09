package Helper;

import com.badlogic.gdx.Gdx;

public class Constants {
    public static String playerSkinPath = "Sprites/Icons/Cubes/Cube-1.png";

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

    public static final int worldWidth = 20;
    public static final int worldHeight = 12;

    public static int oneBlockWidth;
    public static int oneBlockHeight;

    public static final float acceleration = 0.876f;
    public static final float xSpeed = 5f;
    public static final float ySpeedJump = 1.94f;
    public static final float ySpeedMax = 2.6f;

    public static final int startX = 0;

    public static float maxJumpHeight;
    public static int startY;
    public static float maxHeight;
    public static float jumpVelocity;

    public static float cameraFollowThresholdX;

    public static float jumpHorizontalSpeed;

    public static void init(){
        oneBlockWidth = Gdx.graphics.getWidth()/worldWidth;
        oneBlockHeight = oneBlockWidth;

        maxJumpHeight = 2.148f*oneBlockHeight;
        startY = (int)3.5*oneBlockHeight;
        maxHeight = 1.5f * Constants.oneBlockHeight;
        jumpVelocity = (float)Math.sqrt(2 * -acceleration * maxHeight);

        cameraFollowThresholdX = Constants.oneBlockWidth * 4; // After 4 blocks

        jumpHorizontalSpeed = Constants.xSpeed * Constants.oneBlockWidth; // or whatever speed you want
    }
}
