package farcore.interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface IItemIconInfo
{
	@SideOnly(Side.CLIENT)
	void registerIcons(IIconRegister register);

	@SideOnly(Side.CLIENT)
	IIcon getIcon(ItemStack stack, int pass);

	@SideOnly(Side.CLIENT)
	int getColor(ItemStack stack, int pass);

	@SideOnly(Side.CLIENT)
	int getPasses();
}
