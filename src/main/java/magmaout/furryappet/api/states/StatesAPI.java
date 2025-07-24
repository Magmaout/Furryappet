package magmaout.furryappet.api.states;

import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.BaseAPI;
import magmaout.furryappet.api.data.PlayerContainer;
import magmaout.furryappet.api.states.save.StatesSavedData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class StatesAPI extends BaseAPI {
    private static final String SERVER_STATES_ID = "states";
    private final PlayerContainer<States> statesContainer;

    public StatesAPI() {
        statesContainer = Furryappet.furryappetAPIManager.getDataAPI().registerPlayerContainer("states", States::new);
    }

    public States getPlayerStates(EntityPlayerMP player) {
        if (player == null) return null;
        return statesContainer.getData(player);
    }

    public States getServerStates() {
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
        StatesSavedData data = (StatesSavedData) world.loadData(StatesSavedData.class, SERVER_STATES_ID);
        if (data == null) {
            data = new StatesSavedData(SERVER_STATES_ID);
            world.setData(SERVER_STATES_ID, data);
        }
        return data.states;
    }
}
