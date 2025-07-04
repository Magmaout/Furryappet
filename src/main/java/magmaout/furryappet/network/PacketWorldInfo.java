package magmaout.furryappet.network;

import io.netty.buffer.ByteBuf;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketWorldInfo extends ClientMessageHandler<PacketWorldInfo> implements IMessage {
    public static String NAME = "Undefined";
    public static String SEED = "Undefined";

    @Override
    public void toBytes(ByteBuf buf) {
        WorldInfo world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getWorldInfo();
        ByteBufUtils.writeUTF8String(buf, world.getWorldName());
        ByteBufUtils.writeUTF8String(buf, String.valueOf(world.getSeed()));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NAME = ByteBufUtils.readUTF8String(buf);
        SEED = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void run(EntityPlayerSP player, PacketWorldInfo message) {}
}
