/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.world;

import farcore.lib.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;

/**
 * @author ueyudiud
 */
public interface IModifiableCoord extends ICoord
{
	/**
	 * Mark tile entity in this coordinate is
	 * required refreshing saved NBT.
	 */
	void markDirty();

	default boolean setBlockState(IBlockState state, int flag)
	{
		return world().setBlockState(pos(), state, flag);
	}
	
	default boolean setBlockState(Direction offset, IBlockState state, int flag)
	{
		return world().setBlockState(offset.offset(pos()), state, flag);
	}
	
	default boolean setBlockState(int x, int y, int z, IBlockState state, int flag)
	{
		return world().setBlockState(pos().add(x, y, z), state, flag);
	}
	
	default boolean removeBlock()
	{
		return world().setBlockToAir(pos());
	}
	
	default boolean removeBlock(Direction offset)
	{
		return world().setBlockToAir(offset.offset(pos()));
	}
	
	default boolean removeBlock(int x, int y, int z)
	{
		return world().setBlockToAir(pos().add(x, y, z));
	}
	
	default void setTileEntity(TileEntity tile)
	{
		world().setTileEntity(pos(), tile);
	}
	
	default void setTileEntity(TileEntity tile, Direction offset)
	{
		world().setTileEntity(offset.offset(pos()), tile);
	}
	
	default void setTileEntity(TileEntity tile, int x, int y, int z)
	{
		world().setTileEntity(pos().add(x, y, z), tile);
	}

	void markBlockUpdate();
	
	void markBlockRenderUpdate();
	
	/**
	 * Mark light for update.
	 * Usually used when machine state changed,
	 * example : furnace start burning fuel.
	 * Marked to recalculate light.
	 * @param type
	 */
	void markLightForUpdate(EnumSkyBlock type);

	void explode(boolean removeTile, float strength, boolean isFlaming, boolean isSmoking);
}