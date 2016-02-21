package flapi.fluid;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.IFluidTank;

public interface IUpdatableFluid
{
	void onTankFluidUpdate(IFluidTank stack, TileEntity tile);
}
