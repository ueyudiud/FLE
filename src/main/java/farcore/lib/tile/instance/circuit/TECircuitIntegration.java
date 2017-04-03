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
		nbt.setShort("integration", this.integration);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.integration = nbt.getShort("integration");
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		if((this.world.getWorldTime() & 0x1) == 0)
		{
			int power = getRedstonePower(Facing.BACK);
			int last = this.power;
			int i = this.integration + power;
			i = i * 16 / 17;
			this.integration = (short) i;
			if(last != this.integration / 16)
			{
				setRedstonePower(this.integration / 16);
			}
		}
	}
}