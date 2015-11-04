package fle.api.block;

import java.util.ArrayList;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
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

	@Override
	public abstract TileEntity createNewTileEntity(World aWorld, int aMeta);
	
	@Override
	public boolean hasTileEntity(int metadata) 
	{
		return true;
	}
	
	/**
	 * Get block drop from tile.
	 */
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			int metadata, int fortune) 
	{
		TileEntity tile = tileThread.get();
		if(tile != null)
			return getDrops(world, x, y, z, tile, metadata, fortune);
		return super.getDrops(world, x, y, z, getDamageValue(world, x, y, z), fortune);
	}
	

	public abstract ArrayList<ItemStack> getDrops(World world, int x, int y, int z
			, TileEntity tile, int metadata, int fortune);
}