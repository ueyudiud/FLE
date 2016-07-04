package farcore.alpha.interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

public interface INamedIconRegister
{
	@SideOnly(Side.CLIENT)
	IIcon getIconFromName(String name);

	@SideOnly(Side.CLIENT)
	IIcon registerIcon(String name, String textureName);
}