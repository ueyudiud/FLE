package farcore.lib.block;

import java.util.Random;

import farcore.data.EnumSlabState;
import farcore.lib.block.instance.ItemBlockSlab;
import farcore.util.U;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSlab extends BlockBase
{
	public BlockSlab(String name, Material materialIn)
	{
		super(name, materialIn);
		lightOpacity = -1;
	}
	public BlockSlab(String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(name, blockMaterialIn, blockMapColorIn);
		lightOpacity = -1;
	}
	public BlockSlab(String modid, String name, Material materialIn)
	{
		super(modid, name, materialIn);
		lightOpacity = -1;
	}
	public BlockSlab(String modid, String name, Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(modid, name, blockMaterialIn, blockMapColorIn);
		lightOpacity = -1;
	}

	@Override
	protected Item createItemBlock()
	{
		return new ItemBlockSlab(this);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, EnumSlabState.PROPERTY);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(EnumSlabState.PROPERTY).ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(EnumSlabState.PROPERTY, EnumSlabState.values()[meta]);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer)
	{
		return getDefaultState().withProperty(EnumSlabState.PROPERTY, EnumSlabState.values()[U.Worlds.fixSide(facing, hitX, hitY, hitZ)]);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		double maxX = 1.0F, maxY = 1.0F, maxZ = 1.0F, minX = 0.0F, minY = 0.0F, minZ = 0.0F;
		switch (state.getValue(EnumSlabState.PROPERTY))
		{
		case down :
			maxY = 0.5F;
			break;
		case up :
			minY = 0.5F;
			break;
		case south :
			maxZ = 0.5F;
			break;
		case north :
			minZ = 0.5F;
			break;
		case west :
			maxX = 0.5F;
			break;
		case east :
			minX = 0.5F;
			break;
		default :
			break;
		}
		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	@Override
	public boolean isFullyOpaque(IBlockState state)
	{
		return state.getValue(EnumSlabState.PROPERTY).fullCube;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return state.getValue(EnumSlabState.PROPERTY).fullCube;
	}

	@Override
	public int getLightOpacity(IBlockState state)
	{
		return lightOpacity != -1 ? lightOpacity : 3;
	}

	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return lightOpacity != -1 ? (isOpaqueCube(state) ? lightOpacity : Math.min(lightOpacity, 3)) :
			(isOpaqueCube(state) ? 255 : 3);
	}

	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		EnumSlabState state = base_state.getValue(EnumSlabState.PROPERTY);
		return state.fullCube || state.ordinal() == side.ordinal();
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return state.getValue(EnumSlabState.PROPERTY).dropMul;
	}

	@Override
	protected ItemStack createStackedBlock(IBlockState state)
	{
		return new ItemStack(this, quantityDropped(state, 0, RANDOM), damageDropped(state));
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
	{
		EnumSlabState state = world.getBlockState(pos).getValue(EnumSlabState.PROPERTY);
		if(state != EnumSlabState.rotationState[axis.ordinal()][state.ordinal()])
		{
			U.Worlds.switchProp(world, pos, EnumSlabState.PROPERTY, EnumSlabState.rotationState[axis.ordinal()][state.ordinal()], 3);
			return true;
		}
		return false;
	}

	@Override
	public EnumFacing[] getValidRotations(World world, BlockPos pos)
	{
		return EnumFacing.VALUES;
	}
}