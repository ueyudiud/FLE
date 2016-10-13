package farcore.lib.solid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class SolidAbstract extends IForgeRegistryEntry.Impl<SolidAbstract>
{
	public SolidAbstract()
	{
	}

	@SideOnly(Side.CLIENT)
	public abstract ResourceLocation getIcon();

	@SideOnly(Side.CLIENT)
	public ResourceLocation getIcon(SolidStack stack)
	{
		return getIcon(stack);
	}
}