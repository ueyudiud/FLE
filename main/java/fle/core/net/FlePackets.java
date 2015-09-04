package fle.core.net;

import java.io.IOException;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fle.FLE;
import fle.api.net.FleAbstractPacket;
import fle.api.util.FleDataInputStream;
import fle.api.util.FleDataOutputStream;
import fle.api.util.FleLog;
import fle.api.world.BlockPos;
import fle.core.te.TileEntityCrop;

public class FlePackets
{
	public static class CoderKeyType extends FleAbstractPacket<CoderKeyType>
	{
		int keys;
		
		public CoderKeyType() 
		{
			
		}
		public CoderKeyType(int key) 
		{
			keys = key;
		}
		
		@Override
		protected void write(FleDataOutputStream os) throws IOException
		{
			os.writeInt(keys);
		}
		
		@Override
		protected void read(FleDataInputStream is) throws IOException
		{
			keys = is.readInt();	
		}
		
		@Override
		public IMessage onMessage(CoderKeyType message, MessageContext ctx)
		{
			FLE.fle.getKeyboard().processKeyUpdate(ctx.getServerHandler().playerEntity, message.keys);
			return null;
		}
	}

	public static class CoderCropUpdate extends FleAbstractPacket<CoderCropUpdate>
	{
		private BlockPos pos;
		private int a;
		private double b;
		private int c;
		private String crop;
		
		public CoderCropUpdate() {}
		
		public CoderCropUpdate(TileEntityCrop tile) 
		{
			pos = tile.getBlockPos();
			a = tile.getCropAge();
			b = tile.getCropBuf();
			c = tile.getCropCushion();
			crop = tile.getCrop().getCropName();
		}

		@Override
		protected void write(FleDataOutputStream os) throws IOException
		{
			os.writeBlockPos(pos);
			os.writeInt(a);
			os.writeDouble(b);
			os.writeInt(c);
			os.writeString(crop);
		}

		@Override
		protected void read(FleDataInputStream is) throws IOException
		{
			pos = is.readBlockPos();
			a = is.readInt();
			b = is.readDouble();
			c = is.readInt();
			crop = is.readString();
		}

		@Override
		public IMessage onMessage(CoderCropUpdate message, MessageContext ctx)
		{
			BlockPos pos = message.pos;
			if(pos.getBlockTile() instanceof TileEntityCrop)
			{
				TileEntityCrop tile = (TileEntityCrop) pos.getBlockTile();
				tile.setupCrop(FLE.fle.getCropRegister().getCropFromName(message.crop));
				tile.setCropAge(message.a);
				tile.setCropBuf(message.b);
				tile.setCropCushion(message.c);
				if(tile.getWorldObj() != null)
					tile.getWorldObj().markBlockRangeForRenderUpdate(pos.x - 1, pos.y - 1, pos.z - 1, pos.x + 1, pos.y + 1, pos.z + 1);
			}
			return null;
		}
	}

	public static class CoderFWMAskMeta extends FleAbstractPacket<CoderFWMAskMeta>
	{		
		BlockPos pos;
		
		public CoderFWMAskMeta()
		{
			
		}
		public CoderFWMAskMeta(BlockPos aPos)
		{
			pos = aPos;
		}
		
		@Override
		protected void write(FleDataOutputStream os) throws IOException
		{
			os.writeBlockPos(pos);
		}

		@Override
		protected void read(FleDataInputStream is) throws IOException
		{
			pos = is.readBlockPos();
		}

		@Override
		public IMessage onMessage(CoderFWMAskMeta message, MessageContext ctx)
		{
			FLE.fle.getWorldManager().markPosForUpdate(message.pos);
			return null;
		}
	}
}