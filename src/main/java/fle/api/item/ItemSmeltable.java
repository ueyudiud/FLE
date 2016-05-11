package fle.api.item;

import farcore.energy.thermal.ThermalNet;
import farcore.interfaces.energy.thermal.IThermalItem;
import farcore.interfaces.item.IItemInfo;
import farcore.util.V;
import fle.api.item.behavior.IThermalBehavior;
import fle.api.util.TemperatureHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSmeltable extends ItemResource implements IThermalItem
{
	protected ItemSmeltable(String unlocalized)
	{
		super(unlocalized);
	}
	protected ItemSmeltable(String unlocalized, String unlocalizedTooltip)
	{
		super(unlocalized, unlocalizedTooltip);
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
		return false;
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
		if(entityItem.worldObj.isRemote) return true;
		ItemStack stack = entityItem.getEntityItem();
		float temp1 = ThermalNet.getTemp(entityItem);
		float temp = getTemperature(stack);
		if(temp1 > temp || canOutputThermalEnergy(stack))
		{
			stack = onHeatChanged(stack, (temp1 - temp) * getThermalConductivity(stack));
			if(stack == null)
			{
				entityItem.setDead();
				return false;
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
			return false;
		}
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int tick, boolean flag)
	{
		if(world.isRemote) return;
		float temp1 = ThermalNet.getTemp(entity);
		float temp = getTemperature(stack);
		if(temp1 > temp || canOutputThermalEnergy(stack))
		{
			stack = onHeatChanged(stack, (temp1 - temp) * getThermalConductivity(stack));
		}
	}
}