/*
 * copyright© 2016-2017 ueyudiud
 */

package ed.chunk;

import net.minecraft.block.state.IBlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.IBlockStatePalette;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BlockStatePaletteRegistry implements IBlockStatePalette
{
	public static final BlockStatePaletteRegistry INSTANCE = new BlockStatePaletteRegistry();
	
	@Override
	public int idFor(IBlockState state)
	{
		return ExtendedBlockStateRegister.INSTANCE.getNetworkID(state);
	}
	
	@Override
	public IBlockState getBlockState(int indexKey)
	{
		return ExtendedBlockStateRegister.INSTANCE.getStateFromNetworkID(indexKey);
	}
	
	@SideOnly(Side.CLIENT)
	public void read(PacketBuffer buf)
	{
		buf.readVarInt();
	}
	
	public void write(PacketBuffer buf)
	{
		buf.writeVarInt(0);
	}
	
	public int getSerializedState()
	{
		return PacketBuffer.getVarIntSize(0);
	}
}