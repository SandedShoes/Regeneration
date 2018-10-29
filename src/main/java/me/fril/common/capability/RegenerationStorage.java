package me.fril.common.capability;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by Sub
 * on 16/09/2018.
 */
public class RegenerationStorage implements Capability.IStorage<IRegeneration> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IRegeneration> capability, IRegeneration instance, EnumFacing side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<IRegeneration> capability, IRegeneration instance, EnumFacing side, NBTBase nbt) {
        instance.deserializeNBT(nbt instanceof NBTTagCompound ? (NBTTagCompound) nbt : new NBTTagCompound());
    }
}