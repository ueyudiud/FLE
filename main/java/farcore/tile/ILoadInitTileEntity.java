package farcore.tile;

import java.io.IOException;

import farcore.util.io.FleDataInputStream;
import farcore.util.io.FleDataOutputStream;

public interface ILoadInitTileEntity
{
	void sendData(FleDataOutputStream stream) throws IOException;
	
	void receiveData(FleDataInputStream stream) throws IOException;
}