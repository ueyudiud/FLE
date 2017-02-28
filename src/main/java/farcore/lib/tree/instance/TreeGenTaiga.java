package farcore.lib.tree.instance;

import java.util.Random;

import farcore.lib.tree.Tree;
import farcore.lib.tree.TreeGenAbstract;
import farcore.lib.tree.TreeInfo;
import nebula.common.util.L;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TreeGenTaiga extends TreeGenAbstract
{
	protected int minHeight = 6;
	protected int randHeight = 4;
	protected float generateCoreLeavesChance;

	public TreeGenTaiga(Tree tree, float generateCoreLeavesChance)
	{
		super(tree, generateCoreLeavesChance);
	}

	public void setHeight(int minHeight, int randHeight)
	{
		this.minHeight = minHeight;
		this.randHeight = randHeight;
	}

	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		int l = L.nextInt(randHeight, random) + minHeight;
		int i1 = 1 + random.nextInt(2);
		int j1 = l - i1;
		int k1 = 2 + random.nextInt(2);

		if (y >= 1 && y + l + 1 <= 256)
		{
			if (!checkLogGrow(world, x, y, z, 0, i1, 0, false))
				return false;
			if (!checkLogGrow(world, x, y + i1 + 1, z, k1, j1 - 1, k1, true))
				return false;
			int i2;
			int l3;
			BlockPos pos;
			IBlockState state = world.getBlockState(pos = new BlockPos(x, y - 1, z));

			boolean isSoil = state.getBlock().canSustainPlant(state, world, pos, EnumFacing.UP, (BlockSapling) Blocks.SAPLING);
			if (isSoil && y < 256 - l - 1)
			{
				state.getBlock().onPlantGrow(state, world, pos, pos.up());
				l3 = random.nextInt(2);
				i2 = 1;
				byte b0 = 0;
				int k2;
				int i4;

				for (i4 = 0; i4 <= j1; ++i4)
				{
					k2 = y + l - i4;

					for (int l2 = x - l3; l2 <= x + l3; ++l2)
					{
						int i3 = l2 - x;

						for (int j3 = z - l3; j3 <= z + l3; ++j3)
						{
							int k3 = j3 - z;

							if (Math.abs(i3) != l3 || Math.abs(k3) != l3 || l3 <= 0)
								generateTreeLeaves(world, x, y, z, 0, generateCoreLeavesChance, random, info);
						}
					}

					if (l3 >= i2)
					{
						l3 = b0;
						b0 = 1;
						++i2;

						if (i2 > k1)
							i2 = k1;
					} else
						++l3;
				}

				i4 = random.nextInt(3);

				for (k2 = 0; k2 < l - i4; ++k2)
					if (isLogReplaceable(world, x, y + k2, z))
						generateLog(world, x, y, z, 0);

				return true;
			} else
				return false;
		} else
			return false;
	}
}