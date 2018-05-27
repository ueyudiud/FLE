/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.material;

import static farcore.data.V.MATERIAL_SIZE;
import static net.minecraft.init.Bootstrap.isRegistered;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;

import farcore.data.MP;
import farcore.lib.crop.ICropSpecie;
import farcore.lib.material.behavior.IMaterialProperty;
import farcore.lib.tree.Tree;
import nebula.base.collection.HashIntMap;
import nebula.base.collection.HashPropertyMap;
import nebula.base.collection.IPropertyMap;
import nebula.base.collection.IPropertyMap.IProperty;
import nebula.base.function.F;
import nebula.base.function.Judgable;
import nebula.base.register.IRegister;
import nebula.base.register.IRegisteredNameable;
import nebula.base.register.SortedRegister;
import nebula.common.G;
import nebula.common.LanguageManager;
import nebula.common.nbt.INBTReaderAndWriter;
import nebula.common.util.ISubTagContainer;
import nebula.common.util.ItemStacks;
import nebula.common.util.L;
import nebula.common.util.SubTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;

/**
 * Materials of all elements in game.
 * 
 * @author ueyudiud
 */
public class Mat implements ISubTagContainer, IRegisteredNameable, Comparable<Mat>
{
	private static final IRegister<Mat> REGISTER = new SortedRegister<Mat>(MATERIAL_SIZE, m->m.id)
	{
		private final Mat[] materials = new Mat[MATERIAL_SIZE];
		
		@Override
		protected void reg(int id, String name, Mat material)
		{
			super.reg(id, name, material);
			this.materials[id] = material;
		}
		
		@Override
		protected void build()
		{
			if (!this.sorted)
			{
				((ArrayList<?>) this.resource).trimToSize();
				Arrays.sort(this.sortedNames = this.resource.toArray(new Delegate[this.resource.size()]), Delegate::compareName);
				this.sorted = true;
			}
		}
		
		@Override
		public String name(int id)
		{
			Mat material = this.materials[id];
			return material != null ? material.name : null;
		}
		
		@Override
		public Mat get(int id)
		{
			return this.materials[id];
		}
		
		@Override
		public Mat get(int id, Mat def)
		{
			Mat material = this.materials[id];
			return material != null ? material : def;
		}
		
		@Override
		public String name(Mat arg)
		{
			return arg == null || arg.id < 0 ? null : arg.name;
		}
		
		@Override
		public int id(Mat arg)
		{
			return arg == null ? -1 : arg.id;
		}
		
		@Override
		public boolean contain(int id)
		{
			return this.materials[id] != null;
		}
		
		@Override
		public Iterator<Mat> iterator()
		{
			build();
			return new Iterator<Mat>()
			{
				int count = 0;
				int id = -1;
				
				@Override
				public boolean hasNext()
				{
					return this.count < size();
				}
				
				@Override
				public Mat next()
				{
					if (this.count >= size())
						throw new IndexOutOfBoundsException();
					while (materials[++this.id] == null);
					this.count++;
					return materials[this.id];
				}
			};
		}
	};
	
	private static final Map<Judgable<? super Mat>, List<Mat>> MATERIALS_CACHE = new HashMap<>();
	
	/**
	 * Default material, will not register in to list.
	 * <p>
	 * The default result when fail to search material from list.
	 */
	public static final Mat VOID = new Mat(-1, false, "", "void", "Void", "Void");
	
	public static final INBTReaderAndWriter<Mat, NBTTagString> WITH_NULL_RW = new INBTReaderAndWriter<Mat, NBTTagString>()
	{
		@Override
		public Mat readFrom(NBTTagString nbt)
		{
			return REGISTER.get(nbt.getString());
		}
		
		@Override
		public NBTTagString writeTo(Mat target)
		{
			return new NBTTagString(target.name);
		}
		
		@Override
		public Class<Mat> type()
		{
			return Mat.class;
		}
	};
	
	static void onDataChanged()
	{
		MATERIALS_CACHE.clear();
	}
	
	static
	{
		if (isRegistered())
		{
			VOID.builder().setToolable(0, 1, 1.0F, 0.0F, 1.0F, 1.0F, 0).setHandable(1.0F).addProperty(MP.property_crop, ICropSpecie.VOID).addProperty(MP.property_tree, Tree.VOID).build();
		}
		/**
		 * For debugging use, generate model file, export material properties,
		 * etc.
		 * <p>
		 * The game will not initialize so it need prevent using class needed
		 * initialized.
		 */
		else
			System.out.println("The material may be in debug enviorment, skip VOID property registeration.");
	}
	
	public static IRegister<Mat> materials()
	{
		return REGISTER;
	}
	
	public static Mat material(int id)
	{
		return REGISTER.get(id);
	}
	
	public static Mat material(int id, Mat def)
	{
		return REGISTER.get(id, def);
	}
	
	public static <V> V propertyOf(String name, IPropertyMap.IProperty<V> property)
	{
		return material(name).getProperty(property);
	}
	
	public static Mat material(String name)
	{
		return REGISTER.get(name, VOID);
	}
	
	public static Mat material(String name, Mat def)
	{
		return REGISTER.get(name, def);
	}
	
	public static boolean contain(String name)
	{
		return REGISTER.contain(name);
	}
	
	public static <V> Iterable<V> filtAndGet(Judgable<? super Mat> filter, IProperty<V> property)
	{
		return filtAndGet(filter, false, property);
	}
	
