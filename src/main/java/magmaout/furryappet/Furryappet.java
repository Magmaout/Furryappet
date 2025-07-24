package magmaout.furryappet;

import magmaout.furryappet.api.FurryappetAPIManager;
import magmaout.furryappet.api.data.DataAPI;
import magmaout.furryappet.api.states.States;
import magmaout.furryappet.api.states.StatesAPI;
import magmaout.furryappet.client.InputHandler;
import magmaout.furryappet.client.RenderHandler;
import magmaout.furryappet.commands.FurryappetCommand;
import magmaout.furryappet.network.PacketWorldInfo;
import mchorse.mclib.commands.utils.L10n;
import mchorse.mclib.network.AbstractDispatcher;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Furryappet.MODID, name = Furryappet.TITLE, version = "5.0.07")
public class Furryappet {
    public static final String TITLE = "Furryappet";
    public static final String MODID = "furryappet";

    public static int STRING_LENGTH = 32767;

    public static FurryappetAPIManager furryappetAPIManager;

    public static final AbstractDispatcher DISPATCHER = new AbstractDispatcher(Furryappet.MODID) {
        @Override
        public void register() {
            register(PacketWorldInfo.class, PacketWorldInfo.class, Side.CLIENT);
        }
    };

    public static L10n l10n = new L10n(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ignored) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            InputHandler.register();
            RenderHandler.register();
        }
        EventHandler.register();
        DISPATCHER.register();

        CapabilityManager.INSTANCE.register(DataAPI.FurryappetPlayerDataStorage.class, new Capability.IStorage<DataAPI.FurryappetPlayerDataStorage>() {
            @Override
            public NBTBase writeNBT(Capability<DataAPI.FurryappetPlayerDataStorage> capability, DataAPI.FurryappetPlayerDataStorage instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<DataAPI.FurryappetPlayerDataStorage> capability, DataAPI.FurryappetPlayerDataStorage instance, EnumFacing side, NBTBase nbt) {
                instance.deserializeNBT((NBTTagCompound) nbt);
            }
        }, DataAPI.FurryappetPlayerDataStorage::new);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        furryappetAPIManager = new FurryappetAPIManager();
        furryappetAPIManager.init();

        event.registerServerCommand(new FurryappetCommand());
    }
    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        furryappetAPIManager = null;
    }
}