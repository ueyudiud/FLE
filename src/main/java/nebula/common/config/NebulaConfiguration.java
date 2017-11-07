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

import nebula.Log;
import nebula.common.util.Game;
import nebula.common.util.L;
import nebula.common.util.Strings;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

/**
 * The configuration handler, use to load configuration from file.
 * <p>
 * 
 * @author ueyudiud
 * @see nebula.common.config.Config
 */
public final class NebulaConfiguration
{
	/**
	 * Load configuration from file, the config can not be null.
	 * 
	 * @param config
	 * @return
	 */
	public static Configuration loadConfig(@Nonnull Object config)
	{
		return loadConfig(config, "", config.getClass());
	}
	
	public static Configuration loadConfig(@Nonnull Class<?> configClass)
	{
		return loadConfig(null, configClass);
	}
	
	public static Configuration loadConfig(String category, @Nonnull Class<?> configClass)
	{
		try
		{
			return loadConfig(configClass.newInstance(), Strings.validate(category), configClass);
		}
		catch (Exception exception)
		{
			return null;
		}
	}
	
	/**
	 * Load configuration and inject into static field. The field should be
	 * <tt>static</tt> and <tt>public</tt>.
	 * 
	 * @param configClass
	 */
	public static Configuration loadStaticConfig(Class<?> configClass)
	{
		return loadConfig(null, null, configClass);
	}
	
	public static void loadStaticConfig(Class<?> configClass, Configuration config)
	{
		loadConfig(null, null, configClass, config, false);
	}
	
	private static Configuration loadConfig(Object obj, String category, Class<?> configClass)
	{
		if (!configClass.isAnnotationPresent(Config.class)) throw new IllegalArgumentException("The cache type should has @Config annotation.");
		Config annotation = configClass.getAnnotation(Config.class);
		Configuration config = new Configuration(new File(Game.getMCFile(), "config/" + annotation.value() + ".cfg"));
		return loadConfig(obj, category, configClass, config, true);
	}
	
	private static Configuration loadConfig(Object c, String category, Class<?> configClass, Configuration config, boolean load)
	{
		if (!configClass.isAnnotationPresent(Config.class)) throw new IllegalArgumentException("The cache type should has @Config annotation.");
		
		if (load)
		{
			config.load();
		}
		putField(c, config, category, configClass);
		config.save();
		
		return config;
	}
	
	private static void putField(Object object, Configuration configuration, String categoryBase, Class<?> configClass)
	{
		boolean flag = object == null;
		for (Field field : configClass.getFields())
		{
			int modifier = field.getModifiers();
			if ((modifier & Modifier.FINAL) != 0) continue;// Skip final value.
			if (flag ^ (modifier & Modifier.STATIC) != 0) continue;
			@SuppressWarnings("unchecked")
			TypeAdapter<? super Object> adapter = (TypeAdapter<? super Object>) getAdapter(field);
			if (adapter == null)
			{
				adapter = ADAPTER_ANY;
			}
			String category;
			String name;
			String defValue = defaultValue(field).toString();
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
			try
			{
				adapter.injectProperty(object, field, configuration, category, name, defValue, comments);
			}
			catch (Exception exception)
			{
				Log.warn("Fail to inject configuration. category: {}, name: {}, default: {}", category, name, defValue);
			}
		}
	}
	
	static TypeAdapter<?> getAdapter(Field field)
	{
		return TYPE_ADAPTER_MAP.get(field.getType());
	}
	
	static Object defaultValue(Field field)
	{
		return DEFAULT_VALUE_APPLIER.getOrDefault(field.getType(), DEF_VALUE_FUNCTION).apply(field);
	}
	
	private static final Function<Field, String> DEF_VALUE_FUNCTION = field -> field.isAnnotationPresent(ConfigProperty.class) ? field.getAnnotation(ConfigProperty.class).defValue() : "";
	
	private static final TypeAdapter<Object> ADAPTER_ANY = (arg, field, config, category, name, defValue, comments) -> {
		if (field.isAnnotationPresent(Config.class))
		{
			Config c = field.getAnnotation(Config.class);
			field.set(arg, loadConfig(c.value(), field.getType()));
			return;
		}
		throw new IllegalArgumentException("Can not found type adapter for " + field.getType().getName() + ".");
	};
	
	private static final TypeAdapter<Integer>	ADAPTER_INT		= (arg, field, config, category, name, defValue, comments) -> {
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
	private static final TypeAdapter<Long>		ADAPTER_LONG	= (arg, field, config, category, name, defValue, comments) -> {
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
	private static final TypeAdapter<?>			ADAPTER_FLOAT	= (arg, field, config, category, name, defValue, comments) -> {
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
	private static final TypeAdapter<Boolean>	ADAPTER_BOOLEAN	= (arg, field, config, category, name, defValue, comments) -> {
																	Property property = config.get(category, name, defValue, comments, Type.BOOLEAN);
																	property.setDefaultValue(defValue);
																	field.setBoolean(arg, property.getBoolean());
																};
	private static final TypeAdapter<String>	ADAPTER_STRING	= (arg, field, config, category, name, defValue, comments) -> {
																	Property property = config.get(category, name, defValue, comments, Type.STRING);
																	property.setDefaultValue(defValue);
																	field.set(arg, property.getString());
																};
	
	private static final Map<Class<?>, TypeAdapter<?>> TYPE_ADAPTER_MAP = new HashMap<>();
	
	private static final Map<Class<?>, Function<Field, ?>> DEFAULT_VALUE_APPLIER = new HashMap<>();
	
	/**
	 * Register a {@link nebula.common.config.TypeAdapter} to configuration
	 * loader.
	 * 
	 * @param type the type for adapter to loader.
	 * @param adapter the adapter.
	 * @param function
	 */
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
		registerTypeAdapter(double.class, (TypeAdapter<Double>) ADAPTER_FLOAT, null);
		registerTypeAdapter(boolean.class, ADAPTER_BOOLEAN, null);
		registerTypeAdapter(String.class, ADAPTER_STRING, null);
	}
	
	private NebulaConfiguration()
	{
	}
}
