/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.material.behavior;

import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import nebula.common.environment.IEnvironment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;

/**
 * @author ueyudiud
 */
public abstract class MatBehaviorTemperature<T, N extends NBTBase> implements IMaterialProperty<T, N>
{
	@Override
	public ItemStack updateItem(T arg, ItemStack stack, Mat material, MatCondition condition, IEnvironment environment)
	{
		int heat = getHeat(arg);
		if (heat > 0)
		{
			setHeat(arg, heat - 1);
		}
		return stack;
	}
	
	protected abstract int getHeat(T arg);
	
	protected abstract void setHeat(T arg, int value);
}
