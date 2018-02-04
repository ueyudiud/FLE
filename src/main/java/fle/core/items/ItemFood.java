/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.items;

import com.google.common.collect.Maps;

import fle.core.FLE;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.common.item.FoodStatBase;
import nebula.common.item.IBehavior;
import nebula.common.item.IFoodStat;
import nebula.common.item.ItemSubEdible;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class ItemFood extends ItemSubEdible
{
	public ItemFood()
	{
		super(FLE.MODID, "food");
		initalize();
	}
	
	protected void initalize()
	{
		addSubItem(1001, "salad1", "Salad", new FoodStatBase(7.0F, 2.0F, 3.0F));
	}
	
	@Override
	public void addSubItem(int id, String name, String localName, IFoodStat stat, IBehavior...behaviors)
	{
		super.addSubItem(id, name, localName, stat, behaviors);
	}
	
	@Override
	public void postInitalizedItems()
	{
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		NebulaModelLoader.registerModel(this, new ResourceLocation(FLE.MODID, "group/food"));
		NebulaModelLoader.registerItemMetaGenerator(getRegistryName(), stack -> this.nameMap.get(getBaseDamage(stack)));
		NebulaModelLoader.registerTextureSet(getRegistryName(), () -> Maps.asMap(this.idMap.keySet(), key -> new ResourceLocation(FLE.MODID, "items/group/food/" + key)));
	}
}
