package io.github.BitBlast;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.*;

public class ScreenManager {
    private static ScreenManager instance;
    private Main game;
    private final Map<ScreenType, Screen> screens = new HashMap<>();

    private ScreenManager() {}

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void initialize(Main game) {
        this.game = game;
    }

    public void setScreen(ScreenType type) {
        Screen screen = screens.get(type);

        if (screen == null) {
            screen = createScreen(type);
            screens.put(type, screen);
        }
        game.setScreen(screen);
    }

    private Screen createScreen(ScreenType type) {
        OrthographicCamera camera = new OrthographicCamera();

        switch (type) {
            case MENU:
                return new MenuScreen(game, camera);
            case SKIN:
                return new SkinScreen(game, camera);
            case SETTINGS:
                return new SetingsScreen(game, camera);
            case LEVEL1_SELECT:
                return new Level1SelectorScreen(game, camera);
            case LEVEL2_SELECT:
                return new Level2SelectorScreen(game, camera);
            case LEVEL3_SELECT:
                return new Level3SelectorScreen(game, camera);
            case FIRST_LEVEL:
                return new FirstScreen(game, camera, "ud");
            default:
                throw new IllegalArgumentException("Unknown screen type: " + type);
        }

    }

    public void setScreenWithFade(ScreenType type, Screen fromScreen, float duration) {
        Screen toScreen = screens.get(type);
        if (toScreen == null) {
            toScreen = createScreen(type);
            screens.put(type, toScreen);
        }
        game.setScreen(new FadeTransitionScreen(game, fromScreen, toScreen, duration));
    }

    public ScreenType getScreenTypeByScreen(Screen screen) {
        for (Map.Entry<ScreenType, Screen> entry : screens.entrySet()) {
            if ((screen == null && entry.getValue() == null) ||
                (screen != null && screen.equals(entry.getValue()))) {
                return entry.getKey();
            }
        }
        return null; // якщо ключа з таким значенням немає
    }

    public void dispose() {
        for (Screen screen : screens.values()) {
            if (screen != null) {
                screen.dispose();
            }
        }
        screens.clear();
    }
}
