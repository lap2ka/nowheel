package dev.lapt.nowheel.core.flywheel;

import dev.engine_room.flywheel.api.visual.Visual;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.AbstractEntityVisual;

public interface CullableVisual {
    // Doing this makes the inliner very happy
    static boolean isVisualCulled(Visual visual) {
        //noinspection rawtypes
        if (visual instanceof AbstractBlockEntityVisual v) return ((CullableVisual) v).nowheel$isCulled();
        return isEntityVisualCulled(visual);
    }

    private static boolean isEntityVisualCulled(Visual visual) {
        return visual instanceof AbstractEntityVisual<?> v && ((CullableVisual) v).nowheel$isCulled();
    }

    boolean nowheel$isCulled();
}
