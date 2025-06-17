package io.github.BitBlast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicManager {
    private static Music currentMusic;
    private static String currentPath;

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
        currentMusic.setVolume(1f);
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
        if (currentMusic != null) {
            currentMusic.setVolume(volume);
        }
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
}
