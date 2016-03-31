package farcore.block;

import java.util.ArrayList;
import java.util.Random;

import farcore.interfaces.tile.IDropHandler;
import farcore.lib.tile.TileEntityBase;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public abstract class BlockHasTile extends BlockBase implements ITileEntityProvider
{
	protected Random itemRand = new Random();
	protected ThreadLocal<TileEntity> threadTile = new ThreadLocal();

	protected BlockHasTile(Material material)
	{
		super(material);
	}
	protected BlockHasTile(String name, Material material)
	{
		super(name, material);
	}
	protected BlockHasTile(String name, Class<? extends ItemBlockBase> clazz, Material material, Object...objects)
	{
		super(name, clazz, material, objects);
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		;
	}
	
	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TileEntityBase)
		{
			return ((TileEntityBase) tile).getLightValue();
		}
		return super.getLightValue(world, x, y, z);
	}
		
	@Override
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TileEntityBase)
		{
			((TileEntityBase) tile).markUpdate();
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,	Block block)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TileEntityBase)
		{
			((TileEntityBase) tile).markUpdate();
		}
	}
	
	@Override
	public void onBlockClicked(World world, int x, int y, int z,
			EntityPlayer player)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TileEntityBase)
		{
			((TileEntityBase) tile).markUpdate();
		}
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block,
			int meta)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TileEntityBase)
		{
			((TileEntityBase) tile).onBlockBreak(meta);
		}
		if(tile instanceof IInventory)
		{
			dropInventoryItem(world, x, y, z, (IInventory) tile);
		}
		threadTile.set(tile);
	}
	
	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y,
			int z, int meta)
	{
		if(threadTile.get() == null)
		{
			threadTile.set(world.getTileEntity(x, y, z));
		}
        player.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
        player.addExhaustion(0.025F);

        if (EnchantmentHelper.getSilkTouchModifier(player) && canSilkHarvest(world, player, x, y, z, meta))
        {
        	ArrayList<ItemStack> items = getDrops(world, x, y, z, meta, 0, true, threadTile.get());

            ForgeEventFactory.fireBlockHarvesting(items, world, this, x, y, z, meta, 0, 1.0f, true, player);
            for (ItemStack is : items)
            {
                dropBlockAsItem(world, x, y, z, is);
            }
        }
        else
        {
            int i1 = EnchantmentHelper.getFortuneModifier(player);
        	ArrayList<ItemStack> items = getDrops(world, x, y, z, meta, 0, false, threadTile.get());

            ForgeEventFactory.fireBlockHarvesting(items, world, this, x, y, z, meta, i1, 1.0f, false, player);
            for (ItemStack is : items)
            {
                dropBlockAsItem(world, x, y, z, is);
            }
        }
		threadTile.set(null);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		if(world.getTileEntity(x, y, z) != null)
		{
			return getDrops(world, x, y, z, metadata, fortune, false, world.getTileEntity(x, y, z));
		}
		return threadTile.get() == null ? new ArrayList() : getDrops(world, x, y, z, metadata, fortune, false, threadTile.get());
	}
	
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune, boolean silktouch, TileEntity tile)
	{
		return new ArrayList();
	}
	
	protected void dropInventoryItem(World world, int x, int y, int z, IInventory inventory)
	{
		for(int i = 0; i < inventory.getSizeInventory(); ++i)
		{
			ItemStack stack = inventory.getStackInSlotOnClosing(i);
			if(stack != null)
			{
				while(stack.stackSize > 0)
				{
					int size = 16 + itemRand.nextInt(8);
					if(size >= stack.stackSize)
					{
						size = stack.stackSize;
					}
					EntityItem item = new EntityItem(world,
							(double) x + 0.5 + itemRand.nextDouble() * 0.2 - itemRand.nextDouble() * 0.2, 
							(double) y + 0.5 + itemRand.nextDouble() * 0.2 - itemRand.nextDouble() * 0.2, 
							(double) z + 0.5 + itemRand.nextDouble() * 0.2 - itemRand.nextDouble() * 0.2, stack.splitStack(size));
					world.spawnEntityInWorld(item);
				}
			}
		}
	}
}