package dev.lapt.nowheel.flywheel;

import dev.engine_room.flywheel.api.visualization.VisualManager;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.lapt.nowheel.cull.CullTransitionListener;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public final class FlywheelVisualToggleListener<T> implements CullTransitionListener<T> {

    private final Function<T, Level> levelOf;
    private final Function<VisualizationManager, VisualManager<T>> managerOf;

    public FlywheelVisualToggleListener(
        Function<T, Level> levelOf,
        Function<VisualizationManager, VisualManager<T>> managerOf
    )
    {
        this.levelOf = levelOf;
        this.managerOf = managerOf;
    }

    @Override
    public void onCullChanged(T subject, boolean nowCulled) {
        Level level = levelOf.apply(subject);
        if (level == null) return;
        VisualizationManager manager = VisualizationManager.get(level);
        if (manager == null) return;

        VisualManager<T> visuals = managerOf.apply(manager);
        if (nowCulled) {
            visuals.queueRemove(subject);
        }
        else {
            visuals.queueAdd(subject);
        }
    }
}
