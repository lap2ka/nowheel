package dev.lapt.nowheel.bbox;

import com.simibubi.create.content.contraptions.pulley.PulleyBlockEntity;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltSlope;
import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import com.simibubi.create.content.trains.track.BezierConnection;
import com.simibubi.create.content.trains.track.TrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

// Fallbacks for the stuff that breaks
public final class CreateBoxes {

    private CreateBoxes() {
    }

    // Stolen from SmartBounds lol
    public static AABB chainConveyor(ChainConveyorBlockEntity ccbe, BlockPos pos) {
        double minX = pos.getX();
        double minY = pos.getY();
        double minZ = pos.getZ();
        double maxX = pos.getX() + 1.0;
        double maxY = pos.getY() + 1.0;
        double maxZ = pos.getZ() + 1.0;
        for (BlockPos rel : ccbe.connections) {
            double cx = pos.getX() + rel.getX();
            double cy = pos.getY() + rel.getY();
            double cz = pos.getZ() + rel.getZ();
            if (cx < minX) minX = cx;
            if (cy < minY) minY = cy;
            if (cz < minZ) minZ = cz;
            if (cx + 1.0 > maxX) maxX = cx + 1.0;
            if (cy + 1.0 > maxY) maxY = cy + 1.0;
            if (cz + 1.0 > maxZ) maxZ = cz + 1.0;
        }
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    // Stolen from SmartBounds lol
    public static AABB beltController(BeltBlockEntity bbe, BlockPos pos) {
        int l = bbe.beltLength - 1;
        if (l <= 0) return new AABB(pos);
        BlockState state = bbe.getBlockState();
        Direction dir = state.getValue(BeltBlock.HORIZONTAL_FACING);
        BeltSlope slope = state.getValue(BeltBlock.SLOPE);
        AABB aabb = new AABB(pos);
        if (slope == BeltSlope.VERTICAL) {
            int dy = dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? l : -l;
            return aabb.expandTowards(0, dy, 0);
        }
        Vec3i n = dir.getNormal();
        int dy = switch (slope) {
            case UPWARD -> l;
            case DOWNWARD -> -l;
            default -> 0;
        };
        return aabb.expandTowards(n.getX() * l, dy, n.getZ() * l);
    }

    public static AABB track(TrackBlockEntity tbe, BlockPos pos) {
        AABB box = new AABB(pos);
        for (BezierConnection bc : tbe.getConnections().values()) {
            if (bc == null) continue;
            AABB b = bc.getBounds();
            if (b != null) box = box.minmax(b);
        }
        return box;
    }

    public static AABB ropePulley(PulleyBlockEntity pulley, BlockPos pos) {
        int extend = Math.max(0, (int) Math.ceil(pulley.offset));
        return new AABB(pos).expandTowards(0, -extend, 0);
    }

    public static AABB hosePulley(HosePulleyBlockEntity pulley, BlockPos pos) {
        int extend = Math.max(0, (int) Math.ceil(pulley.getInterpolatedOffset(0f)));
        return new AABB(pos).expandTowards(0, -extend, 0);
    }

    public static AABB fluidTankController(FluidTankBlockEntity tank, BlockPos pos) {
        int width = Math.max(1, tank.getWidth()) - 1;
        int height = Math.max(1, tank.getHeight()) - 1;
        return new AABB(pos).expandTowards(width, height, width);
    }
}
