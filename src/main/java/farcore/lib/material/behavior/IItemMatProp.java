/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.material.behavior;

import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import nebula.client.util.UnlocalizedList;
import nebula.common.environment.IEnvironment;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The material property.
 * 
 * @author ueyudiud
 */
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
	
	/**
	 * Get offset meta.
	 * 
	 * @param stack
	 * @param material
	 * @param condition
	 * @param saveTag
	 * @return
	 * @see nebula.common.item.ItemBase#getStackMetaOffset(ItemStack)
	 */
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
	 * @param saveTag Some item might is multiple materials item, give material
	 *            a saving NBT tag.
	 * @return
	 */
	ItemStack updateItem(ItemStack stack, Mat material, MatCondition condition, IEnvironment environment, String saveTag);
	
	@SideOnly(Side.CLIENT)
	default void addInformation(ItemStack stack, Mat material, MatCondition condition, UnlocalizedList list)
	{
		addInformation(stack, material, condition, list, DEFAULT_SAVE_TAG);
	}
	
	/**
	 * Add extra information (For material property specific) to tool tips.
	 * 
	 * @param stack
	 * @param material
	 * @param condition
	 * @param list
	 * @param saveTag
	 */
	@SideOnly(Side.CLIENT)
	void addInformation(ItemStack stack, Mat material, MatCondition condition, UnlocalizedList list, String saveTag);
	
	/**
	 * Get entity attack damage multiple.
	 * 
	 * @param stack
	 * @param material
	 * @param target
	 * @return
	 */
	float entityAttackDamageMultiple(ItemStack stack, Mat material, Entity target, String saveTag);
}
