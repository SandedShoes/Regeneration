package me.swirtzly.regen.common.regen.transitions;

import me.swirtzly.regen.client.transitions.FieryTransitionRenderer;
import me.swirtzly.regen.common.objects.RSounds;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.config.RegenConfig;
import me.swirtzly.regen.util.PlayerUtil;
import me.swirtzly.regen.util.RConstants;
import net.minecraft.block.Block;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class FieryTransition implements TransitionType<FieryTransitionRenderer> {


    @Override
    public void onUpdateMidRegen(IRegen capability) {

        LivingEntity livingEntity = capability.getLiving();
        livingEntity.extinguish();

        if (!livingEntity.world.isRemote) {
            if (capability.getLiving() instanceof ServerPlayerEntity) {
                //TODO PlayerUtil.setPerspective((ServerPlayerEntity) livingEntity, true, false);
            }
        }

        if (livingEntity.world.isRemote) return;

        BlockPos livingPos = new BlockPos(livingEntity.getPositionVec());
        if (livingEntity.world.getBlockState(livingPos).getBlock() instanceof FireBlock)
            livingEntity.world.removeBlock(livingPos, false);
    }

    @Override
    public void onFinishRegeneration(IRegen capability) {
        if (capability.getLiving() instanceof ServerPlayerEntity) {
            //TODO PlayerUtil.setPerspective((ServerPlayerEntity) capability.getLiving() , false, true);
        }
        //TODO capability.setAnimationTicks(0);
    }

    @Override
    public int getAnimationLength() {
        return 280; // 14 seconds of 20 ticks
    }

    @Override
    public double getAnimationProgress(IRegen cap) {
        return 10;
        //TODO return Math.min(1, cap.getAnimationTicks() / (double) getAnimationLength());
    }


    @Override
    public SoundEvent[] getRegeneratingSounds() {
        return new SoundEvent[]{RSounds.REGENERATION_0.get(), RSounds.REGENERATION_1.get(), RSounds.REGENERATION_2.get(), RSounds.REGENERATION_3.get(), RSounds.REGENERATION_4.get(), RSounds.REGENERATION_5.get(), RSounds.REGENERATION_6.get(), RSounds.REGENERATION_7.get()};

    }

    @Override
    public Vector3d getDefaultPrimaryColor() {
        return new Vector3d(0.93F, 0.61F, 0F);
    }

    @Override
    public Vector3d getDefaultSecondaryColor() {
        return new Vector3d(1F, 0.5F, 0.18F);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(RConstants.MODID, "fiery");
    }

    @Override
    public FieryTransitionRenderer getRenderer() {
        return FieryTransitionRenderer.INSTANCE;
    }

}