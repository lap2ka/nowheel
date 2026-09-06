package dev.lapt.nowheel.core.flywheel;

import dev.engine_room.flywheel.api.visual.Visual;

public interface CullableVisual {
    static boolean isVisualCulled(Visual visual) {
        return visual instanceof CullableVisual c && c.nowheel$isCulled();
    }

    boolean nowheel$isCulled();
}
