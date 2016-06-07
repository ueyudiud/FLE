package fle.core.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.interfaces.IItemIconInfo;
import fle.core.item.ItemToolFle;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemToolHeadRenderInfo implements IItemIconInfo
{
	private String textureName;
	@SideOnly(Side.CLIENT)
	private IIcon headIcon;
	
	public ItemToolHeadRenderInfo(String textureName)
	{
		this.textureName = textureName;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register)
	{
		headIcon = register.registerIcon(textureName + "/head");
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass)
	{
		return headIcon;
	}

	@SideOnly(Side.CLIENT)
	public int getColor(ItemStack stack, int pass)
	{
		return ItemToolFle.getToolMaterial(stack).color;
	}

	@SideOnly(Side.CLIENT)
	public int getPasses()
	{
		return 1;
	}
}