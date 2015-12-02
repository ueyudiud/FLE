package fle.core.tree;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.enums.EnumWorldNBT;
import fle.api.util.ITextureLocation;
import fle.api.world.BlockPos;
import fle.core.block.BlockLog;
import fle.core.init.IB;
import fle.core.util.TextureLocation;

public class TreeSisal extends TreeBase
{
	public TreeSisal()
	{
		super("sisal");
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public int getGenerateWeight(World world, int x, int z)
	{
		BiomeGenBase tBiome = world.getBiomeGenForCoords(x, z);
		return tBiome.getFloatTemperature(x, 128, z) > 0.85F ? 4 : 0;
	}

	@Override
	public boolean generate(World world, int x, int y, int z, Random rand)
	{
		if(!world.getBlock(x, y - 1, z).canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this))
			return false;
		if(rand.nextInt() != 4)
		{
			int height = getGrowHeight(world, x, y, z, 3, 2, 0.4F);
			if(height == 0) return false;
			setBlock(log(), world, x, y, z);
			setBlock(leaves(), world, x + 1, y + 1, z);
			setBlock(leaves(), world, x - 1, y + 1, z);
			setBlock(leaves(), world, x, y + 1, z + 1);
			setBlock(leaves(), world, x, y + 1, z - 1);
		}
		else
		{
			int height = getGrowHeight(world, x, y, z, 2, 3, 0.4F);
			if(height == 0) return false;
			setBlock(log(), world, x, y, z);
			setBlock(leaves(), world, x, y + 1, z);
		}		
		return true;
	}
	
	private void setBlock(Block block, World world, int x, int y, int z)
	{
		if(world.getBlock(x, y, z).isReplaceable(world, x, y, z))
		{
			setBlock(world, x, y, z, block, 0);
			BlockLog.setData(new BlockPos(world, x, y, z), (short) BlockLog.trees.serial(this));
		}
	}

	@Override
	public ITextureLocation getTextureLocate(boolean isLog)
	{
		return isLog ? new TextureLocation(new String[]{"log/" + name.toLowerCase() + "_side", "log/" + name.toLowerCase() + "_top"}) : 
			new TextureLocation(new String[]{"leaf/" + name.toLowerCase(), "leaf/" + name.toLowerCase() + "_top"});
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
		return dir != ForgeDirection.UP && dir != ForgeDirection.DOWN ?
				0 : 1;
	}

	@Override
	public int getLeavesIconID(ForgeDirection dir, IBlockAccess world, int x, int y, int z)
	{
		return dir != ForgeDirection.UP && dir != ForgeDirection.DOWN ?
				0 : 1;
	}

	@Override
	public boolean onLeavesToss(World world, int x, int y, int z,
			EntityPlayer player, ItemStack tool)
	{
		return false;
	}

	@Override
	public void onLeavesUpdate(World world, int x, int y, int z, Random rand)
	{
		
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