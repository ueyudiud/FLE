package farcore.alpha.interfaces.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.alpha.interfaces.INamedIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface IHookedIconInfo
{
	@SideOnly(Side.CLIENT)
	void registerIcons(INamedIconRegister register);

	@SideOnly(Side.CLIENT)
	IIcon getIcon(INamedIconRegister register, ItemStack stack, int pass);

	@SideOnly(Side.CLIENT)
	int getColor(ItemStack stack, int pass);

	@SideOnly(Side.CLIENT)
	int getPasses();
}
