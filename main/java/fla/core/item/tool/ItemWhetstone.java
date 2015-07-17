package fla.core.item.tool;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import fla.api.item.IPolishTool;
import fla.api.item.tool.ItemDamageResource;
import fla.api.util.FlaValue;

public class ItemWhetstone extends ItemFlaTool implements IPolishTool
{	
	public ItemWhetstone(int maxUse)
	{
		setNoRepair();
		setMaxDamage(maxUse);
	}

	@Override
	public void damageItem(EntityLivingBase entity, ItemStack stack,
			ItemDamageResource resource) 
	{
		stack.damageItem(1, entity);
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
		return FlaValue.whetstone;
	}

}
