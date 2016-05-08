package fle.core.block;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.BlockBase;
import farcore.enums.EnumBlock;
import farcore.util.V;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSnow extends BlockBase
{
	public BlockSnow()
	{
		super("snow", ItemSnow.class, Material.snow);
        setBlockBounds(0);
        setTickRandomly(true);
        setHardness(0.1F);
        EnumBlock.snow.setBlock(this);
	}

    protected void setBlockBounds(int meta)
    {
        float f = (float) (meta + 1) / 16.0F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		int c = 1;
		while(world.getBlock(x, y + c, z).getMaterial() == Material.snow)
		{
			++c;
		}
		return c < 3 ? null : AxisAlignedBB.getBoundingBox(x + 0D, y + 0D, z + 0D, x + 1D, y + 1D, z + 1D);
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public void setBlockBoundsForItemRender()
	{
		setBlockBounds(0);
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		setBlockBounds(world.getBlockMetadata(x, y, z));
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y - 1, z);
		return block.isSideSolid(world, x, y - 1, z, ForgeDirection.UP) ||
				block.isLeaves(world, x, y - 1, z);
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return side == ForgeDirection.DOWN ? true :
			world.getBlockMetadata(x, y, z) == 15;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z,
			Block block)
	{
		checkAndDrop(world, x, y, z);
	}

    private boolean checkAndDrop(World world, int x, int y, int z)
    {
        if (!canPlaceBlockAt(world, x, y, z))
        {
            world.setBlockToAir(x, y, z);
            return false;
        }
        else
        {
            return true;
        }
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune,
    		boolean silkTouching) 
    {
    	return new ArrayList();
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
    	if(world.getSavedLightValue(EnumSkyBlock.Block, x, y, z) > 11)
    	{
    		if(V.spawnWaterBySnow)
    		{
    			EnumBlock.water.spawn(world, x, y, z, 0);
    		}
    		else
    		{
    			world.setBlockToAir(x, y, z);
    		}
    	}
    }
    
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z,
    		int side)
    {
    	return side == 0 ? false : super.shouldSideBeRendered(world, x, y, z, side);
    }
    
    @Override
    public boolean isReplaceable(IBlockAccess world, int x, int y, int z)
    {
    	return world.getBlockMetadata(x, y, z) < 3;
    }
}