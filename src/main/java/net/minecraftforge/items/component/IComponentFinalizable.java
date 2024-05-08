package net.minecraftforge.items.component;


public interface IComponentFinalizable {
    // Will set the Component onto the ItemStack
    default void finalizeComponent() {}
}
