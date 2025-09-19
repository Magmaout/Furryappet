package magmaout.furryappet.api.huds;

import magmaout.furryappet.api.data.INBTData;
import mchorse.mclib.utils.NBTUtils;
import mchorse.metamorph.api.Morph;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.vecmath.Vector3f;
import java.util.Objects;

public class HUDMorph implements INBTData {
    public Morph morph = new Morph();
    public int expire;
    public Vector3f translate = new Vector3f();
    public Vector3f scale = new Vector3f(1, 1, 1);
    public Vector3f rotate = new Vector3f();
    public boolean enableLight = true;

    public int tick = 0;
    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagCompound morphNBT = morph.toNBT();
        if(morphNBT != null) {
            compound.setTag("morph", morphNBT);
        }
        if(expire != 0) {
            compound.setInteger("expire", expire);
        }
        if(translate.x + translate.y + translate.z != 0) {
            compound.setTag("translate", NBTUtils.writeFloatList(new NBTTagList(), this.translate));
        }
        if(scale.x != 1 && scale.y != 1 && scale.z != 1) {
            compound.setTag("scale", NBTUtils.writeFloatList(new NBTTagList(), this.scale));
        }
        if(rotate.x + rotate.y + rotate.z != 0) {
            compound.setTag("rotate", NBTUtils.writeFloatList(new NBTTagList(), this.rotate));
        }
        if(!enableLight) {
            compound.setBoolean("light", false);
        }

        return compound;
    }

    @Override
    public void fromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("morph"))
        {
            this.morph.fromNBT(nbt.getCompoundTag("morph"));
        }

        this.expire = nbt.getInteger("expire");

        NBTUtils.readFloatList(nbt.getTagList("translate", 5), this.translate);
        NBTUtils.readFloatList(nbt.getTagList("scale", 5), this.scale);
        NBTUtils.readFloatList(nbt.getTagList("rotate", 5), this.rotate);
        enableLight = !nbt.hasKey("light") || nbt.getBoolean("light");
    }

    @Override
    public boolean isValid(NBTTagCompound nbt) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        HUDMorph hudMorph = (HUDMorph) o;
        return expire == hudMorph.expire && Objects.equals(morph, hudMorph.morph) && translate.equals(hudMorph.translate) && scale.equals(hudMorph.scale) && rotate.equals(hudMorph.rotate) && enableLight == hudMorph.enableLight;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(morph.get());
        result = 31 * result + expire;
        result = 31 * result + translate.hashCode();
        result = 31 * result + scale.hashCode();
        result = 31 * result + rotate.hashCode();
        result = 31 * result + Boolean.hashCode(enableLight);

        return result;
    }
}
