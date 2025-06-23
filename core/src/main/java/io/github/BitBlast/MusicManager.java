package io.github.BitBlast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;

public class MusicManager {
    private static Music currentMusic;
    private static String currentPath;
    private static final Preferences preferences = Gdx.app.getPreferences("game_settings");
    private static float volume = 1f; // значення за замовчуванням

    public static boolean jojoMusicEnabled = false;

    public static void load(String path, boolean looping) {
        if (currentPath != null && currentPath.equals(path) && currentMusic != null) {
            return;
        }

        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
        }

        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(path));
        currentMusic.setLooping(looping);
        currentMusic.setVolume(getVolume()); // використовує збережену гучність
        currentPath = path;
    }

    public static void play() {
        if (currentMusic != null && !currentMusic.isPlaying()) {
            currentMusic.play();
        }
    }

    public static void rewind() {
        if (currentMusic != null) {
            currentMusic.setPosition(0);
        }
    }

    public static void setVolume(float volume) {
        MusicManager.volume = volume;
        if (currentMusic != null) {
            currentMusic.setVolume(volume);
        }
        preferences.putFloat("music_volume", volume);
        preferences.flush(); // зберігаємо на диск
    }

    public static float getVolume() {
        return preferences.getFloat("music_volume", 1f); // якщо немає — значення за замовчуванням 1
    }

    public static void stop() {
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    public static void pause() {
        if (currentMusic != null) {
            currentMusic.pause();
        }
    }

    public static void setCurrentPath(String currentPath) {
        MusicManager.currentPath = currentPath;
    }

    public static void dispose() {
        if (currentMusic != null) {
            currentMusic.dispose();
            currentMusic = null;
            currentPath = null;
        }
    }

    public static String getCurrentPath() {
        return currentPath;
    }

    public static Music getCurrentMusic() {
        return currentMusic;
    }

    public static boolean isPlaying() {
        return currentMusic != null && currentMusic.isPlaying();
    }
}
