package magmaout.furryappet.events;

import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.data.capability.FurryCapability;
import magmaout.furryappet.network.Dispatcher;
import magmaout.furryappet.network.api.data.PlayerContainerSyncPacket;
import magmaout.furryappet.network.api.data.SyncContainerSyncPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventHandler {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event) {
        Furryappet.APIManager.update();
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayerMP) {
            event.addCapability(new ResourceLocation(Furryappet.MODID, "furry"), new FurryCapability());
        }
    }
}
