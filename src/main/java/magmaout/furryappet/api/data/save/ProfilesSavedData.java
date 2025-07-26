package magmaout.furryappet.api.data.save;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProfilesSavedData extends WorldSavedData {
    public static final String ID = "user_profiles";
    public List<GameProfile> profiles = new ArrayList<>();
    private int lastHash = 0;
    public ProfilesSavedData() {
        super(ID);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        profiles.clear();
        tag.getKeySet().forEach(id -> profiles.add(new GameProfile(UUID.fromString(id), tag.getString(id))));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        profiles.forEach(profile -> tag.setString(profile.getId().toString(), profile.getName()));
        return tag;
    }

    @Override
    public boolean isDirty() {
        return lastHash != profiles.hashCode();
    }

    @Override
    public void setDirty(boolean isDirty) {
        if (!isDirty) lastHash = profiles.hashCode();
    }
}
