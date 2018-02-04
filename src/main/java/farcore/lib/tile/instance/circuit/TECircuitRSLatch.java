/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.tile.instance.circuit;

import nebula.common.util.Direction;
import nebula.common.util.Facing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TECircuitRSLatch extends TECircuitCompacted
{
	private static final int		Actived	= 24;
	private static final Facing[]	OUT		= { Facing.LEFT, Facing.RIGHT };
	
	private boolean	lastFront;
	private boolean	lastBack;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setBoolean("flag1", this.lastFront);
		nbt.setBoolean("flag2", this.lastBack);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.lastFront = nbt.getBoolean("flag1");
		this.lastBack = nbt.getBoolean("flag2");
	}
	
	@Override
	protected void updateCircuit()
	{
		int front = getRedstonePower(Facing.FRONT);
		int back = getRedstonePower(Facing.BACK);
		if (this.power > 7)
		{
			if (front > 0 && !this.lastFront)
			{
				setRedstonePower(0);
				disable(Actived);
			}
		}
		else
		{
			if (back > 0 && !this.lastBack)
			{
				setRedstonePower(15);
				enable(Actived);
			}
		}
		this.lastFront = front != 0;
		this.lastBack = back != 0;
		markForDelayUpdate(1);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, Direction side)
	{
		return side.horizontal;
	}
	
	@Override
	public int getStrongPower(IBlockState state, Direction side)
	{
		return side == Facing.LEFT.toDirection(this.facing) ? this.power ^ 15 : side == Facing.RIGHT.toDirection(this.facing) ? this.power : 0;
	}
	
	@Override
	public int getWeakPower(IBlockState state, Direction side)
	{
		return side == Facing.LEFT.toDirection(this.facing) ? this.power ^ 15 : side == Facing.RIGHT.toDirection(this.facing) ? this.power : 0;
	}
	
	@Override
	protected Facing[] getOutputFacings()
	{
		return OUT;
	}
	
	@Override
	public String getState()
	{
		return (is(Actived) ? "on" : "off");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getChannelRedSignalHardness(int i)
	{
		return i == 0 ? getRedstonePower(Facing.BACK) : i == 1 ? getRedstonePower(Facing.FRONT) : i == 2 ? this.power : i == 3 ? this.power ^ 15 : 0;
	}
}
