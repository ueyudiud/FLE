/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.config;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nebula.Log;
import nebula.common.util.Game;
import nebula.common.util.L;
import nebula.common.util.Strings;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

/**
 * @author ueyudiud
 */
public final class NebulaConfiguration
{
	/**
	 * Load configuration from file, the config can not be null.
	 * @param config
	 * @return
	 */
	public static @Nonnull <C> C loadConfig(@Nonnull C config)
	{
		return (C) loadConfig(config, "", config.getClass());
	}
	public static @Nullable <C> C loadConfig(@Nonnull Class<C> configClass)
	{
		return loadConfig(null, configClass);
	}
	public static @Nullable <C> C loadConfig(String category, @Nonnull Class<C> configClass)
	{
		try
		{
			return (C) loadConfig(configClass.newInstance(), Strings.validate(category), configClass);
		}
		catch (Exception exception)
		{
			return null;
		}
	}
	public static void loadStaticConfig(Class<?> configClass)
	{
		loadConfig(null, null, configClass);
	}
	
	private static Object loadConfig(Object c, String category, Class<?> configClass)
	{
		if (!configClass.isAnnotationPresent(Config.class))
			throw new IllegalArgumentException("The cache type should has @Config annotation.");
		Config configA = configClass.getAnnotation(Config.class);
		String name = configA.value();
		Configuration config = new Configuration(new File(Game.getMCFile(), "config/" + configA.value() + ".cfg"));
		config.load();
		
		try
		{
			putField(c, config, category, configClass);
		}
		catch (Exception exception)
		{
			Log.error("Fail to insert configuration to object.", exception);
		}
		
		config.save();
		return c;
	}
	
	private static void putField(Object object, Configuration configuration, String categoryBase, Class<?> configClass) throws Exception
	{
		boolean flag = object == null;
		for (Field field : configClass.getFields())
		{
			int modifier = field.getModifiers();
			if ((modifier & Modifier.FINAL) != 0) continue;//Skip final value.
			if (flag ^ (modifier & Modifier.STATIC) != 0) continue;
			TypeAdapter adapter = TYPE_ADAPTER_MAP.get(field.getType());
			if (adapter == null)
				throw new IllegalArgumentException("Can not found type adapter for " + field.getType().getName() + ".");
			String category;
			String name;
			String defValue = DEFAULT_VALUE_APPLIER.getOrDefault(field.getType(), DEF_VALUE_FUNCTION).apply(field).toString();
			String comments;
			if (field.isAnnotationPresent(ConfigProperty.class))
			{
				ConfigProperty property = field.getAnnotation(ConfigProperty.class);
				category = (categoryBase == null ? "" : categoryBase + "\\.") + property.category();
				name = property.name().length() == 0 ? field.getName() : property.name();
			}
			else
			{
				category = categoryBase;
				name = field.getName();
			}
			comments = field.isAnnotationPresent(ConfigComment.class) ? field.getAnnotation(ConfigComment.class).value() : "";
			adapter.injectProperty(object, field, configuration, category, name, defValue, comments);
		}
	}
	
	private static final Function<Field, String> DEF_VALUE_FUNCTION = field->
	field.isAnnotationPresent(ConfigProperty.class) ? field.getAnnotation(ConfigProperty.class).defValue() : "";
	
	private static final TypeAdapter<Object> ADAPTER_ANY = (arg, field, config, category, name, defValue, comments) -> {
		if (field.isAnnotationPresent(Config.class))
		{
			Config c = field.getAnnotation(Config.class);
			field.set(arg, loadConfig(field.getType()));
		}
	};
	
