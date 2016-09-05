package farcore.lib.item;

import java.util.List;

import farcore.data.EnumToolType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface ITool
{
	List<EnumToolType> getToolTypes(ItemStack stack);

	void onToolUse(EntityLivingBase user, ItemStack stack, EnumToolType toolType, float amount);
}