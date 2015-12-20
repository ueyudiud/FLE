package fle.core.tech;

import net.minecraft.entity.player.EntityPlayer;
import flapi.collection.Register;
import flapi.tech.ITechManager;
import flapi.tech.ITechTag;
import flapi.tech.PlayerTechInfo;
import flapi.tech.Technology;

/**
 * This system have not complete yet.
 * @author ueyudiud
 *
 */
public class FleTechManager extends ITechManager
{
	private final Register<Technology> register1 = new Register();
	private final Register<ITechTag> register2 = new Register();
	
	public FleTechManager init()
	{
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

	public ITechTag registerTechClass(ITechTag tag)
	{
		register2.register(tag, tag.getTechTagName());
		return tag;
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
		return new FlePlayerTechInfo(player);
	}
	
	@Override
	public Register<Technology> getTechs() 
	{
		return register1;
	}
}