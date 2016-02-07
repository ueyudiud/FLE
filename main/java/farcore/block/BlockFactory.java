package farcore.block;

import static net.minecraft.block.BlockFalling.fallInstantly;

import farcore.block.interfaces.IFallable;
import farcore.entity.EntityFleFallingBlock;
import farcore.fluid.BlockFluidBase;
import farcore.fluid.ISmartFluidBlock;
import farcore.world.BlockPos;
import farcore.world.Direction;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

public class BlockFactory 
{
	private static final byte CHECK_AREA = 32;
	
	public static void makeFallingBlock(World world, BlockPos pos,
			IFallable fallable)
	{
		if (!fallInstantly && pos.isLoaded(CHECK_AREA))
		{
			if (!world.isRemote)
			{
				EntityFleFallingBlock entity = new EntityFleFallingBlock(pos);
				if (pos.tile() != null)
				{
					pos.tile().writeToNBT(
							entity.tileEntityData = new NBTTagCompound());
				}
				fallable.onStartFalling(entity);
				world.spawnEntityInWorld(entity);
			}
		}
		else
		{
			// Destroy block.
			NBTTagCompound data = null;
			if (pos.tile() != null)
			{
				pos.tile().writeToNBT(data = new NBTTagCompound());
			}
			int meta = pos.meta();
			pos.setToAir();
			BlockPos blockpos1;
			// Check falling.
			for (blockpos1 = pos.offset(Direction.DOWN); 
					fallable.canFallInto(blockpos1)
					&& blockpos1.y > 0; blockpos1 = blockpos1.offset(Direction.DOWN))
			{
				;
			}
			// Fall to the ground.
			if (blockpos1.y > 0)
			{
				BlockPos up = blockpos1.offset(Direction.UP);
				up.set((Block) fallable, meta, 3);
				if (data != null)
				{
					TileEntity tileentity = world.getTileEntity(up.x, up.y, up.z);
					tileentity.readFromNBT(data);
					tileentity.xCoord = up.x;
					tileentity.yCoord = up.y;
					tileentity.zCoord = up.z;
					tileentity.markDirty();
				}
				fallable.onEndFalling(up);
			}
		}
	}
	
	public static FluidStack useFluid(BlockPos pos, int maxIn,
			boolean process)
	{
		Block block = pos.block();
		if (!(block instanceof IFluidBlock))
			return null;
		if (!(block instanceof ISmartFluidBlock))
		{
			IFluidBlock fluid = (IFluidBlock) block;
			if (fluid.canDrain(pos.world(), pos.x, pos.y, pos.z))
			{
				FluidStack stack = fluid.drain(pos.world(), pos.x, pos.y, pos.z, process);
				if (stack.amount > maxIn)
				{
					stack.amount = maxIn;
				}
				return stack;
			}
		}
		else
		{
			ISmartFluidBlock fluid = (ISmartFluidBlock) block;
			if (fluid.canDrain(pos.world(), pos.x, pos.y, pos.z))
				return fluid.drain(pos.world(), pos.x, pos.y, pos.z, maxIn, process);
		}
		return null;
	}
}