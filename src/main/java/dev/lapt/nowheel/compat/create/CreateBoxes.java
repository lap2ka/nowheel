package dev.lapt.nowheel.compat.create;

import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltSlope;
import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import com.simibubi.create.content.kinetics.waterwheel.LargeWaterWheelBlockEntity;
import com.simibubi.create.content.kinetics.waterwheel.WaterWheelBlockEntity;
import com.simibubi.create.content.trains.track.BezierConnection;
import com.simibubi.create.content.trains.track.TrackBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

// Fallbacks for the stuff that breaks
public final class CreateBoxes {

    private CreateBoxes() { }

    // Stolen from SmartBounds lol
    // Not really needed with the crbb.getRenderBoundingBox thing, but players without Smart Bounds will get miserable perf without this
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

    public static AABB waterWheel(WaterWheelBlockEntity wheel, BlockPos pos) {
        int size = wheel instanceof LargeWaterWheelBlockEntity ? 2 : 1;
        return new AABB(pos).inflate(size);
    }
}
