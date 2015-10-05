package fle.core.energy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.biome.BiomeGenBase.TempCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.gameevent.TickEvent;
import fle.api.energy.IRotationTileEntity;
import fle.api.energy.RotationNet;
import fle.api.world.BlockPos;

public class FleRotationNet extends RotationNet
{
	private List<Integer> windLevels = new ArrayList();
	
	public FleRotationNet()
	{
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}
	
	@EventHandler
	public void onWorldUpdate(TickEvent.WorldTickEvent evt)
	{
		if(evt.world.getWorldTime() % 200 == 0)
		{
			if(!evt.world.provider.isHellWorld)
			{
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
				windLevels.set(evt.world.provider.dimensionId, level);
			}
		}
		
	}
	
	@EventHandler
	public void onWorldLodad(WorldEvent.Load evt)
	{
		windLevels.set(evt.world.provider.dimensionId, 0);
	}
	
	@EventHandler
	public void onWorldUnlodad(WorldEvent.Unload evt)
	{
		windLevels.remove(evt.world.provider.dimensionId);
	}
	
	@Override
	public int getWindSpeed(BlockPos pos)
	{
		return windLevels.get(pos.getDim()) * 10 + Math.max((pos.y - 128) / 10, 0) + (pos.getBiome().getTempCategory() == TempCategory.OCEAN ? 4 : 2);
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
}