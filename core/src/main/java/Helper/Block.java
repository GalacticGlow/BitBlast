package Helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue;
import com.badlogic.gdx.math.Rectangle;

public class Block {
    public Sprite sprite;
    public Rectangle hitBox;

    public float blockWidth;
    public float blockHeight;
    public float blockX;
    public float blockY;

    public Block(String skinPath, float x, float y, float blockWidth, float blockHeight) {
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.blockX = x;
        this.blockY =y;
        Texture texture = new Texture(skinPath);
        sprite = new Sprite(texture);
        sprite.setSize(blockWidth, blockHeight);
        sprite.setPosition(x, y);
        this.hitBox = new Rectangle(x, y, blockWidth, blockHeight);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public float getX(){
        return this.blockX;
    }

    public float getY(){
        return this.blockY;
    }
}
