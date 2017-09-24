/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader;

import fle.core.FLE;
import fle.core.entity.misc.EntityAttributeTag;
import nebula.common.config.Config;
import nebula.common.config.ConfigComment;
import nebula.common.config.ConfigProperty;
import nebula.common.config.NebulaConfiguration;
import nebula.common.util.L;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

/**
 * @author ueyudiud
 */
@Config(FLE.MODID)
public class Configs
{
	@ConfigProperty(category = "client", name = "enableAllToolHeadCreativeTabs", defValue = "true")
	public static boolean createAllToolCreativeTabs;
	@EntityAttributeTag.Attribute(health = 200.0F, followRange = 40.0F, speed = 0.24F, attack = 4.0F, armor = 5.0F)
	public static EntityAttributeTag zombie;
	@EntityAttributeTag.Attribute(health = 200.0F, followRange = 20.0F, speed = 0.25F, attack = 4.0F, armor = 5.0F)
	public static EntityAttributeTag skeleton;
	@EntityAttributeTag.Attribute(health = 160.0F, followRange = 20.0F, speed = 0.3F, attack = 3.0F, armor = 0.0F)
	public static EntityAttributeTag spider;
	@EntityAttributeTag.Attribute(health = 200.0F, followRange = 20.0F, speed = 0.25F, attack = 3.0F, armor = 0.0F)
	public static EntityAttributeTag creeper;
	
	@ConfigProperty(category = "item", defValue = "true")
	@ConfigComment("The material name of which tool made of will be display on name if enable this option.")
	public static boolean displayMaterialTypeOnToolName;
	
	public static void init()
	{
		NebulaConfiguration.registerTypeAdapter(EntityAttributeTag.class,
				(arg, field, config, category, name, defValue, comments) -> {
					ConfigCategory cat = config.getCategory(category == null ? name : category + "\\." + name);
					EntityAttributeTag.Attribute attribute = field.getAnnotation(EntityAttributeTag.Attribute.class);
					EntityAttributeTag tag = new EntityAttributeTag();
					Property property;
					cat.setComment(comments);
					if (!Float.isNaN(attribute.health()))
					{
						property = cat.computeIfAbsent("health", key->
						new Property(key, Float.toString(attribute.health()), Type.DOUBLE));
						property.setDefaultValue(attribute.health());
						property.setMaxValue(1024.0);
						property.setMinValue(0.0);
						tag.maxHealth = L.range(0.0F, 1024.0F, (float) property.getDouble());
					}
					if (!Float.isNaN(attribute.followRange()))
					{
						property = cat.computeIfAbsent("followRange", key->
						new Property(key, Float.toString(attribute.followRange()), Type.DOUBLE));
						property.setDefaultValue(attribute.followRange());
						property.setMaxValue(2048.0);
						property.setMinValue(0.0);
						tag.followRange = L.range(0.0F, 2048.0F, (float) property.getDouble());
					}
					if (!Float.isNaN(attribute.speed()))
					{
						property = cat.computeIfAbsent("speed", key->
						new Property(key, Float.toString(attribute.speed()), Type.DOUBLE));
						property.setDefaultValue(attribute.speed());
						property.setMaxValue(1024.0);
						property.setMinValue(0.0);
						tag.movementSpeed = L.range(0.0F, 1024.0F, (float) property.getDouble());
					}
					if (!Float.isNaN(attribute.attack()))
					{
						property = cat.computeIfAbsent("attack", key->
						new Property(key, Float.toString(attribute.attack()), Type.DOUBLE));
						property.setDefaultValue(attribute.attack());
						property.setMaxValue(1024.0);
						property.setMinValue(0.0);
						tag.attackDamage = L.range(0.0F, 1024.0F, (float) property.getDouble());
					}
					if (!Float.isNaN(attribute.armor()))
					{
						property = cat.computeIfAbsent("armor", key->
						new Property(key, Float.toString(attribute.armor()), Type.DOUBLE));
						property.setDefaultValue(attribute.armor());
						property.setMaxValue(30.0);
						property.setMinValue(0.0);
						tag.armor = L.range(0.0F, 30.0F, (float) property.getDouble());
					}
					field.set(arg, tag);
				}, field-> {
					EntityAttributeTag.Attribute attribute = field.getAnnotation(EntityAttributeTag.Attribute.class);
					return new EntityAttributeTag(attribute.health(), attribute.followRange(), attribute.speed(), attribute.attack(), attribute.armor());
				});
		NebulaConfiguration.loadStaticConfig(Configs.class);
	}
}