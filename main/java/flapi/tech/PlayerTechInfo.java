package flapi.tech;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

public abstract class PlayerTechInfo 
{
	protected final EntityPlayer player;
	
	protected PlayerTechInfo(EntityPlayer aPlayer)
	{
		player = aPlayer;
	}
	
	public abstract boolean isPlayerKnowTech(Technology tech);
	
	public abstract List<Technology> getPlayerTechList();
	
	public abstract void setTech(Technology tech);
	
	public abstract void removeTech(Technology tech);
}
