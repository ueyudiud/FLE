/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.inventory;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import farcore.lib.solid.SolidStack;
import nebula.common.nbt.INBTSelfCompoundReaderAndWriter;

/**
 * @author ueyudiud
 */
public interface ISolidHandler extends INBTSelfCompoundReaderAndWriter
{
	/**
	 * Get solid handler property.
	 * @return the property
	 */
	@Nonnull ISoildHandlerProperty getProperty();
	
	/**
	 * Take the <tt>fill</tt> action.
	 * <p>
	 * Fill solid stack to handler. The detail behavior of how solid
	 * tank handle is unknown, you should consider of any happened
	 * after fill action took.
	 * 
	 * @param source the source of solid stack, it will not changed
	 *               in this method.
	 * @param doFill the <tt>fill</tt> action will be invoke virtual
	 *               if <code>false</code> is input.
	 * @return the amount actually fill in. If <tt>doFill</tt> is
	 *         <code>false</code>, the result is only the predicated
	 *         if <tt>fill</tt> action really took.
	 */
	@Nonnegative int fill(SolidStack source, boolean doFill);
	
	/**
	 * Take the <tt>drain</tt> action.
	 * <p>
	 * Drain solid stack from handler. The detail behavior of how solid
	 * tank handle is unknown, you should consider of any happened
	 * after drain action took.
	 * 
	 * @param maxDrain the max drain amount.
	 * @param doDrain the <tt>drain</tt> action will be invoke virtual
	 *                if <code>false</code> is input.
	 * @return the drained stack. If <tt>doDrain</tt> is <code>false</code>,
	 *         the result is only the predicated if <tt>drain</tt> action
	 *         really took.
	 */
	@Nullable SolidStack drain(@Nonnegative int maxDrain, boolean doDrain);
	
	/**
	 * Take the <tt>drain</tt> action.
	 * <p>
	 * Drain solid stack from handler. The detail behavior of how solid
	 * tank handle is unknown, you should consider of any happened
	 * after drain action took.
	 * 
	 * @param source the specific type of solid stack required. The max output
	 *               amount is the source amount.
	 * @param doDrain the <tt>drain</tt> action will be invoke virtual
	 *                if <code>false</code> is input.
	 * @return the drained stack. If <tt>doDrain</tt> is <code>false</code>,
	 *         the result is only the predicated if <tt>drain</tt> action
	 *         really took.
	 */
	@Nullable SolidStack drain(SolidStack source, boolean doDrain);
	
	/**
	 * The solid handler properties.
	 * 
	 * @author ueyudiud
	 */
	interface ISoildHandlerProperty
	{
		@Nonnull SolidStack[] getContents();
		
		int getCapacity();
		
		boolean canFill();
		
		boolean canDrain();
		
		boolean canFill(@Nullable SolidStack stack);
		
		boolean canDrain(@Nullable SolidStack stack);
	}
}