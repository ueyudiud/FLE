package fle.resource.world;

import java.util.Random;

import farcore.collection.abs.IStackList;
import farcore.collection.abs.Stack;
import farcore.util.Part;
import flapi.material.MaterialOre;
import flapi.world.BlockPos;
import fle.core.init.IB;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class FleDivergeOreGen extends FleDivergeMineableGen
{
	protected IStackList<Stack<MaterialOre>,MaterialOre> genList;
	protected Block base;
	
	public FleDivergeOreGen(IStackList<Stack<MaterialOre>,MaterialOre> aGenWeight, Block base, float size, float div, int amount)
	{
		super(size, div, amount);
		this.genList = aGenWeight;
		this.base = base;
	}
	
	@Override
	protected boolean matchCanGen(World world, Random rand, int x, int y,
			int z, int distanceSQ)
	{
		return y > 10 &&
				!world.getBlock(x, y, z).isAir(world, x, y, z) &&
				world.getBlock(x, y, z).isReplaceableOreGen(world, x, y, z, base);
	}
	
	@Override
	protected void genBlockAt(World world, Random rand, int x, int y, int z,
			int distanceSQ)
	{
		MaterialOre ore = genList.randomGet(rand);
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		world.setBlock(x, y, z, IB.ore);
		BlockOre.setData(new BlockPos(world, x, y, z), block, meta, MaterialOre.getOreID(ore), Part.chunk.resolution * 100 / (distanceSQ + 36));
	}

	@Override
	protected void genBlockAt(World world, Random rand, int x, int y, int z)
	{
		MaterialOre ore = genList.randomGet(rand);
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		world.setBlock(x, y, z, IB.ore);
		BlockOre.setData(new BlockPos(world, x, y, z), block, meta, MaterialOre.getOreID(ore), Part.chunk.resolution * 2);
	}
}