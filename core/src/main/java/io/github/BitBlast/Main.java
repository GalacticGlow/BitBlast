package io.github.BitBlast;

import Helper.Constants;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public static Main INSTANCE;
    public int widthScreen, heightScreen;
    private OrthographicCamera camera;

    public Main() {
        INSTANCE = this;
    }

    @Override
    public void create() {
        this.widthScreen = Gdx.graphics.getWidth();
        this.heightScreen = Gdx.graphics.getHeight();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, this.widthScreen, this.heightScreen);
        Constants.init();
        setScreen(new FirstScreen(camera));
    }
}
