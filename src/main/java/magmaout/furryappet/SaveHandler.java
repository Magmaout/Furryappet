package magmaout.furryappet;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SaveHandler {
    public static String WORLD_PATH;

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new SaveHandler());
    }

    @SideOnly(Side.CLIENT)
    public static String configPath() {
        return Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "/config/" + Furryappet.MODID + "/";
    }

    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event) {
        WORLD_PATH = event.getWorld().getSaveHandler().getWorldDirectory().getAbsolutePath();
    }
}