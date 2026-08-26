package dev.lapt.nowheel;

import com.mojang.logging.LogUtils;
import dev.lapt.nowheel.compat.sable.SableCompat;
import dev.lapt.nowheel.config.NowheelConfigScreen;
import dev.tr7zw.entityculling.EntityCullingModBase;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod(
    value = NowheelMod.MODID,
    dist = net.neoforged.api.distmarker.Dist.CLIENT
)
public class NowheelMod {
    public static final String MODID = "nowheel";
    public static final Logger LOGGER = LogUtils.getLogger();

    private boolean sableHooked = false;
    private Consumer<ClientTickEvent.Post> sableHook;

    public NowheelMod(ModContainer container) {
        if (ModList.get().isLoaded("cloth_config")) {
            Supplier<IConfigScreenFactory> configScreen = () -> (mc, previousScreen) -> NowheelConfigScreen.create(previousScreen);
            container.registerExtensionPoint(IConfigScreenFactory.class, configScreen);
        }

        if (ModList.get().isLoaded("sablecompanion")) {
            sableHook = this::hookSable;
            NeoForge.EVENT_BUS.addListener(sableHook);
        }

        LOGGER.info("Nowheel loaded");
    }

    // https://github.com/tr7zw/EntityCulling/issues/299
    // I could PR but idk
    private void hookSable(ClientTickEvent.Post event) {
        if (sableHooked) {
            return;
        }
        EntityCullingModBase ec = EntityCullingModBase.instance;
        if (ec == null) {
            return;
        }
        sableHooked = true;
        NeoForge.EVENT_BUS.unregister(sableHook);
        ec.addDynamicEntityWhitelist(SableCompat::onSubLevel);
        LOGGER.info("Nowheel Sable compat active");
    }
}
