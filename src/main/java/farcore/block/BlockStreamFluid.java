package farcore.block;

import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

@Deprecated
public class BlockStreamFluid extends BlockFluidBase
{
    protected boolean[] isOptimalFlowDirection = new boolean[4];
    protected int[] flowCost = new int[4];

    /**
     * The flow fluid block.
     */
    protected final Block[] blocks;
    protected final int maxQunata;
    protected final int qunata;
    protected FluidStack stack;
    
	public BlockStreamFluid(Block[] list, Fluid fluid, Material material, int maxQuanta, int quanta)
	{
		super(fluid, material);
		this.blocks = list;
		this.maxQunata = maxQuanta;
		this.qunata = quanta;
		setQuantaPerBlock(16);
        stack = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
	    GameRegistry.registerBlock(this, ItemStreamFluid.class, "stream" + fluid.getName() + "." + quanta);
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

	@Override
	public int getQuantaValue(IBlockAccess world, int x, int y, int z)
	{
        if (world.getBlock(x, y, z) == Blocks.air)
        {
            return 0;
        }

        if (world.getBlock(x, y, z) != this)
        {
            return -1;
        }
        
		return qunata * 16 + (16 - world.getBlockMetadata(x, y, z)) + 1;
	}
	
	public void setQuantaValue(World world, int x, int y, int z, int value)
	{
		if(value == 0)
		{
			world.setBlockToAir(x, y, z);
		}
		else
		{
			if(value > maxQunata * 16) value = maxQunata * 16;
			value -= 1;
			world.setBlock(x, y, z, blocks[value / 16], 16 - (value & 0xF), 3);
		}
	}

	@Override
	public boolean canCollideCheck(int meta, boolean fullHit)
	{
		return fullHit && meta == 0 && qunata == maxQunata;
	}

	@Override
	public FluidStack drain(World world, int x, int y, int z, boolean doDrain)
	{
		if(getQuantaValue(world, x, y, z) == maxQunata * 16)
		{
			if(doDrain)
			{
				setQuantaValue(world, x, y, z, 0);
			}
			return stack;
		}
		return null;
	}

	@Override
	public boolean canDrain(World world, int x, int y, int z)
	{
		return false;
	}

	@Override
	public int getMaxRenderHeightMeta()
	{
		return quantaPerBlock - 1;
	}

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        if (maxScaledLight == 0)
        {
            return super.getLightValue(world, x, y, z);
        }
        int data = getQuantaValue(world, x, y, z);
        return (int) (data / (maxQunata * 16) * maxScaledLight);
    }
    
    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        int quantaRemaining = getQuantaValue(world, x, y, z);
        int expQuanta = -101;

        // check adjacent block levels if non-source
        if (quantaRemaining < quantaPerBlock)
        {
            int y2 = y - densityDir;

            if (world.getBlock(x,     y2, z    ) == this ||
                world.getBlock(x - 1, y2, z    ) == this ||
                world.getBlock(x + 1, y2, z    ) == this ||
                world.getBlock(x,     y2, z - 1) == this ||
                world.getBlock(x,     y2, z + 1) == this)
            {
                expQuanta = quantaPerBlock - 1;
            }
            else
            {
                int maxQuanta = -100;
                maxQuanta = getLargerQuanta(world, x - 1, y, z,     maxQuanta);
                maxQuanta = getLargerQuanta(world, x + 1, y, z,     maxQuanta);
                maxQuanta = getLargerQuanta(world, x,     y, z - 1, maxQuanta);
                maxQuanta = getLargerQuanta(world, x,     y, z + 1, maxQuanta);

                expQuanta = maxQuanta - 1;
            }

            // decay calculation
            if (expQuanta != quantaRemaining)
            {
                quantaRemaining = expQuanta;

                if (expQuanta <= 0)
                {
                    world.setBlock(x, y, z, Blocks.air);
                }
                else
                {
                    world.setBlockMetadataWithNotify(x, y, z, quantaPerBlock - expQuanta, 3);
                    world.scheduleBlockUpdate(x, y, z, this, tickRate);
                    world.notifyBlocksOfNeighborChange(x, y, z, this);
                }
            }
        }
        // This is a "source" block, set meta to zero, and send a server only update
        else if (quantaRemaining >= quantaPerBlock)
        {
            world.setBlockMetadataWithNotify(x, y, z, 0, 2);
        }

        // Flow vertically if possible
        if (canDisplace(world, x, y + densityDir, z))
        {
            flowIntoBlock(world, x, y + densityDir, z, 1);
            return;
        }

        // Flow outward if possible
        int flowMeta = quantaPerBlock - quantaRemaining + 1;
        if (flowMeta >= quantaPerBlock)
        {
            return;
        }

