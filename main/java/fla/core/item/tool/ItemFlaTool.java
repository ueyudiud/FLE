package fla.core.item.tool;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import fla.api.item.tool.ITool;
import fla.core.item.ItemBase;

public abstract class ItemFlaTool extends ItemBase implements ITool
{
	public ItemFlaTool() 
	{
		this.maxStackSize = 1;
		this.setCreativeTab(CreativeTabs.tabTools);
	}
    
    @Override
    public boolean showDurabilityBar(ItemStack stack) 
    {
    	return this.getToolDamage(stack) != 0;
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack stack) 
    {
    	return (double) this.getToolDamage(stack) / this.getToolMaxDamage(stack);
    }

	public abstract int getToolMaxDamage(ItemStack stack);

	public abstract int getToolDamage(ItemStack stack);

	public abstract String getToolType(ItemStack stack);
}
