package magmaout.furryappet.client;

import magmaout.furryappet.Furryappet;
import magmaout.furryappet.client.gui.GuiDashboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class InputHandler {
    public static final KeyBinding DASHBOARD = new KeyBinding(
        "Dashboard", Keyboard.KEY_Y, Furryappet.TITLE
    );

    public static void register() {
        ClientRegistry.registerKeyBinding(DASHBOARD);
        MinecraftForge.EVENT_BUS.register(new InputHandler());
    }

    @SubscribeEvent
    public void trigger(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (DASHBOARD.isKeyDown() && mc.playerController.isInCreativeMode())
            mc.displayGuiScreen(GuiDashboard.get(mc));
    }
}
