package fle.core.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameData;
import fle.FLE;
import fle.api.block.BlockFle;
import fle.api.block.ItemFleBlock;
import fle.api.world.BlockPos;

public class BlockOilLamp extends BlockFle
{
	protected BlockOilLamp(Class<? extends ItemFleBlock> aItemClass,
			String aName, Material aMaterial)
	{
		super(aItemClass, aName, aMaterial);
		setTickRandomly(true);
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		return FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), 3) != 0 ? 12 : 0;
	}

	@Override
    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World aWorld, int x, int y, int z, Random aRand)
    {
		BlockPos tPos = new BlockPos(aWorld, x, y, z);
		if(FLE.fle.getWorldManager().getData(tPos, 3) != 0)
		{
			switch(FLE.fle.getWorldManager().getData(tPos, 2))
			{
			case 0 : FLE.fle.getWorldManager().setData(tPos, 3, 0);
			return;
			case 1 : break;
			case 2 : FLE.fle.getWorldManager().setData(tPos, 2, 1);
			aWorld.markBlockForUpdate(x, y, z);
			break;
			}
			int amount = FLE.fle.getWorldManager().getData(tPos, 1) - 1;
			if(amount > 0)
			{
				FLE.fle.getWorldManager().setData(tPos, 1, amount);
			}
			else
			{
				FLE.fle.getWorldManager().setData(tPos, 1, 0);
				FLE.fle.getWorldManager().setData(tPos, 3, 0);
				aWorld.markBlockForUpdate(x, y, z);
			}
		}
    }
	
	public static void setOilLamp(World aWorld, int x, int y, int z, Block aBlock, int amount, boolean hasWick, boolean isBurning)
	{
		BlockPos tPos = new BlockPos(aWorld, x, y, z);
		FLE.fle.getWorldManager().setData(tPos, 0, GameData.getBlockRegistry().getId(aBlock));
		FLE.fle.getWorldManager().setData(tPos, 1, amount);
		FLE.fle.getWorldManager().setData(tPos, 2, hasWick ? 1 : 0);
		FLE.fle.getWorldManager().setData(tPos, 3, isBurning ? 1 : 0);
	}
}