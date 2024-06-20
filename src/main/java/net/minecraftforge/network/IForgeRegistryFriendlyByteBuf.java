package net.minecraftforge.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.Connection;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.function.Function;

public interface IForgeRegistryFriendlyByteBuf {
    default RegistryFriendlyByteBuf self() {
        return (RegistryFriendlyByteBuf) this;
    }

    default ConnectionType getConnectionType() {
        var connection = getConnection();
        if (connection == null) return null;
        return NetworkContext.get(connection).getType();
    }

    Connection getConnection();

    static Function<ByteBuf, RegistryFriendlyByteBuf> decorator(RegistryAccess p_336066_, Connection connection) {
        net.minecraft.server.network.ServerConfigurationPacketListenerImpl#handleConfigurationFinished
        return p_328649_ -> new RegistryFriendlyByteBuf(p_328649_, p_336066_, connection);
    }
}
