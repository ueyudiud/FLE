package farcore.lib.tile.instance.circuit;

import nebula.common.util.Direction;
import nebula.common.util.Facing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TECircuitSpatial extends TECircuitCompacted
{
	protected static final AxisAlignedBB REDSTONE_SPATIAL_DIODE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D);
	
	private static final Facing[] OUT = {Facing.FRONT, Facing.BACK, Facing.LEFT, Facing.RIGHT};
	
	protected byte powerFB;
	protected byte powerLR;
	
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
		return side.horizontal ?
				(side == this.facing || side == this.facing.getOpposite() ?
						this.powerFB : this.powerLR) : 0;
	}
	
	@Override
	public int getStrongPower(IBlockState state, Direction side)
	{
		return side.horizontal ?
				(side == this.facing || side == this.facing.getOpposite() ?
						this.powerFB : this.powerLR) : 0;
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, Direction side)
	{
		return side.horizontal;
	}
	
	protected byte getPowerHigherThan(byte min, byte power, Facing side)
	{
		byte power1 = (byte) getRedstonePower(side);
		if(power1 <= power) return power;
		else if(power1 < min) return 0;
		return (byte) (power1 - 1);
	}
	
	@Override
	protected Facing[] getOutputFacings()
	{
		return OUT;
	}
}