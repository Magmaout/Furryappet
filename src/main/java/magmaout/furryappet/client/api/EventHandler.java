package magmaout.furryappet.client.api;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class EventHandler {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
    @SubscribeEvent
    public void connectedEvent(FMLNetworkEvent.ClientConnectedToServerEvent connectedEvent) {
        ClientAPIManager.instance = new ClientAPIManager();
        ClientAPIManager.instance.init();

    }
    @SubscribeEvent
    public void disconnectedEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent disconnectedEvent) {
        ClientAPIManager.instance = null;
    }
}
