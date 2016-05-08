package farcore.interfaces;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

public interface ICondition
{
	String name();
	
	@SideOnly(Side.CLIENT)
	IIcon[] getIcons();
}