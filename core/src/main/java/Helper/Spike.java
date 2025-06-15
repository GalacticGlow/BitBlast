package Helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue;
import com.badlogic.gdx.math.Rectangle;

public class Spike {
    public Sprite sprite;
    public Rectangle hitBox;

    public float spikeWidth;
    public float spikeHeight;
    public float spikeX;
    public float spikeY;

    final float hitBoxXOffsetCoefficient = 0.425f;
    final float hitBoxYOffsetCoefficient = 0.307f;
    final float hitBoxWidthCoefficient = 0.15f;
    final float hitBoxHeightCoefficient = 0.423f;

    public Spike(String skinPath, float x, float y, float spikeWidth, float spikeHeight) {
        this.spikeWidth = spikeWidth;
        this.spikeHeight = spikeHeight;
        this.spikeX = x;
        this.spikeY =y;
        Texture texture = new Texture(skinPath);
        sprite = new Sprite(texture);
        sprite.setSize(spikeWidth, spikeHeight);
        sprite.setPosition(x, y);
        this.hitBox = new Rectangle(x + Constants.oneBlockHeight*hitBoxXOffsetCoefficient, y + Constants.oneBlockHeight*hitBoxYOffsetCoefficient, Constants.oneBlockWidth*hitBoxWidthCoefficient, Constants.oneBlockHeight*hitBoxHeightCoefficient);
        sprite.setOriginCenter();
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public Float[] hitBoxData(){
        return new Float[]{hitBox.x,hitBox.y, hitBox.width, hitBox.height};
    }

    public float getX(){
        return this.spikeX;
    }

    public float getY(){
        return this.spikeY;
    }
}
