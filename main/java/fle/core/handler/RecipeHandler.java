package fle.core.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import farcore.util.U.I;
import flapi.enums.EnumDamageResource;
import flapi.recipe.stack.AbstractStack;
import flapi.recipe.stack.ArrayStack;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.OreStack;
import fle.core.init.IB;
import fle.core.item.ItemFleSeed;
import fle.core.item.ItemFleSub;
import fle.core.tool.AxeHandler;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class RecipeHandler 
{
	private static final List<IRecipe> list1 = new ArrayList();
//	private static final Map<IRecipe, IDivideRecipe> list2 = new HashMap();
	private final AbstractStack decortingPlate = new OreStack("craftingToolDecortingPlate");
	private final AbstractStack decortingStick = new OreStack("craftingToolDecortingStick");
	private final AbstractStack chisel = new OreStack("craftingToolChisel");
	private final AbstractStack needle = new OreStack("craftingToolNeedle");
	private final AbstractStack hardHammer = new OreStack("craftingToolHardHammer");
	private final AbstractStack saw = new OreStack("craftingToolSaw");
	private final AbstractStack adz = new OreStack("craftingToolAdz");
	private final AbstractStack whetstone = new OreStack("craftingToolWhetstone");
	private final AbstractStack spinning = new OreStack("craftingToolSpinning");
	private final AbstractStack knife = new OreStack("craftingToolKnife");
	private final AbstractStack woodenBucket = new ArrayStack(ItemFleSub.a("wood_bucket_0_plant_ash_mortar"), 
			ItemFleSub.a("wood_bucket_0_lime_mortar"));
	private final AbstractStack cotton = new BaseStack(ItemFleSub.a("cotton_rough"));

	public static void setNoUseTool(IRecipe recipe)
	{
		list1.add(recipe);
	}
//	public static void setDivideItem(IRecipe recipe, IDivideRecipe recipe2)
//	{
//		list2.put(recipe, recipe2);
//	}
	
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent evt)
	{
		for(IRecipe recipe : list1)
			if(recipe.matches((InventoryCrafting) evt.craftMatrix, evt.player.worldObj))
				return;
//		for(Entry<IRecipe, IDivideRecipe> recipe : list2.entrySet())
//			if(recipe.getKey().matches((InventoryCrafting) evt.craftMatrix, evt.player.worldObj))
//			{
//				recipe.getValue().matchAndSet(evt.craftMatrix, evt.player);
//			}
		for(int i = 0; i < evt.craftMatrix.getSizeInventory(); ++i)
		{
			ItemStack stack = evt.craftMatrix.getStackInSlot(i);
			if(stack != null)
			{
				if(AxeHandler.isItemAxe(stack))
				{
					++stack.stackSize;
					I.damageItem(evt.player, stack, EnumDamageResource.Crafting, 1F);
					continue;
				}
				else if(decortingPlate.similar(stack))
				{
					++stack.stackSize;
					I.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.125F);
				}
				else if(decortingStick.similar(stack))
				{
					++stack.stackSize;
					I.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.125F);
				}
				else if(chisel.similar(stack))
				{
					++stack.stackSize;
					I.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.5F);
				}
				else if(needle.similar(stack))
				{
					++stack.stackSize;
					I.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.0625F);
				}
				else if(hardHammer.similar(stack))
				{
					++stack.stackSize;
					I.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.5F);
				}
				else if(saw.similar(stack))
				{
					++stack.stackSize;
					I.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.5F);
				}
				else if(adz.similar(stack))
				{
					++stack.stackSize;
					I.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.5F);
				}
				else if(whetstone.similar(stack))
				{
					++stack.stackSize;
					I.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.1875F);
				}
				else if(spinning.similar(stack))
				{
					++stack.stackSize;
					I.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.0625F);
				}
				else if(woodenBucket.similar(stack))
				{
					++stack.stackSize;
					IB.subItem.setDamage(stack, ItemFleSub.a("wood_bucket_0_empty").getItemDamage());
				}
				else if(knife.similar(stack))
				{
					++stack.stackSize;
					I.damageItem(evt.player, stack, EnumDamageResource.Crafting, 0.25F);
				}
				else if(cotton.similar(stack))
				{
					if(evt.player.getRNG().nextBoolean())
						evt.player.dropPlayerItemWithRandomChoice(ItemFleSeed.a("cotton"), false);
				}
			}
		}
	}
}