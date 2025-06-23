package Helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input;

import java.util.ArrayList;

public class Player{
    private Sprite sprite;

    public Rectangle hitBox;

    public int playerWidth;
    public int playerHeight;

    public float curYSpeed;

    public boolean onGround;

    final float UNIT = 10.38f;
    final float GRAVITY = -0.876f * UNIT * UNIT;
    final float INITIAL_JUMP_SPEED = 1.94f * UNIT;
    final float TERMINAL_VELOCITY = -2.6f * UNIT;
    final float BASE_X_SPEED = UNIT;
    final float JUMP_PAD_ACCELERATION = 2.5f * UNIT;
    final float JUMP_ORB_SPEED = 1.91F * UNIT;

    public boolean alive;
    public boolean orbTrigger;
    public float jumpTimer = 0f;
    public float jumpTime = -2*INITIAL_JUMP_SPEED/GRAVITY;
    public float rotation;

    public Player(String skinPath, int x, int y) {
        this.playerWidth = Constants.oneBlockWidth;
        this.playerHeight = Constants.oneBlockHeight;
        Texture texture = new Texture(skinPath);
        sprite = new Sprite(texture);
        sprite.setSize(Constants.oneBlockWidth, Constants.oneBlockHeight);
        sprite.setPosition(x, y);
        sprite.setOriginCenter();

        this.hitBox = new Rectangle(x, y, playerWidth, playerHeight);
        this.onGround = true;
        this.alive = true;
        this.rotation = 360f;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    private float normalizeRotation(float rotation) {
        return ((rotation % 360f) + 360f) % 360f;
    }

    public void update(float delta, ArrayList<Block> blockList, ArrayList<JumpPad> jumpPadList, ArrayList<Orb> orbList, ArrayList<Key> curKeys) {
        float dx = delta * BASE_X_SPEED * Constants.oneBlockWidth;
        float x = sprite.getX() + dx;
        float y = sprite.getY();

        boolean jumpPressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isTouched();
        boolean mouseJumpPressed = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        if (jumpPressed || mouseJumpPressed) {
            if (onGround) {
                curYSpeed = INITIAL_JUMP_SPEED;
                onGround = false;
                jumpTimer = 0f;
            }
            orbTrigger = true;
        }

        if (!onGround) {
            jumpTimer += delta;
            float progress = Math.min(jumpTimer / jumpTime, 1f);
            sprite.setRotation(this.rotation - progress * 180f);
        } else {
            if (jumpTimer > 0) {
                this.rotation -= 180f;
                this.rotation = normalizeRotation(this.rotation);
                jumpTimer = 0;
            }
            sprite.setRotation(this.rotation);
        }

        for (JumpPad jumpPad : jumpPadList) {
            if (this.hitBox.overlaps(jumpPad.getHitBox())) {
                curYSpeed = JUMP_PAD_ACCELERATION;
                onGround = false;
                break;
            }
        }

        for (Orb orb : orbList) {
            if (this.hitBox.overlaps(orb.getHitBox()) && orbTrigger) {
                curYSpeed = JUMP_ORB_SPEED;
                break;
            }
        }

        for (Key key : curKeys) {
            if (key != null && this.hitBox.overlaps(key.getHitBox())) {
                curKeys.set(curKeys.indexOf(key), null);
            }
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
        orbTrigger = false;
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
