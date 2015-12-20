package fle.api.tech;

import net.minecraft.entity.player.EntityPlayer;
import flapi.collection.Register;

public abstract class ITechManager
{
	public static ITechTag flaBase;
	
	public static Technology fire;
	public static Technology mineTire1;
	public static Technology agricultureTire1;
	
	public abstract Technology registerTech(Technology tech);

	public abstract ITechTag registerTechClass(ITechTag clazz);

	public abstract Technology getTechFromId(String id);

	public abstract int getIdFromTech(Technology tech);

	public abstract PlayerTechInfo getPlayerInfo(EntityPlayer player);

	public abstract Register<Technology> getTechs();
}
