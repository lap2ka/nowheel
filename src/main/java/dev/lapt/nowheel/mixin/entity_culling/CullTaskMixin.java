package dev.lapt.nowheel.mixin.entity_culling;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.lapt.nowheel.config.NowheelConfig;
import dev.tr7zw.entityculling.CullTask;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(
    value = CullTask.class,
    remap = false
)
public abstract class CullTaskMixin {

    @ModifyExpressionValue(
        method = "<init>",
        at = @At(
            value = "FIELD",
            target = "Ldev/tr7zw/entityculling/versionless/Config;hitboxLimit:I",
            opcode = Opcodes.GETFIELD
        )
    )
    private int nowheel$extendHitboxLimit(int original) {
        return NowheelConfig.get().overrideEntityCulling ? Math.max(original, NowheelConfig.HITBOX_LIMIT_OVERRIDE) : original;
    }

    // Flywheel renders its stuff way past the vanilla 64
    @SuppressWarnings("UnqualifiedMemberReference")
    @Redirect(
        method = "cullBlockEntities",
        at = @At(
            value = "INVOKE",
            target = "Ldev/tr7zw/entityculling/CullTask;closerThan"
        )
    )
    private boolean nowheel$extendCloserThan(BlockPos blockPos, Position position, double d) {
        return true;
    }
}
