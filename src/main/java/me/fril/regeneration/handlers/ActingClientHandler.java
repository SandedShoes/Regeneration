package me.fril.regeneration.handlers;

import java.io.IOException;

import me.fril.regeneration.RegenConfig;
import me.fril.regeneration.RegenerationMod;
import me.fril.regeneration.client.skinhandling.SkinChangingHandler;
import me.fril.regeneration.client.sound.ConditionalSound;
import me.fril.regeneration.client.sound.MovingSoundEntity;
import me.fril.regeneration.common.capability.IRegeneration;
import me.fril.regeneration.util.ClientUtil;
import me.fril.regeneration.util.RegenState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;

class ActingClientHandler implements IActingHandler {
	
	public static final IActingHandler INSTANCE = new ActingClientHandler();
	
	// TODO 'now a timelord' into toast
	
	private ActingClientHandler() {
	}
	
	/**
	 * SOON test multiplayer sound handling with hydro
	 * Is opening watch heard by others?
	 * Is transferring heard by others?
	 * Is critical heard by others?
	 * Is heartbeat heard by others? < DEAD
	 * Make sure hand-glow is heard by others < It is
	 */
	
	@Override
	public void onRegenTick(IRegeneration cap) {
		// never forwarded as per the documentation
	}
	
	@Override
	public void onEnterGrace(IRegeneration cap) {
		Minecraft.getMinecraft().getSoundHandler().playSound(new ConditionalSound(PositionedSoundRecord.getRecord(RegenObjects.Sounds.CRITICAL_STAGE, 1.0F, 0.5F), () -> cap.getState() != RegenState.GRACE_CRIT));
		Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundEntity(cap.getPlayer(), RegenObjects.Sounds.HEART_BEAT, SoundCategory.PLAYERS, true, () -> !cap.getState().isGraceful()));
	}
	
	@Override
	public void onHandsStartGlowing(IRegeneration cap) {
		Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundEntity(cap.getPlayer(), RegenObjects.Sounds.HAND_GLOW, SoundCategory.PLAYERS, true, () -> !cap.areHandsGlowing()));
	}
	
	@Override
	public void onRegenFinish(IRegeneration cap) {
		ClientUtil.createToast(new TextComponentTranslation("regeneration.toast.regenerated"), new TextComponentTranslation("regeneration.toast.regenerations_left", cap.getRegenerationsLeft()), cap.getState());
	}
	
	@Override
	public void onRegenTrigger(IRegeneration cap) {
		if (Minecraft.getMinecraft().player.getUniqueID().equals(cap.getPlayer().getUniqueID())) {
			try {
				SkinChangingHandler.skinChangeRandom(cap.getPlayer().world.rand, cap.getPlayer());
			} catch (IOException e) {
				RegenerationMod.LOG.error(e.getMessage());
			}
		}
	}
	
	@Override
	public void onGoCritical(IRegeneration cap) {
		ClientUtil.createToast(new TextComponentTranslation("regeneration.toast.enter_critical"), new TextComponentTranslation("regeneration.toast.enter_critical.sub", RegenConfig.grace.criticalPhaseLength / 60), cap.getState());
	}
	
}
