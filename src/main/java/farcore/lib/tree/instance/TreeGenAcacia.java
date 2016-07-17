package farcore.lib.tree.instance;

import java.util.Random;

import farcore.lib.tree.TreeBase;
import farcore.lib.tree.TreeGenAbstract;
import farcore.lib.tree.TreeInfo;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TreeGenAcacia extends TreeGenAbstract
{
	protected int randHeightHlf1;
	protected int randHeightHlf2;
	protected int baseHeight;

	public TreeGenAcacia(TreeBase tree, float generateCoreLeavesChance)
	{
		super(tree, generateCoreLeavesChance);
	}

	public void setHeight(int baseHeight, int randHeight)
	{
		this.baseHeight = baseHeight;
		randHeightHlf1 = randHeight / 2;
		randHeightHlf2 = randHeight - randHeightHlf1;
	}

	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		int l = U.L.nextInt(randHeightHlf1, random) + U.L.nextInt(randHeightHlf2, random) + baseHeight;

		if (y >= 1 && y + l + 1 <= 256)
		{
			int j1;
			int k1;

			if(!checkEmpty(world, x, y + 1, z, 2, l - 2, 2, true)) return false;
			if(!checkEmpty(world, x, y + l - 1, z, 1, 2, 1, true)) return false;
			Block block3 = world.getBlock(x, y - 1, z);

			boolean isSoil = block3.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (BlockSapling)Blocks.sapling);
			if (isSoil && y < 256 - l - 1)
			{
				block3.onPlantGrow(world, x, y - 1, z, x, y, z);
				int j3 = random.nextInt(4);
				j1 = l - random.nextInt(4) - 1;
				k1 = 3 - random.nextInt(3);
				int k3 = x;
				int l1 = z;
				int i2 = 0;
				int j2;
				int k2;

				for (j2 = 0; j2 < l; ++j2)
				{
					k2 = y + j2;

					if (j2 >= j1 && k1 > 0)
					{
						k3 += Direction.offsetX[j3];
						l1 += Direction.offsetZ[j3];
						--k1;
					}

					if (isReplaceable(world, k3, k2, l1))
					{
						generateLog(world, k3, k2, l1, 0);
						i2 = k2;
					}
				}

				for (j2 = -1; j2 <= 1; ++j2)
					for (k2 = -1; k2 <= 1; ++k2)
						if(isReplaceable(world, k3 + j2, i2 + 1, l1 + k2))
							generateTreeLeaves(world, k3 + j2, i2 + 1, l1 + k2, 0, random, info);

				generateTreeLeaves(world, k3 + 2, i2 + 1, l1, 0, random, info);
				generateTreeLeaves(world, k3 - 2, i2 + 1, l1, 0, random, info);
				generateTreeLeaves(world, k3, i2 + 1, l1 + 2, 0, random, info);
				generateTreeLeaves(world, k3, i2 + 1, l1 - 2, 0, random, info);
				for (j2 = -3; j2 <= 3; ++j2)
					for (k2 = -3; k2 <= 3; ++k2)
						if (Math.abs(j2) != 3 || Math.abs(k2) != 3)
							generateTreeLeaves(world, k3 + j2, i2, l1 + k2, 0, random, info);

				k3 = x;
				l1 = z;
				j2 = random.nextInt(4);

				if (j2 != j3)
				{
					k2 = j1 - random.nextInt(2) - 1;
					int l3 = 1 + random.nextInt(3);
					i2 = 0;
					int l2;
					int i3;

					for (l2 = k2; l2 < l && l3 > 0; --l3)
					{
						if (l2 >= 1)
						{
							i3 = y + l2;
							k3 += Direction.offsetX[j2];
							l1 += Direction.offsetZ[j2];
							if (isReplaceable(world, k3, i3, l1))
							{
								generateLog(world, k3, i3, l1, 0);
								i2 = i3;
							}
						}

						++l2;
					}

					if (i2 > 0)
					{
						for (l2 = -1; l2 <= 1; ++l2)
							for (i3 = -1; i3 <= 1; ++i3)
								generateTreeLeaves(world, k3 + l2, i2 + 1, l1 + i3, 0, random, info);

						for (l2 = -2; l2 <= 2; ++l2)
							for (i3 = -2; i3 <= 2; ++i3)
								if (Math.abs(l2) != 2 || Math.abs(i3) != 2)
									generateTreeLeaves(world, k3 + l2, i2, l1 + i3, 0, random, info);
					}
				}
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
}