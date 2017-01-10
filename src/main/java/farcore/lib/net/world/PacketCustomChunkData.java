/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.net.world;

import java.io.IOException;
import java.util.Arrays;

import farcore.lib.block.IExtendedDataBlock;
import farcore.lib.net.PacketChunkCoord;
import farcore.network.IPacket;
import farcore.network.Network;
import farcore.network.PacketBufferExt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

/**
 * @author ueyudiud
 */
public class PacketCustomChunkData extends PacketChunkCoord
{
	private boolean needToSend;
	private int[] data;

	public PacketCustomChunkData()
	{
	}
	public PacketCustomChunkData(World world, ChunkPos pos, int[] cache)
	{
		super(world, pos.chunkXPos, pos.chunkZPos);
		Chunk chunk = world.getChunkFromChunkCoords(pos.chunkXPos, pos.chunkZPos);
		Arrays.fill(cache, 0);
		int index = 0;
		for(int i = 0; i < 16; ++i)
		{
			ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[i];
			if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE)
			{
				for(int i1 = 0; i1 < 16; ++i1)
				{
					for(int j = 0; j < 16; ++j)
					{
						for(int k = 0; k < 16; ++k)
						{
							IBlockState state = extendedblockstorage.get(j, i1, k);
							if(state.getBlock() instanceof IExtendedDataBlock)
							{
								needToSend = true;
								cache[index] = ((IExtendedDataBlock) state.getBlock()).getDataFromState(state);
							}
							++index;
						}
					}
				}
			}
			else
			{
				index += 0x1000;
			}
		}
		if(needToSend)
		{
			data = cache.clone();
		}
	}
	
	@Override
	public boolean needToSend()
	{
		return needToSend;
	}
	
	@Override
	protected void encode(PacketBufferExt output) throws IOException
	{
		super.encode(output);
		output.writeVarIntArray(data);
	}
	
	@Override
	protected void decode(PacketBufferExt input) throws IOException
	{
		super.decode(input);
		data = input.readVarIntArray();
	}
	
	@Override
	public IPacket process(Network network)
	{
		Chunk chunk = world().getChunkProvider().getLoadedChunk(x, z);
		if(chunk != null)
		{
			int index = 0;
			for(int i = 0; i < 16; ++i)
			{
				ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[i];
				if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE)
				{
					for(int i1 = 0; i1 < 16; ++i1)
					{
						for(int j = 0; j < 16; ++j)
						{
							for(int k = 0; k < 16; ++k)
							{
								IBlockState state = extendedblockstorage.get(j, i1, k);
								if(state.getBlock() instanceof IExtendedDataBlock)
								{
									state = ((IExtendedDataBlock) state.getBlock()).getStateFromData(data[index]);
									extendedblockstorage.set(j, i1, k, state);
								}
								++index;
							}
						}
					}
				}
				else
				{
					index += 0x1000;
				}
			}
		}
		return null;
	}
}