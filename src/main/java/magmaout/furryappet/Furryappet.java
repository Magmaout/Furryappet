package magmaout.furryappet;

import magmaout.furryappet.api.APIManager;
import magmaout.furryappet.api.data.DataAPI;
import magmaout.furryappet.client.InputHandler;
import magmaout.furryappet.client.RenderHandler;
import magmaout.furryappet.commands.FurryappetCommand;
import magmaout.furryappet.events.EventHandler;
import magmaout.furryappet.network.Dispatcher;
import mchorse.mclib.commands.utils.L10n;
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

@Mod(modid = Furryappet.MODID, name = Furryappet.TITLE, version = "5.0.07")
public class Furryappet {
    public static final String TITLE = "Furryappet";
    public static final String MODID = "furryappet";

    public static int STRING_LENGTH = 32767;

    public static APIManager APIManager;

    public static L10n l10n = new L10n(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ignored) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            InputHandler.register();
            RenderHandler.register();
        }
        Dispatcher.INSTANCE.register();
        EventHandler.register();

        CapabilityManager.INSTANCE.register(DataAPI.FurryDataStorage.class, new Capability.IStorage<DataAPI.FurryDataStorage>() {
            @Override
            public NBTBase writeNBT(Capability<DataAPI.FurryDataStorage> capability, DataAPI.FurryDataStorage instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<DataAPI.FurryDataStorage> capability, DataAPI.FurryDataStorage instance, EnumFacing side, NBTBase nbt) {
                instance.deserializeNBT((NBTTagCompound) nbt);
            }
        }, DataAPI.FurryDataStorage::new);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        APIManager = new APIManager();
        APIManager.init();

        event.registerServerCommand(new FurryappetCommand());
    }
    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        APIManager = null;
    }
}