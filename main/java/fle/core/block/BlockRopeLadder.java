package fle.core.block;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.block.BlockFle;

public class BlockRopeLadder extends BlockFle
{
	public BlockRopeLadder(String aName, String aLocalized)
	{
		super(ItemRopeLadder.class, aName, aLocalized, Material.circuits);
		setHardness(0.3F);
		setResistance(0.0F);
		maxStackSize = 1;
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World aWorld, int x,
			int y, int z, int aMeta)
	{
		int length1 = 1;
		while(aWorld.getBlock(x, y + length1, z) == this)
		{
			aWorld.setBlockToAir(x, y + length1, z);
			++length1;
		}
		int length2 = 1;
		while(aWorld.getBlock(x, y - length2, z) == this)
		{
			aWorld.setBlockToAir(x, y - length2, z);
			++length2;
		}
		metaThread.set(length1 + length2 - 1);
		super.onBlockDestroyedByPlayer(aWorld, x, y, z, aMeta);
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World aWorld, int x,
			int y, int z, Explosion explosion)
	{
		
	}

	@Override
	public void breakBlock(World aWorld, int x, int y, int z, Block aBlock, int aMeta)
	{
		;
	}
	
	@Override
	public void addInformation(ItemStack aStack, List<String> aList,
			EntityPlayer aPlayer)
	{
		aList.add(String.format("Length : %sm", ItemRopeLadder.b(aStack)));
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			int metadata, int fortune)
	{
		int length = metaThread.get();
		if(length <= 0) return new ArrayList();
		
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(ItemRopeLadder.a(length));
		return ret;
	}
	
    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int x, int y, int z)
    {
        setBlockBoundsBasedOnState(aWorld, x, y, z);
        return super.getCollisionBoundingBoxFromPool(aWorld, x, y, z);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess aWorld, int x, int y, int z)
    {
        func_149797_b(aWorld.getBlockMetadata(x, y, z));
    }

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World aWorld, int x, int y, int z)
    {
        setBlockBoundsBasedOnState(aWorld, x, y, z);
        return super.getSelectedBoundingBoxFromPool(aWorld, x, y, z);
    }

    public void func_149797_b(int aFace)
    {
        float f = 0.125F;

        if (aFace == 2)
        {
            setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        }

        if (aFace == 3)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        }

        if (aFace == 4)
        {
            setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if (aFace == 5)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
        }
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

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 8;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World aWorld, int x, int y, int z)
    {
        return aWorld.isSideSolid(x - 1, y, z, EAST ) ||
               aWorld.isSideSolid(x + 1, y, z, WEST ) ||
               aWorld.isSideSolid(x, y, z - 1, SOUTH) ||
               aWorld.isSideSolid(x, y, z + 1, NORTH);
    }

    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    public int onBlockPlaced(World aWorld, int x, int y, int z, int aSide, float aHitX, float aHitY, float aHitZ, int aMeta)
    {
        int j1 = aMeta;

        if(aWorld.getBlock(x, y + 1, z) == this)
        {
        	return onBlockPlaced(aWorld, x, y + 1, z, aSide, aHitX, aHitY, aHitZ, aMeta);
        }
        
        if ((j1 == 0 || aSide == 2) && aWorld.isSideSolid(x, y, z + 1, NORTH))
        {
            j1 = 2;
        }

        if ((j1 == 0 || aSide == 3) && aWorld.isSideSolid(x, y, z - 1, SOUTH))
        {
            j1 = 3;
        }

        if ((j1 == 0 || aSide == 4) && aWorld.isSideSolid(x + 1, y, z, WEST))
        {
            j1 = 4;
        }

        if ((j1 == 0 || aSide == 5) && aWorld.isSideSolid(x - 1, y, z, EAST))
        {
            j1 = 5;
        }

        return j1;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World aWorld, int x, int y, int z, Block aBlock)
    {
        int l = aWorld.getBlockMetadata(x, y, z);
        boolean flag = false;

        if (aWorld.getBlock(x, y + 1, z) == this)
        {
        	flag = true;
        }
        
        if (l == 2 && aWorld.isSideSolid(x, y, z + 1, NORTH))
        {
            flag = true;
        }

        if (l == 3 && aWorld.isSideSolid(x, y, z - 1, SOUTH))
        {
            flag = true;
        }

        if (l == 4 && aWorld.isSideSolid(x + 1, y, z, WEST))
        {
            flag = true;
        }

        if (l == 5 && aWorld.isSideSolid(x - 1, y, z, EAST))
        {
            flag = true;
        }

        if (!flag)
        {
            dropBlockAsItem(aWorld, x, y, z, l, 0);
            aWorld.setBlockToAir(x, y, z);
        }

        super.onNeighborBlockChange(aWorld, x, y, z, aBlock);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random p_149745_1_)
    {
        return 1;
    }

    @Override
    public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity)
    {
        return true;
    }
    
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab,
    		List aList)
    {
    	aList.add(ItemRopeLadder.a(1));
    	aList.add(ItemRopeLadder.a(5));
    	aList.add(ItemRopeLadder.a(20));
    }
    
    @Override
    public String getItemIconName()
    {
    	return FleValue.TEXTURE_FILE + ":tools/rope_ladder_item";
    }
}