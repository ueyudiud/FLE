/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.cover;

import farcore.lib.tile.IPlateCoverableTile;
import nebula.common.util.Direction;

/**
 * @author ueyudiud
 */
public interface ICoverable
{
	ICover createNewCover(IPlateCoverableTile tile, Direction side);
}