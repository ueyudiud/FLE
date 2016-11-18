package farcore.lib.tile.abstracts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import farcore.data.V;
import farcore.lib.tile.sided.SidedTileEntity;
import farcore.lib.util.Facing;
import farcore.lib.util.Log;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ReportedException;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;

public class TESidedTile extends TESynchronization
{
	public static class SidedTileProp
	{
		Class<? extends SidedTileEntity>[] defaultStiles = new Class[Facing.length];

		public void addSideTile(Class<? extends SidedTileEntity> sideTileEntityClass, Facing facing)
		{
			if(facing == Facing.UNKNOWN || facing == null)
				throw new IllegalArgumentException("Invalid facing for side tile entity.");
			try
			{
				sideTileEntityClass.newInstance();
			}
			catch (Exception exception)
			{
				throw new RuntimeException(exception);
			}
			if(defaultStiles[facing.ordinal()] != null)
				throw new RuntimeException("The facing has already set.");
			defaultStiles[facing.ordinal()] = sideTileEntityClass;
		}

		void putForTile(TESidedTile tile)
		{
			for(int i = 0; i < Facing.length; ++i)
			{
				if(defaultStiles[i] != null)
				{
					try
					{
						tile.stiles[i] = defaultStiles[i].newInstance();
						tile.stiles[i].setParent(tile, Facing.values()[i]);
					}
					catch (Exception exception)
					{
						;
					}
				}
			}
		}
	}
	
	private static final Map<Class<? extends TileEntity>, SidedTileProp> propMap = new HashMap();

	protected EnumFacing front = EnumFacing.SOUTH;
	protected SidedTileEntity[] stiles = new SidedTileEntity[Facing.length];

	public TESidedTile()
	{
		if(propMap.containsKey(getClass()))
		{
			propMap.get(getClass()).putForTile(this);
		}
	}

	public EnumFacing getFrontFacing()
	{
		return front;
	}

	public void setFrontFacing(EnumFacing front)
	{
		this.front = front;
	}
	
	public static SidedTileProp getSidedTileProp(Class<? extends TileEntity> tileClass)
	{
		SidedTileProp prop = propMap.get(tileClass);
		if(prop == null)
		{
			prop = new SidedTileProp();
			propMap.put(tileClass, prop);
			return prop;
		}
		return prop;
	}
	
	@Override
	public void update()
	{
		for(SidedTileEntity tile : stiles)
		{
			if(tile instanceof ITickable)
			{
				try
				{
					((ITickable) tile).update();
				}
				catch (Exception exception)
				{
					if(worldObj != null)
						if(!worldObj.isRemote)
						{
							Log.error("The sided of tile entity throws an exception during ticking in the world, "
									+ "if your enable the option of remove errored tile, this tile will "
									+ "be removed soon. Please report this bug to modder.", exception);
							if(V.removeErroredTile)
							{
								removeBlock();
							}
							else
							{
								CrashReport report = CrashReport.makeCrashReport(exception, "Ticking sided of tile.");
								CrashReportCategory category = report.getCategory();
								category.addCrashSection("world", worldObj);
								category.addCrashSection("pos", pos);
								category.addCrashSection("tile", this.getClass());
								category.addCrashSection("side", tile.getFacing());
								throw new ReportedException(report);
							}
						}
						else
						{
							Log.warn("The tile might disconnect from server caused an exception, "
									+ "if not, please report this bug to modder. If you are playing "
									+ "client world, this exception might cause this world can not "
									+ "load next time, if you can not load the world second time, "
									+ "you can try to remove errored block.", exception);
						}
					else
					{
						Log.warn("Tile entity throws an exception when not ticking in the world. "
								+ "Something might update out of world!", exception);
					}
				}
			}
		}
		super.update();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for(SidedTileEntity stile : stiles)
		{
			if(stile != null)
			{
				list.appendTag(stile.writeToNBT1(stile, new NBTTagCompound()));
			}
		}
		nbt.setTag("sidetile", list);
		return super.writeToNBT(nbt);
	}

	@Override
	public void writeToDescription(NBTTagCompound nbt)
	{
		super.writeToDescription(nbt);
		NBTTagCompound nbt1;
		for (int i = 0; i < Facing.length; ++i)
		{
			if(stiles[i] != null)
			{
				stiles[i].writeToDescription(nbt1 = new NBTTagCompound());
				nbt.setTag("f" + i, nbt1);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		if(nbt.hasKey("sidetile"))
		{
			Arrays.fill(stiles, null);
			NBTTagList list = nbt.getTagList("sidetile", NBT.TAG_COMPOUND);
			for(int i = 0; i < list.tagCount(); ++i)
			{
				SidedTileEntity tile = SidedTileEntity.loadFromNBT(this, list.getCompoundTagAt(i));
				if(tile != null)
				{
					stiles[tile.getFacing().ordinal()] = tile;
				}
			}
		}
	}

	@Override
	public void readFromDescription1(NBTTagCompound nbt)
	{
		super.readFromDescription1(nbt);
		for (int i = 0; i < Facing.length; ++i)
		{
			if(stiles[i] != null && nbt.hasKey("f" + i))
			{
				stiles[i].readFromDescription(nbt.getCompoundTag("f" + i));
			}
		}
	}

	@Override
	public final boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		Facing f1 = Facing.toFacing(front, facing);
		return f1 != Facing.UNKNOWN && stiles[f1.ordinal()].hasCapability(capability) ?
				true : hasCapabilityWithoutSide(capability, facing);
	}

	public boolean hasCapabilityWithoutSide(Capability<?> capability, EnumFacing facing)
	{
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		Facing f1 = Facing.toFacing(front, facing);
		T result;
		return f1 != Facing.UNKNOWN && (result = stiles[f1.ordinal()].getCapability(capability)) != null ?
				result : getCapabilityWithoutSide(capability, facing);
	}

	public <T> T getCapabilityWithoutSide(Capability<T> capability, EnumFacing facing)
	{
		return super.getCapability(capability, facing);
	}
}