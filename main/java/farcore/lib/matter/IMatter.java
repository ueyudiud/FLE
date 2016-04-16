package farcore.lib.matter;

import farcore.lib.collection.IPropertyMap;
import net.minecraft.item.ItemStack;

public interface IMatter
{
	String name();
	
	/**
	 * Return an item, fluid, item stack or fluid stack for display in 
	 * NEI and guide book.
	 * @return
	 */
	Object display(MatterStack stack);
	
	/**
	 * Get display item.
	 * @param stack
	 * @return
	 */
	String getDisplayName(MatterStack stack);
	
	/**
	 * Check is matter valid in this container, 
	 * if return false, the container is suggested to remove
	 * this stack at once.
	 * @param container
	 * @return
	 */
	boolean canStayAt(MatterStack stack, IMatterContainer container);
}