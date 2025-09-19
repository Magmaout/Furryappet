package magmaout.furryappet.network.api.data;

import io.netty.buffer.ByteBuf;
import magmaout.furryappet.Furryappet;
import magmaout.furryappet.client.api.ClientAPIManager;
import magmaout.furryappet.client.api.data.ClientDataAPI;
import magmaout.furryappet.network.Dispatcher;
import mchorse.mclib.network.AbstractMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncContainerSyncPacket extends AbstractMessageHandler<SyncContainerSyncPacket> implements IMessage {
    public String id;
    public NBTTagCompound data;

    public SyncContainerSyncPacket() {}

    public SyncContainerSyncPacket(String id, NBTTagCompound data) {
        this.id = id;
        this.data = data;
    }
    public SyncContainerSyncPacket(String id) {
        this.id = id;
    }

    @Override
    public IMessage handleClientMessage(SyncContainerSyncPacket message) {
        ClientAPIManager.getInstance().getDataAPI().handleSyncData(message.id, message.data);
        return null;
    }

    @Override
    public IMessage handleServerMessage(EntityPlayerMP target, SyncContainerSyncPacket message) {
        Dispatcher.INSTANCE.sendTo(new SyncContainerSyncPacket(message.id, Furryappet.APIManager.getDataAPI().getPlayerContainer(message.id).getData(target).toNBT()), target);
        return null;
    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, id);
        if(data == null) {
            buf.writeByte(0);
            return;
        }
        buf.writeByte(1);
        ByteBufUtils.writeTag(buf, data);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = ByteBufUtils.readUTF8String(buf);
        if(buf.readByte() == 0) {
            return;
        }
        data = ByteBufUtils.readTag(buf);
    }
}