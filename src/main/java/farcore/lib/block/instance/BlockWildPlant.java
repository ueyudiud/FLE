package farcore.lib.block.instance;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import farcore.data.MP;
import farcore.lib.material.Mat;
import farcore.lib.plant.IPlant;
import nebula.common.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockWildPlant extends BlockBase implements IPlantable
{
	public static BlockWildPlant create(Mat material)
	{
		IPlant<?> plant = material.getProperty(MP.property_plant);
		return new BlockWildPlant(material.name)
		{
			@Override
			protected BlockStateContainer createBlockState()
			{
				return plant.createBlockState(this);
			}
			
			@Override
			public int getMetaFromState(IBlockState state)
			{
				return plant.getMeta(this, state);
			}
			
			@Override
			public IBlockState getStateFromMeta(int meta)
			{
				return plant.getState(this, meta);
			}
			
			@Override
			protected IBlockState initDefaultState(IBlockState state)
			{
				return plant.initDefaultState(super.initDefaultState(state));
			}
		};
	}
	
	private IPlant<?> plant;
	
	protected BlockWildPlant(String name)
	{
		super("wild.plant" + name, Material.PLANTS);
		setHardness(0.2F);
		setTickRandomly(true);
		setLightOpacity(1);
	}
	
	private Set<String> toolSet = ImmutableSet.of("knife");
	
	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return false;
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return this.toolSet.contains(type);
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		IBlockState state;
		return (state = worldIn.getBlockState(pos.down())).getBlock()
				.canSustainPlant(state, worldIn, pos.down(), EnumFacing.UP, this);
	}
	
	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return getDefaultState();
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return this.plant.getPlantType(world, pos);
	}
}