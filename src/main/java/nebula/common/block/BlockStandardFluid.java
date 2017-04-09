/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.block;

import java.util.Random;
import java.util.function.Function;

import nebula.Nebula;
import nebula.client.util.IRenderRegister;
import nebula.common.fluid.FluidBase;
import nebula.common.fluid.FluidBlockEvent.FluidTouchBlockEvent;
import nebula.common.util.Direction;
import nebula.common.util.Game;
import nebula.common.util.L;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The standard fluid type.
 * @author ueyudiud
 *
 */
public class BlockStandardFluid extends BlockFluidBase implements ISmartFluidBlock, IRenderRegister
{
	public final FluidBase fluid;
	private final FluidStack blockValue;
	
	protected static Function<Object[], IBlockState> createFunctionApplier(BlockStandardFluid block)
	{
		return objects -> objects.length == 0 ?  block.getDefaultState() :
			block.getDefaultState().withProperty(LEVEL, ((Number) objects[0]).intValue() + 1);
	}
	
	public BlockStandardFluid(String registerName, FluidBase fluid, Material material)
	{
		super(fluid, material);
		setQuantaPerBlock(16);
		setCreativeTab(Nebula.tabFluids);
		setDefaultState(initDefaultState(getDefaultState()));
		Game.registerBlock(this, registerName);
		this.blockValue = new FluidStack(fluid, 1000);
		this.fluid = fluid;
	}
	public BlockStandardFluid(FluidBase fluid, Material material)
	{
		this("fluid." + fluid.getName(), fluid, material);
	}
	
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(LEVEL, 15);
	}
	
	protected IProperty<Integer> getLevelProperty()
	{
		return LEVEL;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		Game.registerFluid(this);
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return this.fluid.getUnlocalizedName();
	}
	
	@Override
	public String getLocalizedName()
	{
		return this.fluid.getLocalizedName(this.blockValue);
	}
	
	public int getFluidLevel(IBlockAccess world, BlockPos pos)
	{
		if(world.isAirBlock(pos))
			return 0;
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() != this)
			return -1;
		else
			return state.getValue(getLevelProperty()) + 1;
	}
	
	public void setFluidLevel(World world, BlockPos pos, int level, boolean update)
	{
		if(level == 0)
		{
			world.setBlockToAir(pos);
		}
		else
		{
			world.setBlockState(pos, getDefaultState().withProperty(getLevelProperty(), level - 1), update ? 3 : 2);
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
		int level = state.getValue(getLevelProperty()) + 1;
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
				worldIn.setBlockState(pos, state.withProperty(getLevelProperty(), level - 1), 2);
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
		Direction direction = L.random(Direction.DIRECTIONS_2D, world.rand);
		BlockPos pos1 = pos.add(direction.x, 0, direction.z);
		BlockPos pos2 = pos1.add(0, this.densityDir, 0);
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
				if(l1 >= 0 && l1 < this.quantaPerBlock)
				{
					setFluidLevel(world, pos2, l1 + 1, false);
					world.scheduleUpdate(pos2, this, this.tickRate);
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
		BlockPos other = pos.add(0, this.densityDir, 0);
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
		
		int amt = getQuantaValueBelow(world, other, this.quantaPerBlock);
		if (amt >= 0)
		{
			amt += amtToInput;
			if (amt > this.quantaPerBlock)
			{
				world.setBlockState(other, myState.withProperty(getLevelProperty(), this.quantaPerBlock - 1), 3);
				world.scheduleUpdate(other, this, this.tickRate);
				return amt - this.quantaPerBlock;
			}
			else if (amt > 0)
			{
				world.setBlockState(other, myState.withProperty(getLevelProperty(), amt - 1), 3);
				world.scheduleUpdate(other, this, this.tickRate);
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
					world.setBlockState(other, myState.withProperty(getLevelProperty(), amtToInput - 1), 3);
					world.scheduleUpdate(other, this, this.tickRate);
					world.setBlockToAir(pos);
					return 0;
				}
				else
					return amtToInput;
			}
			
			if (this.densityDir < 0)
			{
				if (density_other < this.density) // then swap
				{
					world.setBlockState(other, myState.withProperty(getLevelProperty(), amtToInput - 1), 3);
					world.setBlockState(pos,   state, 3);
					world.scheduleUpdate(other, this, this.tickRate);
					world.scheduleUpdate(pos,   state.getBlock(), state.getBlock().tickRate(world));
					return 0;
				}
			}
			else
			{
				if (density_other > this.density)
				{
					world.setBlockState(other, myState.withProperty(getLevelProperty(), amtToInput - 1), 3);
					world.setBlockState(other, state, 3);
					world.scheduleUpdate(other, this,  this.tickRate);
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
		
		if (this.displacements.containsKey(block))
		{
			if (this.displacements.get(block))
			{
				block.dropBlockAsItem(world, pos, state, 0);
				world.setBlockToAir(pos);
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
			world.setBlockToAir(pos);
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
		float per = 1000F / this.quantaPerBlockFloat;
		float all = maxDrain;
		int a = getFluidLevel(world, pos);
		int q = Math.min((int) (all / per), a);
		if(q == 0) return null;
		if(doDrain)
		{
			setFluidLevel(world, pos, a - q, true);
		}
		return new FluidStack(this.fluid, (int) (per * q));
	}
	
	
	@Override
	public int fill(World world, BlockPos pos, FluidStack resource, boolean doFill)
	{
		if(resource == null || resource.getFluid() != this.fluid)
			return 0;
		int level = getFluidLevel(world, pos);
		if(level == -1) return 0;
		int level1 = (int) (resource.amount * this.quantaPerBlockFloat / 1000F);
		if(level1 == 0) return 0;
		if(doFill)
		{
			if(level + level1 > 16)
			{
				level1 = 16 - level;
			}
			setFluidLevel(world, pos, level + level1, true);
		}
		
		return (int) Math.ceil(level1 * 1000F / this.quantaPerBlockFloat);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if(entityIn instanceof EntityLivingBase)
		{
			float amount;
			if(entityIn.motionY < 0 &&
					!this.fluid.isGaseous() &&
					(amount = (float) (this.fluid.getDensity(worldIn, pos) * -entityIn.motionY)) > 1500)
			{
				entityIn.attackEntityFrom(DamageSource.fall, amount / 1000F);
			}
			if(this.fluid.fireAttackDamage > 0)
			{
				entityIn.attackEntityFrom(DamageSource.inFire, this.fluid.fireAttackDamage);
			}
		}
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
	}
	
	@Override
	public Boolean isAABBInsideMaterial(World world, BlockPos pos, AxisAlignedBB boundingBox, Material materialIn)
	{
		IBlockState state = world.getBlockState(pos);
		if (materialIn != state.getMaterial()) return false;
		return boundingBox.intersectsWith(getCollisionBoundingBox(state, world, pos));
	}
}