package fle.api.net;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import fle.api.util.FleDataInputStream;

public interface IFlePacketDecoder
{
	public void decode(FleDataInputStream aStream) throws IOException;
	
	public void process(Side aSide, EntityPlayer player);
}
