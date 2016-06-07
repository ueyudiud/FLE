package fle.api.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.block.BlockBase;
import farcore.block.ItemBlockBase;
import farcore.lib.recipe.DropHandler;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockClassicalVine extends BlockBase implements IShearable
{
	protected int growOffset = -1;
	protected DropHandler[] handlers = new DropHandler[16];
	
	public BlockClassicalVine(String name)
	{
		super(name, ItemBlockBase.class, Material.vine, true);
		setTickRandomly(true);
	}

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return FarCore.handlerA.getRenderId();
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
    	return null;
    }
    
    @Override
    public boolean canBlockStay(World world, int x, int y, int z)
    {
    	int meta = world.getBlockMetadata(x, y, z);
    	return canBlockStay(world, x, y, z, meta);
    }
    
    protected boolean canBlockStay(World world, int x, int y, int z, int meta)
    {
    	if(U.Worlds.isBlock(world, x, y + growOffset, z, this, meta, false))
    	{
        	return true;
    	}
    	return world.getBlock(x, y, z + 1).isSideSolid(world, x, y, z + 1, ForgeDirection.NORTH) ||
    			world.getBlock(x, y, z - 1).isSideSolid(world, x, y, z - 1, ForgeDirection.SOUTH) ||
    			world.getBlock(x + 1, y, z).isSideSolid(world, x + 1, y, z, ForgeDirection.WEST) ||
    			world.getBlock(x - 1, y, z).isSideSolid(world, x - 1, y, z, ForgeDirection.EAST);
	}
    
//    @SideOnly(Side.CLIENT)
//    public int getBlockColor()
//    {
//        return ColorizerFoliage.getFoliageColorBasic();
//    }
//
//    /**
//     * Returns the color this block should be rendered. Used by leaves.
//     */
//    @SideOnly(Side.CLIENT)
//    public int getRenderColor(int meta)
//    {
//        return ColorizerFoliage.getFoliageColorBasic();
//    }
//
//    /**
//     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
//     * when first determining what to render.
//     */
//    @SideOnly(Side.CLIENT)
//    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
//    {
//        return world.getBiomeGenForCoords(x, z).getBiomeFoliageColor(x, y, z);
//    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
    	if(!world.isRemote && !canBlockStay(world, x, y, z))
    	{
    		world.setBlockToAir(x, y, z);
    	}
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
    	if(!world.isRemote)
    	{
    		if(!canBlockStay(world, x, y, z))
        	{
        		world.setBlockToAir(x, y, z);
        		return;
        	}
    		int meta = world.getBlockMetadata(x, y, z);
    		if(world.isAirBlock(x, y + growOffset, z))
    		{
				if(rand.nextInt(5) == 0)
				{
					world.setBlock(x, y + growOffset, z, this, meta, 2);
				}
			}
    	}
    }
    
	@Override
	protected void onBlockHarvest(World world, EntityPlayer player, int x, int y, int z, int meta,
			boolean silkTouching)
	{
		Block block;
		int meta1;
		do
		{
			y += growOffset;
			block = world.getBlock(x, y, z);
			meta1 = world.getBlockMetadata(x, y, z);
			if(block == this && meta1 == meta)
			{
				block.dropBlockAsItem(world, x, y, z, meta, 0);
				world.setBlockToAir(x, y, z);
			}
			else
			{
				break;
			}
		}
		while(y < 256);
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune,
			boolean silkTouching)
	{
		return handlers[metadata] == null ? 
				(handlers[metadata] = getDropHandler(metadata)).randomDrops(world.rand) :
					handlers[metadata].randomDrops(world.rand);
	}
	
	protected DropHandler getDropHandler(int meta)
	{
		return DropHandler.EMPTY;
	}
    
    @Override
    public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity)
    {
    	return true;
    }

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune)
	{
		return new ArrayList(Arrays.asList(new ItemStack(this, 1, damageDropped(world.getBlockMetadata(x, y, z)))));
	}
}