package nebula.common.item;

import java.util.List;

import nebula.common.data.EnumToolType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ITool
{
	List<EnumToolType> getToolTypes(ItemStack stack);

	int getToolLevel(ItemStack stack, EnumToolType type);
	
	void onToolUse(EntityLivingBase user, ItemStack stack, EnumToolType toolType, float amount);
}