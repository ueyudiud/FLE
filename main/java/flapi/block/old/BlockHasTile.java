package flapi.block.old;

import java.util.ArrayList;
import java.util.List;

import farcore.block.BlockFle;
import farcore.block.ItemFleBlock;
import farcore.block.TEBase;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockHasTile extends BlockFle implements ITileEntityProvider
{
	protected BlockHasTile(Class<? extends ItemFleBlock> aItemClass,
			String aName, Material aMaterial)
	{
		super(aItemClass, aName, aMaterial);
	}
	protected BlockHasTile(String aName, Material aMaterial)
	{
		super(aName, aMaterial);
	}
//	protected BlockHasTile(Class<? extends ItemFleBlock> aItemClass,
//			String aName, String aLocalized,  Material aMaterial)
//	{
//		super(aItemClass, aName, aLocalized, aMaterial);
//	}
//	protected BlockHasTile(String aName, String aLocalized, Material aMaterial)
//	{
//		super(aName, aLocalized, aMaterial);
//	}

	@Override
	public abstract TileEntity createNewTileEntity(World aWorld, int aMeta);
	
	@Override
	public boolean hasTileEntity(int metadata) 
	{
		return true;
	}
	
	@Override
	protected boolean hasSub()
	{
		return true;
	}
	
	/**
	 * Get block drop from tile.
	 */
	@Override
	protected void getDrops(List<ItemStack> list, World world, int x, int y, int z, int meta, int fortune,
			TileEntity tile)
	{
		list.add(new ItemStack(this, 1, meta));
	}
	

	public abstract ArrayList<ItemStack> getDrops(World world, int x, int y, int z
			, TileEntity tile, int metadata, int fortune);
}