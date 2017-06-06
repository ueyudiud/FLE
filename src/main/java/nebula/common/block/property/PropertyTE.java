package nebula.common.block.property;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import nebula.base.IRegister;
import nebula.base.Register;
import nebula.common.block.property.PropertyTE.TETag;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class PropertyTE extends PropertyHelper<TETag>
{
	public static PropertyTE create(String name, IRegister<Class<? extends TileEntity>> list)
	{
		IRegister<TETag> register = new Register<>(list.size());
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
			return this.clazz;
		}
		
		public TileEntity newInstance()
		{
			try
			{
				return this.clazz.newInstance();
			}
			catch(IllegalAccessException | InstantiationException exception)
			{
				throw new RuntimeException("The class " + this.clazz.getName() + " is missing a valid constructor.", exception);
			}
		}
		
		public void registerTileEntity(String prefix)
		{
			GameRegistry.registerTileEntity(this.clazz, prefix + "." + this.name);
		}
		
		@Override
		public int compareTo(TETag other)
		{
			return Integer.compare(this.id, other.id);
		}
		
		public int id()
		{
			return this.id;
		}
		
		public String name()
		{
			return this.name;
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
		return this.list.size();
	}
	
	public TileEntity getTileFromMeta(int meta)
	{
		return this.list.get(meta).newInstance();
	}
	
	public int getTileMeta(TileEntity tile)
	{
		return this.list.id(this.map.get(tile.getClass()));
	}
	
	public IBlockState withProperty(IBlockState state, TileEntity tile)
	{
		return state.withProperty(this, this.list.get(this.map.get(tile.getClass()), this.def));
	}
	
	public IBlockState withProperty(IBlockState state, String tag)
	{
		return state.withProperty(this, this.list.get(tag, this.def));
	}
	
	public IBlockState withProperty(IBlockState state, int meta)
	{
		return state.withProperty(this, this.list.get(meta, this.def));
	}
	
	public int getMetaFromState(IBlockState state)
	{
		return this.list.id(state.getValue(this));
	}
	
	@Override
	public Collection<TETag> getAllowedValues()
	{
		return this.list.targets();
	}
	
	@Override
	public Optional<TETag> parseValue(String value)
	{
		return this.list.contain(value) ? Optional.of(this.list.get(value)) : Optional.absent();
	}
	
	@Override
	public String getName(TETag value)
	{
		return value.name;
	}
}