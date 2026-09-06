package dev.lapt.nowheel.mixin.flywheel;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.engine_room.flywheel.api.task.Plan;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visual.TickableVisual;
import dev.engine_room.flywheel.api.visual.Visual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.impl.visualization.storage.Storage;
import dev.engine_room.flywheel.lib.task.ConditionalPlan;
import dev.engine_room.flywheel.lib.task.functional.ConsumerWithContext;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import dev.engine_room.flywheel.lib.visual.SimpleTickableVisual;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.lapt.nowheel.core.util.CullingUtil.isRemovingBackend;
import static dev.lapt.nowheel.core.flywheel.CullableVisual.isVisualCulled;
import static dev.lapt.nowheel.core.util.IsCulledUtil.isCulled;

@Mixin(
    value = Storage.class,
    remap = false
)
public abstract class StorageMixin {

    @Inject(
        method = "add",
        at = @At("HEAD"),
        cancellable = true
    )
    private void nowheel$skipCulledAdd(VisualizationContext visualizationContext, Object obj, float partialTick, CallbackInfo ci) {
        if (isRemovingBackend() && isCulled(obj)) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(
        method = "setup",
        at = @At(
            value = "INVOKE",
            target = "Ldev/engine_room/flywheel/api/visual/DynamicVisual;planFrame()Ldev/engine_room/flywheel/api/task/Plan;"
        )
    )
    private Plan<DynamicVisual.Context> nowheel$gateFrame(Plan<DynamicVisual.Context> plan, @Local(argsOnly = true) Visual visual) {
        if (isRemovingBackend()) {
            return plan;
        }
        return ConditionalPlan.<DynamicVisual.Context>on(() -> !isVisualCulled(visual)).then(plan);
    }

    @ModifyExpressionValue(
        method = "setup",
        at = @At(
            value = "INVOKE",
            target = "Ldev/engine_room/flywheel/api/visual/TickableVisual;planTick()Ldev/engine_room/flywheel/api/task/Plan;"
        )
    )
    private Plan<TickableVisual.Context> nowheel$gateTick(Plan<TickableVisual.Context> plan, @Local(argsOnly = true) Visual visual) {
        if (isRemovingBackend()) {
            return plan;
        }
        return ConditionalPlan.<TickableVisual.Context>on(() -> !isVisualCulled(visual)).then(plan);
    }

    @ModifyArg(
        method = "framePlan",
        at = @At(value = "INVOKE", target = "Ldev/engine_room/flywheel/lib/task/ForEachPlan;of(Ldev/engine_room/flywheel/lib/task/functional/SupplierWithContext$Ignored;Ldev/engine_room/flywheel/lib/task/functional/ConsumerWithContext;)Ldev/engine_room/flywheel/lib/task/ForEachPlan;"),
        index = 1
    )
    private ConsumerWithContext<SimpleDynamicVisual, DynamicVisual.Context> nowheel$gateSimpleFrame(ConsumerWithContext<SimpleDynamicVisual, DynamicVisual.Context> beginFrame) {
        if (isRemovingBackend()) {
            return beginFrame;
        }
        return (visual, context) -> {
            if (!isVisualCulled(visual)) {
                beginFrame.accept(visual, context);
            }
        };
    }

    @ModifyArg(
        method = "tickPlan",
        at = @At(value = "INVOKE", target = "Ldev/engine_room/flywheel/lib/task/ForEachPlan;of(Ldev/engine_room/flywheel/lib/task/functional/SupplierWithContext$Ignored;Ldev/engine_room/flywheel/lib/task/functional/ConsumerWithContext;)Ldev/engine_room/flywheel/lib/task/ForEachPlan;"),
        index = 1
    )
    private ConsumerWithContext<SimpleTickableVisual, TickableVisual.Context> nowheel$gateSimpleTick(ConsumerWithContext<SimpleTickableVisual, TickableVisual.Context> tick) {
        if (isRemovingBackend()) {
            return tick;
        }
        return (visual, context) -> {
            if (!isVisualCulled(visual)) {
                tick.accept(visual, context);
            }
        };
    }
}
