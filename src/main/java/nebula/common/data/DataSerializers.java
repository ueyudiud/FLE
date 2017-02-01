/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.data;

import static net.minecraft.network.datasync.DataSerializers.registerSerializer;

import java.io.IOException;

import nebula.common.world.chunk.ExtendedBlockStateRegister;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * @author ueyudiud
 */
public class DataSerializers
{
	public static final DataSerializer<Boolean> BOOLEAN = net.minecraft.network.datasync.DataSerializers.BOOLEAN;
	public static final DataSerializer<Byte> BYTE = net.minecraft.network.datasync.DataSerializers.BYTE;
	public static final DataSerializer<Integer> VARINT = net.minecraft.network.datasync.DataSerializers.VARINT;
	public static final DataSerializer<String> STRING = net.minecraft.network.datasync.DataSerializers.STRING;
	public static final DataSerializer<BlockPos> BLOCK_POS = net.minecraft.network.datasync.DataSerializers.BLOCK_POS;
	public static final DataSerializer<EnumFacing> FACING = net.minecraft.network.datasync.DataSerializers.FACING;
	public static final DataSerializer<IBlockState> BLOCK_STATE = new DataSerializer<IBlockState>()
	{
		@Override
		public void write(PacketBuffer buf, IBlockState value)
		{
			buf.writeInt(ExtendedBlockStateRegister.getCachedID(value));
		}
		
		@Override
		public IBlockState read(PacketBuffer buf) throws IOException
		{
			return ExtendedBlockStateRegister.getCachedState(buf.readInt());
		}
		
		@Override
		public DataParameter<IBlockState> createKey(int id)
		{
			return new DataParameter<>(id, this);
		}
	};
	
	static
	{
		registerSerializer(BLOCK_STATE);
	}
}