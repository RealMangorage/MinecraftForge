package net.minecraftforge.items;

public interface IMutableDataType<T, M> {
    M mutable();
    T immutable();

}
