package farcore.block.fluid;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import flapi.util.DamageResources;
import flapi.util.FleValue;

public class BlockFluidBase extends BlockFluidFinite implements IFleFluidBlock
{
	private static Map<String, Material> materials = new HashMap();

	public static final Material water = material("water");
	public static final Material lava = material("lava");
	public static final Material oil = material("oil", MapColor.blackColor);
	public static final Material light = material("light", MapColor.yellowColor);
	
	private static final IFluidHandler defaultHandler = new IFluidHandler()
	{
		@Override
		public boolean onFluidCollide(World world, BlockPos collidePos,
				Fluid fluid, Block fluidBlock, Block collideBlock)
		{
			if(defaultDisplacements.get(collideBlock) == true)
			{
				collideBlock.dropBlockAsItem(world, collidePos, world.getBlockState(collidePos), 0);
				return true;
			}
			return false;
		}
	};
	private static final Function<Block, IFluidHandler> FUNCTION1 = new Function<Block, IFluidHandler>()
	{		
		@Override
		public IFluidHandler apply(Block input)
		{
			return defaultHandler;
		}		
	};
	
	public static Material material(String name, MapColor color)
	{
		Material ret = material(name);
		if(ret != null) return ret;
		ret = new MaterialFluid(color);
		materials.put(name, ret);
		return ret;
	}
	public static Material material(String name)
	{
		if("water".equals(name)) return Material.water;
		if("lava".equals(name)) return Material.lava;
		return materials.get(name);
	}
	
	private static class MaterialFluid extends MaterialLiquid
	{
		public MaterialFluid(MapColor color)
		{
			super(color);
		}
	}

	protected Map<Block, IFluidHandler> map = new HashMap();
	
	public void addReaction(Block block, IFluidHandler handler)
	{
		map.put(block, handler);
	}
	
	public void addDefaultReaction()
	{
		map.putAll(Maps.asMap(defaultDisplacements.keySet(), FUNCTION1));
	}
	
	/**
	 * The fluid base only support fluid which implements FluidBase.
	 */
	protected FluidBase target;
	
	public BlockFluidBase(final FluidBase fluid, Material material)
	{
		super(fluid, material);
		this.target = fluid;
		setUnlocalizedName(fluid.getUnlocalizedName());
		GameRegistry.registerBlock(this, target.getName().toLowerCase().replace(' ', '.').trim());
		ModelLoader.addVariantName(Item.getItemFromBlock(this), FleValue.FLUID_RENDER_CONTROL + ":" + fluid.getName());
	}

