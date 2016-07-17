package farcore.lib.tree.instance;

import java.util.Random;

import farcore.lib.tree.TreeBase;
import farcore.lib.tree.TreeGenAbstract;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TreeGenHuge extends TreeGenAbstract
{
	/** The base height of the tree */
	protected int baseHeight;
	/** The random height of the tree */
	protected int randHeight;

	public TreeGenHuge(TreeBase tree, float generateCoreLeavesChance)
	{
		super(tree, generateCoreLeavesChance);
	}

	public void setHeight(int baseHeight, int randHeight)
	{
		this.baseHeight = baseHeight;
		this.randHeight = randHeight;
	}

	private boolean checkCanGrow(World world, Random rand, int x, int y, int z, int height)
	{
		if (y >= 1 && y + height + 1 <= 256)
		{
			if(!checkEmpty(world, x, y, z, 1, 0, 1, false)) return false;
			if(!checkEmpty(world, x, y + 1, z, 2, height, 2, true)) return false;
			return true;
		}
		else
			return false;
	}

	private boolean canHugeTreePlantAt(World world, Random rand, int x, int y, int z)
	{
		Block block = world.getBlock(x, y - 1, z);

		boolean isSoil = block.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (BlockSapling) Blocks.sapling);
		if (isSoil && y >= 2)
		{
			block.onPlantGrow(world, x    , y - 1, z    , x, y, z);
			world.getBlock(x + 1, y - 1, z    ).onPlantGrow(world, x + 1, y - 1, z    , x, y, z);
			world.getBlock(x    , y - 1, z + 1).onPlantGrow(world, x    , y - 1, z + 1, x, y, z);
			world.getBlock(x + 1, y - 1, z + 1).onPlantGrow(world, x + 1, y - 1, z + 1, x, y, z);
			return true;
		}
		else
			return false;
	}

	protected boolean matchHugeTreeGrow(World world, Random rand, int x, int y, int z, int height)
	{
		return checkCanGrow(world, rand, x, y, z, height) && canHugeTreePlantAt(world, rand, x, y, z);
	}
}