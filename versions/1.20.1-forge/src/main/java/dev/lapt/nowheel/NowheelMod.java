package dev.lapt.nowheel;

import com.mojang.logging.LogUtils;
import dev.lapt.nowheel.config.NowheelConfigScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(NowheelMod.MODID)
public class NowheelMod {
    public static final String MODID = "nowheel";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NowheelMod() {
        if (FMLEnvironment.dist != Dist.CLIENT) return;

        if (ModList.get().isLoaded("cloth_config")) {
            ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((mc, parent) -> NowheelConfigScreen.create(parent))
            );
        }

        LOGGER.info("Nowheel loaded");
    }
}
