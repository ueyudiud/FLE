package farcore.block.fluid;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public interface IFluidHandler
{
	boolean onFluidCollide(World world, BlockPos collidePos, Fluid fluid, Block fluidBlock, Block collideBlock);
}