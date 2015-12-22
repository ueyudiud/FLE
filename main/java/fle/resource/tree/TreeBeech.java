package fle.resource.tree;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;
import fle.core.util.TextureLocation;

public class TreeBeech extends TreeBase
{
	public TreeBeech()
	{
		super("beech");
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public int getGenerateWeight(World world, int x, int z)
	{
		return 0;
	}

	@Override
	public boolean generate(World world, int x, int y, int z, Random rand)
	{
		if(!world.getBlock(x, y - 1, z).canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this))
			return false;
		int height = getGrowHeight(world, x, y, z, 3, 8, 0.7F);
		if (height < 5)
			return false;
		for (int cHeight = 0; cHeight < height; cHeight++)
		{
			setBlock(world, x, y + cHeight, z, log(), 0);
			if (cHeight <= height / 2)
				continue;
			int r = height - cHeight + height / 4;
			for(int cx = -r; cx <= r; ++cx)
				for(int cz = -r; cz <= r; ++cz)
					if(cx * cx + cz * cz <= r * r && world.getBlock(x + cx, y + cHeight, z + cz).isReplaceable(world, x + cx, y + cHeight, z + cz))
						setBlock(world, x + cx, y + cHeight, z + cz, leaves(), 0);
		}
		for (int height1 = height; height1 < height + height / 4 + 1; ++height1)
		{
			int r = height - height1 + height / 4 + 1;
			for(int cx = -r; cx <= r; ++cx)
				for(int cz = -r; cz <= r; ++cz)
					if(cx * cx + cz * cz <= r * r && world.getBlock(x + cx, y + height1, z + cz).isReplaceable(world, x + cx, y + height1, z + cz))
						setBlock(world, x + cx, y + height1, z + cz, leaves(), 0);
		}
		return true;
	}
	
	@Override
	public TextureLocation getTextureLocate(boolean isLog)
	{
		return isLog ? new TextureLocation(new String[]{"log/" + name.toLowerCase() + "_side", "log/" + name.toLowerCase() + "_top"}) : new TextureLocation("leaf/" + name.toLowerCase());
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
		return 0;
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