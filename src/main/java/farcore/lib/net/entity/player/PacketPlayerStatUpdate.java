package farcore.lib.net.entity.player;

import java.io.IOException;

import farcore.lib.net.PacketWorld;
import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.network.PacketAbstract;
import farcore.util.FarFoodStats;
import farcore.util.io.DataStream;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class PacketPlayerStatUpdate extends PacketAbstract
{
	public long state;
	public int[] value;
	
	public PacketPlayerStatUpdate()
	{
		
	}
	public PacketPlayerStatUpdate(FarFoodStats stats)
	{
		int[] locate = new int[64];
		int i = 0;
		if(stats.foodLevel != stats.prevFoodLevel)
		{
			locate[0] = Float.floatToIntBits(stats.prevFoodLevel = stats.foodLevel);
			i = 1;
			state |= 0x1;
		}
		if(stats.foodSaturationLevel != stats.prevFoodSaturationLevel)
		{
			locate[i++] = Float.floatToIntBits(stats.prevFoodSaturationLevel = stats.foodSaturationLevel);
			state |= 0x2;
		}
		if(stats.waterLevel != stats.prevWaterLevel)
		{
			locate[i++] = Float.floatToIntBits(stats.prevWaterLevel = stats.waterLevel);
			state |= 0x4;
		}
		value = new int[i];
		System.arraycopy(locate, 0, value, 0, value.length);
	}
	public PacketPlayerStatUpdate(long state, int[] value)
	{
		this.state = state;
		this.value = value;
	}
	
	@Override
	protected void encode(DataStream output) throws IOException
	{
		output.writeLong(state);
		output.writeIntArray(value);
	}
	
	@Override
	protected void decode(DataStream input) throws IOException
	{
		state = input.readLong();
		value = input.readIntArray();
	}
	
	@Override
	public IPacket process(NetworkBasic network)
	{
		EntityPlayer player = getPlayer();
		FarFoodStats stats = (FarFoodStats) player.getFoodStats();
		int i = 0;
		if((state & 0x1) != 0)
		{
			stats.foodLevel =Float.intBitsToFloat(value[i++]);
		}
		if((state & 0x2) != 0)
		{
			stats.foodSaturationLevel = Float.intBitsToFloat(value[i++]);
		}
		if((state & 0x4) != 0)
		{
			stats.waterLevel = Float.intBitsToFloat(value[i++]);
		}
		return null;
	}
	
	@Override
	public boolean needToSend()
	{
		return state != 0;
	}
}