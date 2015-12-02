package fle.core.block.plant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleValue;
import fle.api.block.BlockFle;
import fle.api.world.BlockPos;
import fle.core.item.ItemFleSub;

public class BlockPlantRattan extends BlockFle implements IShearable
{
	public BlockPlantRattan(String aName, String aLocalized) 
	{
		super(aName, aLocalized, Material.vine);
        setTickRandomly(true);
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return FleValue.FLE_NOINV_RENDER_ID;
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
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        int l = world.getBlockMetadata(x, y, z);
        float f1 = 1.0F;
        float f2 = 1.0F;
        float f3 = 1.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        switch(l)
        {
        case 0 :
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            break;
        case 1 :
        	setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        case 2 : 
            setBlockBounds(0.0F, 0.0F, 0.9375F, 1.0F, 1.0F, 1.0F);
            break;
        case 3 : 
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0625F);
            break;
        case 4 : 
            setBlockBounds(0.9375F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            break;
        case 5 : 
            setBlockBounds(0.0F, 0.0F, 0.0F, 0.0625F, 1.0F, 1.0F);
            break;
        }
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }
    
    /**
     * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
    	return new BlockPos(world, x, y, z)
    	.toPos(ForgeDirection.VALID_DIRECTIONS[side].getOpposite())
    	.getBlock().isSideSolid(world, x, y, z, ForgeDirection.VALID_DIRECTIONS[side]);
    }
    
    @Override
    public boolean canBlockStay(World aWorld, int x,
    		int y, int z)
    {
    	int meta = aWorld.getBlockMetadata(x, y, z);
    	if(meta == 0) return aWorld.getBlock(x, y - 1, z).isSideSolid(aWorld, x, y - 1, z, ForgeDirection.UP);
    	BlockPos pos = new BlockPos(aWorld, x, y, z);
    	ForgeDirection dir = direction(meta);
    	return pos.toPos(dir.getOpposite()).getBlock().isSideSolid(aWorld, x, y, z, dir);
    }
    
    private boolean check(Block block)
    {
        return block.renderAsNormalBlock() && block.getMaterial().blocksMovement();
    }

    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        return 0xFFFFFF;//ColorizerFoliage.getFoliageColorBasic();
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta)
    {
        return 0xFFFFFF;//ColorizerFoliage.getFoliageColorBasic();
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess aWorld, int x, int y, int z)
    {
        return 0xFFFFFF;//aWorld.getBiomeGenForCoords(x, z).getBiomeFoliageColor(x, y, z);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World aWorld, int x, int y, int z, Block block)
    {
        if (!aWorld.isRemote && !canBlockStay(aWorld, x, y, z))
        {
            dropBlockAsItem(aWorld, x, y, z, aWorld.getBlockMetadata(x, y, z), 0);
            aWorld.setBlockToAir(x, y, z);
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World aWorld, int x, int y, int z, Random rand)
    {
    	if (!canBlockStay(aWorld, x, y, z))
    	{
    		onNeighborBlockChange(aWorld, x, y, z, this);
    	}
    	else 
    		if (!aWorld.isRemote && rand.nextInt(4) == 0)
        {
        	int i = aWorld.getBlockMetadata(x, y, z);
    		if(!aWorld.getBlock(x, y + 1, z).isAir(aWorld, x, y + 1, z)) return;
        	if(aWorld.getLightBrightness(x, y, z) > 0.875F)
        	{
        		if(i != 0)
        		{
            		int l = 0;
            		while(aWorld.getBlock(x, y - l, z) == this && aWorld.getBlockMetadata(x, y - l, z) != 0) ++l;
            		if(rand.nextInt(l + 2) != 0) return;
            		if(aWorld.getBlockMetadata(x, y - l, z) == 0 && aWorld.getBlock(x, y - l, z) == this)
            		{
            			aWorld.setBlock(x, y + 1, z, this, i, 2);
            		}
            		else
            		{
            			return;
            		}
        		}
        		else if(i == 0)
        		{
            		if(rand.nextBoolean()) return;
        			for(int side = 2; side < 6; ++side)
        			{
        				if(canPlaceBlockOnSide(aWorld, x, y, z, side))
        				{
        					aWorld.setBlock(x, y + 1, z, this, side, 2);
        					return;
        				}
        			}
        		}
        	}
        }
    }

    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    public int onBlockPlaced(World world, int x, int y, int z, int side, float xPos, float yPos, float zPos, int meta)
    {
        return meta == 0 ? 0 : side == 1 ? 2 : side;
    }

    public Item getItemDropped(int item, Random rand, int level)
    {
        return null;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random rand)
    {
        return 0;
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta)
    {
        super.harvestBlock(world, player, x, y, z, meta);
    }

    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z)
    {
        return true;
    }
    
    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        if(world.getBlockMetadata(x, y, z) == 0)
        {
        	ret.add(new ItemStack(this));
        }
        else
        {
        	ret.add(ItemFleSub.a("rattan"));
        }
        return ret;
    }

    @Override
    public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity)
    {
        return world.getBlockMetadata(x, y, z) != 0;
    }

    private ForgeDirection direction(int meta)
    {
    	return ForgeDirection.VALID_DIRECTIONS[meta];
    }
    
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab,
    		List list)
    {
    	list.add(new ItemStack(this, 1, 0));
    	list.add(new ItemStack(this, 1, 1));
    }

    @SideOnly(Side.CLIENT)
    private IIcon rootIcon;
    
    @Override
    public void registerBlockIcons(IIconRegister register)
    {
    	blockIcon = register.registerIcon(FleValue.TEXTURE_FILE + ":plant/vine_rattan");
    	rootIcon = register.registerIcon(FleValue.TEXTURE_FILE + ":plant/bush_rattan");
    }
    
    @Override
    public IIcon getIcon(int side, int meta)
    {
    	return meta == 0 ? rootIcon : blockIcon;
    }
}