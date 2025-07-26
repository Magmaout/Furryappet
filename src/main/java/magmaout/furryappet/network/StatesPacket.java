package magmaout.furryappet.network;

import io.netty.buffer.ByteBuf;
import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.states.States;
import magmaout.furryappet.api.states.StatesAPI;
import magmaout.furryappet.client.gui.GuiDashboard;
import mchorse.mclib.network.AbstractMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class StatesPacket extends AbstractMessageHandler<StatesPacket> implements IMessage {
    public String owner;
    public States states;

    public StatesPacket() {}

    public StatesPacket(String owner) {
        this.owner = owner;
    }

    public StatesPacket(String owner, States states) {
        this.owner = owner;
        this.states = states;
    }

    @Override
    public IMessage handleClientMessage(StatesPacket message) {
        GuiDashboard dashboard = GuiDashboard.get(Minecraft.getMinecraft());
        dashboard.general.statesEditor.setupStates(message.owner, message.states);
        return null;
    }

    @Override
    public IMessage handleServerMessage(EntityPlayerMP target, StatesPacket message) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        StatesAPI api = Furryappet.APIManager.getStatesAPI();
        if (message.owner.equals(server.getEntityWorld().getWorldInfo().getWorldName())) {
            message.states = api.getServerStates();
        } else {
            EntityPlayerMP player = server.getPlayerList().getPlayerByUsername(message.owner);
            message.states = api.getPlayerStates(player);
        }
        Dispatcher.INSTANCE.sendTo(message, target);
        return null;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, owner);
        ByteBufUtils.writeTag(buf, states.toNBT(true));
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        owner = ByteBufUtils.readUTF8String(buf);
        states = new States().fromNBT(ByteBufUtils.readTag(buf), true);
    }
}