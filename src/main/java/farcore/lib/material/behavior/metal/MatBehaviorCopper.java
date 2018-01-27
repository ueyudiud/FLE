/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.material.behavior.metal;

import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.lib.material.behavior.IItemMatProp;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.environment.IEnvironment;
import nebula.common.util.ItemStacks;
import nebula.common.util.Strings;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class MatBehaviorCopper implements IItemMatProp
{
	{
		LanguageManager.registerLocal("info.material.behavior.metal.copper.rustness", "Rustness: %s");
	}
	
	@Override
	public int getOffsetMetaCount()
	{
		return 4;
	}
	
	@Override
	public String getReplacedLocalName(int metaOffset, Mat material)
	{
		switch (metaOffset)
		{
		case 3:
			return "Rusted " + material.localName;
		case 2:
		case 1:
			return "Rusting " + material.localName;
		default:
			return material.localName;
		}
	}
	
	@Override
	public void setInstanceFromMeta(ItemStack stack, int metaOffset, Mat material, MatCondition condition, String saveTag)
	{
		ItemStacks.getSubOrSetupNBT(stack, saveTag, true).setFloat("rustness", (metaOffset & 0x3) / 3.0F);
	}
	
	@Override
	public int getMetaOffset(ItemStack stack, Mat material, MatCondition condition, String saveTag)
	{
		return Math.round(ItemStacks.getSubOrSetupNBT(stack, saveTag, false).getFloat("rustness") * 3);
	}
	
	@Override
	public ItemStack updateItem(ItemStack stack, Mat material, MatCondition condition, IEnvironment environment, String saveTag)
	{
		return stack;
	}
	
	@Override
	public void addInformation(ItemStack stack, Mat material, MatCondition condition, UnlocalizedList list, String saveTag)
	{
		list.add("info.material.behavior.metal.copper.rustness", Strings.progress(ItemStacks.getSubOrSetupNBT(stack, saveTag, false).getFloat("rustness")));
	}
	
	@Override
	public float entityAttackDamageMultiple(ItemStack stack, Mat material, Entity target, String saveTag)
	{
		return 1.0F - 0.4F * ItemStacks.getSubOrSetupNBT(stack, saveTag, true).getFloat("rustness");
	}
}