        if (isSourceBlock(world, x, y, z) || !isFlowingVertically(world, x, y, z))
        {
            if (world.getBlock(x, y - densityDir, z) == this)
            {
                flowMeta = 1;
            }
            boolean flowTo[] = getOptimalFlowDirections(world, x, y, z);

            if (flowTo[0]) flowIntoBlock(world, x - 1, y, z,     flowMeta);
            if (flowTo[1]) flowIntoBlock(world, x + 1, y, z,     flowMeta);
            if (flowTo[2]) flowIntoBlock(world, x,     y, z - 1, flowMeta);
            if (flowTo[3]) flowIntoBlock(world, x,     y, z + 1, flowMeta);
        }
    }

    public boolean isFlowingVertically(IBlockAccess world, int x, int y, int z)
    {
        return world.getBlock(x, y + densityDir, z) == this ||
            (world.getBlock(x, y, z) == this && canFlowInto(world, x, y + densityDir, z));
    }

    public boolean isSourceBlock(IBlockAccess world, int x, int y, int z)
    {
        return world.getBlock(x, y, z) == this && world.getBlockMetadata(x, y, z) == 0;
    }

    protected boolean[] getOptimalFlowDirections(World world, int x, int y, int z)
    {
        for (int side = 0; side < 4; side++)
        {
            flowCost[side] = 1000;

            int x2 = x;
            int y2 = y;
            int z2 = z;

            switch (side)
            {
                case 0: --x2; break;
                case 1: ++x2; break;
                case 2: --z2; break;
                case 3: ++z2; break;
            }

            if (!canFlowInto(world, x2, y2, z2) || isSourceBlock(world, x2, y2, z2))
            {
                continue;
            }

            if (canFlowInto(world, x2, y2 + densityDir, z2))
            {
                flowCost[side] = 0;
            }
            else
            {
                flowCost[side] = calculateFlowCost(world, x2, y2, z2, 1, side);
            }
        }

        int min = flowCost[0];
        for (int side = 1; side < 4; side++)
        {
            if (flowCost[side] < min)
            {
                min = flowCost[side];
            }
        }
        for (int side = 0; side < 4; side++)
        {
            isOptimalFlowDirection[side] = flowCost[side] == min;
        }
        return isOptimalFlowDirection;
    }

    protected int calculateFlowCost(World world, int x, int y, int z, int recurseDepth, int side)
    {
        int cost = 1000;
        for (int adjSide = 0; adjSide < 4; adjSide++)
        {
            if ((adjSide == 0 && side == 1) ||
                (adjSide == 1 && side == 0) ||
                (adjSide == 2 && side == 3) ||
                (adjSide == 3 && side == 2))
            {
                continue;
            }

            int x2 = x;
            int y2 = y;
            int z2 = z;

            switch (adjSide)
            {
                case 0: --x2; break;
                case 1: ++x2; break;
                case 2: --z2; break;
                case 3: ++z2; break;
            }

            if (!canFlowInto(world, x2, y2, z2) || isSourceBlock(world, x2, y2, z2))
            {
                continue;
            }

            if (canFlowInto(world, x2, y2 + densityDir, z2))
            {
                return recurseDepth;
            }

            if (recurseDepth >= 4)
            {
                continue;
            }

            int min = calculateFlowCost(world, x2, y2, z2, recurseDepth + 1, adjSide);
            if (min < cost)
            {
                cost = min;
            }
        }
        return cost;
    }

    protected void flowIntoBlock(World world, int x, int y, int z, int meta)
    {
        if (meta < 0) return;
        if (displaceIfPossible(world, x, y, z))
        {
            world.setBlock(x, y, z, this, meta, 3);
        }
    }

    protected boolean canFlowInto(IBlockAccess world, int x, int y, int z)
    {
        if (world.getBlock(x, y, z).isAir(world, x, y, z)) return true;

        Block block = world.getBlock(x, y, z);
        if (block == this)
        {
            return true;
        }

        if (displacements.containsKey(block))
        {
            return displacements.get(block);
        }

        Material material = block.getMaterial();
        if (material.blocksMovement()  ||
            material == Material.water ||
            material == Material.lava  ||
            material == Material.portal)
        {
            return false;
        }

        int density = getDensity(world, x, y, z);
        if (density == Integer.MAX_VALUE) 
        {
             return true;
        }
        
        if (this.density > density)
        {
            return true;
        }
        else
        {
        	return false;
        }
    }

    protected int getLargerQuanta(IBlockAccess world, int x, int y, int z, int compare)
    {
        int quantaRemaining = getQuantaValue(world, x, y, z);
        if (quantaRemaining <= 0)
        {
            return compare;
        }
        return quantaRemaining >= compare ? quantaRemaining : compare;
    }
}