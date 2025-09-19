package magmaout.furryappet.network.api.data;

import io.netty.buffer.ByteBuf;
import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.data.INBTData;
import magmaout.furryappet.client.api.ClientAPIManager;
import magmaout.furryappet.client.api.data.WrapperPlayerContainer;
import magmaout.furryappet.network.Dispatcher;
import mchorse.mclib.network.AbstractMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerContainerSyncPacket extends AbstractMessageHandler<PlayerContainerSyncPacket> implements IMessage {
    public NBTTagCompound data;
    public Action action;
    public String id;
    public byte messageID;

    public PlayerContainerSyncPacket() {}

    public PlayerContainerSyncPacket(String containerID, PlayerContainerSyncPacket.Action action, NBTTagCompound data, byte id) {
        this.action = action;
        this.data = data;
        this.id = containerID;
        this.messageID = id;
    }

    @Override
    public IMessage handleClientMessage(PlayerContainerSyncPacket message) {
        WrapperPlayerContainer<? extends INBTData> filesWrapperData = ClientAPIManager.getInstance().getDataAPI().getPlayerWrapperData(message.id);
        if (message.action == Action.LOAD) {
            INBTData data = filesWrapperData.instanceCreator.get();
            data.fromNBT(message.data);
            filesWrapperData.handleMessage(message.messageID, data);
        }
        return null;
    }

    @Override
    public IMessage handleServerMessage(EntityPlayerMP target, PlayerContainerSyncPacket message) {
        switch (message.action) {
            case LOAD: {
                EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(message.data.getString("name"));
                if(player == null) {
                    break;
                }
                INBTData data = Furryappet.APIManager.getDataAPI().getPlayerContainer(message.id).getData(player);
                Dispatcher.INSTANCE.sendTo(new PlayerContainerSyncPacket(message.id, message.action, data.toNBT(), message.messageID), target);
                break;
            }
            case SAVE: {
                EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(message.data.getString("name"));
                if(player == null) {
                    break;
                }
                INBTData data = Furryappet.APIManager.getDataAPI().getPlayerContainer(message.id).getData(player);
                data.fromNBT((NBTTagCompound) message.data.getTag("data"));
                break;
            }
        }
        return null;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, data);
        buf.writeInt(action.ordinal());
        ByteBufUtils.writeUTF8String(buf, id);
        buf.writeByte(messageID);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        data = ByteBufUtils.readTag(buf);
        action = Action.values()[buf.readInt()];
        id = ByteBufUtils.readUTF8String(buf);
        messageID = buf.readByte();
    }
    public enum Action {
        LOAD,
        SAVE;
    }
}