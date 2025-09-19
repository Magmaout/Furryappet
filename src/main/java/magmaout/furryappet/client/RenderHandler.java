package magmaout.furryappet.client;

import magmaout.furryappet.client.api.ClientAPIManager;
import mchorse.mclib.McLib;
import mchorse.mclib.events.RenderOverlayEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class RenderHandler {
    public static final HashMap<AxisAlignedBB, Entity> BOXES = new HashMap<>();

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new RenderHandler());
        McLib.EVENT_BUS.register(new RenderHandler());
    }

    @SubscribeEvent
    public void worldRender(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player != null && mc.world != null)
            boxesRender(mc.player, event.getPartialTicks());
    }

    private void boxesRender(EntityPlayerSP player, float partialTicks) {
        for (AxisAlignedBB box : BOXES.keySet()) {
            if (BOXES.get(box) != null) {
                Entity entity = BOXES.get(box);
                if (!entity.equals(player)) {
                    box = box.offset(new Vec3d(
                            player.prevPosX + (player.posX - player.prevPosX) * partialTicks,
                            player.prevPosY + (player.posY - player.prevPosY) * partialTicks,
                            player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks
                    ).subtract(
                            (entity.posX - entity.prevPosX) * partialTicks,
                            (entity.posY - entity.prevPosY) * partialTicks,
                            (entity.posZ - entity.prevPosZ) * partialTicks
                    ).scale(-1));
                } else {
                    box = box.offset(player.getPositionVector().scale(-1));
                }
            }
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            RenderGlobal.drawBoundingBox(
                    box.minX, box.minY, box.minZ,
                    box.maxX, box.maxY, box.maxZ,
                    0, 1, 0, 1
            );
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
        }
    }
    @SubscribeEvent
    public void onRenderGuiOverlay(RenderOverlayEvent.Pre event) {
        if(ClientAPIManager.getInstance() == null) {
            return;
        }
        ClientAPIManager.getInstance().getHUDsAPI().renderHUDScene(event.mc.getRenderPartialTicks(), event.resolution);
    }

}
