/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.material.behavior;

import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import nebula.client.util.UnlocalizedList;
import nebula.common.environment.IEnvironment;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The material property.
 * 
 * @author ueyudiud
 */
public interface IMaterialProperty<T, N extends NBTBase>
{
	default Class<T> getTargetType()
	{
		return MaterialPropertyManager.getType(this);
	}
	
	default int getOffsetMetaCount()
	{
		return 1;
	}
	
	default String getReplacedLocalName(int metaOffset, Mat material)
	{
		return material.localName;
	}
	
	default T instance(Mat material)
	{
		return instance(0, material);
	}
	
	T instance(int metaOffset, Mat material);
	
	/**
	 * Get offset meta.
	 * 
	 * @param value
	 * @param material
	 * @param condition
	 * @return
	 * @see nebula.common.item.ItemBase#getStackMetaOffset(ItemStack)
	 */
	default int getMetaOffset(T value, Mat material)
	{
		return 0;
	}
	
	T copyOf(T source, Mat material);
	
	/**
	 * Called when item update.
	 * 
	 * @param stack
	 * @param material
	 * @param condition
	 * @param environment
	 * @return
	 */
	default ItemStack updateItem(T value, ItemStack stack, Mat material, MatCondition condition, IEnvironment environment)
	{
		return stack;
	}
	
	@SideOnly(Side.CLIENT)
	default void addInformation(T value, Mat material, MatCondition condition, UnlocalizedList list)
	{
		
	}
	
	/**
	 * Get entity attack damage multiple.
	 * 
	 * @param stack
	 * @param material
	 * @param target
	 * @return
	 */
	default float entityAttackDamageMultiple(T value, Mat material, Entity target)
	{
		return 1.0F;
	}
	
	N write(Mat material, T value);
	
	T read(N tag, Mat material);
}
