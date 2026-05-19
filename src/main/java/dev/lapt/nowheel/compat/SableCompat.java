package dev.lapt.nowheel.compat;

import dev.ryanhcode.sable.companion.SableCompanion;
import net.minecraft.world.entity.Entity;

public final class SableCompat {

    private SableCompat() { }

    public static boolean onSubLevel(Entity entity) {
        return SableCompanion.INSTANCE.getContaining(entity) != null;
    }
}
