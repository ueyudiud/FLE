/*
 * copyright© 2016-2017 ueyudiud
 */

package ed.chunk;

import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntIdentityHashBiMap;
import net.minecraft.world.chunk.IBlockStatePalette;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockStatePaletteMap implements IBlockStatePalette
{
	private final int bitCount;
	private final IntIdentityHashBiMap<IBlockState> map;
	private final BlockStateContainerExt resizer;
	
	public BlockStatePaletteMap(int bitCount, BlockStateContainerExt resizer)
	{
		this.bitCount = bitCount;
		this.map = new IntIdentityHashBiMap<>(1 << bitCount);
		this.resizer = resizer;
	}
	
	@Override
	public int idFor(IBlockState state)
	{
		int id = this.map.getId(state);
		if(id == -1)
		{
			id = this.map.add(state);
			
			if (id >= 1 << this.bitCount)
			{
				id = this.resizer.onResize(this.bitCount + 1, state);
			}
		}
		return id;
	}
	
	@Override
	public IBlockState getBlockState(int indexKey)
	{
		return this.map.get(indexKey);
	}
	
	@SideOnly(Side.CLIENT)
	public void read(PacketBuffer buf)
	{
		this.map.clear();
		int i = buf.readVarInt();
		
		for (int j = 0; j < i; ++j)
		{
			this.map.add(ExtendedBlockStateRegister.INSTANCE.getStateFromNetworkID(buf.readVarInt()));
		}
	}
	
	public void write(PacketBuffer buf)
	{
		int i = this.map.size();
		buf.writeVarInt(i);
		
		for (int j = 0; j < i; ++j)
		{
			buf.writeVarInt(ExtendedBlockStateRegister.INSTANCE.getNetworkID(this.map.get(j)));
		}
	}
	
	public int getSerializedState()
	{
		int i = PacketBuffer.getVarIntSize(this.map.size());
		
		for (int j = 0; j < this.map.size(); ++j)
		{
			i += PacketBuffer.getVarIntSize(ExtendedBlockStateRegister.INSTANCE.getNetworkID(this.map.get(j)));
		}
		
		return i;
	}
}