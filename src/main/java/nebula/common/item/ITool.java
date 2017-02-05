package nebula.common.item;

import java.util.List;

import nebula.common.tool.EnumToolType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ITool extends ICustomUsableItem
{
	List<EnumToolType> getToolTypes(ItemStack stack);
	
	int getToolLevel(ItemStack stack, EnumToolType type);
	
	void onToolUse(EntityLivingBase user, ItemStack stack, EnumToolType toolType, float amount);
	
	@Override
	default void onItemUse(ItemStack stack, float amount, EnumToolType type, EntityLivingBase user)
	{
		onToolUse(user, stack, type, amount);
	}
}