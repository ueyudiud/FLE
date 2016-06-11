package fle.core.item.resource;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.enums.EnumItem;
import farcore.interfaces.IItemIconInfo;
import farcore.interfaces.energy.thermal.IThermalItem;
import farcore.interfaces.energy.thermal.IThermalTile;
import farcore.interfaces.item.IItemInfo;
import farcore.item.ItemBase;
import farcore.item.ItemSubBehavior;
import farcore.lib.substance.SubstanceWood;
import fle.api.item.behavior.BehaviorBase;
import fle.api.util.TemperatureHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemStick extends ItemBase implements IThermalItem
{
	public ItemStick()
	{
		super("stick");
		EnumItem.stick.set(new ItemStack(this));
	}
		
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register)
	{
		icon = register.registerIcon(getIconString());
	}

	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public int getRenderPasses(int metadata)
	{
		return 1;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass)
	{
		return icon;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta)
	{
		return icon;
	}
	
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass) 
	{
		return 0xFFFFFF;
	}

	@Override
	public float getTemperature(ItemStack target)
	{
		return TemperatureHandler.getTemperature(target);
	}

	@Override
	public boolean canOutputThermalEnergy(ItemStack target)
	{
		return true;
	}

	@Override
	public float getThermalConductivity(ItemStack target)
	{
		return 10;
	}

	@Override
	public ItemStack onHeatChanged(ItemStack target, float deltaHeat)
	{
		float temp = TemperatureHandler.getTemperature(target);
		if((temp += deltaHeat / 10F) >= 420)
		{
			return EnumItem.torch.instance(target.stackSize);
		}
		else
		{
			TemperatureHandler.setTemperature(target, temp);
			return target;
		}
	}
}