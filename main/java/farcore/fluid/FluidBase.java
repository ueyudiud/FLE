package farcore.fluid;

import farcore.FarCore;
import farcore.util.IUnlocalized;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidBase extends Fluid implements IUnlocalized
{
	protected String textureName;
	
	public FluidBase(String fluidName)
	{
		super(fluidName);
		FluidRegistry.registerFluid(this);
	}
	
	@Override
	public String getLocalizedName(FluidStack stack)
	{
		return FarCore.lang.translateToLocal(this);
	}

	@Override
	public String getUnlocalized()
	{
		return getUnlocalizedName();
	}
	
	protected String getTextureName()
	{
		return textureName == null ? "MISSING_NO_" + fluidName.toUpperCase() : textureName;
	}

	public void registerFluidIcons(IIconRegister register)
	{
		setStillIcon(register.registerIcon(getTextureName() + "_still"));
		setFlowingIcon(register.registerIcon(getTextureName() + "_flow"));
	}

	public int getColor(IBlockAccess world, int x, int y, int z)
	{
		return 0xFFFFFF;
	}
}