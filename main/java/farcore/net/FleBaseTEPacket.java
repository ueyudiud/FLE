package farcore.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import farcore.tileentity.TEBase;
import farcore.util.Direction;
import farcore.world.IObjectInWorld;

public class FleBaseTEPacket extends FleCoordinatesPacket
{
	private static final byte ID_ALL = -1;
	private static final byte ID_DIRECTION_UPDATE = 0;
	private static final byte ID_COLOUR_UPDATE = 1;
	private byte[] arrays;
	
	public FleBaseTEPacket()
	{
		super();
	}
	public FleBaseTEPacket(IObjectInWorld world, int type, int value)
	{
		super(world);
		arrays = new byte[]{(byte) type, (byte) value};
	}
	public FleBaseTEPacket(IObjectInWorld world, Direction dir)
	{
		super(world);
		arrays = new byte[]{ID_DIRECTION_UPDATE, (byte) dir.ordinal()};
	}
	public FleBaseTEPacket(IObjectInWorld world, int[] colourState)
	{
		super(world);
		arrays = 
				new byte[]
						{ID_COLOUR_UPDATE, 
				(byte) (colourState[0] >> 24), (byte) ((colourState[0] >> 16) & 0xFF), (byte) ((colourState[0] >> 8) & 0xFF), (byte) ((colourState[0]) & 0xFF),
				(byte) (colourState[1] >> 24), (byte) ((colourState[1] >> 16) & 0xFF), (byte) ((colourState[1] >> 8) & 0xFF), (byte) ((colourState[1]) & 0xFF),
				(byte) (colourState[2] >> 24), (byte) ((colourState[2] >> 16) & 0xFF), (byte) ((colourState[2] >> 8) & 0xFF), (byte) ((colourState[2]) & 0xFF),
				(byte) (colourState[3] >> 24), (byte) ((colourState[3] >> 16) & 0xFF), (byte) ((colourState[3] >> 8) & 0xFF), (byte) ((colourState[3]) & 0xFF),
				(byte) (colourState[4] >> 24), (byte) ((colourState[4] >> 16) & 0xFF), (byte) ((colourState[4] >> 8) & 0xFF), (byte) ((colourState[4]) & 0xFF),
				(byte) (colourState[5] >> 24), (byte) ((colourState[5] >> 16) & 0xFF), (byte) ((colourState[5] >> 8) & 0xFF), (byte) ((colourState[5]) & 0xFF),
				};
	}
	public FleBaseTEPacket(IObjectInWorld world, Direction dir, int lightValue, int[] colourState)
	{
		super(world);
		ByteArrayOutputStream s;
		DataOutputStream stream = new DataOutputStream(s = new ByteArrayOutputStream());
		try
		{
			stream.writeByte(ID_ALL);
			stream.writeByte(dir.ordinal());
			for(int i = 0; i < 6; ++i)
				stream.writeInt(colourState[i]);
			arrays = s.toByteArray();
		}
		catch(IOException e){}
	}
	
	@Override
	protected void write(FlePacketBuffer os) throws IOException
	{
		super.write(os);
		os.writeByteArray(arrays);
	}
	
	@Override
	protected void read(FlePacketBuffer is) throws IOException
	{
		super.read(is);
		arrays = is.readByteArray();
	}

	@Override
	public IPacket process(INetworkHandler handler)
	{
		if(world().getTileEntity(pos) instanceof TEBase)
		{
			TEBase tile = (TEBase) world().getTileEntity(pos);
			switch(arrays[0])
			{
			case -1 :
			{
				DataInputStream stream = new DataInputStream(new ByteArrayInputStream(arrays));
				try
				{
					stream.readByte();
					tile.setDirction(Direction.values()[stream.readByte()]);
					tile.setLightValue(stream.readByte());
					int[] colours = new int[6];
					for(int i = 0; i < 6; ++i)
						colours[i] = stream.readInt();
					tile.setColorState(colours);
				}catch(IOException e){}
			}
			break;
			case 0 : tile.setDirction(Direction.values()[arrays[1]]);
			break;
			case 1 :
			int 
			a = arrays[1 ] << 24 | arrays[2 ] << 16 | arrays[3 ] << 8 | arrays[4 ], 
			b = arrays[5 ] << 24 | arrays[6 ] << 16 | arrays[7 ] << 8 | arrays[8 ], 
			c = arrays[9 ] << 24 | arrays[10] << 16 | arrays[11] << 8 | arrays[12], 
			d = arrays[13] << 24 | arrays[14] << 16 | arrays[15] << 8 | arrays[16], 
			e = arrays[17] << 24 | arrays[18] << 16 | arrays[19] << 8 | arrays[20], 
			f = arrays[21] << 24 | arrays[22] << 16 | arrays[23] << 8 | arrays[24];
			tile.setColorState(new int[]{a, b, c, d, e, f});
			break;
			case 2 : tile.setLightValue(arrays[1]);
			break;
			}
		}
		return null;
	}
}