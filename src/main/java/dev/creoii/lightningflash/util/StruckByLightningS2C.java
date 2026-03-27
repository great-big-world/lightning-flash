package dev.creoii.lightningflash.util;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record StruckByLightningS2C(UUID uuid) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StruckByLightningS2C> PACKET_ID = new CustomPacketPayload.Type<>(ResourceLocation.parse("great_big_world:struck_by_lightning"));
    public static final StreamCodec<RegistryFriendlyByteBuf, StruckByLightningS2C> PACKET_CODEC = StreamCodec.ofMember(StruckByLightningS2C::write, StruckByLightningS2C::new);

    public StruckByLightningS2C(RegistryFriendlyByteBuf buf) {
        this(buf.readUUID());
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeUUID(uuid);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_ID;
    }
}
