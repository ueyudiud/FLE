/* 
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.network;

import java.io.IOException;

/**
 * @author ueyudiud
 */
public interface Encoder
{
	void encode(PacketBufferExt output) throws IOException;
}
