package fle.core.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import fle.FLE;
import fle.api.material.MaterialOre;
import fle.api.util.WeightHelper;
import fle.api.world.BlockPos;
import fle.core.block.BlockOre;
import fle.core.init.IB;

public class FleOreGen extends FleMineableGen
{
	protected WeightHelper<MaterialOre> weightHelper;

	public FleOreGen(WeightHelper<MaterialOre> aGenWeight, int number, Block target, int targetID)
	{
		super(IB.ore, (short) 0, number, target);
		weightHelper = aGenWeight;
	}

	public FleOreGen(WeightHelper<MaterialOre> aGenWeight, int number, Block target)
	{
		super(IB.ore, (short) 0, number, target);
		weightHelper = aGenWeight;
	}

	@Override
	protected void genBlockAt(World aWorld, Random aRand, int x, int y, int z) 
	{
		MaterialOre ore = weightHelper.randomGet(aRand);
		Block block = aWorld.getBlock(x, y, z);
		int baseMeta = aWorld.getBlockMetadata(x, y, z);
		aWorld.setBlock(x, y, z, target);
		BlockOre.setData(new BlockPos(aWorld, x, y, z), base, block.getDamageValue(aWorld, x, y, z), MaterialOre.getOreID(ore));
	}
}