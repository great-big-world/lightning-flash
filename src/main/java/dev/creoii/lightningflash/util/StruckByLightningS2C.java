package dev.creoii.lightningflash.util;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.UUID;

public record StruckByLightningS2C(UUID uuid) implements CustomPayload {
    public static final CustomPayload.Id<StruckByLightningS2C> PACKET_ID = new CustomPayload.Id<>(Identifier.of("great_big_world:struck_by_lightning"));
    public static final PacketCodec<RegistryByteBuf, StruckByLightningS2C> PACKET_CODEC = PacketCodec.of(StruckByLightningS2C::write, StruckByLightningS2C::new);

    public StruckByLightningS2C(RegistryByteBuf buf) {
        this(buf.readUuid());
    }

    public void write(RegistryByteBuf buf) {
        buf.writeUuid(uuid);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
