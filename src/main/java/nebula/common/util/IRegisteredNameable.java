/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.util;

import javax.annotation.Nonnull;

/**
 * The object which has named in a register.
 * <p>
 * Use to get registered name, and split different element contain in a
 * register.
 * 
 * @author ueyudiud
 * @see nebula.common.item.ItemBase
 * @see nebula.common.block.BlockBase
 */
public interface IRegisteredNameable
{
	/**
	 * Get registered name.
	 * 
	 * @return
	 */
	@Nonnull String getRegisteredName();
}
