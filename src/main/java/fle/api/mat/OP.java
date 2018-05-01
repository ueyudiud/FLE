/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.api.mat;

import java.util.List;

import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.lib.oredict.OreDictExt;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class OP
{
	public static Mat materialOf(MatCondition condition, ItemStack stack)
	{
		if (ItemMulti.getMaterialCondition(stack) == condition)
		{
			return ItemMulti.getMaterial(stack);
		}
		List<String> list = OreDictExt.getOreNames(stack);
		for (Mat material : Mat.filt(condition))
		{
			if (list.contains(condition.getOreName(material)))
			{
				return material;
			}
		}
		return null;
	}
}
