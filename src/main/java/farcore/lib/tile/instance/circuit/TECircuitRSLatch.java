package farcore.lib.tile.instance.circuit;

import nebula.common.util.Direction;
import nebula.common.util.Facing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;

public class TECircuitRSLatch extends TECircuitCompacted
{
	private static final int Actived = 24;
	private static final Facing[] OUT = {Facing.LEFT, Facing.RIGHT};
	
	private boolean lastFront;
	private boolean lastBack;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setBoolean("flag1", lastFront);
		nbt.setBoolean("flag2", lastBack);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		lastFront = nbt.getBoolean("flag1");
		lastBack = nbt.getBoolean("flag2");
	}
	
	@Override
	protected void updateCircuit()
	{
		super.updateCircuit();
		int front = Math.max(getWeakPower(Facing.FRONT), getStrongPower(Facing.FRONT));
		int back = Math.max(getWeakPower(Facing.BACK), getStrongPower(Facing.BACK));
		if(strongPower > 7)
		{
			if(front > 0 && !lastFront)
			{
				setWeakPower(0);
				setStrongPower(0);
				disable(Actived);
			}
		}
		else
		{
			if(back > 0 && !lastBack)
			{
				setWeakPower(15);
				setStrongPower(15);
				enable(Actived);
			}
		}
		if(front != 0)
		{
			lastFront = true;
		}
		else
		{
			lastFront = false;
		}
		if(back != 0)
		{
			lastBack = true;
		}
		else
		{
			lastBack = false;
		}
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, Direction side)
	{
		return side.horizontal;
	}

	@Override
	public int getStrongPower(IBlockState state, Direction side)
	{
		return side == Facing.LEFT.toDirection(facing) ? strongPower :
			side == Facing.RIGHT.toDirection(facing) ? 15 - strongPower : 0;
	}

	@Override
	public int getWeakPower(IBlockState state, Direction side)
	{
		return side == Facing.LEFT.toDirection(facing) ? weakPower :
			side == Facing.RIGHT.toDirection(facing) ? 15 - weakPower : 0;
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
}