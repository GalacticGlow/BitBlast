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
    final float INITIAL_JUMP_SPEED = 2.4f * UNIT;
    final float TERMINAL_VELOCITY = -2.6f * UNIT;
    final float BASE_X_SPEED = UNIT;

    public Player(String skinPath, int x, int y) {
        this.playerWidth = Constants.oneBlockWidth;
        this.playerHeight = Constants.oneBlockHeight;
        Texture texture = new Texture(skinPath);
        sprite = new Sprite(texture);
        sprite.setSize(Constants.oneBlockWidth, Constants.oneBlockHeight);
        sprite.setPosition(x, y);

        this.hitBox = new Rectangle(x, y, playerWidth, playerHeight);
        this.onGround = true;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void update(float delta) {
        System.out.println("Current X: " + sprite.getX());
        System.out.println("Jump start curYSpeed: " + curYSpeed);

        float dx = delta * BASE_X_SPEED * Constants.oneBlockWidth;
        float x = sprite.getX() + dx;
        float y = sprite.getY();

        // Handle jump input
        boolean jumpPressed = Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.SPACE);

        if (jumpPressed && this.onGround) {
            curYSpeed = INITIAL_JUMP_SPEED;
            this.onGround = false;
        }

        jumpPressedLastFrame = jumpPressed;

        // Apply gravity
        curYSpeed += GRAVITY * delta;

        // Clamp to terminal velocity
        if (curYSpeed < TERMINAL_VELOCITY) {
            curYSpeed = TERMINAL_VELOCITY;
        }

        // Update vertical position
        y += curYSpeed * delta * Constants.oneBlockHeight;

        // Simulate ground collision (temporary — later you'll use block collision)
        if (y <= Constants.startY) {
            y = Constants.startY;
            curYSpeed = 0;
            this.onGround = true;
        }

        System.out.println("Current Y (blocks): " + (y - Constants.startY)/Constants.oneBlockWidth);

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
}
