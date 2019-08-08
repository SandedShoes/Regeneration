package me.swirtzly.regeneration.client.gui;

import me.swirtzly.regeneration.RegenerationMod;
import me.swirtzly.regeneration.client.gui.parts.BlankContainer;
import me.swirtzly.regeneration.client.skinhandling.SkinChangingHandler;
import me.swirtzly.regeneration.common.capability.CapabilityRegeneration;
import me.swirtzly.regeneration.common.types.TypeHandler;
import me.swirtzly.regeneration.network.MessageChangeType;
import me.swirtzly.regeneration.network.NetworkHandler;
import me.swirtzly.regeneration.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.config.GuiButtonExt;

import java.awt.*;
import java.io.IOException;

import static me.swirtzly.regeneration.util.ClientUtil.playerModelAlex;
import static me.swirtzly.regeneration.util.ClientUtil.playerModelSteve;
import static me.swirtzly.regeneration.util.RenderUtil.drawModelToGui;

public class GuiModelChoice extends GuiContainer {

	public static final int ID = 1;
	private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
	private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
    private static final ResourceLocation BACKGROUND = new ResourceLocation(RegenerationMod.MODID, "textures/gui/customizer_background.png");
	private static TypeHandler.RegenType SELECTED_TYPE = CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player).getType();
    private float ROTATION = 0;
	
	public GuiModelChoice() {
		super(new BlankContainer());
		xSize = 256;
		ySize = 173;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		int cx = (width - xSize) / 2;
		int cy = (height - ySize) / 2;
		final int btnW = 66, btnH = 18;
        ROTATION = 0;

		GuiButtonExt btnBack = new GuiButtonExt(98, width /2 - 115, cy + 145, btnW, btnH, new TextComponentTranslation("regeneration.gui.back").getFormattedText());
		GuiButtonExt btnNext = new GuiButtonExt(4, width /2 + 50, cy + 125, btnW , btnH, new TextComponentTranslation("regentype."+SELECTED_TYPE.name().toLowerCase()).getUnformattedComponentText());
		GuiButtonExt btnColor = new GuiButtonExt(99, width /2 + 50, cy + 105, btnW , btnH, new TextComponentTranslation("regeneration.gui.color").getUnformattedComponentText());




		GuiButtonExt btnOpenFolder = new GuiButtonExt(100, width /2 + 50, cy + 145, btnW, btnH, new TextComponentTranslation("regeneration.gui.skin_choice").getFormattedText());
		
		buttonList.add(btnNext);
		buttonList.add(btnOpenFolder);
		buttonList.add(btnBack);
		buttonList.add(btnColor);

		SELECTED_TYPE = CapabilityRegeneration.getForPlayer(Minecraft.getMinecraft().player).getType();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(BACKGROUND);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		GlStateManager.pushMatrix();
		playerModelAlex.isChild = false;
		playerModelSteve.isChild = false;
		GuiInventory.drawEntityOnScreen(width / 2 - 80, height / 2 + 35, 55, (float)(guiLeft + 51) - mouseX, (float)(guiTop + 75 - 50) - mouseY, Minecraft.getMinecraft().player);
		GlStateManager.popMatrix();
		
		drawCenteredString(Minecraft.getMinecraft().fontRenderer, new TextComponentTranslation("regeneration.gui.preferences").getUnformattedComponentText(), width / 2, height / 2 - 80, Color.WHITE.getRGB());
		
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		switch (button.id) {
			case 98:
				Minecraft.getMinecraft().player.openGui(RegenerationMod.INSTANCE, GuiCustomizer.ID, Minecraft.getMinecraft().world, 0, 0, 0);
				break;
			
			case 4:
				if (SELECTED_TYPE.next() != null) {
					SELECTED_TYPE = (TypeHandler.RegenType) SELECTED_TYPE.next();
				} else {
					SELECTED_TYPE = TypeHandler.RegenType.FIERY;
				}
				button.displayString = new TextComponentTranslation("regentype."+SELECTED_TYPE.name().toLowerCase()).getUnformattedComponentText();
				NetworkHandler.INSTANCE.sendToServer(new MessageChangeType(SELECTED_TYPE));
				break;
			case 99:
				Minecraft.getMinecraft().player.openGui(RegenerationMod.INSTANCE, GuiCustomizer.ID, Minecraft.getMinecraft().world, 0, 0, 0);
				break;

			case 100:
				Minecraft.getMinecraft().player.openGui(RegenerationMod.INSTANCE, GuiSkinChange.ID, Minecraft.getMinecraft().world, 0, 0, 0);
				break;
		}
	}
	
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
        ROTATION++;
        if (ROTATION > 360) {
            ROTATION = 0;
		}
	}
	
}
