package io.github.BitBlast;

import Helper.Constants;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public static Main INSTANCE;
    public int widthScreen, heightScreen;
    private OrthographicCamera camera;
    BitmapFont fontSmall;
    private BitmapFont fontMedium;
    private BitmapFont fontLarge;

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

        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().setScreen(ScreenType.MENU);
    }
}
