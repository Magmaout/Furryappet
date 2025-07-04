package magmaout.furryappet;

import magmaout.furryappet.network.PacketWorldInfo;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class EventHandler {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @SubscribeEvent
    public void playerEntry(PlayerEvent.PlayerLoggedInEvent event) {
        Furryappet.DISPATCHER.sendTo(new PacketWorldInfo(), (EntityPlayerMP) event.player);
    }
}
