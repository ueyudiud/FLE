package nebula.client.render;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IIconLoader
{
	@SideOnly(Side.CLIENT)
	void registerIcon(IIconRegister register);
}