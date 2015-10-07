package fle.core.tree;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.world.BlockPos;
import fle.api.world.TreeInfo;

public abstract class TreeBase extends TreeInfo
{
	public TreeBase(String aName)
	{
		super(aName);
	}

	public int getGrowHeight(World world, int x, int y, int z, final int width, final int height, final float growHardness)
	{
		BlockPos pos = new BlockPos(world, x, y, z);
		Block base = pos.toPos(ForgeDirection.DOWN).getBlock();
		if(!base.isSideSolid(world, x, y, z, ForgeDirection.UP))
			return 0;
		int height1;
		int c;
		BlockPos pos1 = pos.toPos(ForgeDirection.UP);
		for (height1 = 1; height1 < height; height1++)
		{
			if(pos1.getBlock().isAir(world, x, y, z))
			{
				pos1 = pos1.toPos(ForgeDirection.UP);
				c = 0;
				for(int i = -width; i <= width; ++i)
					for(int j = -width; j <= width; ++j)
						if(!pos1.toPos(i, 0, j).isAir()) ++c;
				if(c > 4 * width * width / growHardness) break;
				continue;
			}
			else
			{
				height1 -= 1;
				break;
			}
		}
		return height1;
	}

	public void setBlock(World world, int x, int y, int z, Block block, int meta)
	{
		if(genFlag)
			world.setBlock(x, y, z, block, meta, 2);
		else
			world.setBlock(x, y, z, block, meta, 3);
	}
	
	protected void dropBlockAsItem(World aWorld, int x, int y, int z, ItemStack aStack)
    {
        if (!aWorld.isRemote && aWorld.getGameRules().getGameRuleBooleanValue("doTileDrops") && !aWorld.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
        {
            float f = 0.7F;
            double d0 = (double)(aWorld.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(aWorld.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(aWorld.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(aWorld, (double)x + d0, (double)y + d1, (double)z + d2, aStack);
            entityitem.delayBeforeCanPickup = 10;
            aWorld.spawnEntityInWorld(entityitem);
        }
    }
}