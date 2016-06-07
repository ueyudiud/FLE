package fle.api.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.energy.thermal.ThermalNet;
import farcore.enums.EnumItem.IInfomationable;
import farcore.interfaces.energy.thermal.IThermalItem;
import farcore.interfaces.item.IFoodStat;
import farcore.interfaces.item.IItemInfo;
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

public class ItemDivideSmeltableFood extends ItemDividableFood implements IThermalItem
{
	protected ItemDivideSmeltableFood(String unlocalized)
	{
		super(unlocalized);
	}
	protected ItemDivideSmeltableFood(String unlocalized, String unlocalizedTooltip)
	{
		super(unlocalized, unlocalizedTooltip);
	}
	protected ItemDivideSmeltableFood(String modid, String unlocalized, String unlocalizedTooltip)
	{
		super(modid, unlocalized, unlocalizedTooltip);
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
}