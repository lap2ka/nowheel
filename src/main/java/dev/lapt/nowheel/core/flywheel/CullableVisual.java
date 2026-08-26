package dev.lapt.nowheel.core.flywheel;

import dev.engine_room.flywheel.api.visual.Visual;

public interface CullableVisual {
    boolean nowheel$isCulled();

    static boolean isVisualCulled(Visual visual) {
        return visual instanceof CullableVisual c && c.nowheel$isCulled();
    }
}
