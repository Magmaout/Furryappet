package magmaout.furryappet.api.states;

import magmaout.furryappet.Furryappet;
import magmaout.furryappet.api.states.save.StatesCapability;
import magmaout.furryappet.api.states.save.StatesSavedData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StatesManager {
    private static final String SERVER_STATES_ID = "furryappet_server_states";
    private static final String WORLD_STATES_ID = "furryappet_world_states_";
    private static final String PLAYER_STATES_ID = "player_states";
    public static States getPlayerStates(EntityPlayerMP player) {
        if(player == null) {
            return null;
        }
        return player.getCapability(StatesCapability.STATES, null);
    }
    public static States getServerStates() {
        World world = Furryappet.getServer().getWorld(0);
        StatesSavedData statesData = (StatesSavedData) world.loadData(StatesSavedData.class, SERVER_STATES_ID);
        if(statesData == null) {
            statesData = new StatesSavedData(SERVER_STATES_ID);
            world.setData(SERVER_STATES_ID, statesData);
        }

        return statesData.states;
    }
    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof EntityPlayerMP) {
            event.addCapability(new ResourceLocation(Furryappet.MODID, PLAYER_STATES_ID), new StatesCapability());
        }
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(States.class, new Capability.IStorage<States>() {
            @Override
            public NBTBase writeNBT(Capability<States> capability, States instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<States> capability, States instance, EnumFacing side, NBTBase nbt) {
                instance.deserializeNBT((NBTTagCompound) nbt);
            }
        }, States::new);

        MinecraftForge.EVENT_BUS.register(new StatesManager());
    }
}
