package farcore.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import farcore.FarCoreSetup;
import farcore.lib.collection.IRegister;
import farcore.lib.net.entity.player.PacketRegisterSynclizeAsk;
import farcore.lib.net.world.PacketRegisterSynclize;
import farcore.util.U;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.world.WorldEvent;

public class FarCoreDataHandler
{
	public static final Map<String, IRegister<?>> worldSynclizeMap = new HashMap();
	public static final List<EntityPlayer> synclizingPlayers = new ArrayList();
	public static final List<EntityPlayer> synclizedPlayers = new ArrayList();
	
	/**
	 * Register a sort cache when player login the world.
	 * @param tag
	 * @param register
	 */
	public static final void registerRegister(String tag, IRegister<?> register)
	{
		if(worldSynclizeMap.containsKey(tag)) throw new RuntimeException("The tag '" + tag + "' has already registered!");
		worldSynclizeMap.put(tag, register);
	}
	
	public boolean shouldSync()
	{
		return !worldSynclizeMap.isEmpty();
	}
	
	@SubscribeEvent
	public void onPlayLogin(PlayerLoggedInEvent event)
	{
		if(shouldSync() && U.Sides.isClient() && U.Sides.isSimulating())
		{
			FarCoreSetup.network.sendToServer(new PacketRegisterSynclizeAsk());
		}
	}
		
	@SubscribeEvent
	public void onPlayLogout(PlayerLoggedInEvent event)
	{
		if(shouldSync() && !U.Sides.isClient())
		{
			synclizedPlayers.remove(event.player);
		}
	}
	
	public static void addSynclizePlayer(EntityPlayer player)
	{
		synclizingPlayers.add(player);
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
		if(!shouldSync() || event.world.isRemote) return;
		NBTTagCompound nbt = event.world.getWorldInfo().getNBTTagCompound();
		if(nbt.hasKey("registerData"))
		{
			arrange(nbt.getCompoundTag("registerData"));
		}
		else
		{
			arrange();
		}
	}
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event)
	{
		if(shouldSync() && U.Sides.isSimulating())
		{
			if(!synclizingPlayers.isEmpty())
			{
				for(EntityPlayer player : synclizingPlayers)
				{
					FarCoreSetup.network.sendLargeToPlayer(new PacketRegisterSynclize(), player);
				}
				synclizedPlayers.addAll(synclizingPlayers);
				synclizingPlayers.clear();
			}
		}
	}
	
	public static void arrange()
	{
		for(IRegister register : worldSynclizeMap.values())
		{
			register.arrange();
		}
	}
	
	public static void arrange(NBTTagCompound nbt)
	{
		for(Entry<String, IRegister<?>> entry : worldSynclizeMap.entrySet())
		{
			if(nbt.hasKey(entry.getKey(), NBT.TAG_LIST))
			{
				arrangeSingle(entry.getValue(), nbt.getByteArray(entry.getKey()));
			}
			else
			{
				entry.getValue().arrange();
			}
		}
	}
	
	public static void arrangeSingle(IRegister register, byte[] array)
	{
		DataInputStream stream = new DataInputStream(new ByteArrayInputStream(array));
		try
		{
			int size = stream.readInt();
			int m = 0;
			HashMap<Integer, String> map = new HashMap();
			for(int i = 0; i < size; ++i)
			{
				int id = stream.readInt();
				String tag = stream.readUTF();
				m = Math.max(id, m);
				map.put(id, tag);
			}
			String[] strings = new String[m];
			for(int i = 0; i < m; strings[i] = map.getOrDefault(Integer.valueOf(i), ""));
			register.arrange(strings);
		}
		catch(IOException exception)
		{
			;
		}
	}
	
	public static byte[] applySingle(IRegister register)
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		DataOutputStream stream = new DataOutputStream(output);
		try
		{
			stream.writeInt(register.size());
			for(String string : ((IRegister<?>) register).names())
			{
				stream.writeInt(register.id(string));
				stream.writeUTF(string);
			}
		}
		catch(IOException exception)
		{
			return null;
		}
		return output.toByteArray();
	}
}