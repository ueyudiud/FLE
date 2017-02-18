/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.blocks;

import farcore.data.Materials;
import fle.core.FLE;
import fle.core.tile.wooden.workbench.TEWoodenPolishTable;
import nebula.client.ClientProxy;
import nebula.client.model.StateMapperExt;
import nebula.common.LanguageManager;
import nebula.common.base.IRegister;
import nebula.common.block.BlockTE;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockWoodenSimpleWorkbench extends BlockTE
{
	public BlockWoodenSimpleWorkbench()
	{
		super(FLE.MODID, "wooden.simple.workbench", Materials.WOOD);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), "Polishing Table");
		
		LanguageManager.registerLocal("inventory.polishing.table", "Polishing Table");
	}
	
	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register(0, "polishing_table", TEWoodenPolishTable.class);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		StateMapperExt mapper = new StateMapperExt(FLE.MODID, "simple_workbench", this.property_TE);
		ClientProxy.registerCompactModel(mapper, this, this.property_TE);
	}
}