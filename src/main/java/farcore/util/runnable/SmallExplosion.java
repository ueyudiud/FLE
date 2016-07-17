package farcore.util.runnable;

import farcore.data.V;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class SmallExplosion implements Runnable
{
	World world;
	int iX;
	int iZ;
	double x;
	double y;
	double z;
	float air;
	float damage;
	Chunk[] chunks;
	int range = 1;
	int minX, minZ, maxX, maxZ;

	public SmallExplosion(World world, double x, double y, double z, float airHardness, float damageHardness)
	{
		this.world = world;
		iX = (int) x;
		iZ = (int) z;
		this.x = x;
		this.y = y;
		this.z = z;
		air = airHardness;
		damage = damageHardness;
	}

	public SmallExplosion setRange(int maxRange)
	{
		range = maxRange;
		return this;
	}

	@Override
	public void run()
	{
		int l = range * 2 + 1;
		chunks = new Chunk[l * l];
		int i, j;
		for(i = -range; i <= range; ++i)
			for(j = -range; j <= range; ++j)
				chunks[j * l + i] = world.getChunkFromBlockCoords(iX + i, iZ + j);
		minX = chunks[0].xPosition << 4;
		minZ = chunks[0].zPosition << 4;
		maxX = chunks[chunks.length - 1].xPosition << 4 + 16;
		maxZ = chunks[chunks.length - 1].zPosition << 4 + 16;
		caculateEffect();
	}

	private Block get(int x, int y, int z)
	{
		return x >= minX && x < maxX && z >= minZ && z < maxZ && y >= 0 && y < V.maxTerrainHeight ?
				chunks[(x - minX) >> 4 + ((z - minZ) >> 4) * (2 * range + 1)].getBlock(x, y, z) :
					world.getBlock(x, y, z);
	}

	private void caculateEffect()
	{
		//TODO
	}
}