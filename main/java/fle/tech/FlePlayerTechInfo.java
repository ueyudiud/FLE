package fle.tech;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.eventhandler.Event.Result;
import fle.FLE;
import fle.api.tech.PlayerTechInfo;
import fle.api.tech.Technology;
import fle.core.net.FlePackets.CoderPTUpdate;

/**
 * This system have not complete yet.
 * @author ueyudiud
 *
 */
@Deprecated
public class FlePlayerTechInfo extends PlayerTechInfo
{
	public FlePlayerTechInfo(EntityPlayer aPlayer) 
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
		for(Technology tech : FLE.fle.getTechManager().getTechs())
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
			FLE.fle.getNetworkHandler().sendToPlayer(new CoderPTUpdate(tech, Result.ALLOW), (EntityPlayerMP) player);
	}

	@Override
	public void removeTech(Technology tech) 
	{
		getPlayerTechNBT().setBoolean(tech.getName(), false);
		if(player instanceof EntityPlayerMP)
			FLE.fle.getNetworkHandler().sendToPlayer(new CoderPTUpdate(tech, Result.DENY), (EntityPlayerMP) player);
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