	private static final TypeAdapter<Integer> ADAPTER_INT = (arg, field, config, category, name, defValue, comments) -> {
		int minValue, maxValue;
		
		if (field.isAnnotationPresent(ConfigRangeInt.class))
		{
			ConfigRangeInt range = field.getAnnotation(ConfigRangeInt.class);
			minValue = range.min();
			maxValue = range.max();
			comments += " range: [" + minValue + ", " + maxValue + "]";
		}
		else
		{
			minValue = Integer.MIN_VALUE;
			maxValue = Integer.MAX_VALUE;
		}
		
		Property property = config.get(category, name, defValue, comments, Type.INTEGER);
		property.setMinValue(minValue);
		property.setMaxValue(maxValue);
		property.setDefaultValue(defValue);
		field.setInt(arg, L.range(minValue, maxValue, property.getInt()));
	};
	private static final TypeAdapter<Long> ADAPTER_LONG = (arg, field, config, category, name, defValue, comments) -> {
		long minValue, maxValue;
		
		if (field.isAnnotationPresent(ConfigRangeLong.class))
		{
			ConfigRangeLong range = field.getAnnotation(ConfigRangeLong.class);
			minValue = range.min();
			maxValue = range.max();
			comments += " range: [" + minValue + ", " + maxValue + "]";
		}
		else
		{
			minValue = Long.MIN_VALUE;
			maxValue = Long.MAX_VALUE;
		}
		
		Property property = config.get(category, name, defValue, comments, Type.INTEGER);
		property.setMinValue(minValue);
		property.setMaxValue(maxValue);
		property.setDefaultValue(defValue);
		field.setLong(arg, L.range(minValue, maxValue, property.getLong()));
	};
	private static final TypeAdapter<?> ADAPTER_FLOAT = (arg, field, config, category, name, defValue, comments) -> {
		double minValue, maxValue;
		
		if (field.isAnnotationPresent(ConfigRangeFloat.class))
		{
			ConfigRangeFloat range = field.getAnnotation(ConfigRangeFloat.class);
			minValue = range.min();
			maxValue = range.max();
			comments += " range: [" + minValue + ", " + maxValue + "]";
		}
		else
		{
			if (field.getType() == float.class)
			{
				minValue = Float.MIN_VALUE;
				maxValue = Float.MAX_VALUE;
			}
			else
			{
				minValue = Double.MIN_VALUE;
				maxValue = Double.MAX_VALUE;
			}
		}
		
		Property property = config.get(category, name, defValue, comments, Type.DOUBLE);
		property.setMinValue(minValue);
		property.setMaxValue(maxValue);
		property.setDefaultValue(defValue);
		if (field.getType() == float.class)
		{
			field.setFloat(arg, (float) property.getDouble());
		}
		else
		{
			field.setDouble(arg, property.getDouble());
		}
	};
	private static final TypeAdapter<String> ADAPTER_STRING = (arg, field, config, category, name, defValue, comments) -> {
		Property property = config.get(category, name, defValue, comments, Type.STRING);
		property.setDefaultValue(defValue);
		field.set(arg, property.getString());
	};
	
	private static final Map<Class<?>, TypeAdapter<?>> TYPE_ADAPTER_MAP = new HashMap<>();
	private static final TypeAdapter<Boolean> ADAPTER_BOOLEAN = (arg, field, config, category, name, defValue, comments) -> {
		Property property = config.get(category, name, defValue, comments, Type.BOOLEAN);
		property.setDefaultValue(defValue);
		field.setBoolean(arg, property.getBoolean());
	};
	
	private static final Map<Class<?>, Function<Field, ?>> DEFAULT_VALUE_APPLIER = new HashMap<>();
	
	public static <T> void registerTypeAdapter(Class<T> type, TypeAdapter<? super T> adapter, Function<Field, ? extends T> function)
	{
		TYPE_ADAPTER_MAP.put(type, adapter);
		if (function != null)
		{
			DEFAULT_VALUE_APPLIER.put(type, function);
		}
	}
	
	static
	{
		registerTypeAdapter(int.class, ADAPTER_INT, null);
		registerTypeAdapter(long.class, ADAPTER_LONG, null);
		registerTypeAdapter(float.class, (TypeAdapter<Float>) ADAPTER_FLOAT, null);
		registerTypeAdapter(double.class,(TypeAdapter<Double>) ADAPTER_FLOAT, null);
		registerTypeAdapter(boolean.class, ADAPTER_BOOLEAN, null);
		registerTypeAdapter(String.class, ADAPTER_STRING, null);
	}
	
	private NebulaConfiguration() { }
}