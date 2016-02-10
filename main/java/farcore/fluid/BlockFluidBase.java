package farcore.fluid;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import farcore.block.BlockFactory;
import fle.core.init.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class BlockFluidBase extends BlockFluidFinite implements ISmartFluidBlock
{
	public BlockFluidBase(FluidBase fluid, Material material)
	{
		super(fluid, material);
		setBlockName(fluid.getUnlocalized());
		GameRegistry.registerBlock(this, ItemFluid.class, "fluid." + fluidName);
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(this, 1, quantaPerBlock - 1));
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		((FluidBase) getFluid()).registerFluidIcons(register);
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		return ((FluidBase) getFluid()).getIcon();
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return side == 0 || side == 1 ? ((FluidBase) getFluid()).getIcon() : ((FluidBase) getFluid()).getFlowingIcon();
	}
	
	@Override
	public int getRenderColor(int meta)
	{
		return ((FluidBase) getFluid()).getColor();
	}
	
	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		return ((FluidBase) getFluid()).getColor(world, x, y, z);
	}
	
	public void setQuantaValue(World world, int x, int y, int z, int value)
	{
		if(value == 0) world.setBlockToAir(x, y, z);
		else world.setBlock(x, y, z, this, value - 1, 2);
	}
	
	/**
	 * Use better update method.
	 * This method add this function.
	 */	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		if(Config.useForgeFluidDetermine)
		{
			super.updateTick(world, x, y, z, rand);
			return;
		}
		boolean changed = false;
		int amount = getQuantaValue(world, x, y, z),
				preAmount = amount;
		amount = tryToFlowVerticallyInto(world, x, y, z, amount);
		amount = displaceBlock(world, x - 1, y, z, amount, 4);
		amount = displaceBlock(world, x + 1, y, z, amount, 3);
		amount = displaceBlock(world, x, y, z - 1, amount, 2);
		amount = displaceBlock(world, x, y, z + 1, amount, 1);
		if(amount == 0)
		{
			world.setBlockToAir(x, y, z);
			return;
		}
		else if(preAmount != amount)
		{
			changed = true;
			if(amount == 1)
			{
				world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
				if((amount = tryToFlowNearby(world, x, y, z, amount)) != preAmount)
				{
					setQuantaValue(world, x, y, z, amount);
					world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
				}
				return;
			}
		}
		else if(amount == 1)
		{
			if((amount = tryToFlowNearby(world, x, y, z, amount)) != preAmount)
			{
				setQuantaValue(world, x, y, z, amount);
				world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
			}
			return;
		}
        int north = getQuantaValue(world, x, y, z - 1);
        int south = getQuantaValue(world, x, y, z + 1);
        int west  = getQuantaValue(world, x - 1, y, z);
        int east  = getQuantaValue(world, x + 1, y, z);
        int count = 1;
        int total = amount;

        if(north >= 0)
        {
        	count++;
        	total += north;
        }
        if(south >= 0)
        {
        	count++;
        	total += south;
        }
        if(west >= 0)
        {
        	count++;
        	total += west;
        }
        if(east >= 0)
        {
        	count++;
        	total += east;
        }
        
        if(total == 1)
        {
        	if(changed)
        	{
        		setQuantaValue(world, x, y, z, amount);
        	}
        	return;
        }
        
        int each = total / count;
        int rem = total % count;
        
        if (north >= 0)
        {
            int newnorth = each;
            if (rem == count || rem > 1 && rand.nextInt(count - rem) != 0)
            {
                ++newnorth;
                --rem;
            }

            if (newnorth != north)
            {
            	setQuantaValue(world, x, y, z - 1, newnorth);
                world.scheduleBlockUpdate(x, y, z - 1, this, tickRate);
            }
            --count;
        }

        if (south >= 0)
        {
            int newsouth = each;
            if (rem == count || rem > 1 && rand.nextInt(count - rem) != 0)
            {
                ++newsouth;
                --rem;
            }

            if (newsouth != south)
            {
            	setQuantaValue(world, x, y, z + 1, newsouth);
                world.scheduleBlockUpdate(x, y, z + 1, this, tickRate);
            }
            --count;
        }

        if (west >= 0)
        {
            int newwest = each;
            if (rem == count || rem > 1 && rand.nextInt(count - rem) != 0)
            {
                ++newwest;
                --rem;
            }
            if (newwest != west)
            {
            	setQuantaValue(world, x - 1, y, z, newwest);
                world.scheduleBlockUpdate(x - 1, y, z, this, tickRate);
            }
            --count;
        }

        if (east >= 0)
        {
            int neweast = each;
            if (rem == count || rem > 1 && rand.nextInt(count - rem) != 0)
            {
                ++neweast;
                --rem;
            }

            if (neweast != east)
            {
            	setQuantaValue(world, x + 1, y, z, neweast);
                world.scheduleBlockUpdate(x + 1, y, z, this, tickRate);
            }
            --count;
        }

        if (rem > 0)
        {
            ++each;
        }
        setQuantaValue(world, x, y, z, each);
	}
	
	public int tryToFlowNearby(World world, int x, int y, int z, int amtToInput)
	{
		if(amtToInput == 0) return 0;
		int offset = y + densityDir;
		int a, b, c, d;
		a = getQuantaValue(world, x - 1, offset, z);
		b = getQuantaValue(world, x + 1, offset, z);
		c = getQuantaValue(world, x, offset, z - 1);
		d = getQuantaValue(world, x, offset, z + 1);
		int count = 0;
		if(a != -1 && a < quantaPerBlock) ++count;
		if(b != -1 && b < quantaPerBlock) ++count;
		if(c != -1 && c < quantaPerBlock) ++count;
		if(d != -1 && d < quantaPerBlock) ++count;
		if(count == 0) return amtToInput;
		if(a != -1 && a < quantaPerBlock && (count == 1 || world.rand.nextInt(count--) == 0))
		{
			setQuantaValue(world, x - 1, offset, z, a + 1);
			return amtToInput - 1;
		}
		if(b != -1 && b < quantaPerBlock && (count == 1 || world.rand.nextInt(count--) == 0))
		{
			setQuantaValue(world, x + 1, offset, z, b + 1);
			return amtToInput - 1;
		}
		if(c != -1 && c < quantaPerBlock && (count == 1 || world.rand.nextInt(count--) == 0))
		{
			setQuantaValue(world, x, offset, z - 1, c + 1);
			return amtToInput - 1;
		}
		if(d != -1 && d < quantaPerBlock && (count == 1 || world.rand.nextInt(count--) == 0))
		{
			setQuantaValue(world, x, offset, z + 1, d + 1);
			return amtToInput - 1;
		}
		return amtToInput;
	}

	@Override
	public int tryToFlowVerticallyInto(World world, int x, int y, int z, int amtToInput)
	{
		if(Config.useForgeFluidDetermine)
		{
			return super.tryToFlowVerticallyInto(world, x, y, z, amtToInput);
		}
		int offset = y + densityDir;
		if(offset >= world.getHeight() || offset < 0)
		{
			world.setBlockToAir(x, y, z);
			return 0;
		}
		
		int oV = getQuantaValue(world, x, offset, z);
		if(oV == -1)
		{
			int density1 = getDensity(world, x, offset, z);
			int lastAmt;
			if(density1 == Integer.MAX_VALUE)
			{
				lastAmt = BlockFactory.onFluidTouchBlock(world, x, offset, z, this, amtToInput);
				if(lastAmt != Integer.MAX_VALUE)
				{
					world.scheduleBlockUpdate(x, offset, z, world.getBlock(x, offset, z), world.getBlock(x, offset, z).tickRate(world));
					return lastAmt;
				}
				return amtToInput;
			}
			if((lastAmt = BlockFactory.onFluidTouchFluid(world, x, offset, z, this, amtToInput)) 
					!= Integer.MAX_VALUE)
			{
				world.scheduleBlockUpdate(x, offset, z, world.getBlock(x, offset, z), world.getBlock(x, offset, z).tickRate(world));
				return lastAmt;
			}
			if (densityDir < 0)
	        {
	            if (density1 < density) // then swap
	            {
	                BlockFluidBase block = (BlockFluidBase) world.getBlock(x, offset, z);
	                int otherData = world.getBlockMetadata(x, offset, z);
	                world.setBlock(x, offset, z, this,  amtToInput - 1, 3);
	                world.setBlock(x, y,      z, block, otherData, 3);
	                world.scheduleBlockUpdate(x, offset, z, this,  tickRate(world));
	                world.scheduleBlockUpdate(x, y,      z, block, block.tickRate(world));
	                return 0;
	            }
	        }
	        else
	        {
	            if (density1 > density)
	            {
	                BlockFluidBase block = (BlockFluidBase)world.getBlock(x, offset, z);
	                int otherData = world.getBlockMetadata(x, offset, z);
	                world.setBlock(x, offset, z, this,  amtToInput - 1, 3);
	                world.setBlock(x, y,      z, block, otherData, 3);
	                world.scheduleBlockUpdate(x, offset, z, this,  tickRate);
	                world.scheduleBlockUpdate(x, y,      z, block, block.tickRate(world));
	                return 0;
	            }
	        }
			return amtToInput;
		}
		else
		{
			int amt = getQuantaValue(world, x, offset, z);
            amt += amtToInput;
            if (amt > quantaPerBlock)
            {
                world.setBlock(x, offset, z, this, quantaPerBlock - 1, 3);
                world.scheduleBlockUpdate(x, offset, z, this, tickRate);
                return amt - quantaPerBlock;
            }
            else if (amt > 0)
            {
                world.setBlock(x, offset, z, this, amt - 1, 3);
                world.scheduleBlockUpdate(x, offset, z, this, tickRate);
                world.setBlock(x, y, z, Blocks.air);
                return 0;
            }
            return amtToInput;
		}
	}
	
	public int displaceBlock(World world, int x, int y, int z, int amount, int side)
	{
		if(amount == 0) return 0;
		int d = amount / side;
		int a = amount - d;
		if(d == 0)
		{
			++d;
			--a;
		}
		if(world.isAirBlock(x, y, z)) return amount;
		else
		{
			int density1 = getDensity(world, x, y, z);
			if(density1 == Integer.MAX_VALUE)
			{
				int remain = BlockFactory.onFluidTouchBlock(world, x, y, z, this, d);
				if(remain != Integer.MAX_VALUE)
				{
					return remain + a;
				}
				return amount;
			}
			else
			{
				int remain = BlockFactory.onFluidTouchFluid(world, x, y, z, this, d);
				if(remain != Integer.MAX_VALUE)
				{
					return remain + a;
				}
				return amount;
			}
		}
	}
	
	@Override
	public FluidStack drain(World world, int x, int y, int z, int maxDrain, boolean doDrain)
	{
		if(world.getBlock(x, y, z) != this)
			return null;
		int value = getQuantaValue(world, x, y, z);
		int level = Math.min(value, (int) (maxDrain / ((float) FluidContainerRegistry.BUCKET_VOLUME / quantaPerBlockFloat)));
        if (doDrain)
        {
        	if(value == level)
        	{
        		world.setBlock(x, y, z, Blocks.air);
        	}
        	else
        	{
                world.setBlockMetadataWithNotify(x, y, z, value - level - 1, 2);
        	}
        }
        
        return new FluidStack(getFluid(),
                MathHelper.floor_float(level * FluidContainerRegistry.BUCKET_VOLUME / quantaPerBlock));
	}

	@Override
	public int fill(World world, int x, int y, int z, int maxFill, boolean doFill)
	{
		int value = (int) Math.floor(maxFill / ((float) FluidContainerRegistry.BUCKET_VOLUME / quantaPerBlockFloat));
		if(world.getBlock(x, y, z) != this)
		{
			if(!world.getBlock(x, y, z).isAir(world, x, y, z))
				return 0;
			if(value > quantaPerBlock)
				value = quantaPerBlock;
			if(doFill)
				world.setBlockMetadataWithNotify(x, y, z, value - 1, 2);
		}
		else
		{
			int quanta = getQuantaValue(world, maxFill, y, z);
			value = Math.min(value, quantaPerBlock - quanta);
			if(doFill)
				world.setBlockMetadataWithNotify(x, y, z, quanta + value - 1, 2);
		}
		return (int) (value * ((float) FluidContainerRegistry.BUCKET_VOLUME / quantaPerBlock));
	}
}