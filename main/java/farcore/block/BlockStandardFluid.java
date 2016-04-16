package farcore.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.enums.Direction;
import farcore.enums.EnumBlock.IInfoSpawnable;
import farcore.event.FluidEvent.FlowVerticallyEvent;
import farcore.event.FluidEvent.FluidTouchBlockEvent;
import farcore.interfaces.ISmartFluidBlock;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * 
 * @author ueyudiud
 *
 */
public class BlockStandardFluid extends BlockFluidBase 
implements ISmartFluidBlock, IInfoSpawnable
{
    public BlockStandardFluid(Fluid fluid, Material material)
    {
        super(fluid, material);
        GameRegistry.registerBlock(this, ItemStandardFluid.class, fluid.getName());
    }
    
    @Override
    public String getUnlocalizedName()
    {
    	return getFluid().getUnlocalizedName();
    }
    
    @Override
    public String getLocalizedName()
    {
    	return getFluid().getLocalizedName(new FluidStack(getFluid(), 1000));
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register)
    {
    	getFluid()
    	.setIcons(register.registerIcon(getTextureName() + "_still"),
    			register.registerIcon(getTextureName() + "_flow"));
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
    	return getFluid().getStillIcon();
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
    	return side == 0 || side == 1 ? getFluid().getStillIcon() :
    		getFluid().getFlowingIcon();
    }
    
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list)
    {
    	;
    }
    
    @Override
    public int getQuantaValue(IBlockAccess world, int x, int y, int z)
    {
    	return getFluidLevel(world, x, y, z);
    }

    /**
     * Returns whether this block is collideable based on the arguments passed in 
     * @param par1 block metaData 
     * @param par2 whether the player right-clicked while holding a boat
     */
    @Override
    public boolean canCollideCheck(int meta, boolean fullHit)
    {
        return fullHit;
    }

    @Override
    public int getMaxRenderHeightMeta()
    {
        return quantaPerBlock - 1;
    }
    
    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        boolean changed = false;
        int level1 = getFluidLevel(world, x, y, z);
        int level0 = level1;
        level1 = flowYPos(world, x, y, z, level0);
        if(level1 == 1)
        {
        	level1 = flowYPosNearByWhenMinimumLevel(world, x, y, z);
        }
        if(level1 == 0) return;
        if(level1 == 1)
        {
        	if(level0 != 1)
        	{
        		setFluidLevel(world, x, y, z, 1, true);
        	}
        	return;
        }
        level1 = Math.abs(displaceBlock(world, x - 1, y, z, level1));
        if(level1 == 0) return;
        level1 = Math.abs(displaceBlock(world, x + 1, y, z, level1));
        if(level1 == 0) return;
        level1 = Math.abs(displaceBlock(world, x, y, z - 1, level1));
        if(level1 == 0) return;
        level1 = Math.abs(displaceBlock(world, x, y, z + 1, level1));
        if(level1 == 0) return;
        int xNeg = getFluidLevel(world, x - 1, y, z);
        int xPos = getFluidLevel(world, x + 1, y, z);
        int zNeg = getFluidLevel(world, x, y, z - 1);
        int zPos = getFluidLevel(world, x, y, z + 1);
        if(xNeg > level1) xNeg = -1;
        if(xPos > level1) xPos = -1;
        if(zNeg > level1) zNeg = -1;
        if(zPos > level1) zPos = -1;
        int count = 1;
        int total = level1;

        if(xNeg >= 0)
        {
        	++count;
        	total += xNeg;
        }
        if(xPos >= 0)
        {
        	++count;
        	total += xPos;
        }
        if(zNeg >= 0)
        {
        	++count;
        	total += zNeg;
        }
        if(zPos >= 0)
        {
        	++count;
        	total += zPos;
        }
        if(count == 1)
        {
        	if(level0 != level1)
        	{
        		setFluidLevel(world, x, y, z, level1, true);
        	}
        	return;
        }

        int each = total / count;
        int rem = total % count;
        if (zNeg >= 0)
        {
            int newnorth = each;
            if (rem == count || rem > 1 && rand.nextInt(count - rem) != 0)
            {
                ++newnorth;
                --rem;
            }
            if (newnorth != zNeg)
            {
            	setFluidLevel(world, x, y, z - 1, newnorth, true);
            }
            --count;
        }

        if (zPos >= 0)
        {
            int newsouth = each;
            if (rem == count || rem > 1 && rand.nextInt(count - rem) != 0)
            {
                ++newsouth;
                --rem;
            }
            if (newsouth != zPos)
            {
            	setFluidLevel(world, x, y, z + 1, newsouth, true);
            }
            --count;
        }

        if (xNeg >= 0)
        {
            int newwest = each;
            if (rem == count || rem > 1 && rand.nextInt(count - rem) != 0)
            {
                ++newwest;
                --rem;
            }
            if (newwest != xNeg)
            {
            	setFluidLevel(world, x - 1, y, z, newwest, true);
            }
            --count;
        }

        if (xPos >= 0)
        {
            int neweast = each;
            if (rem == count || rem > 1 && rand.nextInt(count - rem) != 0)
            {
                ++neweast;
                --rem;
            }
            if (neweast != xPos)
            {
                setFluidLevel(world, x + 1, y, z, neweast, true);
            }
            --count;
        }

        if (rem > 0)
        {
            ++each;
        }
        if(each != level0)
        {
        	setFluidLevel(world, x, y, z, each, true);
        }
    }
    
    public int flowYPos(World world, int x, int y, int z, int level)
    {
    	FlowVerticallyEvent event = new FlowVerticallyEvent(world, x, y, z, this, level);
    	MinecraftForge.EVENT_BUS.post(event);
    	switch (event.getResult())
    	{
		case ALLOW : return event.level;
		case DEFAULT :
		{
			int densityWorld = U.Worlds.getAirDensity(world, y);
			if(densityWorld == density)
				return level;
			int otherY = y + (densityWorld > density ? 1 : -1);
	        if (otherY < 0 || otherY >= world.getHeight())
	        {
	            world.setBlock(x, y, z, Blocks.air);
	            return 0;
	        }
	        int amt = getFluidLevel(world, x, otherY, z);
	        if(amt >= 0 && amt < quantaPerBlock)
	        {
	        	int input = Math.min(level, quantaPerBlock - amt);
                setFluidLevel(world, x, otherY, z, amt + input, false);
	            if (level == input)
	            {
	                world.setBlock(x, y, z, Blocks.air);
	                return 0;
	            }
	            return level - input;
	        }
	        else
	        {
	        	int density_other;
	        	if(world.isAirBlock(x, otherY, z))
	        	{
	        		density_other = U.Worlds.getAirDensity(world, y);
	        		
	        	}
	            density_other = getDensity(world, x, otherY, z);
	            if (density_other == Integer.MAX_VALUE)
	            {
	            	int level1 = displaceBlock(world, x, otherY, z, level);
	            	if(level1 > 0)
	            	{
	            		setFluidLevel(world, x, otherY, z, level1, false);
	            		world.setBlockToAir(x, y, z);
	            		return 0;
	            	}
	            	else if(level1 == 0)
	            	{
	            		world.setBlockToAir(x, y, z);
	            		return 0;
	            	}
	            	else
	            	{
						return -level1;
					}
	            }
	            label:
	            {
	            	if(densityDir < 0)
	            	{
	            		if(density_other >= density)
	            			break label;
	            	}
	            	else if(density_other <= density)
	            		break label;
                    BlockFluidBase block = (BlockFluidBase) world.getBlock(x, otherY, z);
                    int otherData = world.getBlockMetadata(x, otherY, z);
                    setFluidLevel(world, x, otherY, z, level, false);
                    world.setBlock(x, y, z, block, otherData, 3);
                    world.scheduleBlockUpdate(x, y, z, block, block.tickRate(world));
                    return 0;
	            }
	            return level;
	        }
		}
		case DENY : return level;
		default : return level;
		}
    }
    
    public int flowYPosNearByWhenMinimumLevel(World world, int x, int y, int z)
    {
    	Direction direction = U.Lang.randomSelect(Direction.directions_2D, world.rand);
    	int oppsetX = x + direction.x;
    	int oppsetY = y + densityDir;
    	int oppsetZ = z + direction.z;
    	int level = displaceBlock(world, oppsetX, y, oppsetZ, 1);
    	if(level < 0)
    	{
    		level = -level;
    	}
    	if(level > 1 || level == 0)
    		return level;
		if(world.isAirBlock(oppsetX, y, oppsetZ))
		{
			int l1 = getFluidLevel(world, oppsetX, oppsetY, oppsetZ);
			if(l1 >= 0 && l1 < quantaPerBlock)
			{
				setFluidLevel(world, oppsetX, oppsetY, oppsetZ, l1 + 1, false);
				world.setBlockToAir(x, y, z);
				return 0;
			}
		}
		return 1;
    }
    
    /**
     * Try to displace block in world.
     * @param level The fluid level.
     * @return A value of fluid remain, characteristics decided whether this
     * block has been removed (Positive means removed).
     */
    public int displaceBlock(World world, int x, int y, int z, int level)
    {
        if (world.getBlock(x, y, z).isAir(world, x, y, z))
        {
            return level;
        }

        Block block = world.getBlock(x, y, z);
        if (block == this)
        {
            return -level;
        }
        
        FluidTouchBlockEvent event = new FluidTouchBlockEvent(world, x, y, z, this, level);
        MinecraftForge.EVENT_BUS.post(event);
        if(event.isBlockDestroyed()) return event.level;
        if(event.level != level)
        {
        	return -event.level;
        }

        if (displacements.containsKey(block))
        {
            if (displacements.get(block))
            {
                block.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
                world.setBlockToAir(x, y, z);
                return level;
            }
            return -level;
        }

        Material material = block.getMaterial();
        if (material.blocksMovement() || material == Material.portal)
        {
            return -level;
        }

        int density = getDensity(world, x, y, z);
        if (density == Integer.MAX_VALUE)
        {
            block.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
            return level;
        }

//        if (this.density > density)
//        {
//            return true;
//        }
//        else
//        {
        return -level;
//        }
    }

    /* IFluidBlock */
    @Override
    public FluidStack drain(World world, int x, int y, int z, boolean doDrain)
    {
        if (doDrain)
        {
            world.setBlock(x, y, z, Blocks.air);
        }
        
        return new FluidStack(getFluid(),
                MathHelper.floor_float(getQuantaPercentage(world, x, y, z) * FluidContainerRegistry.BUCKET_VOLUME));
    }

    @Override
    public boolean canDrain(World world, int x, int y, int z)
    {
        return true;
    }
	
    @Override
	public FluidStack drain(World world, int x, int y, int z, int maxDrain, boolean doDrain)
    {
    	int level = (int) (maxDrain * quantaPerBlockFloat / (float) FluidContainerRegistry.BUCKET_VOLUME);
    	if(level == 0) return null;
    	int level1 = getFluidLevel(world, x, y, z);
    	int amount = MathHelper.floor_double((float) Math.min(level, level1) * FluidContainerRegistry.BUCKET_VOLUME / quantaPerBlockFloat);
        if(doDrain)
        {
        	setFluidLevel(world, x, y, z, level1 - level, true);
        }
        
        return new FluidStack(getFluid(), amount);
	}
    
	@Override
	public int fill(World world, int x, int y, int z, int resource, boolean doFill)
	{
		int level = getFluidLevel(world, x, y, z);
		if(level == -1) return 0;
		int level1 = (int) (resource * quantaPerBlockFloat / (float) FluidContainerRegistry.BUCKET_VOLUME);
		if(level1 == 0) return 0;
		if(doFill)
		{
			setFluidLevel(world, x, y, z, level + level1, true);
		}
		
		return (int) ((float) level1 * FluidContainerRegistry.BUCKET_VOLUME / quantaPerBlockFloat);
	}
	
	protected void setFluidLevel(World world, int x, int y, int z, int amount, boolean flag)
	{
		if(amount <= 0)
		{
			world.setBlockToAir(x, y, z);
		}
		else
		{
			world.setBlock(x, y, z, this, Math.min(quantaPerBlock, amount - 1), flag ? 2 : 3);
			world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
		}
	}
	
	protected int getFluidLevel(IBlockAccess world, int x, int y, int z)
	{
		if(world.isAirBlock(x, y, z))
		{
			return 0;
		}
		else if(world.getBlock(x, y, z) != this)
		{
			return -1;
		}
		else
		{
			return world.getBlockMetadata(x, y, z) + 1;
		}
	}

	/**
	 * Spawn a fluid block, information for fluid amount.
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param objects
	 */
	@Override
	public boolean spawn(World world, int x, int y, int z, Object... objects)
	{
		if(objects.length == 0)
		{
			return world.setBlock(x, y, z, this, quantaPerBlock - 1, 3);
		}
		else if(objects.length == 1 && objects[0] instanceof Number)
		{
			int level = ((Number) objects[0]).intValue();
			setFluidLevel(world, x, y, z, level, true);
			return world.getBlock(x, y, z) == this;
		}
		return false;
	}
}