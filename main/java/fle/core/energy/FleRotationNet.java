package fle.core.energy;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.biome.BiomeGenBase.TempCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import flapi.energy.IRotationTileEntity;
import flapi.energy.RotationNet;
import flapi.world.BlockPos;

public class FleRotationNet extends RotationNet
{
	private Map<Integer, Integer> windLevels = new HashMap();
	
	public FleRotationNet()
	{
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}
	
	@SubscribeEvent
	public void onWorldUpdate(TickEvent.WorldTickEvent evt)
	{
		if(evt.world.getWorldTime() % 200 == 0)
		{
			if(!evt.world.provider.isHellWorld)
			{
				if(!windLevels.containsKey(evt.world.provider.dimensionId)) return;
				int level = windLevels.get(evt.world.provider.dimensionId);
				boolean flag = evt.world.isRaining();
				if(flag)
				{
					if(level > 32)
					{
						level -= evt.world.rand.nextInt(5);
					}
					else if(level > 25)
					{
						level -= evt.world.rand.nextInt(3);
					}
					else if(level > 15)
					{
						level += evt.world.rand.nextInt(16) - 8;
					}
					else if(level > 10)
					{
						level += evt.world.rand.nextInt(3);
					}
					else
					{
						level += evt.world.rand.nextInt(5);
					}
				}
				else
				{
					if(level > 25)
					{
						level -= evt.world.rand.nextInt(5);
					}
					else if(level > 20)
					{
						level -= evt.world.rand.nextInt(3);
					}
					else if(level > 10)
					{
						level += evt.world.rand.nextInt(16) - 8;
					}
					else if(level > 5)
					{
						level += evt.world.rand.nextInt(3);
					}
					else
					{
						level += evt.world.rand.nextInt(5);
					}
				}
				windLevels.put(evt.world.provider.dimensionId, level);
			}
		}
		
	}
	
	@SubscribeEvent
	public void onWorldLodad(WorldEvent.Load evt)
	{
		windLevels.put(evt.world.provider.dimensionId, 0);
	}
	
	@SubscribeEvent
	public void onWorldUnlodad(WorldEvent.Unload evt)
	{
		windLevels.remove(evt.world.provider.dimensionId);
	}
	
	@Override
	public int getWindSpeed(BlockPos pos)
	{
		if(windLevels.containsKey(pos.getDim()))
		{
			return windLevels.get(pos.getDim()) * (6 + Math.max((pos.y - 128) / 10, 0) + (pos.getBiome().getTempCategory() == TempCategory.OCEAN ? 4 : 2));
		}
		else
		{
			return 0;
		}
	}

	@Override
	public void emmitRotationTo(BlockPos pos, ForgeDirection dir,
			RotationPacket packet)
	{
		if(!(pos.getBlockTile() instanceof IRotationTileEntity) || packet == null) return;
		
		IRotationTileEntity tile = (IRotationTileEntity) pos.getBlockTile();
		if(pos.toPos(dir).getBlockTile() instanceof IRotationTileEntity)
		{
			IRotationTileEntity tile1 = (IRotationTileEntity) pos.toPos(dir).getBlockTile();
			RotationPacket rPacket = packet.copy();
			int level = tile1.getStuck(packet, dir);
			if(level > 0)
			{
				rPacket.access(packet.getSpeed() / (double) (level + 1));
				tile.onRotationStuck(level);
			}
			if(tile1.canReciveEnergy(dir.getOpposite()) && tile.canEmitEnergy(dir))
			{
				tile.onRotationEmit(rPacket, dir);
				tile1.onRotationReceive(rPacket, dir.getOpposite());
			}
		}
	}

	@Override
	public String getEnergyNetName()
	{
		return "FRN";
	}
}