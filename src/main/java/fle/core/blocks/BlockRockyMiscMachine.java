/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.blocks;

import static farcore.FarCoreRegistry.registerTESR;

import farcore.data.Materials;
import fle.core.FLE;
import fle.core.client.render.ItemRenderWithTESR;
import fle.core.client.render.TESRSimplyKiln;
import fle.core.tile.TEPlatedArgil;
import fle.core.tile.rocky.TECeramicPot;
import fle.core.tile.rocky.TESimplyKiln;
import nebula.base.register.IRegister;
import nebula.client.NebulaRenderHandler;
import nebula.client.model.StateMapperExt;
import nebula.common.LanguageManager;
import nebula.common.block.BlockTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockRockyMiscMachine extends BlockTE
{
	public BlockRockyMiscMachine()
	{
		super(FLE.MODID, "rocky.misc.machine", Materials.ROCK);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), "Ceramic Pot");
		LanguageManager.registerLocal(getTranslateNameForItemStack(1), "Simply Kiln");
		LanguageManager.registerLocal(getTranslateNameForItemStack(2), "Argil Plates");
	}
	
	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register(0, "ceramic_pot", TECeramicPot.class);
		register.register(1, "simply_klin", TESimplyKiln.class);
		register.register(2, "plated_argil", TEPlatedArgil.class);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		NebulaRenderHandler.registerRender(this.item, ItemRenderWithTESR.INSTANCE);
		
		registerTESR(TESRSimplyKiln.class, this.item, 1);
		
		StateMapperExt mapper = new StateMapperExt(FLE.MODID, "misc_machine", this.property_TE);
		registerRenderMapper(mapper);
		registerCustomBlockRender(mapper, 0, "misc_machine/ceramic_pot");
		registerCustomBlockRender(mapper, 1, "misc_machine/simply_kiln");
		registerCustomBlockRender(mapper, 2, "misc_machine/argil_plates");
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
}
