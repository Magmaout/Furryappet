package magmaout.furryappet;

import magmaout.furryappet.api.states.StatesManager;
import magmaout.furryappet.client.InputHandler;
import magmaout.furryappet.client.RenderHandler;
import magmaout.furryappet.network.PacketWorldInfo;
import mchorse.mclib.network.AbstractDispatcher;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Furryappet.MODID, name = Furryappet.TITLE, version = "5.0.07")
public class Furryappet {
    public static final String TITLE = "Furryappet";
    public static final String MODID = "furryappet";

    public static int FIELD_LENGTH = 32767;

    public static final AbstractDispatcher DISPATCHER = new AbstractDispatcher(Furryappet.MODID) {
        @Override
        public void register() {
            register(PacketWorldInfo.class, PacketWorldInfo.class, Side.CLIENT);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ignored) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            InputHandler.register();
            RenderHandler.register();
        }
        StatesManager.register();
        EventHandler.register();
        DISPATCHER.register();
    }
}