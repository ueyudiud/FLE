package farcore.util.runnable;

import static farcore.lib.util.Direction.D;
import static farcore.lib.util.Direction.E;
import static farcore.lib.util.Direction.N;
import static farcore.lib.util.Direction.Q;
import static farcore.lib.util.Direction.S;
import static farcore.lib.util.Direction.U;
import static farcore.lib.util.Direction.W;

import farcore.lib.block.IBurnCustomBehaviorBlock;
import farcore.lib.util.Direction;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class Burning implements Runnable
{
	World world;
	int x;
	int y;
	int z;
	float hardness;

	public Burning(World world, int x, int y, int z, float hardness)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.hardness = hardness;
	}

	@Override
	public void run()
	{
		int size = (int) Math.ceil(Math.cbrt(hardness));
		float[][][] values = caculateEffect(size);
		affectOn(size, values);
	}

	private float[][][] caculateEffect(int size)
	{
		int range = 2 * size + 1;
		float[][][] effect = new float[range][range][range];
		int dency = farcore.util.U.Worlds.getAirDensity(world, y + .5F);
		float standardEffect = dency < 0 ? 1.0F - dency * 1E-3F : 1.0F + dency * 1.2E-2F;
		int i, j, k;
		for(i = -size; i <= size; ++i)
			for(j = -size; j <= size; ++j)
				for(k = -size; k <= size; ++k)
				{
					Block block = world.getBlock(x + i, y + j, z + k);
					float v;
					if(block instanceof IBurnCustomBehaviorBlock)
						v = ((IBurnCustomBehaviorBlock) block).getThermalConduct(world, x + i, y + j, z + k);
					else
						v = getDefaultTC(block.getMaterial()) + block.getFireSpreadSpeed(world, x + i, y + j, z + k, ForgeDirection.UP) / 100F;
					if(v < 0)
						v = standardEffect;
					effect[i + size][j + size][k + size] = v;
				}
		float[][][] result = new float[range][range][range];
		int l;
		float a, count = 0;
		for(i = -size; i <= size; ++i)
			for(j = -size; j <= size; ++j)
				for(k = -size; k <= size; ++k)
				{
					if(i == 0 && j == 0 && k == 0)
						continue;
					int d = (int) Math.ceil(Math.sqrt(i * i + j * j + k * k));
					float xStep = (float) i / (float) d;
					float yStep = (float) j / (float) d;
					float zStep = (float) k / (float) d;
					float x1 = x, y1 = y, z1 = z;
					a = 0;
					for(l = 0; l < d; ++l)
					{
						x1 += xStep;
						y1 += yStep;
						z1 += zStep;
						a += effect[(int) x1 + size][(int) y1 + size][(int) z1 + size];
					}
					count += (result[i + size][j + size][k + size] = 1 / a);
				}
		hardness /= count;
		for(i = 0; i <= range; ++i)
			for(j = 0; j <= range; ++j)
				for(k = 0; k <= range; ++k)
					result[i][j][k] *= hardness;
		return result;
	}

	private void affectOn(int size, float[][][] values)
	{
		int i, j, k;
		for(i = -size; i <= size; ++i)
			for(j = -size; j <= size; ++j)
				for(k = -size; k <= size; ++k)
				{
					if(i == 0 && j == 0 && k == 0)
						continue;
					farcore.util.U.Worlds.tryBurnBlock(world, x + i, y + j, z + k, values[i + size][j + size][k + size], face(i, j, k));
				}
	}

	private Direction face(int i, int j, int k)
	{
		int i1 = Math.abs(i), j1 = Math.abs(j), k1 = Math.abs(k);
		if(i != 0 && i1 >= j1 && i1 >= k1)
			return i > 0 ? E : W;
		if(k != 0 && k1 > i1 && k1 >= j1)
			return k > 0 ? S : N;
		return j > 0 ? U : j < 0 ? D : Q;
	}

	private float getDefaultTC(Material material)
	{
		if(material == Material.wood) return 9F;
		if(material == Material.cloth) return 8F;
		if(material == Material.clay) return 6F;
		if(material == Material.ground) return 12F;
		if(material == Material.grass) return 11F;
		if(material == Material.glass) return 78.3F;
		if(material == Material.ice) return 37F;
		if(material == Material.iron) return 103F;
		if(material == Material.leaves) return 3.8F;
		if(material == Material.piston) return 17F;
		if(material == Material.anvil) return 92F;
		if(material == Material.water) return 2.1F;
		if(material == Material.lava) return 6F;
		return -1F;
	}
}