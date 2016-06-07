package fle.api.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.gui.GuiBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class GuiPlayerCraftingFle extends GuiBase<InventoryCraftingFle>
{
	public GuiPlayerCraftingFle(World world, EntityPlayer player)
	{
		super(new ContainerPlayerCraftingFle(world, player));
	}

	@Override
	protected void drawOther(int xOffset, int yOffset, int mouseXPosition, int mouseYPosition)
	{
		if(((ContainerPlayerCraftingFle) container).isCrafting())
		{
			int scale = ((ContainerPlayerCraftingFle) container).getCraftingScale(24);
			int k = ((ContainerPlayerCraftingFle) container).getCraftingScale(0x1FF);
			if(scale > 24) scale = 24;
			k &= 0x1FF;
			int color = k < 0x100 ? (0xFFFF0000 | (0x0100 * k)) : (0xFF00FF00 | (0x010000 * (0x1FE - k)));
			drawRect(xoffset + 155, yoffset + 39 + 24 - scale, xoffset + 155 + 2, yoffset + 39 + 24, color);			
		}
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return new ResourceLocation("fle", "textures/gui/crafting_base.png");
	}	
}