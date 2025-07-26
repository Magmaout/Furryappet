package magmaout.furryappet.events;

import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.data.capability.FurryCapability;
import magmaout.furryappet.api.data.save.ProfilesSavedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventHandler {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @SubscribeEvent
    public void entityJoin(EntityJoinWorldEvent event) {
//        if (event.getEntity() instanceof EntityPlayerMP) {
//            ProfilesSavedData data = (ProfilesSavedData) event.getWorld().loadData(ProfilesSavedData.class, ProfilesSavedData.ID);
//        }
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
