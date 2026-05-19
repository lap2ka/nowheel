package dev.lapt.nowheel.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.lapt.nowheel.config.NowheelConfig;
import dev.tr7zw.entityculling.EntityCullingModBase;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityCullingModBase.class, remap = false)
public class EntityCullingConfigOverrideMixin {

    @Unique
    private boolean nowheel$overridenWhitelist = false;

    @Unique
    private static final ResourceLocation ROPE_PULLEY = ResourceLocation.parse("create:rope_pulley");
    @Unique
    private static final ResourceLocation HOSE_PULLEY = ResourceLocation.parse("create:hose_pulley");

    @ModifyExpressionValue(
        method = "onInitialize", at = @At(
        value = "FIELD", target = "Ldev/tr7zw/entityculling/versionless/Config;tracingDistance:I", opcode = Opcodes.GETFIELD
    )
    )
    private int nowheel$overrideCullingLimits(int original) {
        if (!NowheelConfig.get().overrideEntityCulling) {
            return original;
        }
        return Math.max(original, NowheelConfig.TRACING_DISTANCE_OVERRIDE);
    }

    @Inject(method = "clientTick", at = @At("TAIL"))
    private void nowheel$overrideWhitelist(CallbackInfo ci) {
        if (nowheel$overridenWhitelist || !NowheelConfig.get().overrideEntityCulling) {
            return;
        }
        EntityCullingModBase ec = (EntityCullingModBase) (Object) this;

        ec.blockEntityWhitelist.remove(BuiltInRegistries.BLOCK_ENTITY_TYPE.get(ROPE_PULLEY));
        ec.blockEntityWhitelist.remove(BuiltInRegistries.BLOCK_ENTITY_TYPE.get(HOSE_PULLEY));
        nowheel$overridenWhitelist = true;
    }
}
