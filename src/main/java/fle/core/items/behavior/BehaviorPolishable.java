/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.items.behavior;

import farcore.lib.item.ItemTool;
import farcore.lib.material.Mat;
import fle.api.item.behavior.IPolishableBehavior;
import nebula.common.item.BehaviorBase;
import nebula.common.item.ITool;
import nebula.common.tool.EnumToolType;
import nebula.common.util.L;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class BehaviorPolishable extends BehaviorBase implements IPolishableBehavior
{
	EnumToolType type;
	int levelOffset;
	float damageAmount;
	char target;
	char[] allowTransferStates;
	
	public BehaviorPolishable(EnumToolType toolType, int levelOffset, float damage, char target, char...cs)
	{
		this.type = toolType;
		this.levelOffset = levelOffset;
		this.target = target;
		this.damageAmount = damage;
		this.allowTransferStates = cs;
	}
	
	@Override
	public int getPolishLevel(ItemStack stack)
	{
		Mat material = ItemTool.getMaterial(stack, "head");
		return material.toolHarvestLevel + this.levelOffset;
	}
	
	@Override
	public char getPolishResult(ItemStack stack, char base)
	{
		return L.contain(this.allowTransferStates, base) ? this.target : base;
	}
	
	@Override
	public void onPolished(EntityPlayer player, ItemStack stack)
	{
		((ITool) stack.getItem()).onToolUse(player, stack, this.type, this.damageAmount);
	}
}