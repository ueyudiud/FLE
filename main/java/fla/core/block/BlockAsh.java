package fla.core.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.util.FlaValue;

public class BlockAsh extends BlockFalling
{
	public BlockAsh() 
	{
		super(Material.ground);
		setHardness(0.8F);
		setResistance(0.5F);
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list) 
	{
		for(int i = 0; i < 16; ++i)
		{
			list.add(new ItemStack(item, 1, i));
		}
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access,
			int x, int y, int z) 
	{
		setBlockBounds(0F, 0F, 0F, 1F, (float) (access.getBlockMetadata(x, y, z) + 1) / 16F, 1F);
	}
	
	@Override
	public int getRenderType() 
	{
		return FlaValue.ALL_RENDER_ID;
	}

	@Override
	public boolean isOpaqueCube() 
	{
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z) 
	{
		return world.getBlockMetadata(x, y, z) == 15;
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z,
			ForgeDirection side) 
	{
		return side == ForgeDirection.DOWN ? true : world.getBlockMetadata(x, y, z) == 15;
	}
	
    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World world, int x, int y, int z)
    {
        world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
    }
    
    /**
     * How many world ticks before ticking
     */
    public int tickRate(World world)
    {
        return 2;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        if (!world.isRemote)
        {
        	if(dropBlock(world, x, y, z)) return;
        	func_149828_a(world, x, y, z, world.getBlockMetadata(x, y, z));
        }
    }
    
    private boolean dropBlock(World world, int x, int y, int z)
    {
        if (func_149831_e(world, x, y - 1, z) && y >= 0)
        {
            byte b0 = 32;

            if (!fallInstantly && world.checkChunksExist(x - b0, y - b0, z - b0, x + b0, y + b0, z + b0))
            {
                if (!world.isRemote)
                {
                    EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this, world.getBlockMetadata(x, y, z));
                    this.func_149829_a(entityfallingblock);
                    world.spawnEntityInWorld(entityfallingblock);
                }
                return true;
            }
            else
            {
                world.setBlockToAir(x, y, z);

                while (func_149831_e(world, x, y - 1, z) && y > 0)
                {
                    --y;
                }

                if (y > 0)
                {
                    world.setBlock(x, y, z, this);
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int meta)
    {
        return 0;
    }
    
    public void func_149828_a(World world, int x, int y, int z, int damage)
    {
    	if(world.getBlock(x, y - 1, z) == this)
    	{
    		int uContain = damage + 1;
    		int dContain = world.getBlockMetadata(x, y - 1, z) + 1;
    		int add = Math.min(uContain, 16 - dContain);
    		int damage1 = dContain + add - 1;
    		int damage2 = uContain - add - 1;
    		world.setBlockMetadataWithNotify(x, y - 1, z, damage1, 2);
    		if(damage2 >= 0)
    		{
    			world.setBlockMetadataWithNotify(x, y, z, damage2, 2);
    		}
    		else
    		{
    			world.setBlockToAir(x, y, z);
    		}
    	}
    }

    public Item getItemDropped(int meta, Random rand, int fortune)
    {
        return Item.getItemFromBlock(this);
    }

    public int quantityDropped(int meta, int fortune, Random random)
    {
    	return meta + 1;
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
    		int metadata, int fortune) 
    {
    	return super.getDrops(world, x, y, z, metadata, fortune);
    }
}