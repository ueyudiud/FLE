/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.tile.instance.circuit;

import nebula.common.util.Facing;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class TECircuitIntegration extends TECircuitFrontBack
{
	private short integration;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setShort("integration", integration);
		return super.writeToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		integration = nbt.getShort("integration");
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		if((timer & 0x1) == 0)
		{
			int weak = getWeakPower(Facing.BACK);
			int last = strongPower;
			integration += weak;
			integration *= 15;
			integration /= 16;
			if(last != integration / 16)
			{
				setStrongPower(integration / 16);
				setWeakPower(integration / 16);
			}
		}
	}
}