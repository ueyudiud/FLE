package fle.core.render;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidTankInfo;
import fle.api.te.TEIT;

public class RenderCastingPool extends RenderBase
{
	@Override
	public void renderBlock()
	{
		setTexture(Blocks.stone);
		
		float f = 0.0F;
		float f1 = 1.0F;
		float f2 = 0.03125F;
		float f3 = 0.96875F;
		renderBlock(f, f, f, f1, f2, f1);
		renderBlock(f, f, f, f2, f1, f1);
		renderBlock(f, f, f, f1, f1, f2);
		renderBlock(f3, f, f, f1, f1, f1);
		renderBlock(f, f, f3, f1, f1, f1);
		
		if(isItem()) return;
		if(world.getTileEntity(x, y, z) instanceof TEIT)
		{
			FluidTankInfo info = ((TEIT) world.getTileEntity(x, y, z)).getTankInfo(ForgeDirection.UNKNOWN)[0];
			if(info.fluid != null)
			{
				renderFluid(info.fluid, info.capacity, 0.985F, 0, 0, 0, f2, f3, f2, f3);
			}
		}
	}
}