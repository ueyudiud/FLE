/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.solid.container;

import javax.annotation.Nullable;

import farcore.lib.solid.SolidStack;
import nebula.common.util.Direction;

/**
 * Solid container (tile entity predicated) handler.<p>
 * Use to drain or fill solid from player.
 * @author ueyudiud
 */
public interface ISolidHandlerIO
{
	/**
	 * Match side can extract solid.
	 * 
	 * @param to the side to extract.
	 * @return <code>true</code> if solid can extract from side.
	 */
	boolean canExtractSolid(Direction to);
	
	/**
	 * Match side can insert solid.
	 * 
	 * @param from the side to insert.
	 * @param stack For asked stack, null for ask general behavior.
	 * @return
	 */
	boolean canInsertSolid(Direction from, @Nullable SolidStack stack);
	
	SolidStack extractSolid(int amount, Direction to, boolean simulate);
	
	SolidStack extractSolid(SolidStack suggested, Direction to, boolean simulate);
	
	int insertSolid(SolidStack stack, Direction from, boolean simulate);
}
