/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.energy.kinetic;

import farcore.FarCoreRegistry;
import nebula.common.block.BlockSingleTE;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class BlockKineticDebug extends BlockSingleTE
{
	public BlockKineticDebug()
	{
		super("kinetic.debug", Material.ROCK);
	}
	
	@Override
	public void postInitalizedBlocks()
	{
		super.postInitalizedBlocks();
		FarCoreRegistry.registerTileEntity("debug.kinetic", TEKineticDebuger.class);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TEKineticDebuger();
	}
}