package farcore.lib.cover;

import farcore.lib.tile.IPlateCoverableTile;
import nebula.common.util.Direction;

public interface ICoverable
{
	ICover createNewCover(IPlateCoverableTile tile, Direction side);
}