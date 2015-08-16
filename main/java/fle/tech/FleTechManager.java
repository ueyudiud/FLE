package fle.tech;

import net.minecraft.entity.player.EntityPlayer;
import fle.api.tech.ITechManager;
import fle.api.tech.PlayerTechInfo;
import fle.api.tech.TechClass;
import fle.api.tech.Technology;
import fle.api.util.Register;

/**
 * This system have not complete yet.
 * @author ueyudiud
 *
 */
@Deprecated
public class FleTechManager extends ITechManager
{
	private final Register<Technology> register1 = new Register();
	private final Register<TechClass> register2 = new Register();
	
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
		return new FlePlayerTechInfo(player);
	}

	@Override
	public Register<Technology> getTechs() 
	{
		return register1;
	}
}