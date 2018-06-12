/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.blocks;

import static nebula.common.util.Direction.D;
import static nebula.common.util.Direction.E;
import static nebula.common.util.Direction.N;
import static nebula.common.util.Direction.S;
import static nebula.common.util.Direction.U;
import static nebula.common.util.Direction.W;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import farcore.FarCore;
import farcore.data.EnumBlock;
import farcore.data.Materials;
import farcore.lib.block.IThermalCustomBehaviorBlock;
import nebula.base.collection.A;
import nebula.base.collection.ObjArrayParseHelper;
import nebula.client.model.StateMapperExt;
import nebula.common.LanguageManager;
import nebula.common.block.BlockBase;
import nebula.common.block.IBlockStateRegister;
import nebula.common.block.IExtendedDataBlock;
import nebula.common.util.Direction;
import nebula.common.util.L;
import nebula.common.util.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFire extends BlockBase implements IExtendedDataBlock
{
	public static final IProperty<Integer>	STATE			= Properties.create("state", 0, 15);
	public static final PropertyBool		NORTH			= Properties.PROP_NORTH;
	public static final PropertyBool		EAST			= Properties.PROP_EAST;
	public static final PropertyBool		SOUTH			= Properties.PROP_SOUTH;
	public static final PropertyBool		WEST			= Properties.PROP_WEST;
	public static final PropertyBool		UPPER			= Properties.PROP_UP;
	public static final PropertyBool		SPREAD_CHECK	= Properties.create("spread_check");
	public static final PropertyBool		SMOLDER			= Properties.create("smoldering");
	
	public BlockFire()
	{
		super("fire", Materials.FIRE);
		setToolNotRequired();
		setTickRandomly(true);
		EnumBlock.fire.set(this);
		EnumBlock.fire.stateApplier = objs -> {
			ObjArrayParseHelper helper = A.create(objs);
			int level = L.range(0, 15, helper.readOrSkip(15));
			boolean spread = helper.readOrSkip(true);
			boolean smolder = helper.readOrSkip(true);
			return getDefaultState().withProperty(STATE, level).withProperty(SPREAD_CHECK, spread).withProperty(SMOLDER, smolder);
		};
		setLightLevel(11);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, SPREAD_CHECK, STATE, NORTH, EAST, SOUTH, WEST, UPPER, SMOLDER);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), "Fire");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		StateMapperExt mapper = new StateMapperExt(FarCore.ID, "fire", (IProperty<?>) null, STATE, SMOLDER, SPREAD_CHECK);
		ModelLoader.setCustomModelResourceLocation(this.item, 0, new ModelResourceLocation(FarCore.ID + ":fire", "inventory"));
		ModelLoader.setCustomStateMapper(this, mapper);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(STATE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(STATE, meta);
	}
	
	@Override
	public int getDataFromState(IBlockState state)
	{
		int i = state.getValue(STATE);
		if (state.getValue(SMOLDER)) i |= 0x10;
		if (state.getValue(SPREAD_CHECK)) i |= 0x100;
		return i;
	}
	
	@Override
	public IBlockState getStateFromData(int meta)
	{
		IBlockState state = getDefaultState();
		state.withProperty(STATE, meta & 0xF);
		state.withProperty(SMOLDER, (meta & 0x10) != 0);
		state.withProperty(SPREAD_CHECK, (meta & 0x100) != 0);
		return state;
	}
	
	@Override
	public void registerStateToRegister(IBlockStateRegister register)
	{
		register.registerStates(this, STATE, SMOLDER);
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return super.initDefaultState(state).withProperty(STATE, 0).withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(UPPER, false).withProperty(SMOLDER, false).withProperty(SPREAD_CHECK, true);
	}
	
	@Override
	public String getTranslateNameForItemStack(int metadata)
	{
		return getUnlocalizedName() + ".name";
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return canStayFire(worldIn, pos.down(), EnumFacing.UP) ? state : state.withProperty(NORTH, Blocks.FIRE.canCatchFire(worldIn, pos.north(), EnumFacing.SOUTH)).withProperty(EAST, Blocks.FIRE.canCatchFire(worldIn, pos.east(), EnumFacing.WEST))
				.withProperty(SOUTH, Blocks.FIRE.canCatchFire(worldIn, pos.south(), EnumFacing.NORTH)).withProperty(WEST, Blocks.FIRE.canCatchFire(worldIn, pos.west(), EnumFacing.EAST)).withProperty(UPPER, Blocks.FIRE.canCatchFire(worldIn, pos.up(), EnumFacing.DOWN));
	}
	
	@Override
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune, boolean silkTouch)
	{
		return new ArrayList<>();
	}
	
	@Override
	public int tickRate(World worldIn)
	{
		return 28;
	}
	
	@Override
	public boolean requiresUpdates()
	{
		return false;
	}
	
	@Override
	public boolean isCollidable()
	{
		return false;
	}
	
	public boolean canBlockStayAt(World world, BlockPos pos)
	{
		return nebula.common.util.W.isAirNearby(world, pos, true) && (canStayFire(world, pos.down(), EnumFacing.UP) || canStayFire(world, pos.up(), EnumFacing.DOWN) || canStayFire(world, pos.north(), EnumFacing.SOUTH) || canStayFire(world, pos.south(), EnumFacing.NORTH)
				|| canStayFire(world, pos.east(), EnumFacing.WEST) || canStayFire(world, pos.west(), EnumFacing.EAST));
	}
	
	private boolean canBlockBurnAt(World world, BlockPos pos)
	{
		boolean isCatchRain = nebula.common.util.W.isCatchingRain(world, pos, true);
		return canBurnFire(world, pos.down(), EnumFacing.UP, isCatchRain) || canBurnFire(world, pos.up(), EnumFacing.DOWN, isCatchRain) || canBurnFire(world, pos.north(), EnumFacing.SOUTH, isCatchRain) || canBurnFire(world, pos.south(), EnumFacing.NORTH, isCatchRain)
				|| canBurnFire(world, pos.east(), EnumFacing.WEST, isCatchRain) || canBurnFire(world, pos.west(), EnumFacing.EAST, isCatchRain);
	}
	
	private boolean canBurnFire(World world, BlockPos pos, EnumFacing side, boolean isCatchRain)
	{
		IBlockState state;
		return ((state = world.getBlockState(pos)).getBlock() instanceof IThermalCustomBehaviorBlock && ((IThermalCustomBehaviorBlock) state.getBlock()).canFireBurnOn(world, pos, side, isCatchRain)) || state.getBlock().isFireSource(world, pos, side)
				|| state.getBlock().isFlammable(world, pos, side) && !isCatchRain;
	}
	
	private boolean canStayFire(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return nebula.common.util.W.isSideSolid(world, pos, side, false);
	}
	
	@Override
	public boolean isBurning(IBlockAccess world, BlockPos pos)
	{
		return true;
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
	{
		IBlockState newState = state;
		if (!canBlockStayAt(worldIn, pos))
		{
			worldIn.setBlockToAir(pos);
			return;
		}
		if (state.getValue(SPREAD_CHECK) && worldIn.getGameRules().getBoolean("doFireTick"))
		{
			if (state.getValue(SMOLDER))
			{
				int l = state.getValue(STATE) + (3 + random.nextInt(3)) / 2;
				if (l > 15)
				{
					worldIn.setBlockToAir(pos);
					return;
				}
				else
				{
					FireLocationInfo info = new FireLocationInfo(2, worldIn, pos);
					if (canBlockBurnAt(worldIn, pos))
					{
						if (random.nextInt(3) == 0)
						{
							newState = newState.withProperty(SMOLDER, false);
						}
						else
						{
							tryCatchFire(info, state, worldIn, pos.east(), 15, random, l, W);
							tryCatchFire(info, state, worldIn, pos.west(), 15, random, l, E);
							tryCatchFire(info, state, worldIn, pos.down(), 15, random, l, U);
							tryCatchFire(info, state, worldIn, pos.up(), 15, random, l, D);
							tryCatchFire(info, state, worldIn, pos.north(), 15, random, l, S);
							tryCatchFire(info, state, worldIn, pos.south(), 15, random, l, N);
						}
					}
					else
					{
						worldIn.setBlockToAir(pos);
						return;
					}
					newState = newState.withProperty(STATE, l);
					if (newState != state) info.setBlockState(newState, 2);
					worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
				}
			}
			else
			{
				updateTick(worldIn, pos, newState, random);
			}
		}
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!canBlockStayAt(worldIn, pos))
		{
			worldIn.setBlockToAir(pos);
		}
		if (state.getValue(SPREAD_CHECK) && worldIn.getGameRules().getBoolean("doFireTick"))
		{
			if (!canBlockBurnAt(worldIn, pos))
			{
				if (rand.nextInt(19 - state.getValue(STATE)) == 0)
				{
					if (rand.nextInt(3) == 0)
					{
						worldIn.setBlockState(pos, state.withProperty(SMOLDER, true), 2);
					}
					else
					{
						worldIn.setBlockToAir(pos);
					}
					return;
				}
			}
			int l = state.getValue(STATE);
			int range = l < 8 ? 3 : l < 14 ? 2 : 1;
			FireLocationInfo info = new FireLocationInfo(range + 2, worldIn, pos);
			boolean isFireSource = info.isFireSource(0, +1, 0, 0) || info.isFireSource(0, -1, 0, 1) || info.isFireSource(0, 0, +1, 2) || info.isFireSource(0, 0, -1, 3) || info.isFireSource(+1, 0, 0, 4) || info.isFireSource(-1, 0, 0, 5);
			if (!isFireSource)
			{
				if (nebula.common.util.W.isCatchingRain(worldIn, pos, true))
				{
					worldIn.setBlockToAir(pos);
					worldIn.playSound(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.2F + (rand.nextFloat() - rand.nextFloat()) * .8F, true);
					return;
				}
				int l1 = l + rand.nextInt(5) / 3;
				if (l1 > 15)
				{
					info.setToAir(pos);
				}
				else if (l1 != l)
				{
					info.setBlockState(pos, state.withProperty(STATE, l1), 2);
				}
			}
			worldIn.scheduleUpdate(pos, this, tickRate(worldIn) + rand.nextInt(14));
			boolean flag1 = worldIn.isBlockinHighHumidity(pos);
			boolean flag2 = state.getValue(SMOLDER);
			
			int chance = 300;
			
			if (flag1)
			{
				chance -= 50;
			}
			
			if (!flag2)
			{
				tryCatchFire(info, state, worldIn, pos, chance, rand, l, W);
				tryCatchFire(info, state, worldIn, pos, chance, rand, l, E);
				tryCatchFire(info, state, worldIn, pos, chance, rand, l, U);
				tryCatchFire(info, state, worldIn, pos, chance, rand, l, D);
				tryCatchFire(info, state, worldIn, pos, chance, rand, l, S);
				tryCatchFire(info, state, worldIn, pos, chance, rand, l, N);
			}
			if (range > 0)
			{
				for (int i = -range; i <= range; ++i)
				{
					for (int j = -range; j <= range; ++j)
					{
						for (int k = -range; k <= range; ++k)
						{
							if ((i != 0 || j != 0 || k != 0) && rand.nextInt(flag2 ? 8 : 4) == 0)
							{
								int speed;
								if (info.canBlockStay(i, j, k) && (speed = info.getSpreadSpeed(i, j, k)) > 0)
								{
									chance = 100;
									chance *= Math.cbrt(i * i + j * j + k * k);
									if (flag1) chance /= 2;
									trySpreadFire(worldIn, pos, i, j, k, info, rand, l, chance, speed);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void trySpreadFire(World worldIn, BlockPos pos, int i, int j, int k, FireLocationInfo info, Random rand, int level, int chance, int speed)
	{
		BlockPos pos2 = pos.add(i, j, k);
		IBlockState state = worldIn.getBlockState(pos2);
		if (state.getBlock().isReplaceable(worldIn, pos2))
		{
			boolean flag = false;
			for (Direction direction : Direction.DIRECTIONS_3D)
			{
				if (info.isCustomed(i + direction.x, j + direction.y, k + direction.z))
				{
					BlockPos pos3 = direction.offset(pos2);
					Block block = worldIn.getBlockState(pos3).getBlock();
					if (((IThermalCustomBehaviorBlock) block).onBurn(info.setMainPos(pos3), 1000F / chance, direction.opposite()))
					{
						info.resetMainPos();
						flag = true;
					}
				}
			}
			if (flag)
			{
				return;
			}
			if (chance > 0 && rand.nextInt(chance) < speed)
			{
				int l2 = Math.min(15, level + rand.nextInt(4) / 2);
				info.setBlockState(pos2, getDefaultState().withProperty(STATE, l2).withProperty(SMOLDER, false), 3);
			}
		}
	}
	
	private void tryCatchFire(FireLocationInfo info, IBlockState fireState, World worldIn, BlockPos pos, int chance, Random random, int age, Direction face)
	{
		info.setMainPos(pos = face.offset(pos));
		IBlockState state;
		if (info.isCustomed(face.x, face.y, face.z) && ((IThermalCustomBehaviorBlock) worldIn.getBlockState(pos).getBlock()).onBurningTick(info, random, face.opposite(), fireState))
		{
			info.resetMainPos();
			return;
		}
		int i = info.getFlammability(face.x, face.y, face.z, face.opposite());
		if (random.nextInt(chance) < i)
		{
			if (random.nextInt(age + 10) < 5 && !nebula.common.util.W.isCatchingRain(worldIn, pos, true))
			{
				int j = Math.min(15, age + random.nextInt(5) / 4);
				info.setBlockState(getDefaultState().withProperty(STATE, j).withProperty(SMOLDER, false), 3);
			}
			else
			{
				(state = worldIn.getBlockState(pos)).getBlock().onBlockDestroyedByPlayer(worldIn, pos, state);
				info.removeBlock();
			}
		}
		info.resetMainPos();
	}
	
	// private float countBlockDencyInRange(World world, BlockPos pos, int
	// range)
	// {
	// int count = 0;
	// int air = 0;
	// for(int i = -range; i <= range; ++i)
	// {
	// for(int j = -range; j <= range; ++j)
	// {
	// for(int k = -range; k <= range; ++k)
	// {
	// BlockPos pos2;
	// if(world.isAirBlock(pos2 = pos.add(i, j, k)) && canBlockStayAt(world,
	// pos2))
	// {
	// ++air;
	// }
	// else if(world.getBlockState(pos2).getBlock() == this)
	// {
	// ++air;
	// ++count;
	// }
	// }
	// }
	// }
	// return (float) count / (float) air;
	// }
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
	{
		if (!canBlockStayAt(worldIn, pos))
		{
			worldIn.setBlockToAir(pos);
		}
		if (worldIn.isRaining() && worldIn.canSeeSky(pos))
		{
			worldIn.setBlockToAir(pos);
			worldIn.playSound(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.2F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * .8F, true);
		}
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if (worldIn.provider.getDimensionType().getId() > 0 || !Blocks.PORTAL.trySpawnPortal(worldIn, pos))
		{
			if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP) && !canBlockBurnAt(worldIn, pos))
			{
				worldIn.setBlockToAir(pos);
			}
			else
			{
				worldIn.scheduleUpdate(pos.toImmutable(), this, tickRate(worldIn) + worldIn.rand.nextInt(10));
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (rand.nextInt(24) == 0)
		{
			worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
		}
		
		if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) && !Blocks.FIRE.canCatchFire(worldIn, pos.down(), EnumFacing.UP))
		{
			if (Blocks.FIRE.canCatchFire(worldIn, pos.west(), EnumFacing.EAST))
			{
				for (int j = 0; j < 2; ++j)
				{
					double d3 = pos.getX() + rand.nextDouble() * 0.1;
					double d8 = pos.getY() + rand.nextDouble();
					double d13 = pos.getZ() + rand.nextDouble();
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d3, d8, d13, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}
			
			if (Blocks.FIRE.canCatchFire(worldIn, pos.east(), EnumFacing.WEST))
			{
				for (int k = 0; k < 2; ++k)
				{
					double d4 = pos.getX() + 1 - rand.nextDouble() * 0.1;
					double d9 = pos.getY() + rand.nextDouble();
					double d14 = pos.getZ() + rand.nextDouble();
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d4, d9, d14, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}
			
			if (Blocks.FIRE.canCatchFire(worldIn, pos.north(), EnumFacing.SOUTH))
			{
				for (int l = 0; l < 2; ++l)
				{
					double d5 = pos.getX() + rand.nextDouble();
					double d10 = pos.getY() + rand.nextDouble();
					double d15 = pos.getZ() + rand.nextDouble() * 0.1;
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d5, d10, d15, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}
			
			if (Blocks.FIRE.canCatchFire(worldIn, pos.south(), EnumFacing.NORTH))
			{
				for (int i1 = 0; i1 < 2; ++i1)
				{
					double d6 = pos.getX() + rand.nextDouble();
					double d11 = pos.getY() + rand.nextDouble();
					double d16 = pos.getZ() + 1 - rand.nextDouble() * 0.1;
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d6, d11, d16, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}
			
			if (Blocks.FIRE.canCatchFire(worldIn, pos.up(), EnumFacing.DOWN))
			{
				for (int j1 = 0; j1 < 2; ++j1)
				{
					double d7 = pos.getX() + rand.nextDouble();
					double d12 = pos.getY() + 1 - rand.nextDouble() * 0.1;
					double d17 = pos.getZ() + rand.nextDouble();
					worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}
		}
		else
		{
			for (int i = 0; i < 3; ++i)
			{
				double d0 = pos.getX() + rand.nextDouble();
				double d1 = pos.getY() + rand.nextDouble() * 0.5D + 0.5D;
				double d2 = pos.getZ() + rand.nextDouble();
				worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
	}
	
	/**
	 * Get the MapColor for this Block and the given BlockState
	 */
	@Override
	public MapColor getMapColor(IBlockState state)
	{
		return MapColor.TNT;
	}
	
	@Override
	public boolean canReplace(World worldIn, BlockPos pos, EnumFacing side, ItemStack stack)
	{
		return true;
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		return canBlockStayAt(worldIn, pos);
	}
	
	@Override
	public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return true;
	}
}
