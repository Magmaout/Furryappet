package magmaout.furryappet;

import magmaout.furryappet.api.data.capability.FurryappetCapability;
import magmaout.furryappet.network.PacketWorldInfo;
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
    public void playerEntry(PlayerEvent.PlayerLoggedInEvent event) {
        Furryappet.DISPATCHER.sendTo(new PacketWorldInfo(), (EntityPlayerMP) event.player);
    }
    @SubscribeEvent
    public void playerEntry(TickEvent.ServerTickEvent event) {
        Furryappet.furryappetAPIManager.update();
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayerMP) {
            event.addCapability(new ResourceLocation(Furryappet.MODID, "uwu"), new FurryappetCapability());
        }
    }
}
