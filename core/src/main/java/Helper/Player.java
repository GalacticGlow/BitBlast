package Helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

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

    private boolean onGround = true;

    //private boolean isJumping = false;
    private float jumpTime = 0f;
    private final float jumpDuration = 0.7f; // seconds
    private float jumpStartX;
    private final float jumpWidth = 4.1f * Constants.oneBlockWidth;

    private boolean jumpPressedLastFrame = false;

    private float jumpTargetX;

    private float yVelocity = 0;
    private float xVelocity = 1f; // for future use if moving left/right

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

    public void update(float delta, Level level) {
        float newX = hitBox.x;
        float newY = hitBox.y;

        // Apply gravity if in air
        if (!onGround) {
            yVelocity += Constants.acceleration * delta;
        }

        // Apply vertical movement
        newY += yVelocity * delta;

        // Check vertical collision
        if (yVelocity > 0) { // Falling
            if (level.doesRectCollideWithBlock(newX, newY + hitBox.height, hitBox.width, 0)) {
                // Hit ground
                yVelocity = 0;
                onGround = true;
                newY = (float) Math.floor((newY + hitBox.height) / Level.TILE_SIZE) * Level.TILE_SIZE - hitBox.height;
            } else {
                onGround = false;
            }
        } else if (yVelocity < 0) { // Going up
            if (level.doesRectCollideWithBlock(newX, newY, hitBox.width, 0)) {
                // Hit ceiling
                yVelocity = 0;
                newY = (float) Math.floor((newY) / Level.TILE_SIZE + 1) * Level.TILE_SIZE;
            }
        }

        // Apply horizontal movement if needed
        newX += xVelocity * delta;

        // Check horizontal collision (walls)
        if (level.doesRectCollideWithBlock(newX, newY, hitBox.width, hitBox.height)) {
            newX = hitBox.x; // Cancel horizontal movement
        }

        // Final update of hitbox
        hitBox.setPosition(newX, newY);
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

    public void checkGroundCollision(ArrayList<Block> blocks) {
        onGround = false;
        for (Block block : blocks) {
            if (this.hitBox.overlaps(block.getHitBox())) {
                Rectangle blockRect = block.getHitBox();
                System.out.println("Player landed on top of a block");

                // Player is falling and lands on block
                if (sprite.getY() > blockRect.y && curYSpeed <= 0) {
                    updatePosition(hitBox.x, blockRect.y + blockRect.height);
                    curYSpeed = 0;
                    onGround = true;
                    System.out.println("Player landed on top of a block and passed the if");
                    break;
                }
            }
        }
    }
}
