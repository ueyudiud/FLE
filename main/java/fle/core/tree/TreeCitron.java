package fle.core.tree;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.FleValue;
import fle.api.enums.EnumWorldNBT;
import fle.api.world.BlockPos;
import fle.core.block.BlockLog;
import fle.core.init.IB;
import fle.core.item.ItemFleFood;
import fle.core.util.TextureLocation;
import fle.core.world.biome.FLEBiome;

public class TreeCitron extends TreeBase
{	
	public TreeCitron()
	{
		super("citron");
	}
	
	@Override
	public int getGenerateWeight(World world, int x, int z) 
	{
		BiomeGenBase tBiome = world.getBiomeGenForCoords(x, z);
		return tBiome == FLEBiome.warm_forest || tBiome == FLEBiome.forestHills ? 6 :
			tBiome == FLEBiome.jungle || tBiome == FLEBiome.warm_plains ? 2 : -1;
	}

	@Override
	public boolean generate(World world, int x, int y, int z, Random rand) 
	{
		if(!world.getBlock(x, y - 1, z).canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this))
			return false;
		Block woodBlock = IB.log;
		Block leavesBlock = IB.leaf;
		int height = getGrowHeight(world, x, y, z, 2, 6, 1.3F);
		if (height < 2)
			return false;
		height = height + height / 3 - rand.nextInt(height / 2 + 1);
		for (int cHeight = 0; cHeight < height; cHeight++)
		{
			setBlock(woodBlock, world, x, y + cHeight, z, false, rand);
			if (cHeight <= height / 2)
				continue;
			for(int cx = -4; cx <= 4; ++cx)
				for(int cz = -4; cz <= 4; ++cz)
					if(cx * cx + cz * cz <= 7)
						setBlock(leavesBlock, world, x + cx, y + cHeight, z + cz, true, rand);
		}

		int height2 = height / 4 + rand.nextInt(2);
		for (int i = 0; i <= height2; i++)
			for(int cx = -2; cx <= 2; ++cx)
				for(int cz = -2; cz <= 2; ++cz)
					if (world.getBlock(x, y + height + i, z).canBeReplacedByLeaves(world, x, y + height + i, z) && cx * cx + cz * cz <= 1)
						setBlock(leavesBlock, world, x + cx, y + height, z + cz, true, rand);
		return true;
	}
	
	private void setBlock(Block block, World world, int x, int y, int z, boolean doRand, Random rand)
	{
		if(world.getBlock(x, y, z).isReplaceable(world, x, y, z))
		{
			setBlock(world, x, y, z, block, 0);
			BlockLog.setData(new BlockPos(world, x, y, z), (short) BlockLog.trees.serial(this));
			if(doRand && rand.nextInt(4) == 0)
				FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), EnumWorldNBT.Age, 1);
		}
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
		int meta = FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), EnumWorldNBT.Age);
		return meta == 0 ? 0 : meta < 16 ? 1 : 2;
	}

	@Override
	public boolean onLeavesToss(World world, int x, int y, int z,
			EntityPlayer player, ItemStack tool)
	{
		int meta = FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), EnumWorldNBT.Age);
		if(meta == 16) 
		{
			if(!world.isRemote)
			{
				dropBlockAsItem(world, x, y, z, ItemFleFood.a("citron", 1 + player.getRNG().nextInt(2)));
				FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), EnumWorldNBT.Age, 0);
				world.markBlockForUpdate(x, y, z);
			}
			return true;
		}
		return false;
	}

	@Override
	public void onLeavesUpdate(World world, int x, int y, int z, Random rand)
	{
		if(rand.nextInt(8) == 0)
		{
			int meta = FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), EnumWorldNBT.Age);
			boolean flag = false;
			if(meta < 16 && meta != 0)
			{
				FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), EnumWorldNBT.Age, ++meta);
				world.markBlockForUpdate(x, y, z);
				return;
			}
			if(meta < 24 && meta != 0 && rand.nextInt(3) == 0)
			{
				if(rand.nextInt(8) < meta - 16)
				{
					dropBlockAsItem(world, x, y - 1, z, ItemFleFood.a("citron", 1 + rand.nextInt(2)));
					FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), EnumWorldNBT.Age, 0);
				}
				else
				{
					FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), EnumWorldNBT.Age, ++meta);
				}
				world.markBlockForUpdate(x, y, z);
				return;
			}
			if(meta == 0 && rand.nextInt(6) == 0)
			{
				FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), EnumWorldNBT.Age, 1);
				world.markBlockForUpdate(x, y, z);
				return;
			}
			
		}
	}
	
	@Override
	public void getLeavesInfomation(World world, int x, int y, int z,
			List aList)
	{
		int meta = FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), EnumWorldNBT.Age);
		
		if(meta != 0)
		{
			aList.add("Growing Progress : " + FleValue.format_progress.format_c((double) Math.min(meta, 16) / 16D));
		}
		super.getLeavesInfomation(world, x, y, z, aList);
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

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Plains;
	}
}