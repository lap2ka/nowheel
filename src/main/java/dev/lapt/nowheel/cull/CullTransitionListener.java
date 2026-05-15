package dev.lapt.nowheel.cull;

@FunctionalInterface
public interface CullTransitionListener<T> {
    void onCullChanged(T subject, boolean nowCulled);
}
