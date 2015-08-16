package fle.core.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.FleValue;
import fle.api.world.BlockPos;
import fle.core.entity.EntityFleFallingBlock;
import fle.core.item.ItemFleSub;
import fle.core.item.ItemSub;

public class BlockAsh extends BlockFalling
{
	public BlockAsh() 
	{
		super("ash", Material.sand);
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
		return FleValue.FLE_RENDER_ID;
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
    	if(dropBlock(world, x, y, z, world.getBlockMetadata(x, y, z))) return;
    	super.updateTick(world, x, y, z, rand);
    }
    
    public boolean dropBlock(World world, int x, int y, int z, int damage)
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
    		return true;
    	}
    	return false;
    }

    public int quantityDropped(int meta, int fortune, Random random)
    {
    	return meta + 1;
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
    		int metadata, int fortune) 
    {
    	ArrayList<ItemStack> list = new ArrayList();
    	int size = quantityDropped(metadata, fortune, world.rand);
    	for(int i = 0; i < size; ++i)
    	{
    		list.add(ItemFleSub.a("plant_ash"));
    	}
    	return list;
    }

	@Override
	public void onBlockStartMove(World aWorld, BlockPos aPos)
	{
		
	}

	@Override
	public boolean canBlockFallOn(World aWorld, BlockPos aPos, boolean aCheck)
	{
		return aCheck ? true : aPos.getBlock() == this;
	}

	@Override
	public void onBlockEndMove(World aWorld, BlockPos aPos, int metadata)
	{
		if(aPos.toPos(ForgeDirection.UP).isAir() && (!aPos.isAir()))
		{
			aWorld.setBlock(aPos.x, aPos.y + 1, aPos.z, this, metadata, 2);
		}
		else
		{
			aWorld.setBlock(aPos.x, aPos.y, aPos.z, this, metadata, 2);
		}
		FLE.fle.getWorldManager().removeData(aPos);
	}

	@Override
	public void onBlockHitEntity(World aWorld, EntityLivingBase aEntity)
	{
		
	}
}