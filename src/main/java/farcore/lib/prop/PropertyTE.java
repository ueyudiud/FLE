package farcore.lib.prop;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import farcore.FarCoreRegistry;
import farcore.lib.collection.IRegister;
import farcore.lib.collection.Register;
import farcore.lib.prop.PropertyTE.TETag;
import farcore.lib.tile.instance.TELossTile;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;

public class PropertyTE extends PropertyHelper<TETag>
{
	public static PropertyTE create(String name, IRegister<Class<? extends TileEntity>> list)
	{
		IRegister<TETag> register = new Register(list.size());
		ImmutableMap.Builder<Class<? extends TileEntity>, String> builder = ImmutableMap.builder();
		TETag def = null;
		for(String tag : list.names())
		{
			int id = list.id(tag);
			Class<? extends TileEntity> class1 = list.get(tag);
			if(id == 0)
			{
				register.register(id, tag, def = new TETag(id, tag, class1));
			}
			else
			{
				register.register(id, tag, new TETag(id, tag, class1));
			}
			builder.put(class1, tag);
		}
		return new PropertyTE(name, def, register, builder.build());
	}
	
	public static class TETag implements Comparable<TETag>
	{
		int id;
		Class<? extends TileEntity> clazz;
		String name;
		
		public TETag(int id, String name, Class<? extends TileEntity> clazz)
		{
			this.id = id;
			this.clazz = clazz;
			this.name = name;
		}

		public Class<? extends TileEntity> getTEClass()
		{
			return clazz;
		}
		
		public TileEntity newInstance()
		{
			try
			{
				return clazz.newInstance();
			}
			catch(IllegalAccessException | InstantiationException exception)
			{
				throw new RuntimeException("The class " + clazz.getName() + " is missing a valid constructor.", exception);
			}
		}

		public void registerTileEntity(String prefix)
		{
			if(clazz == TELossTile.class) return;//Do not register default tile entity.
			FarCoreRegistry.registerTileEntity(prefix + "." + name, clazz);
		}

		@Override
		public int compareTo(TETag other)
		{
			return Integer.compare(id, other.id);
		}
		
		public int id()
		{
			return id;
		}
		
		public String name()
		{
			return name;
		}
	}

	protected Map<Class<? extends TileEntity>, String> map;
	protected IRegister<TETag> list;
	protected final TETag def;
	
	public PropertyTE(String name, TETag def, IRegister<TETag> list, Map<Class<? extends TileEntity>, String> map)
	{
		super(name, TETag.class);
		this.list = list;
		this.map = map;
		this.def = def;
	}
	
	public int size()
	{
		return list.size();
	}
	
	public TileEntity getTileFromMeta(int meta)
	{
		return list.get(meta).newInstance();
	}

	public IBlockState withProperty(IBlockState state, TileEntity tile)
	{
		return state.withProperty(this, list.get(map.get(tile.getClass()), def));
	}

	public IBlockState withProperty(IBlockState state, String tag)
	{
		return state.withProperty(this, list.get(tag, def));
	}

	public IBlockState withProperty(IBlockState state, int meta)
	{
		return state.withProperty(this, list.get(meta, def));
	}
	
	public int getMetaFromState(IBlockState state)
	{
		return list.id(state.getValue(this));
	}
	
	@Override
	public Collection<TETag> getAllowedValues()
	{
		return list.targets();
	}
	
	@Override
	public Optional<TETag> parseValue(String value)
	{
		return list.contain(value) ? Optional.of(list.get(value)) : Optional.absent();
	}
	
	@Override
	public String getName(TETag value)
	{
		return value.name;
	}
}