package dev.lapt.nowheel.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.lapt.nowheel.NowheelMod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class NowheelConfig {

    public boolean overrideEntityCulling = true;

    private static final File FILE = new File("config", "nowheel.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static NowheelConfig instance;

    public static NowheelConfig get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    private static NowheelConfig load() {
        NowheelConfig config = null;
        if (FILE.exists()) {
            try {
                config = GSON.fromJson(Files.readString(FILE.toPath()), NowheelConfig.class);
            } catch (Exception ex) {
                NowheelMod.LOGGER.error("[nowheel] error loading nowheel config", ex);
            }
        }
        if (config == null) {
            config = new NowheelConfig();
            config.write();
        }
        return config;
    }

    public void write() {
        try {
            Files.writeString(FILE.toPath(), GSON.toJson(this));
        } catch (IOException ex) {
            NowheelMod.LOGGER.error("[nowheel] error writing nowheel config", ex);
        }
    }
}