	public static <V> Iterable<V> filtAndGet(Judgable<? super Mat> filter, boolean alwaysInit, IProperty<V> property)
	{
		if (alwaysInit || !MATERIALS_CACHE.containsKey(filter))
		{
			return () -> REGISTER.stream().filter(filter).map(L.<IPropertyMap.IProperty<V>, V, Mat> toFunction(Mat::getProperty, property)).iterator();
		}
		else
		{
			return Iterables.transform(MATERIALS_CACHE.get(filter), F.cast(F.<Mat, IPropertyMap.IProperty<V>, V> const2f(Mat::getProperty, property)));
		}
	}
	
	public static List<Mat> filt(Judgable<? super Mat> filter)
	{
		return filt(filter, false);
	}
	
	public static List<Mat> filt(Judgable<? super Mat> filter, boolean alwaysInit)
	{
		if (alwaysInit || !MATERIALS_CACHE.containsKey(filter))
		{
			List<Mat> ret = REGISTER.stream().filter(filter).collect(Collectors.toList());
			if (!alwaysInit)
			{
				((ArrayList) ret).trimToSize();//Predicate Collectors.toList() return ArrayList.
				MATERIALS_CACHE.put(filter, ret);
			}
			return ret;
		}
		else
			return MATERIALS_CACHE.get(filter);
	}
	
	public Builder builder()
	{
		return isRegistered() ? new Builder(this) : new MatBuilderTest(this);
	}
	
	public final String	modid;
	public final String	name;
	public final String	oreDictName;
	public final String	localName;
	public final short	id;
	/**
	 * Some material is variant of other material, this field is the source
	 * material target.
	 */
	public Mat			unificationMaterial	= this;
	public String		chemicalFormula;
	public String		customDisplayInformation;
	public short[]		RGBa				= { 255, 255, 255, 255 };
	public int			RGB					= 0xFFFFFF;
	// Multi item configuration.
	public IMaterialProperty itemProp;
	
	/**
	 * The heat capacity of material, unit : J/(m^3*K)
	 */
	public double	heatCapacity;
	/**
	 * The thermal conductivity of material, unit : W/(m*K)
	 */
	public double	thermalConductivity;
	public double	maxSpeed;
	public double	maxTorque;
	public double	dielectricConstant;
	public double	electrialResistance;
	public float	redstoneResistance;
	
	public int		toolMaxUse		= 1;
	public int		toolHarvestLevel;
	public float	toolHardness	= 1.0F;
	public float	toolBrittleness;
	public float	toolDamageToEntity;
	
	IPropertyMap propertyMap = new HashPropertyMap();
	// Reused now.
	HashIntMap<String>	properties	= new HashIntMap<>();
	private Set<SubTag>			subTags		= new HashSet<>();
	
	public Mat(int id, String name, String oreDict, String localized)
	{
		this(id, G.activeModid(), name, oreDict, localized);
	}
	
	public Mat(int id, String modid, String name, String oreDict, String localized)
	{
		this(id, true, modid, name, oreDict, localized);
	}
	
	public Mat(int id, boolean register, String modid, String name, String oreDict, String localized)
	{
		this.id = (short) id;
		this.modid = modid;
		this.name = name;
		this.oreDictName = oreDict;
		this.localName = localized;
		LanguageManager.registerLocal("material." + name + ".name", localized);
		if (register)
		{
			REGISTER.register(name, this);//The id allocator is already exists, need't to set in method.
		}
	}
	
	@Override
	public final String getRegisteredName()
	{
		return this.name;
	}
	
	public String getLocalName()
	{
		return LanguageManager.translateLocal("material." + this.name + ".name");
	}
	
	public <V> V getProperty(IProperty<V> property)
	{
		return getProperty(property, property.get());
	}
	
	public <V> Optional<? extends V> getPropertyOptional(IProperty<V> property)
	{
		return this.propertyMap.getOptional(property);
	}
	
	public <V> V getProperty(IProperty<V> property, V def)
	{
		V value = this.propertyMap.get(property);
		return value == null ? def : value;
	}
	
	public int getProperty(String tag)
	{
		return this.properties.get(tag);
	}
	
	public float getPropertyF(String tag)
	{
		return Float.intBitsToFloat(getProperty(tag));
	}
	
	@Override
	public void add(SubTag...tags)
	{
		for (SubTag tag : tags)
		{
			tag.addContainerToList(this);
		}
		this.subTags.addAll(Arrays.asList(tags));
		onDataChanged();
	}
	
	@Override
	public boolean remove(SubTag tag)
	{
		if (this.subTags.remove(tag))
		{
			onDataChanged();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean contain(SubTag tag)
	{
		return this.subTags.contains(tag);
	}
	
	@Override
	public int compareTo(Mat o)
	{
		return this.name.compareTo(o.name);
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
	
	public static Mat getMaterialByIDOrDefault(NBTTagCompound nbt, String key, Mat def)
	{
		return nbt.hasKey(key) ? material(nbt.getShort(key), def) : def;
	}
	
	public static Mat getMaterialByNameOrDefault(NBTTagCompound nbt, String key, Mat def)
	{
		return nbt.hasKey(key) ? material(nbt.getString(key), def) : def;
	}
	
	public static void setMaterialToStack(ItemStack stack, String key, Mat material)
	{
		ItemStacks.getOrSetupNBT(stack, true).setString(key, material.name);
	}
	
	public static Mat getMaterialFromStack(ItemStack stack, String key, Mat def)
	{
		return getMaterialByNameOrDefault(ItemStacks.getOrSetupNBT(stack, false), key, def);
	}
}
