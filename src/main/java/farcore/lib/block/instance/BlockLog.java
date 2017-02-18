package farcore.lib.block.instance;

import farcore.data.EnumToolTypes;
import farcore.data.Materials;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyTree;
import nebula.client.model.StateMapperExt;
import nebula.client.util.Renders;
import nebula.common.block.BlockBase;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLog extends BlockBase
{
	public PropertyTree tree;
	
	protected BlockLog(String name, Mat material, PropertyTree tree)
	{
		super(material.modid, name, Materials.LOG);
		this.tree = tree;
	}
	
	@Override
	protected IBlockState initDefaultState(IBlockState state)
	{
		return state.withProperty(net.minecraft.block.BlockLog.LOG_AXIS, EnumAxis.NONE);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		Mat material = this.tree.material();
		StateMapperExt mapper = new StateMapperExt(material.modid, "log", null);
		mapper.setVariants("type", material.name);
		Renders.registerCompactModel(mapper, this, null);
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