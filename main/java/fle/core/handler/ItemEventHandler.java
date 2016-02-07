package fle.core.handler;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameData;
import farcore.item.interfaces.IItemSize;
import farcore.substance.SubstanceRegistry;
import farcore.substance.SubstanceRegistry.Key$;
import fle.core.FLE;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.oredict.OreDictionary;

public class ItemEventHandler
{
	private static final String shiftTip = 
			String.format("Press %s<F3 + H>%s to get more infomation.", 
					EnumChatFormatting.AQUA.toString(),
					EnumChatFormatting.GRAY.toString());
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onToolTip(ItemTooltipEvent evt)
	{
		if(evt.entityPlayer.capabilities.isCreativeMode &&
				evt.showAdvancedItemTooltips)
		{
			evt.toolTip.add("Far land era infomations : ");
			evt.toolTip.add("Item data name : " 
			+ GameData.getItemRegistry().getNameForObject(evt.itemStack.getItem()));
			int[] is = OreDictionary.getOreIDs(evt.itemStack);
			if(is.length > 0)
			{
				evt.toolTip.add("Ore dictionary name : ");
				for(int id : is)
				{
					evt.toolTip.add(OreDictionary.getOreName(id));
				}
			}
			if(!evt.itemStack.getItem().getHasSubtypes())
			{
				int max = evt.itemStack.getMaxDamage();
				int d = evt.itemStack.getItemDamage();
				evt.toolTip.add("Item Damage : " + (max - d) + "/" + max);
			}
			Key$ key = SubstanceRegistry.getSubstance(evt.itemStack);
			if(key != null)
			{
				evt.toolTip.add("Substance dictionary info : ");
				evt.toolTip.add("Part : " + EnumChatFormatting.WHITE + key.part.name);
				evt.toolTip.add("Resolution : " + key.part.resolution);
				evt.toolTip.add("Substance : " + key.substance.getName());
				if(key.substance.matter != null)
					evt.toolTip.add("Matter : " + EnumChatFormatting.RED + key.substance.matter.getChemName());
			}
			if(evt.itemStack.getItem() instanceof IItemSize)
			{
				IItemSize item = (IItemSize) evt.itemStack.getItem();
				evt.toolTip.add("Size info : ");
				evt.toolTip.add("Item Size : " + item.getItemSize(evt.itemStack).name());
				evt.toolTip.add("Particle Size : " + item.getParticleSize(evt.itemStack).name());
			}
			if(evt.itemStack.getItem() instanceof IFluidContainerItem)
			{
				IFluidContainerItem item = (IFluidContainerItem) evt.itemStack.getItem();
				evt.toolTip.add("Fluid handler info : ");
				evt.toolTip.add("Capacity : " + item.getCapacity(evt.itemStack));
				FluidStack stack = item.getFluid(evt.itemStack);
				if(stack != null)
				{
					evt.toolTip.add("Fluid name : " + EnumChatFormatting.WHITE + stack.getLocalizedName());
					evt.toolTip.add("Amount : " + EnumChatFormatting.AQUA + stack.amount);
				}
			}
			else if(FluidContainerRegistry.isContainer(evt.itemStack))
			{
				evt.toolTip.add("Capacity : " + FluidContainerRegistry.getContainerCapacity(evt.itemStack));
				FluidStack stack = FluidContainerRegistry.getFluidForFilledItem(evt.itemStack);
				if(stack != null)
				{
					evt.toolTip.add("Fluid name : " + EnumChatFormatting.WHITE + stack.getLocalizedName());
					evt.toolTip.add("Amount : " + EnumChatFormatting.AQUA + stack.amount);
				}
			}
		}
		else
		{
			evt.toolTip.add(shiftTip);
		}
	}
}