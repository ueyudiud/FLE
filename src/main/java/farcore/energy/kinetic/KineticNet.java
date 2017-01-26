package farcore.energy.kinetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import farcore.FarCore;
import farcore.energy.IEnergyNet;
import farcore.energy.IEnergyNet.LocalEnergyNet;
import farcore.lib.collection.IntegerArray;
import farcore.lib.util.Direction;
import farcore.lib.util.EnumModifyFlag;
import farcore.lib.util.Log;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class KineticNet extends IEnergyNet.LocalEnergyNet<IKineticHandler> implements IKineticAccess, IEnergyNet.LocalEnergyNetProvider
{
	public static final KineticNet instance = new KineticNet(null);
	
	private volatile boolean isUpdating = false;
	
	private final Map<BlockPos, IKineticHandler> map = new HashMap();
	private final Map<IntegerArray, double[]> cachedSend = new HashMap();
	private final List<IntegerArray> cachedSended = new ArrayList();
	private IKineticHandler cachedTile;
	private BlockPos cachedPos = BlockPos.ORIGIN;
	private final Map<IKineticHandler, EnumModifyFlag> cachedChangedTile = new HashMap();
	
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
			Map<IntegerArray, double[]> sends = new HashMap();
			Direction direction;
			do
			{
				sends.clear();
				sends.putAll(this.cachedSend);
				this.cachedSend.clear();
				for(Entry<IntegerArray, double[]> entry : sends.entrySet())
				{
					int[] info = entry.getKey().array;
					this.cachedPos = new BlockPos(info[0], info[1], info[2]);
					IKineticHandler source = this.map.get(this.cachedPos);
					direction = Direction.DIRECTIONS_3D[info[3]];
					this.cachedPos = direction.offset(this.cachedPos);
					double[] pkg = entry.getValue();
					if(this.map.containsKey(this.cachedPos))
					{
						IKineticHandler tile = this.map.get(this.cachedPos);
						if(!tile.canAccessKineticEnergyFromDirection(direction.getOpposite()))
						{
							this.cachedTile = source;
							source.emitKineticEnergy(this, direction, pkg[0], pkg[1]);
						}
						else if(!tile.isRotatable(direction.getOpposite(), pkg[0], pkg[1]))
						{
							this.cachedTile = source;
							source.onStuck(direction, pkg[0], pkg[1]);
							source.emitKineticEnergy(this, direction, pkg[0], pkg[1]);
						}
						else
						{
							double speed = pkg[0];
							this.cachedTile = tile;
							double send = tile.receiveKineticEnergy(this, direction.getOpposite(), pkg[0], pkg[1]);
							this.cachedTile = source;
							if(speed != send)
							{
								source.onStuck(direction, Math.abs(send - speed), pkg[1]);
							}
							source.emitKineticEnergy(this, direction, pkg[0], pkg[1]);
						}
					}
					else
					{
						this.cachedTile = source;
						source.emitKineticEnergy(this, direction, pkg[0], pkg[1]);
					}
					this.cachedSended.add(entry.getKey());
				}
			}
			//Some tile maybe is a kinetic conductor, looped check.
			while (!this.cachedSend.isEmpty());
		}
		catch(StackOverflowError error)
		{
			updated = false;
			if(FarCore.debug)
			{
				Log.warn("Stack overflow when update kinetic net.", error);
			}
		}
		catch (OutOfMemoryError error)
		{
			updated = false;
			if(FarCore.debug)
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
	public void sendEnergyTo(Direction direction, double speed, double torque)
	{
		if(this.world == null || this.cachedTile == null || !this.isUpdating || torque <= 0) return;
		BlockPos pos = this.cachedTile.pos();
		IntegerArray array = new IntegerArray(new int[]{
				pos.getX(),
				pos.getY(),
				pos.getZ(),
				direction.ordinal()});
		if(this.cachedSended.contains(array)) return;
		this.cachedSend.put(array, new double[]{speed, torque});
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
	public LocalEnergyNet createEnergyNet(World world)
	{
		return new KineticNet(world);
	}
	
	@Override
	public World getWorldFromTile(Object tile)
	{
		return tile instanceof IKineticHandler ? ((IKineticHandler) tile).world() : null;
	}
}