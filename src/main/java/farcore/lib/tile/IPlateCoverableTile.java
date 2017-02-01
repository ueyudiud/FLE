package farcore.lib.tile;

import farcore.lib.cover.ICover;
import farcore.lib.cover.ICoverable;
import nebula.common.util.Direction;

public interface IPlateCoverableTile
{
	boolean canCoverOn(Direction direction, ICoverable coverable);

	void coverOn(Direction direction, ICoverable coverable);
	
	ICover removeCover(Direction direction);
}