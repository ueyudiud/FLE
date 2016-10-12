package farcore.lib.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IRenderRegister
{
	@SideOnly(Side.CLIENT)
	void registerRender();
}
