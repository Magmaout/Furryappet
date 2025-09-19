package magmaout.furryappet.client.api.huds;

import magmaout.furryappet.api.huds.HUDData;
import magmaout.furryappet.api.huds.HUDMorph;
import magmaout.furryappet.api.huds.HUDScene;
import magmaout.furryappet.client.api.ClientAPIManager;
import magmaout.furryappet.client.api.ClientBaseAPI;
import magmaout.furryappet.client.api.data.ClientSyncContainer;
import magmaout.furryappet.client.api.data.WrapperFilesContainer;
import magmaout.furryappet.client.api.data.WrapperPlayerContainer;
import mchorse.mclib.utils.DummyEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ClientHUDsAPI extends ClientBaseAPI {
    private final ClientSyncContainer<HUDScene> hudScene;
    private DummyEntity entity;
    public WrapperFilesContainer<HUDData> hudsContainer;
    public WrapperPlayerContainer<HUDScene> hudsPlayerContainer;

    public ClientHUDsAPI(ClientAPIManager manager) {
        hudScene = manager.getDataAPI().registerSyncContainer("scene", new HUDScene());
        hudsContainer = manager.getDataAPI().registerFilesWrapperContainer("huds", HUDData::new);
        hudsPlayerContainer = manager.getDataAPI().registerPlayerWrapperContainer("scene", HUDScene::new);
    }
    public void renderHUDScene(float partialTicks, ScaledResolution resolution) {
        boolean lightEnabled = false;

        GlStateManager.enableAlpha();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        float lastX = OpenGlHelper.lastBrightnessX;
        float lastY = OpenGlHelper.lastBrightnessY;

        for (List<HUDMorph> value : hudScene.getData().getHudMorphs().values()) {
//            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
            for (HUDMorph hudMorph : value) {
                if (hudMorph.morph.isEmpty()) {
                    continue;
                }
                if(hudMorph.enableLight && !lightEnabled) {
                    RenderHelper.enableStandardItemLighting();
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY);
                    lightEnabled = true;
                } else if(!hudMorph.enableLight && lightEnabled) {
                    RenderHelper.disableStandardItemLighting();
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 1, 240);
                    lightEnabled = false;
                }
                GlStateManager.pushMatrix();

                float v = resolution.getScaledWidth() / 2f;
                float h = resolution.getScaledHeight() / 2f;
                float min = Math.max((float) 2560 / resolution.getScaledWidth(), (float) 1440 / resolution.getScaledHeight()) / resolution.getScaleFactor();

                GlStateManager.translate(v * hudMorph.translate.x + v, h * hudMorph.translate.y + h, hudMorph.translate.z);
                GlStateManager.rotate(hudMorph.rotate.x, 1f, 0f, 0f);
                GlStateManager.rotate(hudMorph.rotate.y, 0f, 1f, 0f);
                GlStateManager.rotate(hudMorph.rotate.z, 0f, 0f, 1f);
                GlStateManager.scale(10 * hudMorph.scale.x / min, -10 * hudMorph.scale.y / min, 10 * hudMorph.scale.z / min);

                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 1, 240);
                hudMorph.morph.get().render(getEntity(), 0, 0, 0, 0, partialTicks);
                GlStateManager.popMatrix();
            }
        }

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY);
    }
    public DummyEntity getEntity()
    {
        if (this.entity == null)
        {
            this.entity = new DummyEntity(Minecraft.getMinecraft().world);
            this.entity.rotationYaw = this.entity.prevRotationYaw = 0.0F;
            this.entity.rotationPitch = this.entity.prevRotationPitch = 0.0F;
            this.entity.rotationYawHead = this.entity.prevRotationYawHead = 0.0F;
            this.entity.renderYawOffset = this.entity.prevRenderYawOffset = 0.0F;
            this.entity.onGround = true;
        }

        return this.entity;
    }
}
