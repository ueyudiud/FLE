package fle.load;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import farcore.FarCoreRegistry;
import farcore.enums.EnumItem;
import farcore.enums.EnumToolType;
import farcore.event.AchievementDetectEvent.Type;
import farcore.handler.FarCoreAchievementHandler;
import farcore.lib.stack.BaseStack;
import farcore.lib.stack.OreStack;
import fle.api.gui.ContainerCraftingFle;
import fle.api.gui.ContainerPlayerCraftingFle;
import fle.core.achievement.AchievementFle;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

public class Achievements
{
	public static Achievement openCrafting;
	public static Achievement digGravel;
	public static Achievement makeBarGrizzly;
	public static Achievement sharpness;
	public static Achievement log;
	public static Achievement polishing;
	public static Achievement fire;
	
	public static void init()
	{
		openCrafting = new AchievementFle("stoneAge", "openCrafting", 0, 0, EnumItem.debug.instance(), AchievementList.openInventory, false);
		digGravel = new AchievementFle("stoneAge", "digGravel", 0, 2, EnumItem.pile.instance(1, "gravel"), openCrafting, false);
		makeBarGrizzly = new AchievementFle("stoneAge", "makeBarGrizzly", -2, 1, EnumItem.tool.instance(1, "bar_grizzly", "simple_bar_grizzly"), openCrafting, false);
		sharpness = new AchievementFle("stoneAge", "sharpness", 4, 2, EnumItem.stone_chip.instance(1, "flintSharp"), digGravel, false);
		log = new AchievementFle("stoneAge", "log", 4, 4, EnumItem.log.instance(1), sharpness, false);
		polishing = new AchievementFle("stoneAge", "polishing", 2, 4, new ItemStack(BlockItems.machineIAlpha, 1, 0), log, false);
		fire = new AchievementFle("stoneAge", "fire", 6, 4, new ItemStack(BlockItems.campfire), log, false);
		AchievementFle.registerAchievementPage("Fle Stone Age", "stoneAge");
		FarCoreAchievementHandler.addAchievementRequirement(digGravel, Type.PICKUP, new OreStack("pileGravel"));
		FarCoreAchievementHandler.addAchievementRequirement(makeBarGrizzly, Type.CRAFTED, EnumToolType.bar_grizzly.stack());
		FarCoreAchievementHandler.addAchievementRequirement(sharpness, Type.CRAFTED, new BaseStack(EnumItem.stone_chip.instance(1, "flintSharp")));
		FarCoreAchievementHandler.addAchievementRequirement(log, Type.CRAFTED, new OreStack("logWood"));
		FarCoreAchievementHandler.addAchievementRequirement(digGravel, Type.CRAFTED, new OreStack("pileGravel"));
		FarCoreAchievementHandler.addAchievementRequirement(polishing, Type.CRAFTED, new BaseStack(BlockItems.machineIAlpha, 1, 0));
		FarCoreAchievementHandler.addAchievementRequirement(fire, Type.PICKUP, new OreStack("firewood"));
		
		FarCoreRegistry.registerMFEventHandler(new Achievements());
	}
	
	@SubscribeEvent
	public void onContainerOpen(PlayerOpenContainerEvent event)
	{
		if(event.entityPlayer.openContainer instanceof ContainerPlayerCraftingFle)
		{
			event.entityPlayer.addStat(openCrafting, 1);
		}
	}
}