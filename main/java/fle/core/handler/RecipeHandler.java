package fle.core.handler;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import flapi.FleAPI;
import flapi.enums.EnumDamageResource;
import flapi.recipe.stack.ArrayStack;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.recipe.stack.OreStack;
import fle.core.init.IB;
import fle.core.item.ItemFleSeed;
import fle.core.item.ItemFleSub;
import fle.core.tool.AxeHandler;

public class RecipeHandler 
{
	private final ItemAbstractStack decortingPlate = new OreStack("craftingToolDecortingPlate");
	private final ItemAbstractStack decortingStick = new OreStack("craftingToolDecortingStick");
	private final ItemAbstractStack chisel = new OreStack("craftingToolChisel");
	private final ItemAbstractStack needle = new OreStack("craftingToolNeedle");
	private final ItemAbstractStack hardHammer = new OreStack("craftingToolHardHammer");
	private final ItemAbstractStack saw = new OreStack("craftingToolSaw");
	private final ItemAbstractStack adz = new OreStack("craftingToolAdz");
	private final ItemAbstractStack whetstone = new OreStack("craftingToolWhetstone");
	private final ItemAbstractStack spinning = new OreStack("craftingToolSpinning");
	private final ItemAbstractStack knife = new OreStack("craftingToolKnife");
	private final ItemAbstractStack woodenBucket = new ArrayStack(ItemFleSub.a("wood_bucket_0_plant_ash_mortar"), 
			ItemFleSub.a("wood_bucket_0_lime_mortar"));
	private final ItemAbstractStack cotton = new BaseStack(ItemFleSub.a("cotton_rough"));
	
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
				else if(decortingPlate.equal(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.125F);
				}
				else if(decortingStick.equal(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.125F);
				}
				else if(chisel.equal(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.5F);
				}
				else if(needle.equal(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.0625F);
				}
				else if(hardHammer.equal(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.5F);
				}
				else if(saw.equal(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.5F);
				}
				else if(adz.equal(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.5F);
				}
				else if(whetstone.equal(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.1875F);
				}
				else if(spinning.equal(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.0625F);
				}
				else if(woodenBucket.equal(stack))
				{
					++stack.stackSize;
					IB.subItem.setDamage(stack, ItemFleSub.a("wood_bucket_0_empty").getItemDamage());
				}
				else if(knife.equal(stack))
				{
					++stack.stackSize;
					FleAPI.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.25F);
				}
				else if(cotton.equal(stack))
				{
					if(evt.player.getRNG().nextBoolean())
						evt.player.dropPlayerItemWithRandomChoice(ItemFleSeed.a("cotton"), false);
				}
			}
		}
	}
}