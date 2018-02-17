/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.items.behavior;

import farcore.data.EnumToolTypes;
import nebula.common.util.ItemStacks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author ueyudiud
 */
public class BehaviorChiselHelpingSilkTouch extends BehaviorTool
{
	@Override
	public ItemStack onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			if (isSelected && player.isSneaking() && EnumToolTypes.CHISEL.match(player.inventory.getStackInSlot(itemSlot + 1)))
			{
				NBTTagCompound nbt = ItemStacks.getOrSetupNBT(stack, true);
				NBTTagList list;
				label:
				{
					if (nbt.hasKey("ench", NBT.TAG_LIST))
					{
						list = nbt.getTagList("ench", 10);
						for (int i = 0; i < list.tagCount(); ++i)
						{
							NBTTagCompound nbt1 = list.getCompoundTagAt(i);
							if (nbt1.hasKey("shifting") && nbt1.getShort("id") == Enchantment.getEnchantmentID(Enchantments.SILK_TOUCH))
							{
								break label;
							}
						}
					}
					else
					{
						list = new NBTTagList();
						nbt.setTag("ench", list);
					}
					NBTTagCompound nbt1 = new NBTTagCompound();
					list.appendTag(nbt1);
					nbt1.setShort("id", (short) Enchantment.getEnchantmentID(Enchantments.SILK_TOUCH));
					nbt1.setShort("lvl", (short) 1);
					nbt1.setBoolean("shifting", true);
				}
			}
			else if (stack.hasTagCompound())
			{
				NBTTagCompound nbt = stack.getTagCompound();
				if (nbt.hasKey("ench", NBT.TAG_LIST))
				{
					NBTTagList list = nbt.getTagList("ench", 10);
					for (int i = 0; i < list.tagCount(); ++i)
					{
						NBTTagCompound nbt1 = list.getCompoundTagAt(i);
						if (nbt1.hasKey("shifting"))
						{
							list.removeTag(i);
							break;
						}
					}
					if (list.hasNoTags())
					{
						nbt.removeTag("ench");
					}
				}
			}
			return stack;
		}
		return super.onUpdate(stack, world, entity, itemSlot, isSelected);
	}
}
