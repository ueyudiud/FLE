/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.api.tile;

import nebula.common.util.Direction;
import net.minecraftforge.fluids.FluidStack;

/**
 * Use for tile entity for collect production from tree log (Rubber, Lacquer,
 * etc).
 * 
 * @author ueyudiud
 */
public interface ILogProductionCollector
{
	/**
	 * @param direction the resource from direction.
	 * @param stack the product.
	 * @return <tt>true</tt> for production already input by collector.
	 */
	boolean collectLogProductFrom(Direction direction, FluidStack stack);
}
