/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.blocks;

import static farcore.FarCoreRegistry.registerTESR;
import static farcore.data.Materials.METALIC;

import farcore.data.CT;
import farcore.lib.tesr.TESRScreenLineChart;
import farcore.lib.tile.instance.screen.TEScreenLight;
import farcore.lib.tile.instance.screen.TEScreenTemperature;
import nebula.base.register.IRegister;
import nebula.common.LanguageManager;
import nebula.common.block.BlockTE;
import nebula.common.data.Misc;
import nebula.common.util.Direction;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockScreen extends BlockTE
{
	public BlockScreen()
	{
		super("screen", METALIC);
		setCreativeTab(CT.MACHINE);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), "Temperature Chart Simple Monitor");
		LanguageManager.registerLocal(getTranslateNameForItemStack(1), "Light Chart Simple Monitor");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		TESRScreenLineChart tesr = new TESRScreenLineChart();
		registerTESR(TEScreenTemperature.class, tesr);
		registerTESR(TEScreenLight.class, tesr);
	}
	
	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register(0, "temperature", TEScreenTemperature.class);
		register.register(1, "light", TEScreenLight.class);
		return true;
	}
	
	@Override
	public IBlockState getBlockPlaceState(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, ItemStack stackIn, EntityLivingBase placer)
	{
		return super.getBlockPlaceState(worldIn, pos, facing, hitX, hitY, hitZ, stackIn, placer).withProperty(Misc.PROP_DIRECTION_HORIZONTALS, Direction.of(placer.getHorizontalFacing()));
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, createTEProperty(), Misc.PROP_DIRECTION_HORIZONTALS);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state)
	{
		return false;
	}
}
