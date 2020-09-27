package me.swirtzly.regen;

import me.swirtzly.regen.client.rendering.layers.HandGlowLayer;
import me.swirtzly.regen.client.rendering.layers.RenderFieryArms;
import me.swirtzly.regen.common.objects.RSounds;
import me.swirtzly.regen.common.regen.IRegen;
import me.swirtzly.regen.common.regen.RegenCap;
import me.swirtzly.regen.common.regen.RegenStorage;
import me.swirtzly.regen.data.EnglishLang;
import me.swirtzly.regen.network.Dispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@Mod("regen")
public class Regeneration {

    public static final Logger LOG = LogManager.getLogger("Regeneration");

    public Regeneration() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doCommonStuff);
        MinecraftForge.EVENT_BUS.register(this);
        Dispatcher.setUp();
    }


    private void doCommonStuff(final FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(IRegen.class, new RegenStorage(), RegenCap::new);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        /* Attach RenderLayers to Renderers */
        Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();
        for (PlayerRenderer renderPlayer : skinMap.values()) {
            renderPlayer.addLayer(new HandGlowLayer(renderPlayer));
            renderPlayer.addLayer(new RenderFieryArms(renderPlayer));
        }
    }

    @SubscribeEvent
    public void gatherData(GatherDataEvent e) {
        DataGenerator generator = e.getGenerator();
        generator.addProvider(new EnglishLang(generator));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onNewRegistries(RegistryEvent.NewRegistry e) {
        RSounds.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}