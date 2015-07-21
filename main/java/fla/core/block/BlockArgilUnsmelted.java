package fla.core.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.util.FlaValue;
import fla.api.util.SubTag;
import fla.api.world.BlockPos;
import fla.core.FlaItems;
import fla.core.tileentity.TileEntityArgilUnsmelted;

public class BlockArgilUnsmelted extends BlockArgilBase
{
	public BlockArgilUnsmelted() 
	{
		super(Material.clay);
		setItemTextureName(FlaValue.TEXT_FILE_NAME + ":clay/1", 0);
	}

	@Override
	public ForgeDirection getBlockDirection(BlockPos pos) 
	{
		return ForgeDirection.UNKNOWN;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int tile)
	{
		return new TileEntityArgilUnsmelted();
	}

	@Override
	public int getMaxDamage() 
	{
		return 1;
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int level)
	{
		return FlaItems.argil_unsmelted;
	}

	@Override
	public void addNBTToTileByItemStack(ItemStack stack) 
	{
		
	}

	@Override
	public void addNBTToItemStackByTile(ItemStack stack, TileEntity tile,
			int meta) 
	{
		
	}
}