package farcore.lib.block.instance;

import farcore.FarCoreSetup.ClientProxy;
import farcore.data.EnumToolType;
import farcore.lib.block.BlockBase;
import farcore.lib.block.material.MaterialLog;
import farcore.lib.material.Mat;
import farcore.lib.model.block.StateMapperExt;
import farcore.lib.tree.ITree;
import farcore.util.U;
import net.minecraft.block.BlockLog.EnumAxis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLog extends BlockBase
{
	public static final MaterialLog LOG = new MaterialLog();

	public ITree tree;
	
	protected BlockLog(String name, Mat material, ITree tree)
	{
		super(material.modid, name, LOG);
		this.tree = tree;
		U.Mod.registerItemBlockModel(this, 0, tree.material().modid, "log/" + tree.material().name);
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
		Mat material = tree.material();
		StateMapperExt mapper = new StateMapperExt(material.modid, "log", null);
		mapper.setVariants("type", material.name);
		ClientProxy.registerCompactModel(mapper, this, null);
	}
	
	@Override
	public String getHarvestTool(IBlockState state)
	{
		return EnumToolType.axe.name();
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
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer)
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