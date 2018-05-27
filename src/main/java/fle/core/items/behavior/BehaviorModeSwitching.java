/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.items.behavior;

import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.item.BehaviorBase;
import nebula.common.util.ItemStacks;
import nebula.common.util.NBTs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class BehaviorModeSwitching extends BehaviorBase
{
	private String		key;
	private String[]	modes;
	
	public BehaviorModeSwitching(String key, String...modes)
	{
		this.key = key;
		this.modes = modes;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
	{
		if (player.isSneaking())
		{
			if (!world.isRemote)
			{
				NBTTagCompound nbt = ItemStacks.getOrSetupNBT(stack, true);
				NBTs.setRemovableNumber(nbt, "mode", (nbt.getInteger("mode") + 1) % this.modes.length);
			}
		}
		return super.onItemRightClick(stack, world, player, hand);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, UnlocalizedList list, boolean advanced)
	{
		list.add("info.mode.type", LanguageManager.translateLocal("info.mode." + this.key + "." + this.modes[NBTs.getIntOrDefault(ItemStacks.getOrSetupNBT(stack, false), "mode", 0)]));
		super.addInformation(stack, player, list, advanced);
	}
}
