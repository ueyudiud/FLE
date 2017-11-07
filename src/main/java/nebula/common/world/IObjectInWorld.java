package nebula.common.world;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * The object current in world (Such as explosion, lather, etc). See nebula
 * world handler.
 * 
 * @author ueyudiud
 * @see nebula.common.NebulaWorldHandler
 */
public interface IObjectInWorld
{
	World world();
	
	double[] position();
	
	void readFromNBT(NBTBase nbt);
	
	NBTBase writeFromNBT();
	
	boolean isDead();
	
	public static abstract class FlowableObjectInWorld implements IObjectInWorld
	{
		protected World	world;
		public double	posX;
		public double	posY;
		public double	posZ;
		
		public FlowableObjectInWorld(World world)
		{
			this.world = world;
		}
		
		public void setWorld(World world)
		{
			this.world = world;
		}
		
		@Override
		public final World world()
		{
			return world;
		}
		
		@Override
		public double[] position()
		{
			return new double[] { posX, posY, posZ };
		}
		
		@Override
		public void readFromNBT(NBTBase nbt)
		{
			NBTTagCompound compound = (NBTTagCompound) nbt;
			posX = compound.getDouble("x");
			posY = compound.getDouble("y");
			posZ = compound.getDouble("z");
			readFromNBT(compound);
		}
		
		protected abstract void readFromNBT(NBTTagCompound nbt);
		
		@Override
		public NBTBase writeFromNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setDouble("x", posX);
			nbt.setDouble("y", posY);
			nbt.setDouble("z", posZ);
			writeToNBT(nbt);
			return nbt;
		}
		
		protected abstract void writeToNBT(NBTTagCompound nbt);
	}
	
	public static abstract class PosistionedObjectInWorld implements IObjectInWorld
	{
		protected World		world;
		public int			posX;
		public int			posY;
		public int			posZ;
		private BlockPos	pos;
		
		public PosistionedObjectInWorld(World world)
		{
			this.world = world;
		}
		
		public PosistionedObjectInWorld(ICoord coord)
		{
			world = coord.world();
			pos = coord.pos();
			posX = pos.getX();
			posY = pos.getY();
			posZ = pos.getZ();
		}
		
		protected BlockPos pos()
		{
			if (pos == null)
			{
				pos = new BlockPos(posX, posY, posZ);
			}
			return pos;
		}
		
		public void setWorld(World world)
		{
			this.world = world;
		}
		
		@Override
		public final World world()
		{
			return world;
		}
		
		@Override
		public double[] position()
		{
			return new double[] { posX, posY, posZ };
		}
		
		@Override
		public void readFromNBT(NBTBase nbt)
		{
			NBTTagCompound compound = (NBTTagCompound) nbt;
			posX = compound.getInteger("x");
			posY = compound.getInteger("y");
			posZ = compound.getInteger("z");
			readFromNBT(compound);
		}
		
		protected abstract void readFromNBT(NBTTagCompound nbt);
		
		@Override
		public NBTBase writeFromNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("x", posX);
			nbt.setInteger("y", posY);
			nbt.setInteger("z", posZ);
			writeToNBT(nbt);
			return nbt;
		}
		
		protected abstract void writeToNBT(NBTTagCompound nbt);
	}
}
