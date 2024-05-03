package net.minecraftforge.items;

import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public interface IMutableDataComponentHolder extends DataComponentHolder {
    <T> T set(DataComponentType<? super T> componentType, @Nullable T value);
    <T> T remove(DataComponentType<? extends T> p_333259_);

    @SuppressWarnings("unchecked")
    default <M extends T, T extends IMutableDataType<T, M>> void modify(DataComponentType<? extends T> componentType, Consumer<M> modifiableValue) {
        T currentValue = get(componentType);
        if (currentValue != null) {
            M modifiedVal = currentValue.mutable();
            modifiableValue.accept(modifiedVal);
            T finishedValue = modifiedVal.immutable();
            set((DataComponentType<? super T>) componentType, finishedValue);
        }
    }
}
