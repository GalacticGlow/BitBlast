package Helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue;
import com.badlogic.gdx.math.Rectangle;

public class JumpPad {
    public Sprite sprite;
    public Rectangle hitBox;

    public float padWidth;
    public float padHeight;
    public float padX;
    public float padY;

    public JumpPad(float x, float y, float padWidth, float padHeight) {
        this.padWidth = padWidth;
        this.padHeight = padHeight;
        this.padX = x;
        this.padY =y;
        Texture texture = new Texture(Constants.jumpPadPath);
        sprite = new Sprite(texture);
        sprite.setSize(padWidth, padHeight);
        sprite.setPosition(x, y);
        this.hitBox = new Rectangle(x + padWidth/2, y, padWidth/2, padHeight/2);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public float getX(){
        return this.padX;
    }

    public float getY(){
        return this.padY;
    }
}
