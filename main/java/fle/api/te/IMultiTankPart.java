package fle.api.te;

import net.minecraft.tileentity.TileEntity;

public interface IMultiTankPart
{
	/**
	 * Get whether this tile is connected.
	 * @return
	 */
	boolean isConnected();
	
	/**
	 * Check if this tile can connect with another tile, use <br>
	 * <code>
	 * if(!canConnectWith(tile, i, j, k, bSize, ySize))<br>
	 * {<br>
	 * return false;<br>
	 * }<br></code>
	 * to check whether can make a fluid tank.
	 * This method also check this part can put in tank with an right
	 * position (eg: Valve can't put on edge of tank).
	 * @see fle.api.te.TileEntityAbstractTank
	 * @param main The main control check tile.
	 * @param xPos The x offset of this tile.
	 * @param yPos The y offset of this tile.
	 * @param zPos The z offset of this tile.
	 * @param width The width of target tank.
	 * @param height The height of target tank.
	 * @return Can this tile be a part in a new tank.
	 */
	boolean canBeConnect(TileEntityAbstractTank main, int xPos, int yPos, int zPos, int width, int height);
	
	/**
	 * Check this tile can connect with another tile.
	 * This method needn't check whether this tile is be at an
	 * right position.
	 * @see fle.api.te.TileEntityAbstractTank
	 * @param main The main control check tile.
	 * @return Can this tile be a part in a new tank.
	 */
	boolean canBeConnect(TileEntityAbstractTank main);

	/**
	 * Mark this tile in a new tank.
	 * This tile should get the tank from main tile.
	 * @param main The main control tile.
	 * @param xPos
	 * @param yPos
	 * @param zPos
	 * @param width
	 * @param height
	 */
	void onConnect(TileEntityAbstractTank main, int xPos, int yPos, int zPos, int width, int height);

	/**
	 * Mark this tile on tank is break by player, explosive, etc.
	 * Reset tank from main tile.
	 * @param main The post main control of this tile.
	 */
	void onDisconnect(TileEntityAbstractTank main);
}