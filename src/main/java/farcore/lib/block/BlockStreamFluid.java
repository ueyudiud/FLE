package farcore.lib.block;

import java.util.function.Function;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import farcore.lib.block.state.BlockStateContainerM;
import farcore.lib.block.state.IFarProperty;
import farcore.lib.block.state.PropertyFarInt;
import farcore.lib.fluid.FluidBase;
import farcore.util.U;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * There are still some problems need to solve.
 * @author ueyudiud
 *
 */
@Deprecated
public abstract class BlockStreamFluid extends BlockStandardFluid
{
	public static final PropertyFarInt STREAM_LEVEL = new PropertyFarInt("level", 0, 255).setInstance(255);
	public static final Function<IBlockState, Byte> STREAM_BLOCK_SELECTOR = (IBlockState state) -> (byte) (state.getValue(STREAM_LEVEL) >> 4);
	public static final int QUANTA_PER_BLOCK = 256;

	public static BlockStreamFluid[] createFluids(FluidBase fluid, Material material)
	{
		BlockStreamFluid[] blocks = new BlockStreamFluid[16];
		for(int i = 0; i < 16; ++i)
		{
			final int j = i;
			final BlockStateContainer container = i > 0 ? blocks[0].blockState : null;
			blocks[i] = new BlockStreamFluid(blocks[0], i, fluid, material)
			{
				@Override
				protected BlockStateContainer createBlockState()
				{
					return container != null ? container : new BlockStateContainerM(this, new IFarProperty[]{STREAM_LEVEL}, U.L.cast(FLUID_RENDER_PROPS, IUnlistedProperty.class), blocks, STREAM_BLOCK_SELECTOR)
					{
						@Override
						public BlockState createState(ImmutableMap<IProperty<?>, Comparable<?>> properties,
								ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties)
						{
							return new BlockState(properties, unlistedProperties)
							{
								@Override
								public <T extends Comparable<T>, V extends T> BlockState withProperty(
										IProperty<T> property, V value)
								{
									if (property == BlockStreamFluid.LEVEL)
										return super.withProperty(STREAM_LEVEL, (j << 4 | ((Integer) value).intValue()));
									return super.withProperty(property, value);
								}
								
								@Override
								public boolean isOpaqueCube()
								{
									return false;
								}
							};
						}
					};
				}
			};
		}
		return blocks;
	}

	protected BlockStreamFluid parent;
	protected int subid;

	protected BlockStreamFluid(BlockStreamFluid parent, int i, FluidBase fluid, Material material)
	{
		super("fluid." + fluid.getName() + "." + i, fluid, material);
		this.parent = parent == null ? this : parent;
		subid = i;
	}
	
	public final int getSubid()
	{
		return subid;
	}

	@Override
	protected abstract BlockStateContainer createBlockState();

	@Override
	protected IProperty<Integer> getLevelProperty()
	{
		return STREAM_LEVEL;
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(STREAM_LEVEL) & 0xF;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(STREAM_LEVEL, subid << 4 | meta);
	}
	
	@Override
	public int getFluidLevel(IBlockAccess world, BlockPos pos)
	{
		if(world.isAirBlock(pos))
			return 0;
		IBlockState state = world.getBlockState(pos);
		if(!(state.getBlock() instanceof BlockStreamFluid))
			return -1;
		else if(((BlockStreamFluid) state.getBlock()).parent != parent)
			return -1;
		else
			return world.getBlockState(pos).getValue(STREAM_LEVEL);
	}

	@Override
	public int getMaxRenderHeightMeta()
	{
		return 256;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		if (maxScaledLight == 0) return super.getLightValue(state, world, pos);
		int data = state.getValue(STREAM_LEVEL);
		return (int) (data / (float) QUANTA_PER_BLOCK * maxScaledLight);
	}
	
	@Override
	public Vec3d modifyAcceleration(World world, BlockPos pos, Entity entity, Vec3d vec)
	{
		if (densityDir > 0) return vec;
		Vec3d vec_flow = getFlowVector(world, pos);
		return vec.addVector(
				vec_flow.xCoord * (QUANTA_PER_BLOCK * 4),
				vec_flow.yCoord * (QUANTA_PER_BLOCK * 4),
				vec_flow.zCoord * (QUANTA_PER_BLOCK * 4));
	}
	
	@Override
	public Vec3d getFlowVector(IBlockAccess world, BlockPos pos)
	{
		Vec3d vec = new Vec3d(0.0D, 0.0D, 0.0D);
		int decay = QUANTA_PER_BLOCK - getQuantaValue(world, pos);
		
		for (int side = 0; side < 4; ++side)
		{
			int x2 = pos.getX();
			int z2 = pos.getZ();
			
			switch (side)
			{
			case 0: --x2; break;
			case 1: --z2; break;
			case 2: ++x2; break;
			case 3: ++z2; break;
			}
			
			BlockPos pos2 = new BlockPos(x2, pos.getY(), z2);
			int otherDecay = QUANTA_PER_BLOCK - getQuantaValue(world, pos2);
			if (otherDecay >= QUANTA_PER_BLOCK)
			{
				if (!world.getBlockState(pos2).getMaterial().blocksMovement())
				{
					otherDecay = QUANTA_PER_BLOCK - getQuantaValue(world, pos2.down());
					if (otherDecay >= 0)
					{
						int power = otherDecay - (decay - QUANTA_PER_BLOCK);
						vec = vec.addVector((pos2.getX() - pos.getX()) * power, 0, (pos2.getZ() - pos.getZ()) * power);
					}
				}
			}
			else if (otherDecay >= 0)
			{
				int power = otherDecay - decay;
				vec = vec.addVector((pos2.getX() - pos.getX()) * power, 0, (pos2.getZ() - pos.getZ()) * power);
			}
		}
		
		if (world.getBlockState(pos.up()).getBlock() == this)
		{
			boolean flag =
					isBlockSolid(world, pos.add( 0,  0, -1), EnumFacing.NORTH) ||
					isBlockSolid(world, pos.add( 0,  0,  1), EnumFacing.SOUTH) ||
					isBlockSolid(world, pos.add(-1,  0,  0), EnumFacing.WEST) ||
					isBlockSolid(world, pos.add( 1,  0,  0), EnumFacing.EAST) ||
					isBlockSolid(world, pos.add( 0,  1, -1), EnumFacing.NORTH) ||
					isBlockSolid(world, pos.add( 0,  1,  1), EnumFacing.SOUTH) ||
					isBlockSolid(world, pos.add(-1,  1,  0), EnumFacing.WEST) ||
					isBlockSolid(world, pos.add( 1,  1,  0), EnumFacing.EAST);
			
			if (flag)
			{
				vec = vec.normalize().addVector(0.0D, -6.0D, 0.0D);
			}
		}
		vec = vec.normalize();
		return vec;
	}
	
	@Override
	public float getFilledPercentage(World world, BlockPos pos)
	{
		int quantaRemaining = getQuantaValue(world, pos) + 1;
		return (quantaRemaining > QUANTA_PER_BLOCK ? 1.0F : (float) quantaRemaining / (float) QUANTA_PER_BLOCK) * (density > 0 ? 1 : -1);
	}
}