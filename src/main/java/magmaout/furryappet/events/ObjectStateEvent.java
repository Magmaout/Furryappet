package magmaout.furryappet.events;

import net.minecraft.nbt.NBTBase;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class ObjectStateEvent extends Event {
    String stateName;
    Object object;
    NBTBase tagCompound;

    public ObjectStateEvent(String stateName, Object object, NBTBase tagCompound) {
        this.stateName = stateName;
        this.object = object;
        this.tagCompound = tagCompound;
    }

    public NBTBase getTagCompound() {
        return tagCompound;
    }

    public Object getObject() {
        return object;
    }

    public String getStateName() {
        return stateName;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public static class Save extends ObjectStateEvent {
        public Save(String stateName, Object object, NBTBase tagCompound) {
            super(stateName, object, tagCompound);
        }
    }

    public static class Load extends ObjectStateEvent {
        public Load(String stateName, Object object, NBTBase tagCompound) {
            super(stateName, object, tagCompound);
        }
        public void setObject(Object object) {
            this.object = object;
        }
    }
}
