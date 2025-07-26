package magmaout.furryappet.api.states;

import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.BaseAPI;
import magmaout.furryappet.api.data.PlayerContainer;
import magmaout.furryappet.api.data.save.StatesSavedData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class StatesAPI extends BaseAPI {
    private final PlayerContainer<States> statesContainer;

    public StatesAPI() {
        statesContainer = Furryappet.APIManager.getDataAPI().registerPlayerContainer("states", States::new);
    }

    public States getPlayerStates(EntityPlayerMP player) {
        if (player == null) return null;
        return statesContainer.getData(player);
    }

    public States getServerStates() {
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
        StatesSavedData data = (StatesSavedData) world.loadData(StatesSavedData.class, StatesSavedData.ID);
        if (data == null) {
            data = new StatesSavedData();
            world.setData(StatesSavedData.ID, data);
        }
        return data.states;
    }
}
