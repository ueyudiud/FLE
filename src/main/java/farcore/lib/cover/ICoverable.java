package farcore.lib.cover;

import farcore.lib.tile.IPlateCoverableTile;
import farcore.lib.util.Direction;

public interface ICoverable
{
	ICover createNewCover(IPlateCoverableTile tile, Direction side);
}