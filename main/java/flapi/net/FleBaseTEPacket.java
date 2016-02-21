package flapi.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraftforge.common.util.ForgeDirection;
import farcore.block.TEBase;
import flapi.util.io.FleDataInputStream;
import flapi.util.io.FleDataOutputStream;
import flapi.world.BlockPos;
import flapi.world.ITEInWorld;

public class FleBaseTEPacket extends FleCoordinatesPacket
{
	private byte[] arrays;
	
	public FleBaseTEPacket()
	{
		super(true);
	}
	public FleBaseTEPacket(ITEInWorld world, int type, int value)
	{
		super(true);
		arrays = new byte[]{(byte) type, (byte) value};
	}
	public FleBaseTEPacket(ITEInWorld world, ForgeDirection dir)
	{
		super(true, world.getBlockPos());
		arrays = new byte[]{0, (byte) dir.ordinal()};
	}
	public FleBaseTEPacket(ITEInWorld world, int[] colourState)
	{
		super(true);
		arrays = 
				new byte[]
						{1, 
				(byte) (colourState[0] >> 24), (byte) ((colourState[0] >> 16) & 0xFF), (byte) ((colourState[0] >> 8) & 0xFF), (byte) ((colourState[0]) & 0xFF),
				(byte) (colourState[1] >> 24), (byte) ((colourState[1] >> 16) & 0xFF), (byte) ((colourState[1] >> 8) & 0xFF), (byte) ((colourState[1]) & 0xFF),
				(byte) (colourState[2] >> 24), (byte) ((colourState[2] >> 16) & 0xFF), (byte) ((colourState[2] >> 8) & 0xFF), (byte) ((colourState[2]) & 0xFF),
				(byte) (colourState[3] >> 24), (byte) ((colourState[3] >> 16) & 0xFF), (byte) ((colourState[3] >> 8) & 0xFF), (byte) ((colourState[3]) & 0xFF),
				(byte) (colourState[4] >> 24), (byte) ((colourState[4] >> 16) & 0xFF), (byte) ((colourState[4] >> 8) & 0xFF), (byte) ((colourState[4]) & 0xFF),
				(byte) (colourState[5] >> 24), (byte) ((colourState[5] >> 16) & 0xFF), (byte) ((colourState[5] >> 8) & 0xFF), (byte) ((colourState[5]) & 0xFF),
				};
	}
	public FleBaseTEPacket(ITEInWorld world, ForgeDirection dir, int lightValue, int[] colourState)
	{
		super(true, world.getBlockPos());
		ByteArrayOutputStream s;
		DataOutputStream stream = new DataOutputStream(s = new ByteArrayOutputStream());
		try
		{
			stream.writeByte(-1);
			stream.writeByte(dir.ordinal());
			for(int i = 0; i < 6; ++i)
				stream.writeInt(colourState[i]);
			arrays = s.toByteArray();
		}
		catch(IOException e){}
	}
	
	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		super.write(os);
		os.writeBytes(arrays);
	}
	
	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		super.read(is);
		arrays = is.readBytes();
	}

	@Override
	public Object process(FleNetworkHandler nwh)
	{
		BlockPos pos = pos();
		if(pos.getBlockTile() instanceof TEBase)
		{
			switch(arrays[0])
			{
			case -1 :
			{
				DataInputStream stream = new DataInputStream(new ByteArrayInputStream(arrays));
				try
				{
					stream.readByte();
					((TEBase) pos.getBlockTile()).setDirction(ForgeDirection.values()[stream.readByte()]);
					((TEBase) pos.getBlockTile()).setLightOpacity(stream.readByte());
					int[] colours = new int[6];
					for(int i = 0; i < 6; ++i)
						colours[i] = stream.readInt();
					((TEBase) pos.getBlockTile()).setColorState(colours);
				}catch(IOException e){}
			}
			break;
			case 0 : ((TEBase) pos.getBlockTile()).setDirction(ForgeDirection.values()[arrays[1]]);
			break;
			case 1 :
			int 
			a = arrays[1 ] << 24 | arrays[2 ] << 16 | arrays[3 ] << 8 | arrays[4 ], 
			b = arrays[5 ] << 24 | arrays[6 ] << 16 | arrays[7 ] << 8 | arrays[8 ], 
			c = arrays[9 ] << 24 | arrays[10] << 16 | arrays[11] << 8 | arrays[12], 
			d = arrays[13] << 24 | arrays[14] << 16 | arrays[15] << 8 | arrays[16], 
			e = arrays[17] << 24 | arrays[18] << 16 | arrays[19] << 8 | arrays[20], 
			f = arrays[21] << 24 | arrays[22] << 16 | arrays[23] << 8 | arrays[24];
			((TEBase) pos.getBlockTile()).setColorState(new int[]{a, b, c, d, e, f});
			break;
			case 2 : ((TEBase) pos.getBlockTile()).setLightOpacity(arrays[1]);
			break;
			}
		}
		return null;
	}
}