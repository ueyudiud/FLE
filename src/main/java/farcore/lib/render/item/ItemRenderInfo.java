package farcore.lib.render.item;

import java.util.Arrays;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.interfaces.IItemIconInfo;
import farcore.util.U;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemRenderInfo implements IItemIconInfo
{
	private int[] colors;
	private String[] textureNames;
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	public ItemRenderInfo(String fileName, String iconName)
	{
		this(0xFFFFFF, fileName, iconName);
	}
	public ItemRenderInfo(int color, String fileName, String iconName)
	{
		this(new int[]{color}, fileName, new String[]{iconName});
	}
	public ItemRenderInfo(String fileName, String[] iconName)
	{
		this(U.Lang.fillIntArray(iconName.length, 0xFFFFFF), fileName, iconName);
	}
	public ItemRenderInfo(int[] colors, String fileName, String[] iconName)
	{
		if(colors.length != iconName.length) throw new RuntimeException();
		this.textureNames = new String[iconName.length];
		for(int i = 0; i < iconName.length; ++i)
		{
			textureNames[i] = fileName + ":" + iconName[i];
		}
		this.colors = colors;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register)
	{
		icons = new IIcon[textureNames.length];
		for(int i = 0; i < textureNames.length; ++i)
		{
			icons[i] = register.registerIcon(textureNames[i]);
		}
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass)
	{
		return icons[pass];
	}

	@SideOnly(Side.CLIENT)
	public int getColor(ItemStack stack, int pass)
	{
		return colors[pass];
	}
	
	@Override
	public int getPasses()
	{
		return colors.length;
	}
}