package dev.lapt.nowheel;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;

public class NowheelMod implements ClientModInitializer {
    public static final String MODID = "nowheel";
    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitializeClient() {
        LOGGER.info("Nowheel loaded");
    }
}
