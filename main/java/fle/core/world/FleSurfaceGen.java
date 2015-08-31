package fle.core.world;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class FleSurfaceGen extends WorldGenerator
{
	int size;
	int count;
	
	protected FleSurfaceGen(int aSize, int aCount)
	{
		size = aSize;
		count = aCount;
	}
	
	@Override
	public boolean generate(World aWorld, Random aRand,
			int x, int y, int z)
	{
		int size = (int) Math.floor(Math.sqrt(this.size));
		int count = this.count;
		label :
		for(int i = -size; i <= size; ++i)
			for(int j = -size; j < size; ++j)
				for(int k = -size; k < size; ++k)
				{
					if(Math.sqrt(i * i + j * j + k * k) < size)
					{
						if(aRand.nextDouble() < 0.3D) continue;
						if(generateAt(aWorld, aRand, x, y, z)) count -= 1;
					}
					if(count <= 0) break label;
				}
		return count != this.count;
	}
	
	public abstract boolean generateAt(World aWorld, Random aRand, int x, int y, int z);
}