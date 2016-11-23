package farcore.lib.solid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Solid extends SolidAbstract
{
	protected ResourceLocation location;
	
	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getIcon()
	{
		return location == null ? super.getIcon() : location;
	}
	
	public void setIcon(ResourceLocation location)
	{
		this.location = location;
	}
}