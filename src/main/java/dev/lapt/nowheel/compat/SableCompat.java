package dev.lapt.nowheel.compat;

import dev.ryanhcode.sable.companion.SableCompanion;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class SableCompat {

    private SableCompat() {
    }

    public static boolean onSubLevel(Entity entity) {
        return SableCompanion.INSTANCE.getContaining(entity) != null;
    }

    public static boolean onSubLevel(BlockEntity blockEntity) {
        return SableCompanion.INSTANCE.getContaining(blockEntity) != null;
    }
}
