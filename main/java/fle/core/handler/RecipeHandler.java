package fle.core.handler;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import fle.api.FleAPI;
import fle.api.enums.EnumDamageResource;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemArrayStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.recipe.ItemOreStack;
import fle.core.init.IB;
import fle.core.item.ItemFleSeed;
import fle.core.item.ItemFleSub;

public class RecipeHandler 
{
	private final ItemAbstractStack decortingPlate = new ItemOreStack("craftingToolDecortingPlate");
	private final ItemAbstractStack decortingStick = new ItemOreStack("craftingToolDecortingStick");
	private final ItemAbstractStack chisel = new ItemOreStack("craftingToolChisel");
	private final ItemAbstractStack needle = new ItemOreStack("craftingToolNeedle");
	private final ItemAbstractStack hardHammer = new ItemOreStack("craftingToolHardHammer");
	private final ItemAbstractStack saw = new ItemOreStack("craftingToolSaw");
	private final ItemAbstractStack adz = new ItemOreStack("craftingToolAdz");
	private final ItemAbstractStack whetstone = new ItemOreStack("craftingToolWhetstone");
	private final ItemAbstractStack spinning = new ItemOreStack("craftingToolSpinning");
	private final ItemAbstractStack woodenBucket = new ItemArrayStack(ItemFleSub.a("wood_bucket_0_plant_ash_mortar"), 
			ItemFleSub.a("wood_bucket_0_lime_mortar"));
	private final ItemAbstractStack cotton = new ItemBaseStack(ItemFleSub.a("cotton_rough"));
	
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent evt)
	{
		for(int i = 0; i < evt.craftMatrix.getSizeInventory(); ++i)
		{
			ItemStack stack = evt.craftMatrix.getStackInSlot(i);
			if(stack != null)
			{
				if(AxeHandler.isItemAxe(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 1F);
					continue;
				}
				else if(decortingPlate.isStackEqul(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.125F);
				}
				else if(decortingStick.isStackEqul(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.125F);
				}
				else if(chisel.isStackEqul(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.5F);
				}
				else if(needle.isStackEqul(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.0625F);
				}
				else if(hardHammer.isStackEqul(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.5F);
				}
				else if(saw.isStackEqul(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.5F);
				}
				else if(adz.isStackEqul(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.5F);
				}
				else if(whetstone.isStackEqul(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.1875F);
				}
				else if(spinning.isStackEqul(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.0625F);
				}
				else if(woodenBucket.isStackEqul(stack))
				{
					++stack.stackSize;
					IB.subItem.setDamage(stack, ItemFleSub.a("wood_bucket_0_empty").getItemDamage());
				}
				else if(cotton.isStackEqul(stack))
				{
					if(evt.player.getRNG().nextBoolean())
						evt.player.dropPlayerItemWithRandomChoice(ItemFleSeed.a("cotton"), false);
				}
			}
		}
	}
}