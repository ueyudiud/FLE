package fle.api.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface IBlockWithTileBehaviour<E extends BlockHasTile> extends IBlockBehaviour<E>
{
	TileEntity createNewTileEntity(E block, World aWorld, int aMeta);
}
