/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.energy.electric;

import farcore.energy.IEnergyNet;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class ElectricNet implements IEnergyNet.LocalEnergyNetProvider
{
	public static final IEnergyNet NET = new IEnergyNet.Impl(new ElectricNet());
	
	@Override
	public ElectricNetLocal createEnergyNet(World world)
	{
		return new ElectricNetLocal(world);
	}
	
	@Override
	public World getWorldFromTile(Object tile)
	{
		if (tile instanceof IElectricHandler)
		{
			return ((IElectricHandler) tile).world();
		}
		return null;
	}
}
