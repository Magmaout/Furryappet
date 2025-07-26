package magmaout.furryappet.network;

import magmaout.furryappet.Furryappet;
import mchorse.mclib.network.AbstractDispatcher;
import mchorse.mclib.network.AbstractMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class Dispatcher extends AbstractDispatcher {
    public static final Dispatcher INSTANCE = new Dispatcher();

    public Dispatcher() {
        super(Furryappet.MODID);
    }

    @Override
    public void register() {
        register(StatesPacket.class);
    }

    public <T extends AbstractMessageHandler<T> & IMessage> void register(Class<T> message, Side side) {
        register(message, message, side);
    }

    public <T extends AbstractMessageHandler<T> & IMessage> void register(Class<T> message) {
        register(message, message, Side.SERVER);
        register(message, message, Side.CLIENT);
    }
}
