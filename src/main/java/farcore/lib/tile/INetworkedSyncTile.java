/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.tile;

import java.io.IOException;

import farcore.lib.world.ICoord;
import farcore.network.PacketBufferExt;
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
