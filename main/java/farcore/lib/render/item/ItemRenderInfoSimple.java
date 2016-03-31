package farcore.lib.render.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.interfaces.IItemIconInfo;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemRenderInfoSimple implements IItemIconInfo
{
	private String textureName;
	@SideOnly(Side.CLIENT)
	private IIcon icon;
	
	public ItemRenderInfoSimple(String name)
	{
		textureName = name;
	}

	@Override
	public void registerIcons(IIconRegister register)
	{
		icon = register.registerIcon(textureName);
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass)
	{
		return icon;
	}

	@Override
	public int getColor(ItemStack stack, int pass)
	{
		return 0xFFFFFF;
	}

	@Override
	public int getPasses()
	{
		return 1;
	}
}