package Helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue;
import com.badlogic.gdx.math.Rectangle;

public class Decoration {
    public Sprite sprite;

    public float decoWidth;
    public float decoHeight;
    public float decoX;
    public float decoY;

    public Decoration(String skinPath, float x, float y, float blockWidth, float blockHeight) {
        this.decoWidth = blockWidth;
        this.decoHeight = blockHeight;
        this.decoX = x;
        this.decoY =y;
        Texture texture = new Texture(skinPath);
        sprite = new Sprite(texture);
        sprite.setSize(decoWidth, decoHeight);
        sprite.setPosition(x, y);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public float getX(){
        return this.decoX;
    }

    public float getY(){
        return this.decoY;
    }
}
