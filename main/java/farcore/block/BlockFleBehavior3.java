package farcore.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.RotationHelper;

public class BlockFleBehavior3 extends BlockFleMeta2
{
	/**
	 * The local thread correct tile entity of block broken.
	 */
	protected ThreadLocal<TileEntity> tileThread = new ThreadLocal();
	
	protected BlockFleBehavior3(String unlocalized, Material material)
	{
		super(unlocalized, material);
	}
	protected BlockFleBehavior3(Class<? extends ItemBlock> clazz, String unlocalized, Material material)
	{
		super(clazz, unlocalized, material);
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
		else
		{
			tileThread.set(null);
		}
		metaThread.set(new Integer(getDamageValue(aWorld, x, y, z)));
		super.breakBlock(aWorld, x, y, z, aBlock, aMeta);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x,
			int y, int z)
	{
		return super.canPlaceBlockAt(world, x, y, z) && canBlockStay(world, x, y, z);
	}
	
	@Override
	public boolean canBlockStay(World world, int x,
			int y, int z)
	{
		return true;
	}
	
	@Override
	protected void dropBlockAsItem(World world, int x,
			int y, int z, ItemStack stack)
	{
		super.dropBlockAsItem(world, x, y, z, stack);
	}
		
	@Override
	public final ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			int metadata, int fortune)
	{
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        Integer integer = metaThread.get();
        TileEntity tile = tileThread.get();
        getDrops(ret, world, x, y, z, 
        		integer == null ? getDamageValue(world, x, y, z) : integer.intValue(), fortune, 
        				tile == null ? world.getTileEntity(x, y, z) : tile);
        metaThread.set(null);
        tileThread.set(null);
        return ret;
	}
	
	protected void getDrops(List<ItemStack> list, World world, int x, int y, int z, int meta, int fortune, TileEntity tile)
	{
		Item i = getItemDropped(meta, world.rand, fortune);
		if(i != null)
		{
			list.add(new ItemStack(getItemDropped(meta, world.rand, fortune), quantityDropped(meta, fortune, world.rand), meta));
		}
	}
	
	@Override
	public ForgeDirection[] getValidRotations(World world, int x, int y,
			int z)
	{
		return RotationHelper.getValidVanillaBlockRotations(this);
	}
	
	@Override
	public boolean rotateBlock(World world, int x, int y, int z,
			ForgeDirection axis)
	{
		return false;
	}
}