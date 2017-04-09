package farcore.energy.kinetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import farcore.energy.IEnergyNet;
import farcore.energy.IEnergyNet.LocalEnergyNet;
import farcore.lib.util.EnumModifyFlag;
import nebula.Log;
import nebula.Nebula;
import nebula.common.base.IntegerArray;
import nebula.common.util.Direction;
import nebula.common.util.L;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class KineticNet extends IEnergyNet.LocalEnergyNet<IKineticHandler> implements IKineticAccess, IEnergyNet.LocalEnergyNetProvider
{
	public static final KineticNet instance = new KineticNet(null);
	
	private volatile boolean isUpdating = false;
	
	private final Map<BlockPos, IKineticHandler> map = new HashMap<>();
	private final Map<IntegerArray, KineticPackage> cachedSend = new HashMap<>();
	private final List<IntegerArray> cachedSended = new ArrayList<>();
	private IKineticHandler cachedTile;
	private BlockPos cachedPos = BlockPos.ORIGIN;
	private final Map<IKineticHandler, EnumModifyFlag> cachedChangedTile = new HashMap<>();
	
	public KineticNet(World world)
	{
		super(world);
	}
	
	@Override
	protected void add(IKineticHandler tile)
	{
		if(this.world == null) return;
		if(this.isUpdating)
		{
			this.cachedChangedTile.put(tile, EnumModifyFlag.add);
		}
		else
		{
			this.map.put(tile.pos(), tile);
		}
	}
	
	@Override
	public void remove(IKineticHandler tile)
	{
		if(this.world == null) return;
		if(this.isUpdating)
		{
			this.cachedChangedTile.put(tile, EnumModifyFlag.remove);
		}
		else
		{
			this.map.remove(tile.pos());
		}
	}
	
	@Override
	protected void mark(IKineticHandler tile)
	{
		
	}
	
	@Override
	public void reload(IKineticHandler tile)
	{
		if(this.world == null) return;
		remove(tile);
		add(tile);
	}
	
	@Override
	protected void update()
	{
		if(this.world == null) return;
		//Switch update flag to true, to prevent remove element in map when iterating.
		this.isUpdating = true;
		boolean updated = true;
		try
		{
			//Check if last tick is already updated, or just update lasting tile.
			if(this.cachedSend.isEmpty())
			{
				//Pre-update all tile(Suggested update emitter in this method).
				for(IKineticHandler tile : this.map.values())
				{
					this.cachedTile = tile;
					tile.kineticPreUpdate(this);
				}
			}
			this.cachedTile = null;
			//Send kinetic energy to other tile.
			Map<IntegerArray, KineticPackage> sends = new HashMap<>();
			Direction direction;
			do
			{
				sends.clear();
				sends.putAll(this.cachedSend);
				this.cachedSend.clear();
				for(Entry<IntegerArray, KineticPackage> entry : sends.entrySet())
				{
					int[] info = entry.getKey().array;
					this.cachedPos = new BlockPos(info[0], info[1], info[2]);
					IKineticHandler source = this.map.get(this.cachedPos);
					direction = Direction.DIRECTIONS_3D[info[3]];
					this.cachedPos = direction.offset(this.cachedPos);
					KineticPackage pkg = entry.getValue();
					if(this.map.containsKey(this.cachedPos))
					{
						IKineticHandler tile = this.map.get(this.cachedPos);
						if(!tile.canAccessKineticEnergyFromDirection(direction.getOpposite()))
						{
							this.cachedTile = source;
							source.emitKineticEnergy(this, null, direction, pkg);
						}
						else if(!tile.isRotatable(direction.getOpposite(), pkg))
						{
							this.cachedTile = source;
							source.onStuck(direction, pkg);
							source.emitKineticEnergy(this, tile, direction, pkg);
						}
						else
						{
							double speed = pkg.speed;
							this.cachedTile = tile;
							double send = tile.receiveKineticEnergy(this, source, direction.getOpposite(), pkg);
							this.cachedTile = source;
							if(!L.similar(speed, send))
							{
								pkg.setSpeed(pkg.speed - send);
								source.onStuck(direction, pkg);
								pkg.setSpeed(send);
							}
							source.emitKineticEnergy(this, tile, direction, pkg);
						}
					}
					else
					{
						this.cachedTile = source;
						source.emitKineticEnergy(this, null, direction, pkg);
					}
					this.cachedSended.add(entry.getKey());
				}
				this.cachedTile = null;
			}
			//Some tile maybe is a kinetic conductor, looped check.
			while (!this.cachedSend.isEmpty());
		}
		catch(StackOverflowError error)
		{
			updated = false;
			if(Nebula.debug)
			{
				Log.warn("Stack overflow when update kinetic net.", error);
			}
		}
		catch (OutOfMemoryError error)
		{
			updated = false;
			if(Nebula.debug)
			{
				Log.warn("Out of memory when update kinetic net.", error);
			}
		}
		this.cachedPos = BlockPos.ORIGIN;
		this.cachedTile = null;
		if(updated)
		{
			this.isUpdating = false;
			this.cachedSended.clear();
			for(Entry<IKineticHandler, EnumModifyFlag> entry : this.cachedChangedTile.entrySet())
			{
				switch (entry.getValue())
				{
				case add :
					this.map.put(entry.getKey().pos(), entry.getKey());
					break;
				case remove :
					this.map.remove(entry.getKey().pos());
					break;
				case reload :
					this.map.remove(entry.getKey().pos());
					this.map.put(entry.getKey().pos(), entry.getKey());
					break;
				case mark:
					break;
				}
			}
			this.cachedChangedTile.clear();
		}
	}
	
	@Override
	public void sendEnergyTo(Direction direction, KineticPackage pkg)
	{
		if(this.world == null || this.cachedTile == null || !this.isUpdating || !pkg.isUsable()) return;
		BlockPos pos = this.cachedTile.pos();
		IntegerArray array = new IntegerArray(new int[]{
				pos.getX(),
				pos.getY(),
				pos.getZ(),
				direction.ordinal()});
		if(this.cachedSended.contains(array)) return;
		this.cachedSend.put(array, pkg);
	}
	
	@Override
	protected void load()
	{
		
	}
	
	@Override
	protected void unload()
	{
		
	}
	
	@Override
	public LocalEnergyNet<?> createEnergyNet(World world)
	{
		return new KineticNet(world);
	}
	
	@Override
	public World getWorldFromTile(Object tile)
	{
		return tile instanceof IKineticHandler ? ((IKineticHandler) tile).world() : null;
	}
}