package magmaout.furryappet.api.states;

import magmaout.furryappet.events.ObjectStateEvent;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.MinecraftForge;

public enum StateType {
    STRING(String.class) {
        @Override
        public NBTBase toNBT(Object object, String name, boolean isClient) {
            checkType(object);
            return new NBTTagString((String) object);
        }

        @Override
        public Object fromNBT(NBTBase nbt, String name, boolean isClient) {
            if (!(nbt instanceof NBTTagString))
                throw new IllegalArgumentException("Expected NBTTagString, got " + nbt.getClass());
            return ((NBTTagString) nbt).getString();
        }
    },

    NUMBER(Number.class) {
        @Override
        public NBTBase toNBT(Object object, String name, boolean isClient) {
            checkType(object);
            return new NBTTagDouble((Double) object);
        }

        @Override
        public Object fromNBT(NBTBase nbt, String name, boolean isClient) {
            if (!(nbt instanceof NBTTagDouble))
                throw new IllegalArgumentException("Expected NBTTagDouble, got " + nbt.getClass());
            return ((NBTTagDouble) nbt).getDouble();
        }
    },

    NBT(NBTBase.class) {
        @Override
        public NBTBase toNBT(Object object, String name, boolean isClient) {
            checkType(object);
            return (NBTBase) object;
        }

        @Override
        public Object fromNBT(NBTBase nbt, String name, boolean isClient) {
            return nbt;
        }
    },

    OBJECT(Object.class) {
        @Override
        public NBTBase toNBT(Object object, String name, boolean isClient) {
            if(!isClient) {
                ObjectStateEvent event = new ObjectStateEvent.Save(name, object, null);
                MinecraftForge.EVENT_BUS.post(event);

                if (event.isCanceled()) return null;
                return event.getTagCompound();
            }
            if(object == null || object instanceof NBTBase) {
                return null;
            }
            return new NBTTagString(object.toString());
        }

        @Override
        public Object fromNBT(NBTBase nbt, String name, boolean isClient) {
            if(isClient) {
                return nbt;
            }
            ObjectStateEvent event = new ObjectStateEvent.Load(name, null, nbt);
            MinecraftForge.EVENT_BUS.post(event);

            if (event.isCanceled()) return null;
            return event.getObject();
        }
    };

    private final Class<?> type;

    StateType(Class<?> type) {
        this.type = type;
    }

    public Class<?> getClassType() {
        return type;
    }

    protected void checkType(Object object) {
        if (!type.isInstance(object)) {
            throw new IllegalArgumentException("Expected " + type.getSimpleName() + ", got " + object.getClass());
        }
    }

    public abstract NBTBase toNBT(Object object, String name, boolean isClient);

    public abstract Object fromNBT(NBTBase nbt, String name, boolean isClient);
}
