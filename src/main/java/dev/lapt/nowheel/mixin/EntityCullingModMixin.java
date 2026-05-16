package dev.lapt.nowheel.mixin;

import com.simibubi.create.content.contraptions.pulley.PulleyBlockEntity;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import com.simibubi.create.content.trains.track.TrackBlockEntity;
import dev.lapt.nowheel.bbox.CreateBoxes;
import dev.tr7zw.entityculling.EntityCullingMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Fallbacks for the stuff that breaks
@Mixin(value = EntityCullingMod.class, remap = false)
public class EntityCullingModMixin {

    @Inject(
        method = "setupAABB(Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/AABB;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void nowheel$properAABB(BlockEntity entity, BlockPos pos, CallbackInfoReturnable<AABB> cir) {
        if (entity instanceof ChainConveyorBlockEntity chainConveyor) {
            cir.setReturnValue(CreateBoxes.chainConveyor(chainConveyor, pos));
        } else if (entity instanceof BeltBlockEntity belt && belt.isController()) {
            cir.setReturnValue(CreateBoxes.beltController(belt, pos));
        } else if (entity instanceof FluidTankBlockEntity tank && tank.isController()) {
            cir.setReturnValue(CreateBoxes.fluidTankController(tank, pos));
        } else if (entity instanceof TrackBlockEntity track && !track.getConnections().isEmpty()) {
            cir.setReturnValue(CreateBoxes.track(track, pos));
        } else if (entity instanceof PulleyBlockEntity rope) {
            cir.setReturnValue(CreateBoxes.ropePulley(rope, pos));
        } else if (entity instanceof HosePulleyBlockEntity hose) {
            cir.setReturnValue(CreateBoxes.hosePulley(hose, pos));
        }
    }
}
