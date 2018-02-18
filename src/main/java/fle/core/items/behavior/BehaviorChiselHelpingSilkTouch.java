/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.items.behavior;

import static nebula.common.util.EnumChatFormatting.WHITE;
import static nebula.common.util.EnumChatFormatting.YELLOW;

import farcore.data.EnumToolTypes;
import farcore.data.MC;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.tool.ToolHooks;
import nebula.common.util.ItemStacks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BehaviorChiselHelpingSilkTouch extends BehaviorTool
{
	{
		LanguageManager.registerLocal("info.chisel.helping.silk.touch",
				WHITE + "Put " + YELLOW + "%s" + WHITE + " to right slot in order to harvest block in silk touching when player is sneaking.");
	}
	
	@Override
	public ItemStack onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			if (isSelected && !player.isCreative() && player.isSneaking() && EnumToolTypes.CHISEL.match(player.inventory.getStackInSlot(itemSlot + 1)))
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
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity)
	{
		if (entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			ItemStack stack2 = player.inventory.getStackInSlot(player.inventory.currentItem + 1);
			if (player.isSneaking() && EnumToolTypes.CHISEL.match(stack2) &&
					ToolHooks.isToolHarvestable(state.getBlock(), world, pos, player) && state.getBlock().canSilkHarvest(world, pos, state, player))
			{
				ItemStacks.damageTool(stack2, 0.5F, player, EnumToolTypes.CHISEL);
				if (stack2.stackSize <= 0)
				{
					player.inventory.removeStackFromSlot(player.inventory.currentItem + 1);
				}
				return true;
			}
		}
		return super.onBlockDestroyed(stack, world, state, pos, entity);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, UnlocalizedList list, boolean advanced)
	{
		list.add("info.chisel.helping.silk.touch", MC.chisel_metal.translateToLocal());
	}
}
