/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.tile.instance.circuit;

import java.io.IOException;

import nebula.common.NebulaSynchronizationHandler;
import nebula.common.network.PacketBufferExt;
import nebula.common.util.Direction;
import nebula.common.util.Facing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TECircuitSpatial extends TECircuitCompacted
{
	protected static final AxisAlignedBB REDSTONE_SPATIAL_DIODE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D);
	
	private static final Facing[] OUT = { Facing.FRONT, Facing.BACK, Facing.LEFT, Facing.RIGHT };
	
	protected byte	powerFB;
	protected byte	powerLR;
	
	@Override
	public void writeNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		switch (type)
		{
		case 0:
			buf.writeByte(this.powerFB);
			buf.writeByte(this.powerLR);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void readNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		switch (0)
		{
		case 0:
			this.powerFB = buf.readByte();
			this.powerLR = buf.readByte();
			break;
		default:
			break;
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state)
	{
		return REDSTONE_SPATIAL_DIODE_AABB;
	}
	
	@Override
	public AxisAlignedBB getBoundBox(IBlockState state)
	{
		return REDSTONE_SPATIAL_DIODE_AABB;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(IBlockState state)
	{
		return REDSTONE_SPATIAL_DIODE_AABB;
	}
	
	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		nbt.setByte("po1", this.powerFB);
		nbt.setByte("po2", this.powerLR);
	}
	
	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		if (nbt.hasKey("po1"))
		{
			this.powerFB = nbt.getByte("po1");
			markBlockRenderUpdate();
		}
		if (nbt.hasKey("po2"))
		{
			this.powerLR = nbt.getByte("po2");
			markBlockRenderUpdate();
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.powerFB = nbt.getByte("powerfb");
		this.powerLR = nbt.getByte("powerlr");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setByte("poewrfb", this.powerFB);
		nbt.setByte("powerlr", this.powerLR);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public int getWeakPower(IBlockState state, Direction side)
	{
		return side.horizontal ? (side == this.facing || side == this.facing.getOpposite() ? this.powerFB : this.powerLR) : 0;
	}
	
	@Override
	public int getStrongPower(IBlockState state, Direction side)
	{
		return side.horizontal ? (side == this.facing || side == this.facing.getOpposite() ? this.powerFB : this.powerLR) : 0;
	}
	
	@Override
	public void causeUpdate(BlockPos pos, IBlockState state, boolean tileUpdate)
	{
		updateCircuit();
		NebulaSynchronizationHandler.markTileEntityForUpdate(this, 0);
		if (this.updateDelay == 0 && ((this.power = (byte) (this.powerFB << 4 | this.powerLR)) != this.lastPower))
		{
			this.lastPower = this.power;
			super.notifyNeighbors();
		}
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, Direction side)
	{
		return side.horizontal;
	}
	
	protected byte getPowerHigherThan(byte min, byte power, Facing side)
	{
		byte power1 = (byte) getRedstonePower(side);
		if (power1 <= power)
			return power;
		else if (power1 < min) return 0;
		return (byte) (power1 - 1);
	}
	
	@Override
	protected Facing[] getOutputFacings()
	{
		return OUT;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getChannelRedSignalHardness(int i)
	{
		return i == 0 ? this.powerFB : i == 1 ? this.powerLR : 0;
	}
}
