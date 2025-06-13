package Helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue;
import com.badlogic.gdx.math.Rectangle;

public class Orb {
    public Sprite sprite;
    public Rectangle hitBox;

    public float orbWidth;
    public float orbHeight;
    public float orbX;
    public float orbY;

    public Orb(float x, float y, float orbWidth, float orbHeight) {
        this.orbWidth = orbWidth;
        this.orbHeight = orbHeight;
        this.orbX = x;
        this.orbY =y;
        Texture texture = new Texture(Constants.jumpOrbPath);
        sprite = new Sprite(texture);
        sprite.setSize(orbWidth, orbHeight);
        sprite.setPosition(x, y);
        this.hitBox = new Rectangle(x, y, orbWidth, orbHeight);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public float getX(){
        return this.orbX;
    }

    public float getY(){
        return this.orbY;
    }
}
