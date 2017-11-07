/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.items;

import fle.core.FLE;
import nebula.common.item.FoodStatBase;
import nebula.common.item.IBehavior;
import nebula.common.item.IFoodStat;
import nebula.common.item.ItemSubEdible;
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
	}
}
