package farcore.block;

import java.util.ArrayList;
import java.util.List;

import farcore.event.BlockCheckHarvestingEvent;
import farcore.world.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;

public class BlockBase2Control extends BlockBase1Naming
{	
	/**
	 * The local thread correct tile entity of block broken.
	 */
	protected ThreadLocal<TileEntity> tileThread = new ThreadLocal();
	
	protected int maxSize = 64;
	
	BlockBase2Control(String unlocalized, Material Material)
	{
		super(unlocalized, Material);
	}
	
	/**
	 * Get max stack size of block.
	 * @param stack 
	 * @return
	 */
	public int getMaxStackSize(ItemStack stack)
	{
		return maxSize;
	}	
	
	private MapColor mapColor = null;
	
	public BlockBase setMapColor(MapColor aColor)
	{
		mapColor = aColor;
		return (BlockBase) this;
	}
	
	/**
	 * Get map color from block.
	 */
	@Override
	public MapColor getMapColor(int metadata)
	{
		return mapColor == null ? super.getMapColor(metadata) : mapColor;
	}

    /**
     * Called to actually place the block, after the location is determined
     * and all permission checks have been made.
     *
     * @param stack The item stack that was used to place the block. This can be changed inside the method.
     * @param player The player who is placing the block. Can be null if the block is not being placed by a player.
     * @param side The side the player (or machine) right-clicked on.
     */
	public boolean onPlacedAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
       if (!world.setBlock(x, y, z, this, metadata, 3))
       {
           return false;
       }

       if (world.getBlock(x, y, z) == this)
       {
           onBlockPlacedBy(world, x, y, z, player, stack);
           onPostBlockPlaced(world, x, y, z, metadata);
       }

       return true;
    }
	
	@Override
	public final boolean canHarvestBlock(EntityPlayer player, int meta)
	{
		return true; // Use far land era harvest checker.
	}
	
	@Override
	public boolean isToolEffective(String type, int metadata)
	{
		return super.isToolEffective(type, metadata);
	}
	
	public boolean canHarvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta)
	{
		boolean ret = BlockHook.canHarvestBlock(this, player, new BlockPos(world, x, y, z));
		BlockCheckHarvestingEvent event = 
				new BlockCheckHarvestingEvent(x, y, z, world, this, meta, ret);
		MinecraftForge.EVENT_BUS.post(event);
		return event.succeed;
	}
	
	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y,
			int z, int meta)
	{
		if(canHarvestBlock(world, player, x, y, z, meta))
		{
			player.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
			player.addExhaustion(0.025F);
			
			boolean silkHarvest =
					EnchantmentHelper.getSilkTouchModifier(player) && 
					canSilkHarvest(world, player, x, y, z, meta);
			int fortune = 0;
			if(!silkHarvest)
				fortune = EnchantmentHelper.getFortuneModifier(player);
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			getDrops(items, world, player, x, y, z, meta, fortune, silkHarvest, tileThread.get());
			
			ForgeEventFactory.fireBlockHarvesting(items, world, this, 
					x, y, z, meta, fortune, 1.0F, silkHarvest, player);
			for (ItemStack is : items)
			{
				this.dropBlockAsItem(world, x, y, z, is);
			}
		}
		tileThread.set(null);
	}
	
	@Override
	public final ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		getDrops(ret, world, harvesters.get(), x, y, z, metadata, fortune, false, tileThread.get());
		return ret;
	}
	
	protected void getDrops(List<ItemStack> list, World world, EntityPlayer player, 
			int x, int y, int z, int meta, int fortune, boolean silkHarvest, TileEntity tile)
	{
		if(silkHarvest)
		{
			list.add(createStackedBlock(meta));
		}
		else
		{
			Item i = getItemDropped(meta, world.rand, fortune);
			if(i != null)
			{
				list.add(new ItemStack(getItemDropped(meta, world.rand, fortune), quantityDropped(meta, fortune, world.rand), meta));
			}
		}
	}
	
	/**
	 * Saving data during breaking block.
	 */
	@Override
	public void breakBlock(World aWorld, int x, int y, int z, Block aBlock, int aMeta)
	{
		if(aWorld.getTileEntity(x, y, z) != null)
		{
			TileEntity tile = aWorld.getTileEntity(x, y, z);
			tileThread.set(tile);
		}
		super.breakBlock(aWorld, x, y, z, aBlock, aMeta);
	}

	@Override
	public void dropBlockAsItem(World world, int x,
			int y, int z, ItemStack stack)
	{
		super.dropBlockAsItem(world, x, y, z, stack);
	}
}