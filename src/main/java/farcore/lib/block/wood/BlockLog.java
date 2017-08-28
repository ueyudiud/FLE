package farcore.lib.block.wood;

import farcore.data.EnumToolTypes;
import farcore.data.Materials;
import farcore.lib.tree.Tree;
import nebula.common.block.BlockBase;
import nebula.common.block.IExtendedDataBlock;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockLog extends BlockBase implements IExtendedDataBlock
{
	public Tree tree;
	
	protected BlockLog(String name, Tree tree)
	{
		super(tree.material.modid, name, Materials.LOG);
		this.tree = tree;
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(net.minecraft.block.BlockLog.LOG_AXIS, EnumAxis.NONE);
	}
	
	@Override
	public String getHarvestTool(IBlockState state)
	{
		return EnumToolTypes.AXE.name;
	}
	
	@Override
	public boolean isToolEffective(String type, IBlockState state)
	{
		return getHarvestTool(state).equals(type);
	}
	
	@Override
	public String getTranslateNameForItemStack(int metadata)
	{
		return super.getTranslateNameForItemStack(metadata >> 2);
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return this.tree.hardness;
	}
	
	@Override
	public float getExplosionResistance(Entity exploder)
	{
		return this.tree.explosionResistance;
	}
	
	@Override
	public IBlockState getBlockPlaceState(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, ItemStack stackIn, EntityLivingBase placer)
	{
		EnumAxis axis = EnumAxis.fromFacingAxis(facing.getAxis());
		return getDefaultState().withProperty(net.minecraft.block.BlockLog.LOG_AXIS, axis);
	}
	
	@Override
	public boolean isWood(IBlockAccess world, BlockPos pos)
	{
		return true;
	}
}