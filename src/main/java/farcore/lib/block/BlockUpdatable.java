package farcore.lib.block;

import java.util.ArrayList;
import java.util.Random;

import farcore.lib.tile.IUpdatableTile;
import farcore.lib.tile.TEBase;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class BlockUpdatable extends BlockBase
{
	protected ThreadLocal<TileEntity> threadTile = new ThreadLocal();
	
	protected BlockUpdatable(String name, Material material)
	{
		super(name, material);
	}
	protected BlockUpdatable(String name, Class<? extends ItemBlockBase> clazz, Material material,
			Object...objects)
	{
		super(name, clazz, material, objects);
	}
	
	@Override
	public void onBlockClicked(World world, int x, int y, int z,
			EntityPlayer player)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TEBase)
		{
			MovingObjectPosition mop = U.Worlds.getMovingObjectPosition(world, player, !func_149698_L());
			if(((TEBase) tile).onBlockClicked(player, mop.sideHit))
			{
				return;
			}
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xPos,
			float yPos, float zPos)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TEBase)
		{
			if(((TEBase) tile).onBlockActivated(player, side, xPos, yPos, zPos))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block,
			int meta)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile instanceof TEBase)
		{
			((TEBase) tile).onBlockBreak(block, meta);
		}
		if(tile instanceof IInventory)
		{
			dropInventoryItem(world, x, y, z, (IInventory) tile);
		}
		threadTile.set(tile);
		super.breakBlock(world, x, y, z, block, meta);
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
        	onBlockHarvest(world, player, x, y, z, meta, true);
        	ArrayList<ItemStack> items = getDrops(world, x, y, z, meta, 0, true, threadTile.get());

            ForgeEventFactory.fireBlockHarvesting(items, world, this, x, y, z, meta, 0, 1.0f, true, player);
            for (ItemStack is : items)
            {
                dropBlockAsItem(world, x, y, z, is);
            }
        }
        else
        {
        	onBlockHarvest(world, player, x, y, z, meta, false);
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
	public final ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune,
			boolean silkTouching)
	{
		if(world.getTileEntity(x, y, z) != null)
		{
			return getDrops(world, x, y, z, metadata, fortune, false, world.getTileEntity(x, y, z));
		}
		return threadTile.get() == null ? new ArrayList() : getDrops(world, x, y, z, metadata, fortune, silkTouching, threadTile.get());
	}
	
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune, boolean silktouch, TileEntity tile)
	{
		return super.getDrops(world, x, y, z, metadata, fortune, silktouch);
	}
	
	protected void dropInventoryItem(World world, int x, int y, int z, IInventory inventory)
	{
		Random random = new Random();
		for(int i = 0; i < inventory.getSizeInventory(); ++i)
		{
			ItemStack stack = inventory.getStackInSlotOnClosing(i);
			if(stack != null)
			{
				while(stack.stackSize > 0)
				{
					int size = 16 + random.nextInt(8);
					if(size >= stack.stackSize)
					{
						size = stack.stackSize;
					}
					EntityItem item = new EntityItem(world,
							(double) x + 0.5 + random.nextDouble() * 0.2 - random.nextDouble() * 0.2, 
							(double) y + 0.5 + random.nextDouble() * 0.2 - random.nextDouble() * 0.2, 
							(double) z + 0.5 + random.nextDouble() * 0.2 - random.nextDouble() * 0.2, stack.splitStack(size));
					world.spawnEntityInWorld(item);
				}
			}
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,
			Block block)
	{
		TileEntity tile;
		if((tile = world.getTileEntity(x, y, z)) instanceof IUpdatableTile)
		{
			((IUpdatableTile) tile).causeUpdate(x, y, z, this, world.getBlockMetadata(x, y, z), false);
		}
		super.onNeighborBlockChange(world, x, y, z, block);
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ)
	{
		TileEntity tile;
		if((tile = world.getTileEntity(x, y, z)) instanceof IUpdatableTile)
		{
			((IUpdatableTile) tile).causeUpdate(x, y, z, this, world.getBlockMetadata(x, y, z), true);
		}
		super.onNeighborChange(world, x, y, z, tileX, tileY, tileZ);
	}
}