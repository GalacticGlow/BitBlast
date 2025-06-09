package Helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Player{
    private Sprite sprite;

    public Rectangle hitBox;

    public int playerWidth;
    public int playerHeight;

    public float curYSpeed;

    public float jumpStartY;

    public boolean applyGravity = false;
    private boolean isJumping = false;
    private boolean jumped = false;

    public boolean onGround;

    //private boolean isJumping = false;
    private float jumpTime = 0f;
    private final float jumpDuration = 0.7f; // seconds
    private float jumpStartX;
    private final float jumpWidth = 4.1f * Constants.oneBlockWidth;

    private boolean jumpPressedLastFrame = false;

    private float jumpTargetX;

    final float UNIT = 10.38f;
    final float GRAVITY = -0.876f * UNIT * UNIT;
    final float INITIAL_JUMP_SPEED = 1.94f * UNIT;
    final float TERMINAL_VELOCITY = -2.6f * UNIT;
    final float BASE_X_SPEED = UNIT;

    public boolean alive;

    public Player(String skinPath, int x, int y) {
        this.playerWidth = Constants.oneBlockWidth;
        this.playerHeight = Constants.oneBlockHeight;
        Texture texture = new Texture(skinPath);
        sprite = new Sprite(texture);
        sprite.setSize(Constants.oneBlockWidth, Constants.oneBlockHeight);
        sprite.setPosition(x, y);

        this.hitBox = new Rectangle(x, y, playerWidth, playerHeight);
        this.onGround = true;
        this.alive = true;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void update(float delta, ArrayList<Block> blockList) {
        // Move horizontally
        float dx = delta * BASE_X_SPEED * Constants.oneBlockWidth;
        float x = sprite.getX() + dx;
        float y = sprite.getY();

        // Handle jump input
        boolean jumpPressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        if (jumpPressed && onGround) {
            curYSpeed = INITIAL_JUMP_SPEED;
            onGround = false;
        }

        // Gravity and vertical speed
        curYSpeed += GRAVITY * delta;
        if (curYSpeed < TERMINAL_VELOCITY) curYSpeed = TERMINAL_VELOCITY;
        y += curYSpeed * delta * Constants.oneBlockHeight;

        // Predict new hitbox
        Rectangle nextHitBox = new Rectangle(x, y, playerWidth, playerHeight);
        boolean landedOnBlock = false;

        for (Block block : blockList) {
            Rectangle blockHitBox = block.getHitBox();

            if (nextHitBox.overlaps(blockHitBox)) {
                float prevBottom = sprite.getY();
                float blockTop = blockHitBox.y + blockHitBox.height;

                // Only land on block if falling and coming from above
                if (prevBottom >= blockTop && curYSpeed <= 0) {
                    y = blockTop;
                    curYSpeed = 0;
                    onGround = true;
                    landedOnBlock = true;
                    break;
                }
                else {
                    alive = false;
                }
            }
        }

        // Fallback: hit ground
        if (!landedOnBlock && y <= Constants.startY) {
            y = Constants.startY;
            curYSpeed = 0;
            onGround = true;
        } else if (!landedOnBlock) {
            onGround = false;
        }

        updatePosition(x, y);
    }

    public void updatePosition(float x, float y) {
        hitBox.setPosition(x, y);
        sprite.setPosition(x, y);
    }

    public Sprite getSprite(){
        return this.sprite;
    }

    public float getX(){
        return sprite.getX();
    }

    public float getY(){
        return sprite.getY();
    }

    public float getWidth(){
        return sprite.getWidth();
    }

    public float getHeight(){
        return sprite.getHeight();
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public boolean isAlive() {
        return this.alive;
    }
}
