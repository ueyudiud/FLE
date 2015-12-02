package fle.core.block;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.FleValue;
import fle.api.block.BlockHasTile;
import fle.core.init.IB;
import fle.core.te.TileEntityPlacedItem;

public class BlockPlacedItem extends BlockHasTile
{
	public static boolean putPlacedItem(ItemStack stack, EntityPlayer aPlayer, World aWorld, int x, int y, int z, float xPos, float yPos, float zPos)
	{
		if(stack != null)
		{
			if(Item.getItemFromBlock(IB.placedItem).onItemUse(stack, aPlayer, aWorld, x, y, z, 0, xPos, yPos, zPos))
			{
				return true;
			}
			return false;
		}
		return true;
	}
	
	public BlockPlacedItem(String aName)
	{
		super(ItemPlaceable.class, aName, Material.circuits);
		setHardness(0.0F);
		setResistance(0.2F);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
		GameRegistry.registerTileEntity(TileEntityPlacedItem.class, "placedItem");
	}
	
	@Override
	public int getRenderType()
	{
		return FleValue.FLE_RENDER_ID;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean isNormalCube()
	{
		return false;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld,
			int x, int y, int z)
	{
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World aWorld, int aMeta)
	{
		return new TileEntityPlacedItem();
	}
	
	@Override
	protected boolean onBlockActivated(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer, ForgeDirection dir, float xPos, float yPos,
			float zPos)
	{
		return super.onBlockActivated(aWorld, x, y, z, aPlayer, dir, xPos, yPos, zPos);
	}
	
	@Override
	public boolean canPlaceBlockAt(World aWorld, int x,
			int y, int z)
	{
		return canBlockStay(aWorld, x, y, z);
	}
	
	@Override
	public boolean canBlockStay(World aWorld, int x,
			int y, int z)
	{
		return aWorld.getBlock(x, y - 1, z).isSideSolid(aWorld, x, y - 1, z, ForgeDirection.UP);
	}
	
	@Override
	public void onNeighborBlockChange(World aWorld, int x, int y, int z,
			Block block)
	{
		if(!canBlockStay(aWorld, x, y, z))
		{
			destoryItem(aWorld, x, y, z);
		}
	}
	
	private void destoryItem(World aWorld, int x, int y, int z)
	{
		List<ItemStack> list = getDrops(aWorld, x, y, z, aWorld.getTileEntity(x, y, z), 0, 0);
		for(ItemStack stack : list)
			dropBlockAsItem(aWorld, x, y, z, stack);
		aWorld.setBlockToAir(x, y, z);
		aWorld.removeTileEntity(x, y, z);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			TileEntity tile, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		if(tile instanceof TileEntityPlacedItem)
		{
			ret.add(((TileEntityPlacedItem) tile).getDrop());
		}
		return ret;
	}
}