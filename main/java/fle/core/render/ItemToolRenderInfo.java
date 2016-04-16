package fle.core.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.interfaces.IItemIconInfo;
import farcore.lib.render.item.ItemRenderInfo;
import farcore.util.V;
import fle.core.item.ItemToolFle;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemToolRenderInfo implements IItemIconInfo
{
	private String textureName;
	@SideOnly(Side.CLIENT)
	private IIcon headIcon;
	@SideOnly(Side.CLIENT)
	private IIcon handleIcon;
	@SideOnly(Side.CLIENT)
	private IIcon tieIcon;
	@SideOnly(Side.CLIENT)
	private IIcon otherIcon;
	
	public ItemToolRenderInfo(String textureName)
	{
		this.textureName = textureName;
	}
	
	@Override
	public void registerIcons(IIconRegister register)
	{
		headIcon = register.registerIcon(textureName + "/head");
		handleIcon = register.registerIcon(textureName + "/handle");
		tieIcon = register.registerIcon(textureName + "/tie");
		otherIcon = register.registerIcon(textureName + "/override");
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass)
	{
		switch (pass)
		{
		case 0 :
			return headIcon;
		case 1 :
			return tieIcon;
		case 2 : 
			return handleIcon;
		default:
			return otherIcon;
		}
	}

	@Override
	public int getColor(ItemStack stack, int pass)
	{
		switch (pass)
		{
		case 0 :
			return ItemToolFle.getToolMaterial(stack).color;
		case 1 :
			return ItemToolFle.getHandleMaterial(stack).color;
		case 2 : 
			return 0xFFFFFF;
		default:
			return 0xFFFFFF;
		}
	}

	@Override
	public int getPasses()
	{
		return 4;
	}
}