/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.client.gui;

import java.io.IOException;

import fle.core.FLE;
import fle.core.gui.ContainerWoodworkPortable;
import nebula.client.gui.GuiContainerBase;
import nebula.client.gui.GuiIconButton;
import nebula.client.gui.GuiIconButton.ButtonSize;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class GuiWoodworkPortable extends GuiContainerBase
{
	private static final ResourceLocation LOCATION = new ResourceLocation(FLE.MODID, "textures/gui/woodwork1.png");
	
	private ContainerWoodworkPortable container;
	
	public GuiWoodworkPortable(EntityPlayer player, World world, BlockPos pos)
	{
		super(new ContainerWoodworkPortable(player, world, pos), LOCATION);
		this.container = (ContainerWoodworkPortable) this.inventorySlots;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		addButton(new GuiIconButton(0, this.guiLeft + 71 , this.guiTop + 36, ButtonSize.Tiny, LOCATION, 176, 0, this.itemRender));
		addButton(new GuiIconButton(1, this.guiLeft + 105, this.guiTop + 36, ButtonSize.Tiny, LOCATION, 176, 5, this.itemRender));
		addButton(new GuiIconButton(2, this.guiLeft + 71 , this.guiTop + 45, ButtonSize.Tiny, LOCATION, 181, 0, this.itemRender));
		addButton(new GuiIconButton(3, this.guiLeft + 105, this.guiTop + 45, ButtonSize.Tiny, LOCATION, 181, 5, this.itemRender));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		sendGuiData(0, button.id, false);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		if (this.container.hasRecipe())
		{
			int[] value = this.container.getRenderValue();
			this.fontRendererObj.drawString(Integer.toString(value[0]), 79, 40, 0xFFFFFFFF);
			String string = Integer.toString(value[1]);
			this.fontRendererObj.drawString(string, 105 - this.fontRendererObj.getStringWidth(string), 40, 0xFFFFFFFF);
		}
	}
}