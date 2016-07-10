package farcore.lib.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

public interface INamedIconRegister
{
	void push(IRegisteredNameable arg);
	
	void push(String arg);
	
	void pop();
	
	@SideOnly(Side.CLIENT)
	IIcon getIconFromName(String name);

	@SideOnly(Side.CLIENT)
	IIcon registerIcon(String name, String textureName);
}