package magmaout.furryappet.network.api.data;

import io.netty.buffer.ByteBuf;
import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.data.INBTData;
import magmaout.furryappet.client.api.ClientAPIManager;
import magmaout.furryappet.client.api.data.WrapperFilesContainer;
import magmaout.furryappet.network.Dispatcher;
import mchorse.mclib.network.AbstractMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.HashSet;
import java.util.Set;

public class FilesContainerSyncPacket extends AbstractMessageHandler<FilesContainerSyncPacket> implements IMessage {
    public NBTTagCompound data;
    public Action action;
    public String id;
    public byte messageID;

    public FilesContainerSyncPacket() {}

    public FilesContainerSyncPacket(String containerID, Action action, NBTTagCompound data, byte id) {
        this.action = action;
        this.data = data;
        this.id = containerID;
        this.messageID = id;
    }

    @Override
    public IMessage handleClientMessage(FilesContainerSyncPacket message) {
        WrapperFilesContainer<? extends INBTData> filesWrapperData = ClientAPIManager.getInstance().getDataAPI().getFilesWrapperData(message.id);
        switch (message.action) {
            case CREATE:
            case DUPLICATE:
            case DELETE:
            case RENAME: {
                filesWrapperData.handleMessage(message.messageID, message.data.getBoolean("success"));
                break;
            }
            case NAMES: {
                HashSet<String> names = new HashSet<>();
                NBTTagList namesTag = message.data.getTagList("names", 8);
                for (NBTBase nbtBase : namesTag) {
                    names.add(((NBTTagString)nbtBase).getString());
                }
                filesWrapperData.handleMessage(message.messageID, names);
                break;
            }
            case LOAD: {
                INBTData data = filesWrapperData.instanceCreator.get();
                data.fromNBT(message.data);
                filesWrapperData.handleMessage(message.messageID, data);
                break;
            }
        }
        return null;
    }

    @Override
    public IMessage handleServerMessage(EntityPlayerMP target, FilesContainerSyncPacket message) {
        switch (message.action) {
            case CREATE: {
                boolean success = Furryappet.APIManager.getDataAPI().getFilesContainer(message.id).createData(message.data.getString("name"));
                NBTTagCompound tag = new NBTTagCompound();
                tag.setBoolean("success", success);
                Dispatcher.INSTANCE.sendTo(new FilesContainerSyncPacket(message.id, message.action, tag, message.messageID), target);
                break;
            }
            case DUPLICATE: {
                boolean success = Furryappet.APIManager.getDataAPI().getFilesContainer(message.id).duplicateData(message.data.getString("name"), message.data.getString("newName"));
                NBTTagCompound tag = new NBTTagCompound();
                tag.setBoolean("success", success);
                Dispatcher.INSTANCE.sendTo(new FilesContainerSyncPacket(message.id, message.action, tag, message.messageID), target);
                break;
            }
            case DELETE: {
                boolean success = Furryappet.APIManager.getDataAPI().getFilesContainer(message.id).deleteData(message.data.getString("name"));
                NBTTagCompound tag = new NBTTagCompound();
                tag.setBoolean("success", success);
                Dispatcher.INSTANCE.sendTo(new FilesContainerSyncPacket(message.id, message.action, tag, message.messageID), target);
                break;
            }
            case RENAME: {
                boolean success = Furryappet.APIManager.getDataAPI().getFilesContainer(message.id).renameData(message.data.getString("name"), message.data.getString("newName"));
                NBTTagCompound tag = new NBTTagCompound();
                tag.setBoolean("success", success);
                Dispatcher.INSTANCE.sendTo(new FilesContainerSyncPacket(message.id, message.action, tag, message.messageID), target);
                break;
            }
            case NAMES: {
                Set<String> success = Furryappet.APIManager.getDataAPI().getFilesContainer(message.id).getDataNames();
                NBTTagCompound tag = new NBTTagCompound();
                NBTTagList list = new NBTTagList();
                for (String name : success) {
                    list.appendTag(new NBTTagString(name));
                }
                tag.setTag("names", list);
                Dispatcher.INSTANCE.sendTo(new FilesContainerSyncPacket(message.id, message.action, tag, message.messageID), target);
                break;
            }
            case LOAD: {
                INBTData data = Furryappet.APIManager.getDataAPI().getFilesContainer(message.id).getData(message.data.getString("name"));
                Dispatcher.INSTANCE.sendTo(new FilesContainerSyncPacket(message.id, message.action, data.toNBT(), message.messageID), target);
                break;
            }
            case SAVE: {
                INBTData data = Furryappet.APIManager.getDataAPI().getFilesContainer(message.id).getData(message.data.getString("name"));
                data.fromNBT(message.data);
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
        CREATE,
        DUPLICATE,
        DELETE,
        RENAME,
        NAMES,
        LOAD,
        SAVE;
    }
}
