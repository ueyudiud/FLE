/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.tile;

import java.io.IOException;

import nebula.common.network.PacketBufferExt;
import nebula.common.world.ICoord;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public interface INetworkedSyncTile extends ICoord
{
	void writeNetworkData(int type, PacketBufferExt buf) throws IOException;
	
	@SideOnly(Side.CLIENT)
	void readNetworkData(int type, PacketBufferExt buf) throws IOException;
}
