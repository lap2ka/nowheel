package dev.lapt.nowheel.mixin.entity_culling;

import dev.engine_room.flywheel.lib.visualization.VisualizationHelper;
import dev.lapt.nowheel.NowheelMod;
import dev.lapt.nowheel.config.NowheelConfig;
import dev.tr7zw.entityculling.EntityCullingModBase;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(
    value = EntityCullingModBase.class,
    remap = false
)
public class EntityCullingModBaseMixin {

    @Unique
    private static final BlockEntityRenderer<BlockEntity> nowheel$FLYWHEEL_VISUAL_RENDERER = (blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay) -> { };

    // in my testing some flywheel stuff (simple kinetic, etc) didn't appear to have a renderer,
    // nor did they subsequently get culled, so we give them one
    @SuppressWarnings({"rawtypes"})
    @Redirect(
        method = "prefetchBlockEntityData",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderDispatcher;getRenderer(Lnet/minecraft/world/level/block/entity/BlockEntity;)Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderer;",
            remap = true
        )
    )
    private BlockEntityRenderer nowheel$includeFlywheelOnlyVisuals(BlockEntityRenderDispatcher dispatcher, BlockEntity blockEntity) {
        BlockEntityRenderer renderer = dispatcher.getRenderer(blockEntity);
        if (renderer != null) return renderer;
        if (VisualizationHelper.canVisualize(blockEntity)) return nowheel$FLYWHEEL_VISUAL_RENDERER;
        return null;
    }

    @Shadow
    public Set<BlockEntityType<?>> blockEntityWhitelist;

    //? if >=1.21 {
    @Unique
    private static final ResourceLocation ROPE_PULLEY = ResourceLocation.parse("create:rope_pulley");
    @Unique
    private static final ResourceLocation HOSE_PULLEY = ResourceLocation.parse("create:hose_pulley");
    //?} else {
    /*@SuppressWarnings("removal")
    @Unique
    private static final ResourceLocation ROPE_PULLEY = new ResourceLocation("create", "rope_pulley");
    @SuppressWarnings("removal")
    @Unique
    private static final ResourceLocation HOSE_PULLEY = new ResourceLocation("create", "hose_pulley");
    *///?}
    @Unique
    private boolean nowheel$overridenWhitelist = false;

    @Inject(
        method = "clientTick",
        at = @At("TAIL")
    )
    private void nowheel$overrideWhitelist(CallbackInfo ci) {
        if (nowheel$overridenWhitelist || !NowheelConfig.get().overrideEntityCulling) {
            return;
        }
        try {
            blockEntityWhitelist.remove(BuiltInRegistries.BLOCK_ENTITY_TYPE.get(ROPE_PULLEY));
            blockEntityWhitelist.remove(BuiltInRegistries.BLOCK_ENTITY_TYPE.get(HOSE_PULLEY));
            NowheelMod.LOGGER.info("Overriden Entity Culling whitelist");
        } catch (final Throwable t) {
            NowheelMod.LOGGER.error("Error overriding whitelist, what the helly: ", t);
        }
        nowheel$overridenWhitelist = true;
    }
}
