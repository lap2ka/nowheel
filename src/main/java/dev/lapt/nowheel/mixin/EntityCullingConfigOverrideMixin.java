package dev.lapt.nowheel.mixin;

import dev.lapt.nowheel.NowheelMod;
import dev.lapt.nowheel.config.NowheelConfig;
import dev.tr7zw.entityculling.EntityCullingModBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityCullingModBase.class, remap = false)
public class EntityCullingConfigOverrideMixin {

    @Inject(
        method = "onInitialize", at = @At(
        value = "INVOKE", target = "Ldev/tr7zw/entityculling/versionless/EntityCullingVersionlessBase;onInitialize()V", shift = At.Shift.AFTER
    )
    )
    private void nowheel$overrideCullingLimits(CallbackInfo ci) {
        if (!NowheelConfig.get().overrideEntityCulling) {
            return;
        }
        EntityCullingModBase ec = (EntityCullingModBase) (Object) this;
        // Entity Culling assumes that the entites/block entities work like in vanilla, which is just not how it is in Create
        ec.config.tracingDistance = Math.max(ec.config.tracingDistance, 384);
        ec.config.hitboxLimit = Math.max(ec.config.hitboxLimit, 1000);
        // We handle these, so let them be culled
        ec.config.blockEntityWhitelist.remove("create:rope_pulley");
        ec.config.blockEntityWhitelist.remove("create:hose_pulley");
    }
}
