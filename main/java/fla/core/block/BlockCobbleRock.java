package fla.core.block;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.util.SubTag;
import fla.api.world.BlockPos;

public abstract class BlockCobbleRock extends BlockBaseSub
{
	protected BlockCobbleRock(SubTag...tags)
	{
		super(Material.clay, tags);
	}

	@Override
	public ForgeDirection getBlockDirection(BlockPos pos) 
	{
		return ForgeDirection.UNKNOWN;
	}
	
	@Override
	public IIcon getIcon(BlockPos pos, ForgeDirection side) 
	{
		return icons[pos.getBlockMeta()];
	}

	@Override
	public IIcon getIcon(int meta, ForgeDirection side) 
	{
		return icons[meta];
	}

	@Override
	public int getRenderType() 
	{
		return 0;
	}

	@Override
	public boolean isNormalCube() 
	{
		return true;
	}

	@Override
	protected boolean canRecolour(World world, BlockPos pos,
			ForgeDirection side, int colour) 
	{
		return true;
	}
	
	@Override
	public boolean canHarvestBlock(EntityPlayer player, int meta) 
	{
		return true;
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			int metadata, int fortune) 
	{
		return correctItem[metadata] != null ? new ArrayList(Arrays.asList(correctItem[metadata].copy())) : new ArrayList();
	}
	
	protected ItemStack[] correctItem = new ItemStack[16];
	
	protected void registerItem(int meta, ItemStack i)
	{
		correctItem[meta] = i.copy();
	}
	
	public boolean isOpaqueCube() 
	{
		return true;
	}
}