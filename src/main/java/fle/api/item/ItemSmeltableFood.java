package fle.api.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.energy.thermal.ThermalNet;
import farcore.enums.EnumItem.IInfomationable;
import farcore.interfaces.energy.thermal.IThermalItem;
import farcore.interfaces.item.IFoodStat;
import farcore.interfaces.item.IItemInfo;
import farcore.item.ItemFood;
import farcore.lib.render.item.ItemRenderInfoSimple;
import farcore.util.V;
import fle.api.item.behavior.BehaviorBase;
import fle.api.item.behavior.IThermalBehavior;
import fle.api.util.TemperatureHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSmeltableFood extends ItemFood implements IInfomationable, IThermalItem
{
	protected ItemSmeltableFood(String unlocalized)
	{
		super(unlocalized);
	}
	protected ItemSmeltableFood(String unlocalized, String unlocalizedTooltip)
	{
		super(unlocalized, unlocalizedTooltip);
	}
	protected ItemSmeltableFood(String modid, String unlocalized, String unlocalizedTooltip)
	{
		super(modid, unlocalized, unlocalizedTooltip);
	}
	
	public void addSubItem(int id, String name, String local, String iconName, IFoodStat stat)
	{
		addSubItem(id, name, local, BehaviorBase.SIMPLE, iconName, stat);
	}
	
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, String iconName, IFoodStat stat)
	{
		super.addSubItem(id, name, local, itemInfo, new ItemRenderInfoSimple(iconName), stat);
	}
	
	@Override
	public float getTemperature(ItemStack target)
	{
		IItemInfo behavior = register.get(getDamage(target));
		if(behavior instanceof IThermalBehavior)
		{
			return ((IThermalBehavior) behavior).getTemperature(target);
		}
		else
		{
			return TemperatureHandler.getTemperature(target);
		}
	}
	
	@Override
	public boolean canOutputThermalEnergy(ItemStack target)
	{
		IItemInfo behavior = register.get(getDamage(target));
		if(behavior instanceof IThermalBehavior)
		{
			return ((IThermalBehavior) behavior).canOutputThermalEnergy();
		}
		return true;
	}
	
	@Override
	public float getThermalConductivity(ItemStack target)
	{
		IItemInfo behavior = register.get(getDamage(target));
		if(behavior instanceof IThermalBehavior)
		{
			return ((IThermalBehavior) behavior).getThermalConductivity();
		}
		return 16F;
	}
	
	@Override
	public ItemStack onHeatChanged(ItemStack target, float deltaHeat)
	{
		IItemInfo behavior = register.get(getDamage(target));
		if(behavior instanceof IThermalBehavior)
		{
			IThermalBehavior behavior2 = (IThermalBehavior) behavior;
			try
			{
				return behavior2.onHeatChanged(target, deltaHeat);
			}
			catch(Exception exception)
			{
				if(V.debug)
				{
					throw new RuntimeException(exception);
				}
				return target;
			}
		}
		else
		{
			return TemperatureHandler.setTemperature(target, getTemperature(target) + deltaHeat / 1E4F);
		}
	}
	
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		boolean flag = super.onEntityItemUpdate(entityItem);
		if(entityItem.worldObj.isRemote) return flag;
		ItemStack stack = entityItem.getEntityItem();
		float temp1 = ThermalNet.getTemp(entityItem);
		float temp = getTemperature(stack);
		if(temp1 > temp || canOutputThermalEnergy(stack))
		{
			stack = onHeatChanged(stack, (temp1 - temp) * getThermalConductivity(stack));
			if(stack == null)
			{
				entityItem.setDead();
				return flag;
			}
			else
			{
				if(entityItem.getEntityItem() != stack)
				{
					entityItem.setEntityItemStack(stack);
				}
				return true;
			}
		}
		else
		{
			return flag;
		}
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int tick, boolean flag)
	{
		super.onUpdate(stack, world, entity, tick, flag);
		if(world.isRemote) return;
		float temp1 = ThermalNet.getTemp(entity);
		float temp = getTemperature(stack);
		if(temp1 > temp || canOutputThermalEnergy(stack))
		{
			stack = onHeatChanged(stack, (temp1 - temp) * getThermalConductivity(stack));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for(String name : register.names())
		{
			list.add(new ItemStack(item, 1, register.id(name)));
		}
	}
	
	@Override
	public ItemStack provide(int size, Object... objects)
	{
		if(objects.length == 1)
		{
			if(objects[0] instanceof String)
			{
				return new ItemStack(this, size, register.id((String) objects[0]));
			}
			else if(objects[0] instanceof Number)
			{
				return new ItemStack(this, size, ((Number) objects[0]).intValue());
			}
		}
		return null;
	}
}