/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.blocks;

import farcore.data.Materials;
import fle.core.FLE;
import fle.core.tile.pottery.TETerrine;
import nebula.base.IRegister;
import nebula.client.model.StateMapperExt;
import nebula.client.util.Client;
import nebula.common.LanguageManager;
import nebula.common.block.BlockTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockPottery extends BlockTE
{
	public BlockPottery()
	{
		super(FLE.MODID, "pottery", Materials.POTTERY);
	}
	
	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register(0, "terrine", TETerrine.class);
		return true;
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), "Terrine");
		LanguageManager.registerLocal("inventory.terrine", "Terrine");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		StateMapperExt mapper = new StateMapperExt(FLE.MODID, "pottery", this.property_TE);
		Client.registerModel(this.item, 0, FLE.MODID, "pottery/terrine");
		ModelLoader.setCustomStateMapper(this, mapper);
		registerCustomBlockRender(mapper, 0, "pottery/terrine");
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