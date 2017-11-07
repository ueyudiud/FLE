/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.energy.thermal.instance;

import nebula.base.IRegister;
import nebula.common.block.BlockTE;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

/**
 * @author ueyudiud
 */
public class BlockThermalHelper extends BlockTE
{
	public BlockThermalHelper(String name, Material materialIn)
	{
		super(name, materialIn);
	}
	
	@Override
	protected boolean registerTileEntities(IRegister<Class<? extends TileEntity>> register)
	{
		return false;
	}
}
