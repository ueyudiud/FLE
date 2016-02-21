package fle.resource.world;

import java.util.Random;

import flapi.material.MaterialRock;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class FleRockGen extends FleMineableGen
{
	private WorldGenerator[] gens;
	float chance;
	
	public FleRockGen(Block base, MaterialRock rock, int number, float chance, WorldGenerator...generator)
	{
		super(BlockRock.a(rock), (short) 0, number, base);
		this.gens = generator;
		this.chance = chance;
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
}