package magmaout.furryappet.api.huds;

import magmaout.furryappet.api.APIManager;
import magmaout.furryappet.api.BaseAPI;
import magmaout.furryappet.api.data.FilesContainer;
import magmaout.furryappet.api.data.PlayerContainer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

public class HUDsAPI extends BaseAPI {
    private final FilesContainer<HUDData> huds;
    private final PlayerContainer<HUDScene> scenePlayerContainer;
    public HUDsAPI(APIManager api) {
        huds = api.getDataAPI().registerFilesContainer("huds", HUDData::new);
        scenePlayerContainer = api.getDataAPI().registerSyncPlayerContainer("scene", HUDScene::new);
    }

    public boolean setupHUD(EntityPlayerMP player, String hud) {
        HUDData data = huds.getData(hud);
        if(data == null) {
            return false;
        }

        HUDScene scene = scenePlayerContainer.getData(player);
        scene.getHudMorphs().put(data.name, data.copy());
        return true;
    }
    public List<HUDMorph> getHUD(EntityPlayerMP player, String hud) {
        return scenePlayerContainer.getData(player).getHudMorphs().get(hud);
    }
    public boolean isHUDOpen(EntityPlayerMP player, String hud) {
        return scenePlayerContainer.getData(player).getHudMorphs().containsKey(hud);
    }
    public void closeHUD(EntityPlayerMP player, String hud) {
        scenePlayerContainer.getData(player).getHudMorphs().remove(hud);
    }
    public void closeAllHUDs(EntityPlayerMP player) {
        scenePlayerContainer.getData(player).getHudMorphs().clear();
    }
    public HUDScene getScene(EntityPlayerMP player) {
        return scenePlayerContainer.getData(player);
    }
}
