package fle.resource.world;

import java.util.Random;

import flapi.material.MaterialRock;
import fle.core.block.resource.BlockRock;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class FleDivergeRockGen extends FleDivergeMineableGen
{
	private WorldGenerator[] gens;
	float chance;
	Block base;
	Block target;

	public FleDivergeRockGen(Block base, MaterialRock rock, float size, float div, int amount)
	{
		this(base, rock, size, div, amount, -1F, new WorldGenerator[0]);
	}
	public FleDivergeRockGen(Block base, MaterialRock rock, float size, float div, int amount, float chance, WorldGenerator...generator)
	{
		super(size, div, amount);
		this.target = BlockRock.a(rock);
		this.base = base;
		this.chance = chance;
		this.gens = generator;
	}
	
	@Override
	public boolean generate(World aWorld, Random aRand, int x, int y, int z)
	{
		if(super.generate(aWorld, aRand, x, y, z))
		{
			generatorOther(aWorld, aRand, x, y, z);
			return true;
		}
		return false;
	}
	
	public void generatorOther(World world, Random rand, int x, int y, int z)
	{
		for(WorldGenerator gen : gens)
		{
			if(gen != null && rand.nextFloat() < chance)
			{
				gen.generate(world, rand, x, y, z);
			}
		}
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
	protected void genBlockAt(World world, Random rand, int x, int y, int z)
	{
		world.setBlock(x, y, z, target);
	}
}