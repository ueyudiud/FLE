package farcore.energy;

import java.util.HashMap;

import javax.annotation.Nullable;

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
	
	/**
	 * For general energy net uses.
	 * @author ueyudiud
	 *
	 */
	final class Impl implements IEnergyNet
	{
		final LocalEnergyNetProvider provider;
		final HashMap<Integer, LocalEnergyNet> nets = new HashMap<>();
		
		public Impl(LocalEnergyNetProvider provider)
		{
			this.provider = provider;
		}
		
		public HashMap<Integer, LocalEnergyNet> getLocalNets()
		{
			return nets;
		}
		
		private LocalEnergyNet getOrCreate(World world, boolean create)
		{
			int dim = world.provider.getDimension();
			LocalEnergyNet net = nets.get(dim);
			if(net == null)
			{
				if(!create) return null;
				nets.put(dim, provider.createEnergyNet(world));
			}
			return net;
		}
		
		@Override
		public void add(Object tile)
		{
			World world = provider.getWorldFromTile(tile);
			if(world != null)
			{
				getOrCreate(world, true).add(tile);
			}
		}
		
		@Override
		public void remove(Object tile)
		{
			World world = provider.getWorldFromTile(tile);
			if(world != null)
			{
				LocalEnergyNet net = getOrCreate(world, false);
				if(net != null)
				{
					net.remove(tile);
				}
			}
		}
		
		@Override
		public void reload(Object tile)
		{
			World world = provider.getWorldFromTile(tile);
			if(world != null)
			{
				getOrCreate(world, true).reload(tile);
			}
		}
		
		@Override
		public void mark(Object tile)
		{
			World world = provider.getWorldFromTile(tile);
			if(world != null)
			{
				getOrCreate(world, true).mark(tile);
			}
		}
		
		@Override
		public void unload(World world)
		{
			LocalEnergyNet net = nets.remove(world.provider.getDimension());
			if(net != null)
			{
				net.unload();
			}
		}
		
		@Override
		public void load(World world)
		{
			LocalEnergyNet net = provider.createEnergyNet(world);
			net.load();
			nets.put(world.provider.getDimension(), net);
		}
		
		@Override
		public void update(World world)
		{
			LocalEnergyNet net = getOrCreate(world, false);
			if(net != null)
			{
				net.update();
			}
		}
	}
	
	interface LocalEnergyNetProvider
	{
		LocalEnergyNet<?> createEnergyNet(World world);
		
		/**
		 * Get specific world belong for tile, return null when tie is not belong this tile
		 * or it is invalid.
		 * @param tile
		 * @return
		 */
		@Nullable World getWorldFromTile(Object tile);
	}
	
	abstract class LocalEnergyNet<E>
	{
		protected World world;
		
		protected LocalEnergyNet(World world)
		{
			this.world = world;
		}
		
		/**
		 * On world unloading.
		 * @param world
		 */
		protected abstract void unload();
		
		/**
		 * On world loading.
		 * @param world
		 */
		protected abstract void load();
		
		/**
		 * On world ticking update.
		 * @param world
		 */
		protected abstract void update();
		
		protected abstract void add(E tile);
		
		protected abstract void remove(E tile);
		
		protected abstract void reload(E tile);
		
		protected abstract void mark(E tile);
	}
}