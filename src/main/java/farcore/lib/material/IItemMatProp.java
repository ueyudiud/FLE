package farcore.lib.material;

import farcore.lib.util.UnlocalizedList;
import farcore.lib.world.IEnvironment;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemMatProp
{
	void setInstanceFromMeta(ItemStack stack, int metaOffset, Mat material, MatCondition condition);
	
	default int getMetaOffset(ItemStack stack, Mat material, MatCondition condition)
	{
		return getMetaOffset(stack, material, condition, "matprop");
	}
	
	int getMetaOffset(ItemStack stack, Mat material, MatCondition condition, String saveTag);
	
	default ItemStack updateItem(ItemStack stack, Mat material, MatCondition condition, IEnvironment environment)
	{
		return updateItem(stack, material, condition, environment, "matprop");
	}
	
	/**
	 *
	 * @param stack
	 * @param material
	 * @param condition
	 * @param environment
	 * @param saveTag Some item might is multiple materials item, give
	 * material a saving NBT tag.
	 * @return
	 */
	ItemStack updateItem(ItemStack stack, Mat material, MatCondition condition, IEnvironment environment, String saveTag);
	
	@SideOnly(Side.CLIENT)
	default void addInformation(ItemStack stack, Mat material, MatCondition condition, UnlocalizedList list)
	{
		addInformation(stack, material, condition, list, "matprop");
	}

	@SideOnly(Side.CLIENT)
	void addInformation(ItemStack stack, Mat material, MatCondition condition, UnlocalizedList list, String saveTag);

	/**
	 * Get entity attack damage multiple.
	 * @param stack
	 * @param material
	 * @param target
	 * @return
	 */
	float entityAttackDamageMultiple(ItemStack stack, Mat material, Entity target);
}