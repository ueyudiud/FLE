package fle.api.gui;

import cpw.mods.fml.relauncher.Side;

import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.container.ContainerBase;
import farcore.lib.gui.GuiBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class GuiCraftingFle extends GuiBase<InventoryCraftingFle>
{
	public GuiCraftingFle(World world, int x, int y, int z, EntityPlayer player)
	{
		super(new ContainerCraftingFle(world, x, y, z, player));
	}

	@Override
	protected void drawOther(int xOffset, int yOffset, int mouseXPosition, int mouseYPosition)
	{
		if(((ContainerCraftingFle) container).isCrafting())
		{
			int scale = ((ContainerCraftingFle) container).getCraftingScale(24);
			int k = ((ContainerCraftingFle) container).getCraftingScale(0x1FF);
			if(scale > 24) scale = 24;
			k &= 0x1FF;
			int color = k < 0x100 ? (0xFFFF0000 | (0x0100 * k)) : (0xFF00FF00 | (0x010000 * (0x1FE - k)));
			drawRect(xoffset + 161, yoffset + 47 + 24 - scale, xoffset + 161 + 2, yoffset + 47 + 24, color);			
		}
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return new ResourceLocation("fle", "textures/gui/crafting_extend.png");
	}	
}