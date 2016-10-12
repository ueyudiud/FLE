package farcore.lib.block;

import java.util.Random;

import farcore.data.CT;
import farcore.event.FluidBlockEvent.FluidTouchBlockEvent;
import farcore.lib.fluid.FluidBase;
import farcore.lib.util.Direction;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class BlockStandardFluid extends BlockFluidBase implements ISmartFluidBlock
{
	public final FluidBase fluid;
	private final FluidStack blockValue;

	public BlockStandardFluid(FluidBase fluid, Material material)
	{
		super(fluid, material);
		setCreativeTab(CT.tabFluids);
		setDefaultState(getDefaultState().withProperty(LEVEL, 15));
		U.Mod.registerBlock(this, "fluid." + fluid.getName());
		blockValue = new FluidStack(fluid, 1000);
		this.fluid = fluid;
		setQuantaPerBlock(16);
	}

	@Override
	public String getUnlocalizedName()
	{
		return fluid.getUnlocalizedName();
	}

	@Override
	public String getLocalizedName()
	{
		return fluid.getLocalizedName(blockValue);
	}

	public int getFluidLevel(IBlockAccess world, BlockPos pos)
	{
		if(world.isAirBlock(pos))
			return 0;
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() != this)
			return -1;
		else
			return state.getValue(LEVEL) + 1;
	}

	public void setFluidLevel(World world, BlockPos pos, int level, boolean update)
	{
		if(level == 0)
		{
			world.setBlockToAir(pos);
		}
		else
		{
			world.setBlockState(pos, getDefaultState().withProperty(LEVEL, level - 1), update ? 3 : 2);
		}
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock)
	{
		world.scheduleUpdate(pos, this, tickRate(world));
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		boolean changed = false;
		int level = state.getValue(LEVEL) + 1;
		int prelevel = level;
		level = tryToFlowVerticallyInto(worldIn, pos, level);
		if (level == 1)
		{
			flowYPosNearByWhenMinimumLevel(worldIn, pos);
		}
		if (level == 0)
			return;
		if (level == 1)
		{
			if (prelevel != 1)
			{
				worldIn.setBlockState(pos, state.withProperty(LEVEL, level - 1), 2);
			}
			return;
		}
		boolean[] repleaced = new boolean[4];
		for(EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			level = displaceIfPossible(worldIn, pos.offset(facing), pos, level);
			if(level == 0) return;
			repleaced[facing.ordinal() - 2] = level >= 0;
			if(level < 0)
			{
				level = -level;
			}
		}
		if(!(repleaced[0] || repleaced[1] || repleaced[2] || repleaced[3]))
		{
			if(level != prelevel)
			{
				setFluidLevel(worldIn, pos, level, true);
			}
			return;
		}
		int xNeg = getFluidLevel(worldIn, pos.add(-1, 0, 0));
		int xPos = getFluidLevel(worldIn, pos.add(+1, 0, 0));
		int zNeg = getFluidLevel(worldIn, pos.add(0, 0, -1));
		int zPos = getFluidLevel(worldIn, pos.add(0, 0, +1));
		if(xNeg > level)
		{
			xNeg = -1;
		}
		if(xPos > level)
		{
			xPos = -1;
		}
		if(zNeg > level)
		{
			zNeg = -1;
		}
		if(zPos > level)
		{
			zPos = -1;
		}
		
		int count = 1;
		int total = level;
		
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
			if(level != prelevel)
			{
				setFluidLevel(worldIn, pos, level, true);
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
				setFluidLevel(worldIn, pos.add(0, 0, -1), newnorth, true);
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
				setFluidLevel(worldIn, pos.add(0, 0, 1), newsouth, true);
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
				setFluidLevel(worldIn, pos.add(-1, 0, 0), newwest, true);
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
				setFluidLevel(worldIn, pos.add(1, 0, 0), neweast, true);
			}
			--count;
		}
		
		if (rem > 0)
		{
			++each;
		}
		if(each != prelevel)
		{
			setFluidLevel(worldIn, pos, each, true);
		}
	}
	
	public int flowYPosNearByWhenMinimumLevel(World world, BlockPos pos)
	{
		Direction direction = U.L.random(Direction.directions_2D, world.rand);
		BlockPos pos1 = pos.add(direction.x, 0, direction.z);
		BlockPos pos2 = pos1.add(0, densityDir, 0);
		int level = displaceIfPossible(world, pos1, pos, 1);
		if(level < 0)
		{
			level = -level;
		}
		else
		{
			if(level > 1 || level == 0)
				return level;
			if(world.isAirBlock(pos1))
			{
				int l1 = getFluidLevel(world, pos2);
				if(l1 >= 0 && l1 < quantaPerBlock)
				{
					setFluidLevel(world, pos2, l1 + 1, false);
					world.scheduleUpdate(pos2, this, tickRate);
					world.setBlockToAir(pos);
					return 0;
				}
			}
		}
		return level;
	}

	public int tryToFlowVerticallyInto(World world, BlockPos pos, int amtToInput)
	{
		IBlockState myState = world.getBlockState(pos);
		BlockPos other = pos.add(0, densityDir, 0);
		if (other.getY() < 0 || other.getY() >= world.getHeight())
		{
			world.setBlockToAir(pos);
			return 0;
		}
		IBlockState state = world.getBlockState(other);
		FluidTouchBlockEvent event = new FluidTouchBlockEvent(world, pos, other, state, this, amtToInput);
		MinecraftForge.EVENT_BUS.post(event);
		if(event.getResult() == Result.ALLOW)
		{
			if(state != event.getEndingTargetState())
			{
				world.setBlockState(pos, event.getEndingTargetState(), 3);
			}
			return event.amount;
		}
		amtToInput = event.amount;
		
		int amt = getQuantaValueBelow(world, other, quantaPerBlock);
		if (amt >= 0)
		{
			amt += amtToInput;
			if (amt > quantaPerBlock)
			{
				world.setBlockState(other, myState.withProperty(LEVEL, quantaPerBlock - 1), 3);
				world.scheduleUpdate(other, this, tickRate);
				return amt - quantaPerBlock;
			}
			else if (amt > 0)
			{
				world.setBlockState(other, myState.withProperty(LEVEL, amt - 1), 3);
				world.scheduleUpdate(other, this, tickRate);
				world.setBlockToAir(pos);
				return 0;
			}
			return amtToInput;
		}
		else
		{
			int density_other = getDensity(world, other);
			if (density_other == Integer.MAX_VALUE)
			{
				if (displaceIfPossible(world, other))
				{
					world.setBlockState(other, myState.withProperty(LEVEL, amtToInput - 1), 3);
					world.scheduleUpdate(other, this, tickRate);
					world.setBlockToAir(pos);
					return 0;
				}
				else
					return amtToInput;
			}
			
			if (densityDir < 0)
			{
				if (density_other < density) // then swap
				{
					world.setBlockState(other, myState.withProperty(LEVEL, amtToInput - 1), 3);
					world.setBlockState(pos,   state, 3);
					world.scheduleUpdate(other, this, tickRate);
					world.scheduleUpdate(pos,   state.getBlock(), state.getBlock().tickRate(world));
					return 0;
				}
			}
			else
			{
				if (density_other > density)
				{
					world.setBlockState(other, myState.withProperty(LEVEL, amtToInput - 1), 3);
					world.setBlockState(other, state, 3);
					world.scheduleUpdate(other, this,  tickRate);
					world.scheduleUpdate(other, state.getBlock(), state.getBlock().tickRate(world));
					return 0;
				}
			}
			return amtToInput;
		}
	}

	/**
	 * Try to displace block in world.
	 * @param level The fluid level.
	 * @return A value of fluid remain, characteristics decided whether this
	 * block has been removed (Positive means removed).
	 */
	public int displaceIfPossible(World world, BlockPos pos, BlockPos source, int level)
	{
		if(!world.isAreaLoaded(pos, 2))
			return -level;
		if(world.isAirBlock(pos)) return level;
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() == this) return level;
		FluidTouchBlockEvent event = new FluidTouchBlockEvent(world, source, pos, state, this, level);
		MinecraftForge.EVENT_BUS.post(event);
		if(event.getResult() == Result.ALLOW)
		{
			world.setBlockState(pos, event.getEndingTargetState(), 3);
			return event.amount;
		}
		Block block = state.getBlock();

		if (displacements.containsKey(block))
		{
			if (displacements.get(block))
			{
				block.dropBlockAsItem(world, pos, state, 0);
				return level;
			}
			return -level;
		}

		Material material = state.getMaterial();
		if (material.blocksMovement() || material == Material.PORTAL)
			return -level;

		int density = getDensity(world, pos);
		if (density == Integer.MAX_VALUE)
		{
			block.dropBlockAsItem(world, pos, state, 0);
			return level;
		}

		//		if (this.density > density)
		//			return true;
		//		else
		return -level;
	}
	
	@Override
	public FluidStack drain(World world, BlockPos pos, boolean doDrain)
	{
		return drain(world, pos, Integer.MAX_VALUE, doDrain);
	}

	@Override
	public boolean canDrain(World world, BlockPos pos)
	{
		return true;
	}

	@Override
	public int getQuantaValue(IBlockAccess world, BlockPos pos)
	{
		return getFluidLevel(world, pos);
	}

	@Override
	public boolean canCollideCheck(IBlockState state, boolean fullHit)
	{
		return fullHit;
	}

	@Override
	public int getMaxRenderHeightMeta()
	{
		return 16;
	}

	@Override
	public FluidStack drain(World world, BlockPos pos, int maxDrain, boolean doDrain)
	{
		float per = 1000F / quantaPerBlockFloat;
		float all = maxDrain;
		int a = getFluidLevel(world, pos);
		int q = Math.min((int) (all / per), a);
		if(q == 0) return null;
		if(doDrain)
		{
			setFluidLevel(world, pos, a - q, true);
		}
		return new FluidStack(fluid, (int) (per * q));
	}


	@Override
	public int fill(World world, BlockPos pos, FluidStack resource, boolean doFill)
	{
		if(resource == null || resource.getFluid() != fluid)
			return 0;
		int level = getFluidLevel(world, pos);
		if(level == -1) return 0;
		int level1 = (int) (resource.amount * quantaPerBlockFloat / 1000F);
		if(level1 == 0) return 0;
		if(doFill)
		{
			setFluidLevel(world, pos, level + level1, true);
		}
		
		return (int) Math.ceil(level1 * 1000F / quantaPerBlockFloat);
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if(entityIn instanceof EntityLivingBase)
		{
			float amount;
			if(entityIn.motionY < 0 &&
					!fluid.isGaseous() &&
					(amount = (float) (fluid.getDensity(worldIn, pos) * -entityIn.motionY)) > 1500)
			{
				entityIn.attackEntityFrom(DamageSource.fall, amount / 1000F);
			}
			if(fluid.fireAttackDamage > 0)
			{
				entityIn.attackEntityFrom(DamageSource.inFire, fluid.fireAttackDamage);
			}
		}
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
	}
}