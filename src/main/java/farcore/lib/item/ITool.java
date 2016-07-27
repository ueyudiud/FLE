package farcore.lib.item;

import java.util.List;

import farcore.data.EnumToolType;
import net.minecraft.item.ItemStack;

public interface ITool
{
	List<EnumToolType> getToolTypes(ItemStack stack);
	
	void onToolUse(ItemStack stack, EnumToolType toolType, float amount);
}