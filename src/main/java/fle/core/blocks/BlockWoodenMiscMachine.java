/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.blocks;

import farcore.data.Materials;
import fle.core.FLE;
import fle.core.tile.wooden.TEDryingTable;
import nebula.client.ClientProxy;
import nebula.client.model.StateMapperExt;
import nebula.common.LanguageManager;
import nebula.common.base.IRegister;
import nebula.common.block.BlockTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockWoodenMiscMachine extends BlockTE
{
	public BlockWoodenMiscMachine()
	{
		super(FLE.MODID, "wooden.misc.machine", Materials.WOOD);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		LanguageManager.registerLocal(getTranslateNameForItemStack(0), "Drying Table");
		
		LanguageManager.registerLocal("inventory.drying.table", "Drying Table");
	}
	
	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		register.register(0, "dryinging_table", TEDryingTable.class);
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		StateMapperExt mapper = new StateMapperExt(FLE.MODID, "misc_machine", this.property_TE);
		ClientProxy.registerCompactModel(mapper, this, this.property_TE);
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