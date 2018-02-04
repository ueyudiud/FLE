/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.gui;

import java.io.IOException;

import nebula.common.network.PacketBufferExt;

/**
 * @author ueyudiud
 */
public interface IGuiDataReciever
{
	void readData(PacketBufferExt buf) throws IOException;
}
