package fla.api.item.tool;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ITool 
{	
	/**
	 * Get max damage(uses) of tool.
	 * @param stack
	 * @return
	 */
	public int getToolMaxDamage(ItemStack stack);
	
	/**
	 * Get damage of tool.
	 * @param stack
	 * @return
	 */
	public int getToolDamage(ItemStack stack);
	
	/**
	 * Get type of tool.
	 * @param stack
	 * @return
	 */
	public String getToolType(ItemStack stack);
	
	/**
	 * Damage item when it cause a damage event see {@link fla.api.item.tool.ToolDamageEvent}.
	 * @param entity The damage cause by.
	 * @param stack The stack wait for damage.
	 * @param resource The damage type.
	 */
	public void damageItem(EntityLivingBase entity, ItemStack stack, ItemDamageResource resource);
}
