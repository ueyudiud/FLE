package farcore.lib.tile.sided;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import farcore.lib.nbt.INBTReaderAndWritter;
import farcore.lib.tile.TESidedTile;
import farcore.lib.util.Facing;
import farcore.lib.util.Log;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;

public class SidedTileEntity implements INBTReaderAndWritter<SidedTileEntity>
{
	private static final Map<String, Class<? extends SidedTileEntity>> idToClassMap = new HashMap();
	private static final Map<Class<? extends SidedTileEntity>, String> classToIdMap = new HashMap();
	
	public static void registerSidedTileEntity(String id, Class<? extends SidedTileEntity> sideTileEntityClass)
	{
		if(idToClassMap.containsKey(id))
			throw new RuntimeException("The id " + id + " has already registered! Please change a id.");
		idToClassMap.put(id, sideTileEntityClass);
		classToIdMap.put(sideTileEntityClass, id);
	}
	
	public static SidedTileEntity loadFromNBT(TESidedTile tile, NBTTagCompound nbt)
	{
		if(!nbt.hasKey("id"))
			return null;
		String id = nbt.getString("id");
		if(!idToClassMap.containsKey(id))
		{
			Log.warn("The name " + id + "is missing a mapping, this side tile will be default!");
		}
		try
		{
			SidedTileEntity stile = idToClassMap.get(id).newInstance();
			stile.parent = tile;
			stile.facing = Facing.values()[nbt.getByte("facing")];
			return stile.readFromNBT(nbt);
		}
		catch(Exception exception)
		{
			Log.warn("Fail to create new tile entity.", exception);
			return null;
		}
	}

	protected TESidedTile parent;
	protected Facing facing;

	public void setParent(TESidedTile parent, Facing facing)
	{
		this.parent = parent;
		this.facing = facing;
	}
	
	@Override
	public SidedTileEntity readFromNBT(NBTTagCompound nbt)
	{
		return this;
	}

	@Override
	public final void writeToNBT(SidedTileEntity target, NBTTagCompound nbt)
	{
		writeToNBT(nbt);
	}

	public void writeToNBT(NBTTagCompound nbt)
	{

	}

	public final Facing getFacing()
	{
		return facing;
	}
	
	public final TileEntity getParent()
	{
		return parent;
	}
	
	public <T> boolean hasCapability(Capability<T> capability)
	{
		try
		{
			return ((Class<T>) ((ParameterizedType) capability.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).isInstance(this);
		}
		catch (ClassCastException exception)
		{
			return false;
		}
	}

	public <T> T getCapability(Capability<T> capability)
	{
		try
		{
			return (T) this;
		}
		catch (ClassCastException exception)
		{
			return null;
		}
	}

	public void writeToDescription(NBTTagCompound nbt)
	{

	}

	public boolean readFromDescription(NBTTagCompound nbt)
	{
		return false;
	}
	
	protected void markDirty()
	{
		parent.markDirty();
	}
}