package farcore.interfaces.energy.electric;

import farcore.energy.electric.ElectricalPkt;
import farcore.enums.Direction;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;

public interface IElectricTile
{
	Nodable[] getNodes(Direction direction);
	
	void electricPreUpdate();
	  
	void electricUpdate(Direction direction, int channel, ElectricalPkt pkt);
	  
	public static interface Nodable
	{		
		Direction direction();
		
		int channel();
		
		/**
		 * Get node voltage.
		 * @return
		 */
		float voltage();
		
		/**
		 * Get all inner links.
		 */
		Linkable[] getInnerLinks();

		/**
		 * Get all access link path.
		 * Contain:
		 * {xOffset, yOffset, zOffset, direction, accessChannels...}<br>
		 * If access channel with no channel, this channel will connect to
		 * all possible channel.
		 * @return
		 */
		int[][] getAccessOffsetLinkPath();
		
		boolean canLinkWith(IElectricTile tile, Nodable nodable);
		
		/**
		 * Get resistance to another node out of this tile.
		 * @param tile The target tile.
		 * @param nodable The target node.
		 * @return
		 */
		float resistanceToOther(IElectricTile tile, Nodable nodable);
	}
	
	public static interface Linkable
	{
		/**
		 * Get one node.
		 * @return
		 */
		Nodable getThis();
		
		/**
		 * Get another node.
		 * @return
		 */
		Nodable getThat();
		
		/**
		 * Get link resistance.
		 * @return
		 */
		float resistance();
	}
}