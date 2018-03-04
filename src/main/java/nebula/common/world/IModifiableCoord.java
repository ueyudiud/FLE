/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.world;

import nebula.common.data.Misc;
import nebula.common.util.Direction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;

/**
 * The <tt>modifiable</tt> coordinate helper, which can modify block state in
 * world.
 * 
 * @author ueyudiud
 */
public interface IModifiableCoord extends ICoord
{
	@Override
	default IModifiableCoord offset(Direction offset)
	{
		return offset(offset.x, offset.y, offset.z);
	}
	
	@Override
	default IModifiableCoord offset(int x, int y, int z)
	{
		return new WorldCoord1(this, pos().add(x, y, z));
	}
	
	/**
	 * Mark tile entity in this coordinate is required refreshing saved NBT.
	 */
	default void markDirty()
	{
		;
	}
	
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
	
	default boolean setBlockState(BlockPos pos, IBlockState state, int flag)
	{
		return world().setBlockState(pos, state, flag);
	}
	
	default boolean removeBlock()
	{
		return setBlockState(Misc.AIR, 3);
	}
	
	default boolean removeBlock(Direction offset)
	{
		return setBlockState(offset, Misc.AIR, 3);
	}
	
	default boolean removeBlock(int x, int y, int z)
	{
		return setBlockState(x, y, z, Misc.AIR, 3);
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
	
	default void markBlockUpdate()
	{
		IBlockState state = getBlockState();
		world().markAndNotifyBlock(pos(), world().getChunkFromBlockCoords(pos()), state, state, 3);
	}
	
	default void markBlockRenderUpdate()
	{
		world().markBlockRangeForRenderUpdate(pos().add(-1, -1, -1), pos().add(1, 1, 1));
	}
	
	/**
	 * Mark light for update. Usually used when machine state changed, example :
	 * furnace start burning fuel. Marked to recalculate light.
	 * 
	 * @param type
	 */
	default void markLightForUpdate(EnumSkyBlock type)
	{
		world().checkLightFor(type, pos());
	}
	
	default void explode(boolean removeTile, float strength, boolean isFlaming, boolean isSmoking)
	{
		if (removeTile) world().removeTileEntity(pos());
		
		world().newExplosion(null, pos().getX() + .5, pos().getY() + .5, pos().getZ() + .5, strength, isFlaming, isSmoking);
	}
}
