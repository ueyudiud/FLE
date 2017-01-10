/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.net.tile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import farcore.lib.net.PacketChunkCoord;
import farcore.lib.tile.INetworkedSyncTile;
import farcore.network.IPacket;
import farcore.network.Network;
import farcore.network.PacketBufferExt;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;

/**
 * @author ueyudiud
 */
public class PacketChunkNetData extends PacketChunkCoord
{
	int mark;
	Map<BlockPos, byte[]> datas;
	Chunk chunk;
	List<BlockPos> pos;
	
	public PacketChunkNetData()
	{
		
	}
	public PacketChunkNetData(int mark, Chunk chunk, List<BlockPos> pos)
	{
		super(chunk.getWorld(), chunk.xPosition, chunk.zPosition);
		this.mark = mark;
		this.chunk = chunk;
		this.pos = pos;
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		try
		{
			this.datas = new HashMap(this.pos.size(), 1.00F);
			ByteBuf buf;
			for(BlockPos pos : this.pos)
			{
				TileEntity tile = this.chunk.getTileEntity(pos, EnumCreateEntityType.CHECK);
				if(tile instanceof INetworkedSyncTile)
				{
					buf = Unpooled.buffer();
					((INetworkedSyncTile) tile).writeNetworkData(this.mark, new PacketBufferExt(buf));
					byte[] readable = new byte[buf.readableBytes()];
					buf.readBytes(readable);
					this.datas.put(pos, readable);
				}
			}
		}
		catch (IOException exception)
		{
			throw exception;
		}
		catch (Exception exception)
		{
			throw new IOException(exception);
		}
		super.encode(output);
		output.writeShort(this.mark);
		output.writeShort((this.datas.size() & 0xFFFF));
		for(Entry<BlockPos, byte[]> entry : this.datas.entrySet())
		{
			BlockPos pos = entry.getKey();
			byte[] value = entry.getValue();
			output.writeBlockPos(pos);
			output.writeInt(value.length);
			output.writeBytes(value);
		}
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		super.decode(input);
		this.mark = input.readShort();
		this.datas = new HashMap();
		int len = (input.readShort() & 0xFFFF);
		for(int i = 0; i < len; ++i)
		{
			BlockPos pos = input.readBlockPos();
			byte[] array = new byte[input.readInt()];
			input.readBytes(array);
			this.datas.put(pos, array);
		}
	}
	
	@Override
	public IPacket process(Network network) throws IOException
	{
		World world = world();
		Chunk chunk = world.getChunkFromChunkCoords(this.x, this.z);
		for(Entry<BlockPos, byte[]> entry : this.datas.entrySet())
		{
			TileEntity tile = world.getTileEntity(entry.getKey());
			if(tile instanceof INetworkedSyncTile)
			{
				((INetworkedSyncTile) tile).readNetworkData(this.mark, new PacketBufferExt(Unpooled.wrappedBuffer(entry.getValue())));
			}
		}
		return null;
	}
}