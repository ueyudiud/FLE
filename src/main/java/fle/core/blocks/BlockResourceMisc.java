/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.blocks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import farcore.data.M;
import farcore.data.Materials;
import farcore.data.SubTags;
import farcore.lib.material.Mat;
import fle.core.FLE;
import fle.core.tile.TEFirewood;
import nebula.base.IRegister;
import nebula.client.blockstate.BlockStateTileEntityWapper;
import nebula.client.model.StateMapperExt;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.common.LanguageManager;
import nebula.common.block.BlockTE;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockResourceMisc extends BlockTE
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
		
		NebulaModelLoader.registerBlockMetaGenerator(new ResourceLocation(FLE.MODID, "firewood/states"),
				state-> {
					TEFirewood tile = BlockStateTileEntityWapper.unwrap(state);
					return tile == null ? M.oak.name : tile.isCarbonate() ? "charcoal" : tile.getMaterial().name;
				});
		NebulaModelLoader.registerTextureSet(new ResourceLocation(FLE.MODID, "firewood/side_base"),
				()-> Maps.toMap(Lists.transform(Mat.filt(SubTags.WOOD), m->m.name), name->new ResourceLocation(FLE.MODID, "blocks/firewood/" + name + "_side")));
		NebulaModelLoader.registerTextureSet(new ResourceLocation(FLE.MODID, "firewood/top_base"),
				()-> Maps.toMap(Lists.transform(Mat.filt(SubTags.WOOD), m->m.name), name->new ResourceLocation(FLE.MODID, "blocks/firewood/" + name + "_top")));
	}
	
	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register(0, "firewood", TEFirewood.class);
		return true;
	}
}