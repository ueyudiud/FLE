package fle.matter;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import farcore.collection.Register;
import farcore.substance.Matter;
import farcore.util.Part;
import flapi.material.MaterialAbstract;
import flapi.recipe.stack.OreStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

public class FleMatter
{
	public static final Register<MaterialAbstract> register = new Register();
	
	public static void registerFLEMatter(MaterialAbstract material, int index)
	{
		register.register(index, material, material.getName());
	}
	
	final Part elementary = Part.single;
	
	@SubscribeEvent
	public void addToolTipas(ItemTooltipEvent evt)
	{
		if(!evt.entityPlayer.capabilities.isCreativeMode) return;
		try
		{
			List<String> str = new ArrayList();
			for(int idx : OreDictionary.getOreIDs(evt.itemStack))
			{
				str.add(OreDictionary.getOreName(idx));
			}
			if(!str.isEmpty())
			{
				evt.toolTip.add("[FLE] Ore Dict : ");
				evt.toolTip.addAll(str);
			}
//			MatterStack matter = MatterDictionary.toMatter(evt.itemStack);
//			evt.toolTip.add("[FLE] Matter : " + matter.getDisplayName());
		}
		catch(Throwable e){}
	}
	
	@SubscribeEvent
	public void register(OreRegisterEvent evt)
	{
//		for(Part part : Part.values())
//		{
//			if(part == Part.ore) continue;
//			if(evt.Name.startsWith(part.name))
//			{
//				String lastName = evt.Name.substring(part.name.length());
//				if(MatterDictionary.toMatter(evt.Ore) == null && Matter.getMatterFromName(lastName.toLowerCase().replaceAll(" ", "")) != null)
//				{
//					MatterDictionary.registerMatter(new OreStack(evt.Name), Matter.getMatterFromName(lastName), part);
//					break;					
//				}
//			}
//		}
	}
}