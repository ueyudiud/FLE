/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.blocks;

import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import farcore.data.M;
import farcore.data.Materials;
import farcore.data.SubTags;
import farcore.energy.thermal.IThermalHandler;
import farcore.lib.block.IThermalCustomBehaviorBlock;
import farcore.lib.material.Mat;
import fle.core.FLE;
import fle.core.tile.TEFirewood;
import nebula.base.register.IRegister;
import nebula.client.blockstate.BlockStateTileEntityWapper;
import nebula.client.model.StateMapperExt;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.common.LanguageManager;
import nebula.common.block.BlockTE;
import nebula.common.util.Direction;
import nebula.common.world.IModifiableCoord;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockResourceMisc extends BlockTE implements IThermalCustomBehaviorBlock
{
	public BlockResourceMisc()
	{
		super("resource.misc", Materials.WOOD);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), "Firewood");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		StateMapperExt mapper = new StateMapperExt(FLE.MODID, "misc", this.property_TE);
		registerRenderMapper(mapper);
		registerCustomBlockRender(mapper, 0, "misc_resource/firewood");
		
		NebulaModelLoader.registerBlockMetaGenerator(new ResourceLocation(FLE.MODID, "firewood/states"), state -> {
			TEFirewood tile = BlockStateTileEntityWapper.unwrap(state);
			return tile == null ? M.oak.name : tile.isCarbonate() ? "charcoal" : tile.getMaterial().name;
		});
		NebulaModelLoader.registerTextureSet(new ResourceLocation(FLE.MODID, "firewood/side_base"), () -> Maps.toMap(Lists.transform(Mat.filt(SubTags.WOOD), m -> m.name), name -> new ResourceLocation(FLE.MODID, "blocks/firewood/" + name + "_side")));
		NebulaModelLoader.registerTextureSet(new ResourceLocation(FLE.MODID, "firewood/top_base"), () -> Maps.toMap(Lists.transform(Mat.filt(SubTags.WOOD), m -> m.name), name -> new ResourceLocation(FLE.MODID, "blocks/firewood/" + name + "_top")));
	}
	
	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register(0, "firewood", TEFirewood.class);
		return true;
	}
	
	@Override
	public boolean onBurn(IModifiableCoord coord, float burnHardness, Direction direction)
	{
		TileEntity tile = coord.getTE();
		if (tile instanceof TEFirewood)
		{
			((TEFirewood) tile).setFlammed();
		}
		return true;
	}
	
	@Override
	public boolean onBurningTick(IModifiableCoord coord, Random rand, Direction fireSourceDir, IBlockState fireState)
	{
		return true;
	}
	
	@Override
	public double getThermalConduct(World world, BlockPos pos, IBlockState state)
	{
		try
		{
			return ((IThermalHandler) world.getTileEntity(pos)).getThermalConductivity(Direction.Q);
		}
		catch (Exception exception)
		{
			return -1;
		}
	}
	
	@Override
	public int getFireEncouragement(World world, BlockPos pos)
	{
		return getFireSpreadSpeed(world, pos, null);
	}
}
