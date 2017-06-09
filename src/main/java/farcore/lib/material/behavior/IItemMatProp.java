package farcore.lib.material.behavior;

import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import nebula.client.util.UnlocalizedList;
import nebula.common.enviornment.IEnvironment;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemMatProp
{
	String DEFAULT_SAVE_TAG = "matprop";
	
	int getOffsetMetaCount();
	
	String getReplacedLocalName(int metaOffset, Mat material);
	
	default void setInstanceFromMeta(ItemStack stack, int metaOffset, Mat material, MatCondition condition)
	{
		setInstanceFromMeta(stack, metaOffset, material, condition, DEFAULT_SAVE_TAG);
	}
	
	void setInstanceFromMeta(ItemStack stack, int metaOffset, Mat material, MatCondition condition, String saveTag);
	
	default int getMetaOffset(ItemStack stack, Mat material, MatCondition condition)
	{
		return getMetaOffset(stack, material, condition, DEFAULT_SAVE_TAG);
	}
	
	int getMetaOffset(ItemStack stack, Mat material, MatCondition condition, String saveTag);
	
	default ItemStack updateItem(ItemStack stack, Mat material, MatCondition condition, IEnvironment environment)
	{
		return updateItem(stack, material, condition, environment, DEFAULT_SAVE_TAG);
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
		addInformation(stack, material, condition, list, DEFAULT_SAVE_TAG);
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
	float entityAttackDamageMultiple(ItemStack stack, Mat material, Entity target, String saveTag);
}