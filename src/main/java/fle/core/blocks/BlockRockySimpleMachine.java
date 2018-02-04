/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.blocks;

import farcore.data.Materials;
import fle.core.FLE;
import fle.core.tile.rocky.TEHearth;
import nebula.base.register.IRegister;
import nebula.client.model.StateMapperExt;
import nebula.common.LanguageManager;
import nebula.common.block.BlockTE;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockRockySimpleMachine extends BlockTE
{
	public BlockRockySimpleMachine()
	{
		super(FLE.MODID, "rocky.simple.machine", Materials.ROCK);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), "Hearth");
		
		LanguageManager.registerLocal("inventory.hearth", "Hearth");
	}
	
	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register(0, "hearth", TEHearth.class);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		StateMapperExt mapper = new StateMapperExt(FLE.MODID, "misc_machine", this.property_TE);
		registerRenderMapper(mapper);
	}
}
