/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.material.behavior;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;

import farcore.data.MC;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import nebula.base.Ety;
import nebula.client.util.UnlocalizedList;
import nebula.common.environment.IEnvironment;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The material property manager.<p>
 * Use to provide capability related to material handler.<p>
 * Register your property by {@link #registerMaterialProperty(String, IMaterialProperty)}.
 * 
 * @author ueyudiud
 * @see IMaterialProperty
 */
public class MaterialPropertyManager
{
	private static class PropertyWrapper<T>
	{
		private final IMaterialProperty<T, ?> property;
		private final Class<T> type;
		
		PropertyWrapper(IMaterialProperty<T, ?> property)
		{
			this.property = property;
			Type type = TypeToken.of(property.getClass()).getSupertype(IMaterialProperty.class).getType();
			this.type = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
		}
	}
	
	@CapabilityInject(MaterialHandler.class)
	public static Capability<MaterialHandler> CAPABILITY_MATERIAL_HANDLER;
	
	static
	{
		CapabilityManager.INSTANCE.register(MaterialHandler.class, new Capability.IStorage<MaterialHandler>()
		{
			@Override
			public NBTBase writeNBT(Capability<MaterialHandler> capability, MaterialHandler instance, EnumFacing side)
			{
				return instance.writeToNBT();
			}
			
			@Override
			public void readNBT(Capability<MaterialHandler> capability, MaterialHandler instance, EnumFacing side, NBTBase base)
			{
				instance.readFromNBT((NBTTagCompound) base);
			}
		}, () -> new MaterialHandler());
	}
	
	public static final String DEFAULT_PART = "main";
	
	private static final BiMap<String, IMaterialProperty<?, ?>> REGISTRY = HashBiMap.create();
	private static final Map<IMaterialProperty<?, ?>, PropertyWrapper<?>> WRAPPERS = new HashMap<>();
	
	public static void registerMaterialProperty(@Nonnull String key, @Nonnull IMaterialProperty<?, ?> property)
	{
		REGISTRY.put(key, property);
		WRAPPERS.put(property, new PropertyWrapper<>(property));
	}
	
	/**
	 * Get property type.
	 * 
	 * @param property the property.
	 * @return the contained target type of property.
	 * @throws java.lang.IllegalStateException when property is not registered.
	 * @see IMaterialProperty#getTargetType()
	 */
	@Nonnull
	public static <T> Class<T> getType(@Nonnull IMaterialProperty<T, ?> property)
	{
		PropertyWrapper<?> wrapper = WRAPPERS.get(property);
		if (wrapper == null)
			throw new IllegalStateException("The material property " + property + " has not registered yet!");
		return (Class<T>) wrapper.type;
	}
	
	/**
	 * Extract data from ItemStack with {@link #DEFAULT_PART} part's tag.
	 * 
	 * @param stack the data source.
	 * @return the data.
	 * @see #extractData(ItemStack, String)
	 */
	public static Object extractData(@Nonnull ItemStack stack)
	{
		return extractData(stack, DEFAULT_PART);
	}
	
	/**
	 * Extract data from ItemStack with specific part's tag.<p>
	 * If stack does not has {@link #CAPABILITY_MATERIAL_HANDLER} capability, it will return <code>null</code>.
	 * 
	 * @param stack the data source.
	 * @param part the tag of part.
	 * @return the data.
	 */
	public static Object extractData(@Nonnull ItemStack stack, String part)
	{
		return stack.hasCapability(CAPABILITY_MATERIAL_HANDLER, null) ? stack.getCapability(CAPABILITY_MATERIAL_HANDLER, null).extractData(part) : null;
	}
	
	/**
	 * Insert data to ItemStack with {@link #DEFAULT_PART} part's tag.
	 * 
	 * @param stack the target.
	 * @param data the data.
	 */
	public static void insertData(@Nonnull ItemStack stack, Object data)
	{
		insertData(stack, DEFAULT_PART, data);
	}
	
	/**
	 * Insert data to ItemStack with specific part's tag.<p>
	 * If target stack does not has {@link #CAPABILITY_MATERIAL_HANDLER} capability, or data is <code>null</code>,
	 * it will canceled data inject.
	 * 
	 * @param stack the target.
	 * @param part the target tag.
	 * @param data the data.
	 */
	public static void insertData(@Nonnull ItemStack stack, String part, @Nullable Object data)
	{
		if (stack.hasCapability(CAPABILITY_MATERIAL_HANDLER, null) && data != null)
		{
			stack.getCapability(CAPABILITY_MATERIAL_HANDLER, null).insertData(part, data);
		}
	}
	
	public static void copyData(@Nonnull ItemStack input, String inputPart, ItemStack output, String outputPart)
	{
		if (input.hasCapability(CAPABILITY_MATERIAL_HANDLER, null) && output.hasCapability(CAPABILITY_MATERIAL_HANDLER, null))
		{
			output.getCapability(CAPABILITY_MATERIAL_HANDLER, null).insertData(outputPart, input.getCapability(CAPABILITY_MATERIAL_HANDLER, null).extractData(inputPart));
		}
	}
	
	public static class MaterialHandler
	{
		private final Map<String, Pair> properties;
		
		private class Pair
		{
			Mat material;
			MatCondition condition;
			Object value;
			
			Pair(Entry<MatCondition, Mat> entry)
			{
				this.material = entry.getValue();
				this.condition = entry.getKey();
				this.value = this.material.itemProp == null ? null : this.material.itemProp.instance(this.material);
			}
			
			Pair(MatCondition condition, Mat material, Object value)
			{
				this.condition = condition;
				this.material = material;
				this.value = value;
			}
			
			Pair copy()
			{
				return new Pair(this.condition, this.material, this.material.itemProp == null ? null :
					this.material.itemProp.copyOf(this.value, this.material));
			}
		}
		
		MaterialHandler()
		{
			this(MC.LATTICE, Mat.VOID);
		}
		
		public MaterialHandler(MatCondition condition, Mat material)
		{
			this(ImmutableMap.of(DEFAULT_PART, new Ety<>(condition, material)));
		}
		
		public MaterialHandler(Map<String, Entry<MatCondition, Mat>> properties)
		{
			this.properties = ImmutableMap.copyOf(Maps.transformValues(properties, Pair::new));
		}
		
		MaterialHandler(Map<String, Pair> properties, Void unused)
		{
			this.properties = ImmutableMap.copyOf(properties);
		}
		
		public void readFromNBT(NBTTagCompound nbt)
		{
			for (Entry<String, Pair> entry : this.properties.entrySet())
			{
				Pair pair = entry.getValue();
				if (pair.material.itemProp != null)
				{
					pair.value = pair.material.itemProp.read(nbt.getTag(entry.getKey()), pair.material);
				}
			}
		}
		
		public NBTTagCompound writeToNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			for (Entry<String, Pair> entry : this.properties.entrySet())
			{
				Pair pair = entry.getValue();
				if (pair.material.itemProp != null)
				{
					nbt.setTag(entry.getKey(), pair.material.itemProp.write(pair.material, pair.value));
				}
			}
			return nbt;
		}
		
		public int getOffsetMetaCount()
		{
			return getMetaOffset(DEFAULT_PART);
		}
		
		public int getOffsetMetaCount(String part)
		{
			Pair pair = this.properties.get(part);
			if (pair == null || pair.material.itemProp == null)
				return 1;
			return pair.material.itemProp.getOffsetMetaCount();
		}
		
		public void setMetaOffset(int metaOffset, Mat material)
		{
			setMetaOffset(DEFAULT_PART, metaOffset, material);
		}
		
		public void setMetaOffset(String part, int metaOffset, Mat material)
		{
			Pair pair = this.properties.get(part);
			if (pair == null || pair.material.itemProp == null)
				return;
			pair.value = pair.material.itemProp.instance(metaOffset, material);
		}
		
		public String getReplacedLocalName(String defaultName, int metaOffset, String part)
		{
			Pair pair = this.properties.get(part);
			if (pair == null || pair.material.itemProp == null)
				return defaultName;
			return pair.material.itemProp.getReplacedLocalName(metaOffset, pair.material);
		}
		
		public int getMetaOffset()
		{
			return getMetaOffset(DEFAULT_PART);
		}
		
		public int getMetaOffset(String part)
		{
			Pair pair = this.properties.get(part);
			if (pair == null || pair.material.itemProp == null)
				return 0;
			return pair.material.itemProp.getMetaOffset(pair.value, pair.material);
		}
		
		public ItemStack updateItem(ItemStack stack, IEnvironment environment)
		{
			ItemStack start = stack;
			for (Entry<String, Pair> entry : this.properties.entrySet())
			{
				Pair pair = entry.getValue();
				if (pair.material.itemProp != null)
				{
					stack = pair.material.itemProp.updateItem(pair.value, stack, pair.material, pair.condition, environment);
					if (stack != start)
						break;
				}
			}
			return stack;
		}
		
		@SideOnly(Side.CLIENT)
		public void addInformation(UnlocalizedList list)
		{
			for (Entry<String, Pair> entry : this.properties.entrySet())
			{
				Pair pair = entry.getValue();
				if (pair.material.itemProp != null)
				{
					pair.material.itemProp.addInformation(pair.value, pair.material, pair.condition, list);
				}
			}
		}
		
		@SideOnly(Side.CLIENT)
		public void addInformation(String name, UnlocalizedList list)
		{
			Pair pair = this.properties.get(name);
			if (pair != null && pair.material.itemProp != null)
			{
				pair.material.itemProp.addInformation(pair.value, pair.material, pair.condition, list);
			}
		}
		
		public float entityAttackDamageMultiple(String part, Entity target)
		{
			Pair pair = this.properties.get(part);
			if (pair == null || pair.material.itemProp == null)
				return 1.0F;
			return pair.material.itemProp.entityAttackDamageMultiple(pair.value, pair.material, target);
		}
		
		public void insertData(Object value)
		{
			insertData(DEFAULT_PART, value);
		}
		
		public void insertData(String part, Object value)
		{
			Pair pair = this.properties.get(part);
			if (pair == null || pair.material.itemProp == null)
				return;
			pair.value = pair.material.itemProp.copyOf(value, pair.material);
		}
		
		public Object extractData()
		{
			return extractData(DEFAULT_PART);
		}
		
		public Object extractData(String part)
		{
			Pair pair = this.properties.get(part);
			if (pair == null || pair.material.itemProp == null)
				return null;
			return pair.material.itemProp.copyOf(pair.value, pair.material);
		}
	}
}
