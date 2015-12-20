package fle.resource.tree;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.util.FleValue;
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
		int height = getGrowHeight(world, x, y, z, 2, 6, 1.3F);
		if (height < 2)
			return false;
		height = height + height / 3 - rand.nextInt(height / 2 + 1);
		for (int cHeight = 0; cHeight < height; cHeight++)
		{
			setBlock(log(), world, x, y + cHeight, z, false, rand);
			if (cHeight <= height / 2)
				continue;
			for(int cx = -4; cx <= 4; ++cx)
				for(int cz = -4; cz <= 4; ++cz)
					if(cx * cx + cz * cz <= 7)
						setBlock(leaves(), world, x + cx, y + cHeight, z + cz, true, rand);
		}

		int height2 = height / 4 + rand.nextInt(2);
		for (int i = 0; i <= height2; i++)
			for(int cx = -2; cx <= 2; ++cx)
				for(int cz = -2; cz <= 2; ++cz)
					if (world.getBlock(x, y + height + i, z).canBeReplacedByLeaves(world, x, y + height + i, z) && cx * cx + cz * cz <= 1)
						setBlock(leaves(), world, x + cx, y + height, z + cz, true, rand);
		return true;
	}
	
	private void setBlock(Block block, World world, int x, int y, int z, boolean doRand, Random rand)
	{
		if(world.getBlock(x, y, z).isReplaceable(world, x, y, z))
		{
			if(doRand)
				setBlock(world, x, y, z, block, rand.nextInt(4) == 0 ? 1 : 0);
			else
				setBlock(world, x, y, z, block, 0);
		}
	}

	@Override
	public TextureLocation getTextureLocate(boolean isLog)
	{
		return isLog ? new TextureLocation(new String[]{"log/" + name.toLowerCase() + "_side", "log/" + name.toLowerCase() + "_top"}) : new TextureLocation(new String[]{"leaf/" + name.toLowerCase(), "leaf/" + name.toLowerCase() + "_f_u", "leaf/" + name.toLowerCase() + "_f"});
	}

	@Override
	public int getDefaultLogIconID(ForgeDirection dir)
	{
		return dir != ForgeDirection.UP && dir != ForgeDirection.DOWN ?
				0 : 1;
	}

	@Override
	public int getLogIconID(ForgeDirection dir, IBlockAccess world, int x, int y,
			int z)
	{
		return dir != ForgeDirection.UP && dir != ForgeDirection.DOWN ?
				0 : 1;
	}

	@Override
	public int getDefaultLeavesIconID(ForgeDirection dir)
	{
		return 0;
	}

	@Override
	public int getLeavesIconID(ForgeDirection dir, IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		return meta == 0 ? 0 : meta < 9 ? 1 : 2;
	}

	@Override
	public boolean onLeavesToss(World world, int x, int y, int z,
			EntityPlayer player, ItemStack tool)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(meta > 8)
		{
			if(!world.isRemote)
			{
				dropBlockAsItem(world, x, y, z, ItemFleFood.a("citron", 1 + player.getRNG().nextInt(2)));
				world.setBlockMetadataWithNotify(x, y, z, 0, 2);
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
			int meta = world.getBlockMetadata(x, y, z);
			if(meta == 0 && rand.nextInt(6) == 0)
			{
				world.setBlockMetadataWithNotify(x, y, z, 1, 0);
				world.markBlockForUpdate(x, y, z);
				return;
			}
			if(meta < 8 && meta != 0)
			{
				world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
				world.markBlockForUpdate(x, y, z);
				return;
			}
			else if(meta < 16 && rand.nextInt(3) == 0)
			{
				if(rand.nextInt(8) < meta - 8)
				{
					dropBlockAsItem(world, x, y - 1, z, ItemFleFood.a("citron", 1 + rand.nextInt(2)));
					world.setBlockMetadataWithNotify(x, y, z, 0, 2);
				}
				else
				{
					world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
				}
				world.markBlockForUpdate(x, y, z);
				return;
			}			
		}
	}
	
	@Override
	public void getLeavesInfomation(World world, int x, int y, int z,
			List aList)
	{
		int meta = world.getBlockMetadata(x, y, z);
		
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