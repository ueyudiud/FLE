/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.solid.container;

import javax.annotation.Nullable;

import farcore.lib.solid.SolidStack;
import nebula.common.util.Direction;

/**
 * @author ueyudiud
 */
public interface ISolidHandlerIO
{
	/**
	 * Match side can extract solid.
	 * @return
	 */
	boolean canExtractSolid(Direction to);
	
	/**
	 * Match side can insert solid.
	 * @param stack For asked stack, null for ask general behavior.
	 * @return
	 */
	boolean canInsertSolid(Direction from, @Nullable SolidStack stack);
	
	SolidStack extractSolid(int amount, Direction to, boolean simulate);
	
	SolidStack extractSolid(SolidStack suggested, Direction to, boolean simulate);
	
	int insertSolid(SolidStack stack, Direction from, boolean simulate);
}