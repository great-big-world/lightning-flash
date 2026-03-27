package dev.creoii.lightningflash.util;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record StruckByLightningS2C(int id) implements FabricPacket {
    public static final ResourceLocation PACKET_ID = ResourceLocation.tryParse("great_big_world:struck_by_lightning");
    public static final PacketType<StruckByLightningS2C> TYPE = PacketType.create(PACKET_ID, StruckByLightningS2C::new);

    public StruckByLightningS2C(FriendlyByteBuf buf) {
        this(buf.readVarInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(id);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }
}
