package net.minecraftforge.items.component;

import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public interface IMutableDataComponentHolder extends DataComponentHolder {
    <T> T set(DataComponentType<? super T> componentType, @Nullable T value);
    <T> T remove(DataComponentType<? extends T> p_333259_);

    @SuppressWarnings("unchecked")
    default <A extends ImmutableProvider<B>, B extends MutableProvider<A>, T extends ImmutabilityDefiner<A, B> & MutableProvider<A>> void modify(DataComponentType<T> type, Consumer<A> modifyConsumer) {
        var comp = get(type);
        if (comp == null) return;
        A mutable = comp.mutable();
        modifyConsumer.accept(mutable);
        set(type, (T) mutable.immutable());
    }
}
