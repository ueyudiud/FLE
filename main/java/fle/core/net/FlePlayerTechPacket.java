package fle.core.net;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.eventhandler.Event.Result;
import fle.api.FleAPI;
import fle.api.net.FleAbstractPacket;
import fle.api.net.FleNetworkHandler;
import fle.api.tech.Technology;
import fle.api.util.FleDataInputStream;
import fle.api.util.FleDataOutputStream;

public class FlePlayerTechPacket extends FleAbstractPacket
{
	String str;
	byte r;
	public FlePlayerTechPacket() 
	{
		
	}
	public FlePlayerTechPacket(Technology tech, Result result) 
	{
		str = tech.getName();
		r = (byte) result.ordinal();
	}
	
	@Override
	protected void write(FleDataOutputStream os) throws IOException
	{
		os.writeString(str);
		os.writeByte(r);
	}
	
	@Override
	protected void read(FleDataInputStream is) throws IOException
	{
		str = is.readString();
		r = is.readByte();			
	}
	
	@Override
	public Object process(FleNetworkHandler nwh)
	{
		Result ret = Result.values()[r];
		EntityPlayer player = getPlayer();
		if(ret == Result.ALLOW)
			FleAPI.mod.getTechManager().getPlayerInfo(player).setTech(FleAPI.mod.getTechManager().getTechFromId(str));
		else if(ret == Result.DENY)
			FleAPI.mod.getTechManager().getPlayerInfo(player).removeTech(FleAPI.mod.getTechManager().getTechFromId(str));
		return null;
	}
}