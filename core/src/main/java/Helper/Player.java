package Helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

import java.awt.*;
import java.io.File;

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

    private boolean onGround = true;

    //private boolean isJumping = false;
    private float jumpTime = 0f;
    private final float jumpDuration = 0.7f; // seconds
    private float jumpStartX;
    private final float jumpWidth = 4.1f * Constants.oneBlockWidth;

    private boolean jumpPressedLastFrame = false;

    private float jumpTargetX;

    public Player(String skinPath, int x, int y) {
        this.playerWidth = Constants.oneBlockWidth;
        this.playerHeight = Constants.oneBlockHeight;
        Texture texture = new Texture(skinPath);
        sprite = new Sprite(texture);
        sprite.setSize(Constants.oneBlockWidth, Constants.oneBlockHeight);
        sprite.setPosition(x, y);

        this.hitBox = new Rectangle(x, y, playerWidth, playerHeight);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    /*
    public void update(float delta) {
        float dx = delta * Constants.xSpeed * Constants.oneBlockWidth;

        // Always move forward
        float newX = sprite.getX() + dx;
        float newY = sprite.getY();

        if (isJumping) {
            jumpTime += delta;

            if (jumpTime > jumpDuration) {
                // End jump
                isJumping = false;
                onGround = true;
                jumpPressedLastFrame = false;

                // Land at the final jump X position
                newX = jumpTargetX;
                newY = jumpStartY;

                jumpTargetX = 0;
                jumpStartX = 0;
            }
            else {
                float theta = (float)(Math.PI * jumpTime / jumpDuration);
                newX = jumpStartX + (jumpTargetX - jumpStartX) * (theta / (float)Math.PI);
                newY = jumpStartY + Constants.maxJumpHeight * (float)Math.sin(theta);
            }
        }
        else {
            // Use same horizontal speed as jump
            float jumpDistance = jumpTargetX - jumpStartX;
            float jumpSpeedX = jumpDistance / jumpDuration;
            newX += jumpSpeedX * delta;
        }

        System.out.println("Current X speed is: " + (newX - sprite.getX()));

        updatePosition(newX, newY);
    }
     */
    /*
    public void update(float delta) {
        float dx = Constants.xSpeed * Constants.oneBlockWidth * delta;
        float newX = sprite.getX() + dx;
        float newY = sprite.getY();

        if (isJumping) {
            jumpTime += delta;

            if (jumpTime > jumpDuration) {
                isJumping = false;
                onGround = true;
                jumpPressedLastFrame = false;
                jumpTime = 0;
                newY = jumpStartY;
            } else {
                float t = jumpTime / jumpDuration;
                float arc = 4 * Constants.maxJumpHeight * t * (1 - t);
                newY = jumpStartY + arc;
            }
        }

        updatePosition(newX, newY);
    }
        */

    public void update(float delta) {
        float dx = delta * Constants.xSpeed * Constants.oneBlockWidth;
        float newX = sprite.getX() + dx;
        float newY = sprite.getY();

        if (isJumping) {
            jumpTime += delta;

            float t = jumpTime / jumpDuration;

            if (t >= 1f) {
                // End jump
                isJumping = false;
                onGround = true;
                jumpPressedLastFrame = false;

                newX = jumpTargetX;
                newY = jumpStartY;

                jumpTargetX = 0;
                jumpStartX = 0;
            } else {
                // e.g. 3 * oneBlockWidth
                float maxJumpHeight = jumpWidth / 1.5f; // Width:Height = 1.5:1

                newX = jumpStartX + t * jumpWidth;
                newY = jumpStartY + -4 * maxJumpHeight * (t - 0.5f) * (t - 0.5f) + maxJumpHeight;
            }
        }

        updatePosition(newX, newY);
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
        return onGround;
    }

    public void handleJumpInput(boolean jumpPressed) {
        System.out.println("jumpPressed=" + jumpPressed + "      jumpPressedLastFrame=" + jumpPressedLastFrame + "    onGround=" + onGround);
        if (jumpPressed && !jumpPressedLastFrame && onGround) {
            jump();
        }
        jumpPressedLastFrame = jumpPressed;
    }

    public void jump() {
        if (!isJumping) {
            isJumping = true;
            jumpTime = 0f;
            jumpStartX = sprite.getX();
            jumpStartY = sprite.getY();
            jumpTargetX = jumpStartX + jumpWidth; // ensure forward motion
            onGround = false;
            System.out.println("Jump from x=" + jumpStartX + " to x=" + (jumpStartX + jumpWidth));
        }
    }
}
