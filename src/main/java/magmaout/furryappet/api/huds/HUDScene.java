package magmaout.furryappet.api.huds;

import magmaout.furryappet.api.data.IDirtyCheck;
import magmaout.furryappet.api.data.INBTData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HUDScene implements INBTData, IDirtyCheck {
    private final Map<String, List<HUDMorph>> hudMorphs = new HashMap<>();
    private int lastHash = 0;

    public Map<String, List<HUDMorph>> getHudMorphs() {
        return hudMorphs;
    }

    @Override
    public NBTTagCompound toNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        for (Map.Entry<String, List<HUDMorph>> entry : hudMorphs.entrySet()) {
            NBTTagList nbtHudMorphs = new NBTTagList();
            for (HUDMorph hudMorph : entry.getValue()) {
                nbtHudMorphs.appendTag(hudMorph.toNBT());
            }
            compound.setTag(entry.getKey(), nbtHudMorphs);
        }

        return compound;
    }

    @Override
    public void fromNBT(NBTTagCompound nbt) {
        hudMorphs.clear();
        for (String key : nbt.getKeySet()) {
            NBTTagList tagList = nbt.getTagList(key, nbt.getId());
            ArrayList<HUDMorph> hudMorphList = new ArrayList<>();
            for(int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound nbtHudMorph = (NBTTagCompound) tagList.get(i);
                HUDMorph hudMorph = new HUDMorph();
                hudMorph.fromNBT(nbtHudMorph);
                hudMorphList.add(hudMorph);
            }
            hudMorphs.put(key, hudMorphList);
        }
    }

    @Override
    public boolean isValid(NBTTagCompound nbt) {
        return true;
    }

    @Override
    public boolean isDirty() {
        return lastHash != hashCode();
    }

    @Override
    public void markClean() {
        lastHash = hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        HUDScene hudScene = (HUDScene) o;
        return hudMorphs.equals(hudScene.hudMorphs);
    }

    @Override
    public int hashCode() {
        return hudMorphs.hashCode();
    }

}
