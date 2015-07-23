package fla.core.tech;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import fla.api.FlaAPI;
import fla.api.recipe.IItemChecker.ItemChecker;
import fla.api.tech.ITechManager;
import fla.api.tech.PlayerTechInfo;
import fla.api.tech.TechClass;
import fla.api.tech.Technology;
import fla.api.tech.Technology.LearningType;
import fla.api.util.Registry;
import fla.core.FlaBlocks;
import fla.core.FlaItems;
import fla.core.tech.FlaTechnology.BreakBlockCheck;
import fla.core.tech.FlaTechnology.CraftingCheck;

public class TechManager extends ITechManager
{
	private final Registry<Technology> register1 = new Registry();
	private final Registry<TechClass> register2 = new Registry();
	
	public TechManager init()
	{
		flaBase = FlaAPI.techManager.registerTechClass(new TechClass("FLE Base"));
		fire = FlaAPI.techManager.registerTech(new FlaTechnology("Fire")
		.setLearningTypes(LearningType.Break)
		.setLearningCheck(new BreakBlockCheck(Blocks.tallgrass))
		.setPages(new FlaPageText(Blocks.fire).setText(""))
		.setTechClassBelong(flaBase));
		mineTire1 = FlaAPI.techManager.registerTech(new FlaTechnology("OreI")
		.setLearningTypes(LearningType.Break)
		.setLearningCheck(new BreakBlockCheck(FlaBlocks.ore1))
		.setPages(new FlaPageText(FlaBlocks.ore1).setText(""))
		.setTechClassBelong(flaBase));
		agricultureTire1 = FlaAPI.techManager.registerTech(new FlaTechnology("AgricultureI")
		.setLearningTypes(LearningType.Crafting)
		.setLearningCheck(new CraftingCheck(new ItemChecker(FlaItems.stone_spade_hoe)))
		.setPages(new FlaPageText(FlaBlocks.ore1).setText(""))
		.setTechClassBelong(flaBase));
		return this;
	}
	public Technology registerTech(Technology tech)
	{
		register1.register(tech, tech.getName());
		if(tech.getTechClassBelong() != null)
		{
			tech.getTechClassBelong().setTechList(tech);
		}
		PlayerTechLearningHandler.registerLearning(tech);
		return tech;
	}

	public TechClass registerTechClass(TechClass clazz)
	{
		register2.register(clazz, clazz.getTechClassName());
		return clazz;
	}
	
	public Technology getTechFromId(String id)
	{
		return register1.get(id);
	}
	
	public int getIdFromTech(Technology tech)
	{
		return register1.serial(tech);
	}
	
	public PlayerTechInfo getPlayerInfo(EntityPlayer player)
	{
		return new FlaPlayerTechInfo(player);
	}

	@Override
	public Registry<Technology> getTechs() 
	{
		return register1;
	}
}