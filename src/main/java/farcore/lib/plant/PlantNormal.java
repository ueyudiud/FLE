/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.plant;

import java.util.Random;

import farcore.blocks.BlockPlant;
import farcore.lib.material.Mat;
import nebula.common.util.Worlds;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

/**
 * @author ueyudiud
 */
public abstract class PlantNormal implements IPlant<BlockPlant>
{
	protected final Mat		material;
	protected BlockPlant	block;
	
	public PlantNormal(Mat material)
	{
		this.material = material;
		this.block = createBlock(material);
	}
	
	@Override
	public final Block block()
	{
		return this.block;
	}
	
	protected BlockPlant createBlock(Mat material)
	{
		return BlockPlant.create(material.modid, material.name, this);
	}
	
	@Override
	public final Mat material()
	{
		return this.material;
	}
	
	protected abstract IBlockState randomGrowState(IBlockState parent, Random random);
	
	protected void spreadSeed(BlockPlant block, World world, BlockPos pos, Random random, IBlockState source, int range, int seedbase, int seedrand)
	{
		int maxCount = seedbase + random.nextInt(seedrand);
		MutableBlockPos pos2 = new MutableBlockPos();
		for (int i = 0; i < maxCount; ++i)
		{
			pos2.setPos(pos.getX() + random.nextInt(range << 1) - range, pos.getY() + random.nextInt(range << 1) - range, pos.getZ() + random.nextInt(range << 1) - range);
			while (world.isAirBlock(pos2.down()) && pos2.getY() > 0)
			{
				pos2.move(EnumFacing.DOWN);
			}
			if (canBlockStay(block, block.getDefaultState(), world, pos2) && Worlds.isAirOrReplacable(world, pos2))
			{
				world.setBlockState(pos2, randomGrowState(source, random));
			}
		}
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Plains;
	}
}
