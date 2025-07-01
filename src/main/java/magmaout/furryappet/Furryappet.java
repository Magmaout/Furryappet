package magmaout.furryappet;

import magmaout.furryappet.client.InputHandler;
import magmaout.furryappet.client.RenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Furryappet.MODID, name = Furryappet.TITLE, version = "5.0.07")
public class Furryappet {
    public static final String TITLE = "Furryappet";
    public static final String MODID = "furryappet";

    public static MinecraftServer getServer() {
        if (FMLCommonHandler.instance().getSide().isServer())
            return FMLCommonHandler.instance().getMinecraftServerInstance();
        return Minecraft.getMinecraft().getIntegratedServer();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ignored) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            InputHandler.register();
            RenderHandler.register();
            SaveHandler.register();
        }
    }
}