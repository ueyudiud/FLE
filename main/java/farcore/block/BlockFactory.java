package farcore.block;

import static net.minecraft.block.BlockFalling.fallInstantly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import farcore.biology.SpecieRegistry;
import farcore.biology.plant.ICoverablePlantSpecies;
import farcore.biology.plant.IPlant;
import farcore.biology.plant.IPlantSpecies;
import farcore.block.fluid.IFleFluidBlock;
import farcore.entity.EntityFleFallingBlock;
import farcore.substance.Substance;
import farcore.util.SubTag;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidStack;

/**
 * Contain all kinds of block event might use in mod.
 * 
 * @author ueyudiud
 * 		
 */
public class BlockFactory
{
	private static final byte CHECK_AREA = 32;
	
	public static boolean canSustainPlant(IBlockAccess world, BlockPos pos,
			Substance substance, EnumFacing direction, IPlantable plantable)
	{
		if (substance == null)
			return false;
		IBlockState state = world.getBlockState(pos);
		IBlockState plant = plantable.getPlant(world, pos.offset(direction));
		EnumPlantType plantType = plantable.getPlantType(world,
				pos.offset(direction));
				
		switch (plantType) {
		case Desert:
			return substance.contain(SubTag.PLANTABLE_DESERT);
		case Nether:
			boolean isNether = substance.contain(SubTag.PLANTABLE_NETHER);
			boolean hasLava = isBlockLava(world, pos.east())
					|| isBlockLava(world, pos.west())
					|| isBlockLava(world, pos.north())
					|| isBlockLava(world, pos.south());
			return isNether && hasLava;
		case Crop:
			return substance.contain(SubTag.PLANTABLE_CROP);
		case Cave:
			return substance.contain(SubTag.PLANTABLE_CAVE);
		case Plains:
			return substance.contain(SubTag.PLANTABLE_PLAINS);
		case Water:
			return isBlockWater(world, pos)
					&& (Integer) state.getValue(BlockLiquid.LEVEL) == 0;
		case Beach:
			boolean isBeach = substance.contain(SubTag.PLANTABLE_BEACH);
			boolean hasWater = isBlockWater(world, pos.east())
					|| isBlockWater(world, pos.west())
					|| isBlockWater(world, pos.north())
					|| isBlockWater(world, pos.south());
			return isBeach && hasWater;
		}
		return false;
	}
	
	public static boolean isBlockLava(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getBlock() == Blocks.lava;
	}
	
	public static boolean isBlockWater(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos).getBlock() == Blocks.water;
	}
	
	public static void makeFallingBlock(World world, BlockPos pos,
			IFallable fallable)
	{
		if (!fallInstantly && world.isAreaLoaded(
				pos.add(-CHECK_AREA, -CHECK_AREA, -CHECK_AREA),
				pos.add(CHECK_AREA, CHECK_AREA, CHECK_AREA)))
		{
			if (!world.isRemote)
			{
				EntityFleFallingBlock entity = new EntityFleFallingBlock(world,
						pos, world.getBlockState(pos));
				if (world.getTileEntity(pos) != null)
				{
					world.getTileEntity(pos).writeToNBT(
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
			if (world.getTileEntity(pos) != null)
			{
				world.getTileEntity(pos)
						.writeToNBT(data = new NBTTagCompound());
			}
			world.setBlockToAir(pos);
			BlockPos blockpos1;
			// Check falling.
			for (blockpos1 = pos.down(); fallable.canFallInto(world, blockpos1)
					&& blockpos1.getY() > 0; blockpos1 = blockpos1.down())
			{
				;
			}
			// Fall to the ground.
			if (blockpos1.getY() > 0)
			{
				BlockPos up = blockpos1.up();
				world.setBlockState(up, ((Block) fallable).getDefaultState());
				if (data != null)
				{
					TileEntity tileentity = world.getTileEntity(up);
					tileentity.readFromNBT(data);
					tileentity.setPos(up);
					tileentity.markDirty();
				}
				fallable.onEndFalling(world, up);
			}
		}
	}
	
	public static boolean spreadSpecies(World world, BlockPos pos, IPlant iP)
	{
		IPlantSpecies species = iP.getSpecie();
		if (species == null)
			return false;
		if (species instanceof ICoverablePlantSpecies)
		{
			ICoverablePlantSpecies plant = (ICoverablePlantSpecies) species;
			if (plant.matchBlock(world, pos, world.getBlockState(pos)))
			{
				if (plant.canGrow(world, pos))
				{
					int range = plant.getSpreadRange(world, pos);
					int loop = plant.getSpreadLoop(world, pos);
					if (loop <= 0 || range <= 0)
						return false;
					for (int i = 0; i < loop; ++i)
					{
						BlockPos pos1 = pos.add(world.rand.nextInt(range),
								world.rand.nextInt(range),
								world.rand.nextInt(range));
						if (plant.canPlant(world, pos1))
						{
							plant.onPlant(world, pos1,
									SpecieRegistry.variation(iP));
						}
					}
				}
			}
		}
		return false;
	}
	
	public static FluidStack useFluid(World world, BlockPos pos, int maxIn,
			boolean process)
	{
		IBlockState state = world.getBlockState(pos);
		if (!(state.getBlock() instanceof BlockFluidBase))
			return null;
		if (!(state.getBlock() instanceof IFleFluidBlock))
		{
			BlockFluidBase fluid = (BlockFluidBase) state.getBlock();
			if (fluid.canDrain(world, pos))
			{
				FluidStack stack = fluid.drain(world, pos, process);
				if (stack.amount > maxIn)
				{
					stack.amount = maxIn;
				}
				return stack;
			}
		}
		else
		{
			IFleFluidBlock fluid = (IFleFluidBlock) state.getBlock();
			if (fluid.canDrain(world, pos))
				return fluid.drain(world, pos, maxIn, process);
		}
		return null;
	}
}