	public BlockFluidBase(FluidBase fluid, String material)
	{
		this(fluid, material(material));
	}
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos,
			EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
			EntityLivingBase placer)
	{
		return getDefaultState().withProperty(LEVEL, quantaPerBlock - 1);
	}

	@Override
	public boolean canCollideCheck(IBlockState state, boolean fullHit)
	{
		return fullHit;
	}
	
	/**
	 * Attempt to displace the block at (pos), return true if it was displaced.
	 */
	@Override
	public boolean displaceIfPossible(World world, BlockPos pos)
	{
		if (world.isAirBlock(pos))
		{
			return true;
		}

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (block == this)
		{
			return false;
		}

		if (map.containsKey(block))
		{
			if (map.get(block).onFluidCollide(world, pos, getFluid(), this, block))
			{
				return true;
			}
			return false;
		}

		Material material = block.getMaterial();
		if (material.blocksMovement() || material == Material.portal)
		{
			return false;
		}

		int density = getDensity(world, pos);
		if (density == Integer.MAX_VALUE)
		{
			block.dropBlockAsItem(world, pos, state, 0);
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

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		boolean changed = false;
		int quantaRemaining = getQuantaValue(world, pos);
		
		// Flow vertically if possible
		int prevRemaining = quantaRemaining;
		quantaRemaining = tryToFlowVerticallyInto(world, pos, quantaRemaining);
		
		if (quantaRemaining < 1)
		{
			return;
		}
		else if(quantaRemaining == 1)//Let the block which quanta is 1 move to lower area.
		{
			for(EnumFacing side : EnumFacing.Plane.HORIZONTAL)
			{
				BlockPos off = pos.offset(side);
				if(world.isAirBlock(off))
				{
					if(world.setBlockState(off, getDefaultState().withProperty(LEVEL, 0), 3) &&
							tryToFlowVerticallyInto(world, off, 1) == 0)
					{
						world.setBlockToAir(pos);
						break;
					}
					else
					{
						world.setBlockToAir(off);
					}
				}
			}
			return;
		}
		else if (quantaRemaining != prevRemaining)
		{
			changed = true;
			if (quantaRemaining == 1)
			{
				world.setBlockState(pos, state.withProperty(LEVEL, quantaRemaining - 1), 2);
				return;
			}
		}
		
		// Flow out if possible
		int lowerthan = quantaRemaining - 1;
        //Get total fluid amount.
        int total = quantaRemaining;
        //Count the side to move. (One for this facing.
        int count = 1;

        //Check total fluid amount and side access.
        for (EnumFacing side : EnumFacing.Plane.HORIZONTAL)
        {
            BlockPos off = pos.offset(side);
            if (displaceIfPossible(world, off))
            {
            	world.setBlockToAir(off);
            }

            int quanta = getQuantaValueBelow(world, off, lowerthan);
            if (quanta >= 0)
            {
                count++;
                total += quanta;
            }
        }

        if (count == 1)
        {
            if (changed)
            {
                world.setBlockState(pos, state.withProperty(LEVEL, quantaRemaining - 1), 2);
            }
            return;
        }

        int each = total / count; //Get each amount.
        int rem = total % count; //Get more amount.

        for (EnumFacing side : EnumFacing.Plane.HORIZONTAL)
        {
            BlockPos off = pos.offset(side);
            int quanta = getQuantaValueBelow(world, off, lowerthan);
            if (quanta >= 0)
            {
                int newquanta = each;
                if (rem == count || rem > 1 && rand.nextInt(count - rem) != 0)
                {
                    ++newquanta;
                    --rem;
                }

                if (newquanta != quanta)
                {
                	setQuantaValue(world, off, newquanta);
                    world.scheduleUpdate(off, this, tickRate);
                }
                --count;
            }
        }

        if (rem > 0)
        {
            each += rem;
        }
        setQuantaValue(world, pos, state, each);
    }

    public void setQuantaValue(World world, BlockPos pos, int value)
    {
    	setQuantaValue(world, pos, getDefaultState(), value);
    }
    public void setQuantaValue(World world, BlockPos pos, IBlockState state, int value)
    {
    	if(value == 0)
    	{
    		world.setBlockToAir(pos);
    	}
    	else
    	{
    		world.setBlockState(pos, state.withProperty(LEVEL, value - 1), 2);
    	}
    }
	
    @Override
	public FluidStack drain(World world, BlockPos pos, int maxDrain,
			boolean doDrain)
	{
    	float perLevel = (float) FluidContainerRegistry.BUCKET_VOLUME / quantaPerBlockFloat;
    	int levelNeed = MathHelper.ceiling_double_int((float) maxDrain / perLevel);
    	int level = getQuantaValue(world, pos);
    	if(level <= levelNeed)
    	{
            if (doDrain)
            {
                world.setBlockToAir(pos);
            }
    		return new FluidStack(getFluid(), (int) (level * perLevel));
    	}
    	else
    	{
    		if (doDrain)
    		{
    			world.setBlockState(pos, getDefaultState().withProperty(LEVEL, level - levelNeed - 1));
    		}
    		return new FluidStack(getFluid(), (int) (levelNeed * perLevel));
    	}
    }
    
	@Override
	public int fill(World world, BlockPos pos, int maxFill, boolean doFill)
	{
    	float perLevel = (float) FluidContainerRegistry.BUCKET_VOLUME / quantaPerBlockFloat;
    	int levelNeed = quantaPerBlock - getQuantaValue(world, pos);
    	int level = MathHelper.floor_double((float) maxFill / perLevel);
    	if(level <= levelNeed)
    	{
            if (doFill)
            {
                world.setBlockState(pos, getDefaultState().withProperty(LEVEL, quantaPerBlock - levelNeed + level - 1));
            }
    		return (int) (level * perLevel);
    	}
    	else
    	{
    		if (doFill)
    		{
    			world.setBlockState(pos, getDefaultState().withProperty(LEVEL, quantaPerBlock - 1));
    		}
    		return (int) (levelNeed * perLevel);
    	}
	}
	
	@Override
	public Vec3 modifyAcceleration(World world, BlockPos pos, Entity entity,
			Vec3 vec)
	{
        if (densityDir > 0) return vec;
        Vec3 vec_flow = this.getFlowVector(world, pos);
        double xEff = vec_flow.xCoord * (quantaPerBlock * 4);
        double yEff = vec_flow.yCoord * (quantaPerBlock * 4);
        double zEff = vec_flow.zCoord * (quantaPerBlock * 4);
        return new Vec3(
                (((vec.xCoord - xEff) * 1000) / (double) (1 + target.getViscosity())) + xEff,
                (((vec.yCoord - yEff) * 1000) / (double) (1 + target.getViscosity())) + yEff,
                (((vec.zCoord - zEff) * 1000) / (double) (1 + target.getViscosity())) + zEff);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, BlockPos pos,
			EnumFacing side)
	{
		return false;
	}
	
	@Override
	public boolean isReplaceable(World worldIn, BlockPos pos)
	{		
		return false;
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos,
			Entity exploder, Explosion explosion)
	{
		return (float) target.getDensity() / 400F;
	}
	
	/**
	 * Provide the heat damage to entity.
	 */
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos,
			IBlockState state, Entity entityIn)
	{
		if(target.getSubstance() != null && target.getSubstance().heatDamage >= 0)
		{
			entityIn.attackEntityFrom(DamageResources.getFluidHeatDamage(this), target.getSubstance().heatDamage);
		}
	}
}