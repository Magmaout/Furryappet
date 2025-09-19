package magmaout.furryappet.api.huds;

import magmaout.furryappet.api.data.IDirtyCheck;
import magmaout.furryappet.api.data.INBTData;
import magmaout.furryappet.api.data.INamedData;
import mchorse.metamorph.api.Morph;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

public class HUDData implements INBTData, IDirtyCheck, INamedData {
    public String name;
    public final List<HUDMorph> hudMorphs = new ArrayList<>();

    private int lastHash = -1;
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        NBTTagList nbtHudMorphs = new NBTTagList();
        for (HUDMorph hudMorph : hudMorphs) {
            nbtHudMorphs.appendTag(hudMorph.toNBT());
        }
        compound.setTag("morphs", nbtHudMorphs);
        compound.setString("name", name);
        return compound;
    }

    @Override
    public void fromNBT(NBTTagCompound nbt) {
        NBTTagList tagList = nbt.getTagList("morphs", nbt.getId());
        hudMorphs.clear();
        for(int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound nbtHudMorph = (NBTTagCompound) tagList.get(i);
            HUDMorph hudMorph = new HUDMorph();
            hudMorph.fromNBT(nbtHudMorph);
            hudMorphs.add(hudMorph);
        }
        name = nbt.getString("name");
    }

    @Override
    public boolean isValid(NBTTagCompound nbt) {
        return !nbt.getString("name").isEmpty();
    }

    @Override
    public boolean isDirty() {
        return lastHash != hudMorphs.hashCode();
    }

    @Override
    public void markClean() {
        lastHash = hudMorphs.hashCode();
    }
    public List<HUDMorph> copy() {
        ArrayList<HUDMorph> clonedHudMorphs = new ArrayList<>();
        for (HUDMorph hudMorph : hudMorphs) {
            HUDMorph clonedHudMorph = new HUDMorph();
            clonedHudMorph.expire = hudMorph.expire;
            clonedHudMorph.morph = new Morph(hudMorph.morph.copy());
            clonedHudMorph.translate = (Vector3f) hudMorph.translate.clone();
            clonedHudMorph.scale = (Vector3f) hudMorph.scale.clone();
            clonedHudMorph.rotate = (Vector3f) hudMorph.rotate.clone();
            clonedHudMorphs.add(clonedHudMorph);
        }
        return clonedHudMorphs;
    }
}
