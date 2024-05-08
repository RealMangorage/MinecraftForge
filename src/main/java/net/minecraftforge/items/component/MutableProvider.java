package net.minecraftforge.items.component;

public interface MutableProvider<M, O> {
    M mutable(O object);
}
