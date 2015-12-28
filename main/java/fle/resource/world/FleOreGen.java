package fle.resource.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import flapi.collection.abs.IStackList;
import flapi.collection.abs.Stack;
import flapi.material.MaterialOre;
import flapi.world.BlockPos;
import fle.FLE;
import fle.core.init.IB;
import fle.core.init.Parts;
import fle.resource.block.BlockOre;

public class FleOreGen extends FleMineableGen
{
	protected IStackList<Stack<MaterialOre>,MaterialOre> weightHelper;

	public FleOreGen(IStackList<Stack<MaterialOre>,MaterialOre> aGenWeight, int number, Block target, int targetID)
	{
		super(IB.ore, (short) 0, number, target);
		weightHelper = aGenWeight;
	}

	public FleOreGen(IStackList<Stack<MaterialOre>,MaterialOre> aGenWeight, int number, Block target)
	{
		super(IB.ore, (short) 0, number, target);
		weightHelper = aGenWeight;
	}
	
	@Override
	protected void genBlockAt(World aWorld, Random aRand, int x, int y, int z) 
	{
		MaterialOre ore = weightHelper.randomGet(aRand);
		Block block = aWorld.getBlock(x, y, z);
		int baseMeta = FLE.fle.getWorldManager().getData(new BlockPos(aWorld, x, y, z), 0);
		baseMeta = baseMeta == -1 ? aWorld.getBlockMetadata(x, y, z) : baseMeta;
		aWorld.setBlock(x, y, z, target);
		BlockOre.setData(new BlockPos(aWorld, x, y, z), base, block.getDamageValue(aWorld, x, y, z), MaterialOre.getOreID(ore), Parts.chip.resolution * 2);
	}
}