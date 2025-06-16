package Helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue;
import com.badlogic.gdx.math.Rectangle;

public class Key {
    public Sprite sprite;
    public Rectangle hitBox;

    public float keyWidth;
    public float keyHeight;
    public float keyX;
    public float keyY;

    public Key(float x, float y, float keyWidth, float keyHeight) {
        this.keyWidth = keyWidth;
        this.keyHeight = keyHeight;
        this.keyX = x;
        this.keyY =y;
        Texture texture = new Texture(Constants.keyPath);
        sprite = new Sprite(texture);
        sprite.setSize(keyWidth, keyHeight);
        sprite.setPosition(x, y);
        this.hitBox = new Rectangle(x, y, Constants.oneBlockWidth, Constants.oneBlockHeight);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public float getX(){
        return this.keyX;
    }

    public float getY(){
        return this.keyY;
    }
}
