package Helper;

import com.badlogic.gdx.Gdx;

public class Constants {
    public static String playerSkinPath = "Sprites/Icons/Cubes/Cube-1.png";

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
        oneBlockHeight = Gdx.graphics.getHeight()/worldHeight;

        maxJumpHeight = 2.148f*oneBlockHeight;
        startY = (int)3.5*oneBlockHeight;
        maxHeight = 1.5f * Constants.oneBlockHeight;
        jumpVelocity = (float)Math.sqrt(2 * -acceleration * maxHeight);

        cameraFollowThresholdX = Constants.oneBlockWidth * 4; // After 4 blocks

        jumpHorizontalSpeed = Constants.xSpeed * Constants.oneBlockWidth; // or whatever speed you want
    }
}
