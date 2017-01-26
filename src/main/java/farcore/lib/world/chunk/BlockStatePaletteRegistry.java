/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.world.chunk;

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
		return ExtendedBlockStateRegister.getCachedID(state);
	}
	
	@Override
	public IBlockState getBlockState(int indexKey)
	{
		return ExtendedBlockStateRegister.getCachedState(indexKey);
	}
	
	@SideOnly(Side.CLIENT)
	public void read(PacketBuffer buf)
	{
		buf.readVarIntFromBuffer();
	}
	
	public void write(PacketBuffer buf)
	{
		buf.writeVarIntToBuffer(0);
	}
	
	public int getSerializedState()
	{
		return PacketBuffer.getVarIntSize(0);
	}
}