/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.world.chunk;

import javax.annotation.Nullable;

import nebula.Log;
import nebula.common.data.Misc;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.BitArray;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.BlockStateContainer;
import net.minecraft.world.chunk.IBlockStatePalette;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Extract a state container with large capacity.
 * @author ueyudiud
 */
public class BlockStateContainerExt extends BlockStateContainer
{
	/** The a block state container capacity. */
	protected static final int BLOCK_COUNT = 4096;
	
	/** The bit size to send need. */
	protected int bits;
	
	public BlockStateContainerExt()
	{
		this.storage = null;
		this.palette = null;
		initalize();
	}
	
	/**
	 * To prevent initialization, override this method.
	 */
	protected void initalize()
	{
		setBit(4);
	}
	
	protected void setBit(int size)
	{
		if (this.bits != size)
		{
			if(size <= 4)
			{
				this.palette = new BlockStatePaletteArray(4, this);
			}
			else if(size <= 8)
			{
				this.palette = new BlockStatePaletteMap(size, this);
			}
			else
			{
				this.palette = BlockStatePaletteRegistry.INSTANCE;
				size = MathHelper.log2DeBruijn(ExtendedBlockStateRegister.idCapacity());
			}
			this.palette.idFor(Misc.AIR);//Mark air id to 0
			this.bits = size;
			this.storage = new BitArray(size, BLOCK_COUNT);
		}
	}
	
	@Override
	public int onResize(int size, IBlockState state)
	{
		BitArray array = this.storage;
		IBlockStatePalette palette = this.palette;
		setBit(size);
		
		for (int i = 0; i < array.size(); ++i)
		{
			IBlockState state2 = palette.getBlockState(array.getAt(i));
			
			if (state2 != null)
			{
				this.set(i, state2);
			}
		}
		
		return this.palette.idFor(state);
	}
	
	@Override
	public void write(PacketBuffer buf)
	{
		buf.writeByte(this.bits);
		this.palette.write(buf);
		buf.writeLongArray(this.storage.getBackingLongArray());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void read(PacketBuffer buf)
	{
		int i = buf.readByte();
		
		if (this.bits != i)
		{
			setBit(i);
		}
		
		this.palette.read(buf);
		buf.readLongArray(this.storage.getBackingLongArray());
	}
	
	public int[] getDatasToNBT()
	{
		int[] list = new int[BLOCK_COUNT];
		for (int id = 0; id < BLOCK_COUNT; ++id)
		{
			list[id] = ExtendedBlockStateRegister.getStateData(get(id));
		}
		return list;
	}
	
	public void setDataFromNBT(@Nullable int[] datas)
	{
		int id = 0;
		for (int i = 0; i < 16; ++i)
			for(int j = 0; j < 16; ++j)
				for(int k = 0; k < 16; ++k)
				{
					try
					{
						set(k, i, j, datas == null ? Misc.AIR : ExtendedBlockStateRegister.getStateFromData(datas[id]));
					}
					catch (Exception exception)
					{
						Log.catching(exception);
					}
					++id;
				}
	}
}