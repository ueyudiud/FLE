package farcore.energy;

import farcore.energy.EnergyHandler.Energy;
import net.minecraft.world.World;

/**
 * The global energy net.
 * @author ueyudiud
 *
 */
public interface IEnergyNet
{
	/**
	 * The energy formatter, use to exchange energy from two energy net.
	 * @return
	 */
	default Energy getEnergyFormat()
	{
		return EnergyHandler.STANDARD_DOUBLE;
	}

	/**
	 * Add new energy capabilitable object.
	 * Called when post {@link farcore.event.EnergyEvent.Add}
	 * @param tile
	 */
	void add(Object tile);

	/**
	 * Remove energy capabilitable object.
	 * Called when post {@link farcore.event.EnergyEvent.Remove}
	 * @param tile
	 */
	void remove(Object tile);
	
	/**
	 * Reload energy capabilitable object, it is equal to
	 * remove and add this object.
	 * Called when post {@link farcore.event.EnergyEvent.Reload}
	 * @param tile
	 */
	void reload(Object tile);
	
	/**
	 * Mark energy capabilitable object for update.
	 * Called when post {@link farcore.event.EnergyEvent.Mark}
	 * @param tile
	 */
	void mark(Object tile);
	
	/**
	 * On world unloading.
	 * @param world
	 */
	void unload(World world);
	
	/**
	 * On world loading.
	 * @param world
	 */
	void load(World world);
	
	/**
	 * On world ticking update.
	 * @param world
	 */
	void update(World world);
}