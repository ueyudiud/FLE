/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.energy.electric;

import farcore.energy.IEnergyNet;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
class ElectricNetLocal extends IEnergyNet.LocalEnergyNet<IElectricHandler>
{
	private final Resolvor R = new Resolvor();
	
	public ElectricNetLocal(World world)
	{
		super(world);
	}
	
	void onPreCalc()
	{
		
	}
	
	void onCalc()
	{
		
	}
	
	@Override
	protected void unload()
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void load()
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void update()
	{
		this.R.solve();
	}
	
	
	@Override
	protected void add(IElectricHandler tile)
	{
		
	}
	
	
	@Override
	protected void remove(IElectricHandler tile)
	{
		
	}
	
	
	@Override
	protected void reload(IElectricHandler tile)
	{
		remove(tile);
		add(tile);
	}
	
	
	@Override
	protected void mark(IElectricHandler tile)
	{
		
	}
}
