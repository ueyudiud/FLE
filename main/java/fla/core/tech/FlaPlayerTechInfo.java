package fla.core.tech;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import fla.api.FlaAPI;
import fla.api.tech.PlayerTechInfo;
import fla.api.tech.Technology;
import fla.core.Fla;

public class FlaPlayerTechInfo extends PlayerTechInfo
{
	public FlaPlayerTechInfo(EntityPlayer aPlayer) 
	{
		super(aPlayer);
	}

	@Override
	public boolean isPlayerKnowTech(Technology tech) 
	{
		NBTTagCompound nbt = getPlayerTechNBT();
		return nbt.getBoolean(tech.getName());
	}

	@Override
	public List<Technology> getPlayerTechList() 
	{
		List<Technology> list = new ArrayList();
		for(Technology tech : FlaAPI.techManager.getTechs())
		{
			if(isPlayerKnowTech(tech))
			{
				list.add(tech);
			}
		}
		return list;
	}

	@Override
	public void setTech(Technology tech) 
	{
		getPlayerTechNBT().setBoolean(tech.getName(), true);
		if(player instanceof EntityPlayerMP)
			Fla.fla.nwm.get().initatePlayerTechupdate((EntityPlayerMP) player, tech, (byte) 0);
	}

	@Override
	public void removeTech(Technology tech) 
	{
		getPlayerTechNBT().setBoolean(tech.getName(), false);
		if(player instanceof EntityPlayerMP)
			Fla.fla.nwm.get().initatePlayerTechupdate((EntityPlayerMP) player, tech, (byte) 1);
	}

	private NBTTagCompound getPlayerTechNBT()
	{
		if(player == null) return new NBTTagCompound();
		if(!player.getEntityData().hasKey("FLE"))
		{
			player.getEntityData().setTag("FLE", new NBTTagCompound());
		}
		if(!player.getEntityData().getCompoundTag("FLE").hasKey("Tech"))
		{
			player.getEntityData().getCompoundTag("FLE").setTag("Tech", new NBTTagCompound());
		}
		return player.getEntityData().getCompoundTag("FLE").getCompoundTag("Tech");
	}
}