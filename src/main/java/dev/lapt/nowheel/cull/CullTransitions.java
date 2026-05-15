package dev.lapt.nowheel.cull;

import dev.lapt.nowheel.NowheelMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class CullTransitions<T> {
    public static final CullTransitions<BlockEntity> BE = new CullTransitions<>();
    public static final CullTransitions<Entity> ENTITY = new CullTransitions<>();

    private final List<CullTransitionListener<T>> listeners = new CopyOnWriteArrayList<>();

    private CullTransitions() {
    }

    public void register(CullTransitionListener<T> listener) {
        listeners.add(listener);
    }

    public void notify(T subject, boolean nowCulled) {
        for (CullTransitionListener<T> l : listeners) {
            try {
                l.onCullChanged(subject, nowCulled);
            } catch (Throwable t) {
                NowheelMod.LOGGER.error(
                    "[nowheel] cull-transition listener {} errored", l.getClass().getName(), t);
            }
        }
    }
}
