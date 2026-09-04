package dev.lapt.nowheel.mixin.tick_culling;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.contraptions.IControlContraption;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import dev.lapt.nowheel.config.NowheelConfig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import static dev.lapt.nowheel.util.IsCulledUtil.isCulled;

@Mixin(Level.class)
public abstract class LevelMixin {

    @WrapOperation(
        method = "tickBlockEntities",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/TickingBlockEntity;tick()V"
        )
    )
    private void nowheel$cullCulledTick(TickingBlockEntity ticker, Operation<Void> original) {
        if (!nowheel$isTickerCulled(ticker)) {
            original.call(ticker);
        }
    }

    @Unique
    private boolean nowheel$isTickerCulled(TickingBlockEntity ticker) {
        var level = (Level) (Object) this;
        if (!level.isClientSide
            || !NowheelConfig.get().tickCulling
            || !(ticker instanceof LevelChunk.RebindableTickingBlockEntityWrapper tickerWrapper)
            || !(tickerWrapper.ticker instanceof LevelChunk.BoundTickingBlockEntity<?> boundTicker))
        {
            return false;
        }

        BlockEntity blockEntity = boundTicker.blockEntity;
        return blockEntity instanceof SmartBlockEntity
            && !(blockEntity instanceof IControlContraption)
            && isCulled(blockEntity);
    }
}
