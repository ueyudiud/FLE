package farcore.block.plant.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.BlockBase;
import farcore.enums.Direction;
import farcore.lib.substance.SubstanceWood;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

/**
 * 
 * Meta control :
 * 1 for check decay.
 * 2 for marked decayed leaves.
 * @author ueyudiud
 *
 */
public class BlockLeaves extends BlockBase implements IShearable
{
	public Block log;
	@SideOnly(Side.CLIENT)
	private IIcon advIcon;
	@SideOnly(Side.CLIENT)
	private IIcon simpleIcon;
	public SubstanceWood wood;
	
	public BlockLeaves(SubstanceWood wood)
	{
		super("leaves." + wood.getName(), Material.leaves);
		setBlockTextureName("fle:leaf/" + wood.getName());
		setTickRandomly(true);
		setHardness(0.5F);
		this.wood = wood;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		simpleIcon = register.registerIcon(getTextureName() + "_opaque");
		advIcon = register.registerIcon(getTextureName());
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return U.Client.shouldRenderBetterLeaves() ? advIcon : simpleIcon;
	}

    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        double d0 = 0.5D;
        double d1 = 1.0D;
        return ColorizerFoliage.getFoliageColor(d0, d1);
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta)
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        int l = 0;
        int i1 = 0;
        int j1 = 0;

        for (int k1 = -1; k1 <= 1; ++k1)
        {
            for (int l1 = -1; l1 <= 1; ++l1)
            {
                int i2 = world.getBiomeGenForCoords(x + l1, z + k1).getBiomeFoliageColor(x + l1, y, z + k1);
                l += (i2 & 16711680) >> 16;
                i1 += (i2 & 65280) >> 8;
                j1 += i2 & 255;
            }
        }

        return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
    	return null;
    }
    
    @Override
    public boolean isOpaqueCube()
    {
    	return false;
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z,
    		Block block)
    {
    	world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
    	if(world.isRemote) return;
    	int meta = world.getBlockMetadata(x, y, z);
    	if((meta & 0x1) == 0)
    	{
    		label:
    		{
    			for(int i = -3; i <= 3; ++i)
    				for(int j = -3; j <= 3; ++j)
    					for(int k = -3; k <= 3; ++k)
    					{
    						if(isLog(world, x + i, y + j, z + k))
    						{
    							break label;
    						}
    					}
    			world.setBlockToAir(x, y, z);
    			return;
    		}
    		boolean flag = false;
    		boolean flag2 = false;
    		for(Direction direction : Direction.directions)
    		{
    			if(isLog(world, x + direction.x, y + direction.y, z + direction.z))
    			{
    				flag2 = flag = true;
    			}
    			else if(isLeavesLife(world, x + direction.x, y + direction.y, z + direction.z))
    			{
    				flag = true;
    				world.scheduleBlockUpdate(x + direction.x, y + direction.y, z + direction.z, this, tickRate(world) + world.rand.nextInt(10));
    			}
    		}
    		if(!flag)
    		{
    			for(Direction direction : Direction.directions)
    			{
    				beginLeavesDecay(world, x + direction.x, y + direction.y, z + direction.z);
    			}
    			world.setBlockToAir(x, y, z);
    		}
    		else if(flag2)
    		{
    			world.setBlockMetadataWithNotify(x, y, z, meta & 0xD, 4);
    		}
    	}
    }
    
    public boolean isLog(World world, int x, int y, int z)
    {
    	return world.getBlock(x, y, z) == log;
    }
    
    public boolean isLeavesLife(World world, int x, int y, int z)
    {
    	return world.getBlock(x, y, z) == this &&
    			(world.getBlockMetadata(x, y, z) & 0x2) == 0;
    }
    
    @Override
    public void beginLeavesDecay(World world, int x, int y, int z)
    {
    	int meta = world.getBlockMetadata(x, y, z);
    	if((meta & 0x1) == 0)
    	{
    		world.setBlockMetadataWithNotify(x, y, z, meta | 0x2, 2);
    		world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
    	}
    }
    
    @Override
    public int tickRate(World world)
    {
    	return 12;
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        if (U.Worlds.isCatchingRain(world, x, y, z) && !World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && random.nextInt(15) == 1)
        {
            double d0 = (double)((float)x + random.nextFloat());
            double d1 = (double)y - 0.05D;
            double d2 = (double)((float)z + random.nextFloat());
            world.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune,
    		boolean silkTouching)
    {
    	return wood.leafDrop.randomDropsWithCast(world.rand);
    }

	@Override
	public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune)
	{
		return new ArrayList(Arrays.asList(new ItemStack(this, 1, world.getBlockMetadata(x, y, z))));
	}
}