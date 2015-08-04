package fle.core.tree;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.world.BlockPos;
import fle.api.world.TreeInfo;
import fle.core.block.BlockLog;
import fle.core.init.IB;
import fle.core.util.TextureLocation;

public class TreeCitron extends TreeInfo
{	
	public TreeCitron()
	{
		super("citron");
	}
	
	@Override
	public int getGenerateWeight(World world, int x, int z) 
	{
		BiomeGenBase tBiome = world.getBiomeGenForCoords(x, z);
		return tBiome == BiomeGenBase.forest || tBiome == BiomeGenBase.forestHills ? 6 :
			tBiome == BiomeGenBase.jungle || tBiome == BiomeGenBase.plains ? 2 : -1;
	}

	@Override
	public boolean generate(World world, int x, int y, int z, Random rand) 
	{
		Block woodBlock = IB.log;
		Block leavesBlock = IB.leaf;
		int height = getGrowHeight(world, x, y, z);
		if (height < 2)
			return false;
		height -= rand.nextInt(height / 2 + 1);
		for (int cHeight = 0; cHeight < height; cHeight++)
		{
			setBlock(woodBlock, world, x, y + cHeight, z, false, rand);
			if (cHeight <= height / 2)
				continue;
			for(int cx = -4; cx <= 4; ++cx)
				for(int cz = -4; cz <= 4; ++cz)
					if((Math.pow(cx, 2.0D) + Math.pow(cz, 2.0D)) <= 5)
						setBlock(leavesBlock, world, x + cx, y + cHeight, z + cz, true, rand);
		}

		for (int i = 0; i <= height / 4 + rand.nextInt(2); i++)
			if (world.isAirBlock(x, y + height + i, z))
				setBlock(leavesBlock, world, x, y + height, z, true, rand);
		return true;
	}
	
	private void setBlock(Block block, World world, int x, int y, int z, boolean doRand, Random rand)
	{
		if(world.getBlock(x, y, z).isReplaceable(world, x, y, z))
		{
			world.setBlock(x, y, z, block);
			BlockLog.setData(new BlockPos(world, x, y, z), (short) BlockLog.trees.serial(this));
			if(doRand && rand.nextInt(4) == 0)
				FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), 1, 1);
		}
	}

	public int getGrowHeight(World world, int x, int y, int z)
	{
		BlockPos pos = new BlockPos(world, x, y, z);
		Block base = pos.toPos(ForgeDirection.DOWN).getBlock();
		if(!base.isSideSolid(world, x, y, z, ForgeDirection.UP))
			return 0;
		int height;
		BlockPos pos1 = pos.toPos(ForgeDirection.UP);
		for (height = 1; height < 5; height++)
		{
			if(pos1.getBlock().isAir(world, x, y, z))
			{
				pos1 = pos1.toPos(ForgeDirection.UP);
				continue;
			}
			break;
		}
		return height;
	}

	@Override
	public TextureLocation getTextureLocate(boolean isLog)
	{
		return isLog ? new TextureLocation(new String[]{"log/" + name.toLowerCase() + "_side", "log/" + name.toLowerCase() + "_top"}) : new TextureLocation(new String[]{"leaf/" + name.toLowerCase(), "leaf/" + name.toLowerCase() + "_f_u", "leaf/" + name.toLowerCase() + "_f"});
	}

	@Override
	public int getDefaultLogIconID(boolean isSide)
	{
		return isSide ? 0 : 1;
	}

	@Override
	public int getLogIconID(boolean isSide, IBlockAccess world, int x, int y,
			int z)
	{
		return isSide ? 0 : 1;
	}

	@Override
	public int getDefaultLeavesIconID()
	{
		return 0;
	}

	@Override
	public int getLeavesIconID(IBlockAccess world, int x, int y, int z)
	{
		int meta = FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), 1);
		return meta == 0 ? 0 : meta < 16 ? 1 : 2;
	}

	@Override
	public boolean onLeavesToss(World world, int x, int y, int z,
			EntityPlayer player, ItemStack tool)
	{
		int meta = FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), 1);
		if(meta == 16) 
		{
			if(!world.isRemote)
			{
				dropBlockAsItem(world, x, y, z, new ItemStack(Items.apple));
				FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), 1, 0);
				world.markBlockForUpdate(x, y, z);
			}
			return true;
		}
		return false;
	}

	@Override
	public void onLeavesUpdate(World world, int x, int y, int z, Random rand)
	{
		if(rand.nextInt(10) == 0)
		{
			int meta = FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), 1);
			boolean flag = false;
			if(meta < 16 && meta != 0)
			{
				FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), 1, ++meta);
				world.markBlockForUpdate(x, y, z);
				return;
			}
			if(meta == 0 && rand.nextInt(4) == 0)
			{
				FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), 1, 1);
				world.markBlockForUpdate(x, y, z);
				return;
			}
			
		}
	}

	@Override
	public boolean onLogToss(World world, int x, int y, int z,
			EntityPlayer player, ItemStack tool)
	{
		return false;
	}

	@Override
	public void onLogUpdate(World world, int x, int y, int z, Random rand)
	{
		
	}
}