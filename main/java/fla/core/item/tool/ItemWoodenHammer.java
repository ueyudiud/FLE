package fla.core.item.tool;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import fla.api.item.IPolishTool;
import fla.api.item.tool.ItemDamageResource;
import fla.api.util.FlaValue;

public class ItemWoodenHammer extends ItemFlaTool implements IPolishTool
{
	public ItemWoodenHammer() 
	{
		setHarvestLevel(FlaValue.hammer_soft, 0);
		setNoRepair();
		setMaxDamage(6);
		setHasSubtypes(false);
	}

	@Override
	public ItemStack getOutput(EntityPlayer player, ItemStack input) 
	{
		int a = input.getItemDamage() + 1;
		if(a >= input.getMaxDamage())
		{
			return null;
		}
		input.setItemDamage(a);
		return input;
	}

	@Override
	public void damageItem(EntityLivingBase entity, ItemStack stack,
			ItemDamageResource resource) 
	{
		stack.damageItem(1, entity);
	}

	@Override
	public int getToolMaxDamage(ItemStack stack) 
	{
		return stack.getMaxDamage();
	}

	@Override
	public int getToolDamage(ItemStack stack) 
	{
		return stack.getItemDamage();
	}

	@Override
	public String getToolType(ItemStack stack) 
	{
		return FlaValue.hammer_soft;
	}
}
