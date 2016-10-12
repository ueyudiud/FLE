package farcore.lib.block.instance;

import farcore.FarCore;
import farcore.lib.block.BlockMaterial;
import farcore.lib.material.Mat;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPlank extends BlockMaterial
{
	public static final IProperty<Boolean> FIRE_RESISTANCE = PropertyBool.create("fire_resistance");
	public static final IProperty<Boolean> ANTICORROSIVE= PropertyBool.create("anticorrosive");
	public static final IProperty<Boolean> WET = PropertyBool.create("wet");
	/**
	 * Default broken plank only can be get at such as sinking ship.
	 */
	public static final IProperty<Boolean> BROKE = PropertyBool.create("broke");
	
	public BlockPlank(Mat material)
	{
		super(FarCore.ID, "plank." + material.name, Material.WOOD, material);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FIRE_RESISTANCE, ANTICORROSIVE, WET, BROKE);
	}

	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(WET, false).withProperty(FIRE_RESISTANCE, false).withProperty(ANTICORROSIVE, false).withProperty(BROKE, false);
	}
	
	@Override
	public int getLightOpacity(IBlockState state)
	{
		return state.getValue(BROKE) ? 100 : 255;
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		int v = 0;
		if(state.getValue(FIRE_RESISTANCE))
		{
			v |= 1;
		}
		if(state.getValue(ANTICORROSIVE))
		{
			v |= 2;
		}
		if(state.getValue(WET))
		{
			v |= 4;
		}
		if(state.getValue(BROKE))
		{
			v |= 8;
		}
		return v;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState state = getDefaultState();
		if((meta & 0x1) != 0)
		{
			state = state.withProperty(FIRE_RESISTANCE, true);
		}
		if((meta & 0x2) != 0)
		{
			state = state.withProperty(ANTICORROSIVE, true);
		}
		if((meta & 0x4) != 0)
		{
			state = state.withProperty(WET, true);
		}
		if((meta & 0x8) != 0)
		{
			state = state.withProperty(BROKE, true);
		}
		return state;
	}

	@Override
	public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos)
	{
		return (state.getValue(BROKE) ? 0.1F : state.getValue(WET) ? 0.8F : 1.0F) * super.getBlockHardness(state, worldIn, pos);
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return world.getBlockState(pos).getValue(BROKE) ? 0F : super.getExplosionResistance(world, pos, exploder, explosion);
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return world.getBlockState(pos).getValue(FIRE_RESISTANCE) ? 0 : super.getFireSpreadSpeed(world, pos, face);
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return world.getBlockState(pos).getValue(FIRE_RESISTANCE) ? 0 : super.getFlammability(world, pos, face);
	}
}