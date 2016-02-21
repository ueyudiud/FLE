package fle.core.energy;

import java.util.HashMap;
import java.util.Map;

import farcore.collection.CollectionUtil.FleEntry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.energy.IThermalTileEntity;
import flapi.energy.ThermalNet;
import flapi.event.FLEThermalHeatEvent;
import flapi.util.FleValue;
import flapi.world.BlockPos;
import fle.core.util.Attribute;
import fle.core.util.AttributeMap;

public class FleThermalNet extends ThermalNet
{
	private static final Map<Block, AttributeMap> map = new HashMap();

	static
	{
		registerBlockThermalInfo(Blocks.stone, 0.3F);
		registerBlockThermalInfo(Blocks.water, 0.85F);
		registerBlockThermalInfo(Blocks.lava, 950, 0.1F);
		registerBlockThermalInfo(Blocks.cobblestone, 0.32F);
		registerBlockThermalInfo(Blocks.torch, 315, 0.7F);
		registerBlockThermalInfo(Blocks.fire, 500, 0.6F);
		registerBlockThermalInfo(Blocks.netherrack, 425, 0.4F);
		registerBlockThermalInfo(Blocks.ice, 0.5F);
	}
	
	public static void registerBlockThermalInfo(Block target, float thermalConductSpeed)
	{
		registerBlockThermalInfo(target, 
				new AttributeMap(
						new FleEntry(Attribute.max_temp, FleValue.WATER_FREEZE_POINT),
						new FleEntry(Attribute.thermalSpeed, thermalConductSpeed)
						));
	}
	public static void registerBlockThermalInfo(Block target, int temp, float thermalConductSpeed)
	{
		registerBlockThermalInfo(target, 
				new AttributeMap(
						new FleEntry(Attribute.max_temp, temp),
						new FleEntry(Attribute.thermalSpeed, thermalConductSpeed)
						));
	}
	public static void registerBlockThermalInfo(Block target, AttributeMap aMap)
	{
		map.put(target, aMap);
	}
	
	@Override
	public int getEnvironmentTemperature(BlockPos pos)
	{
		BiomeGenBase biome = pos.getBiome();
		float bioTem = biome.getFloatTemperature(pos.x, pos.y, pos.z);
		int ret = (int) (Math.pow(bioTem, 0.27403) * 49.6204) + FleValue.WATER_FREEZE_POINT - 19;
		int c = 1;
		int t = 0;
		for(ForgeDirection dir : ForgeDirection.values())
		{
			Block block = pos.toPos(dir).getBlock();
			if(map.containsKey(block))
			{
				c++;
				int temp = map.get(block).getAttribute(Attribute.max_temp);
				if(temp > ret)
					t += temp;
				else
					t += (temp + ret) / 2;
			}
		}
		ret += t;
		ret /= c;
		return ret;
	}
	
	//To keep the balance with thermal tile conduct speed.
	private static final double conductValue = 0.109375;
	
	private double getBlockMaterialConductSpeed(Block block)
	{
		if(map.containsKey(block))
		{
			return map.get(block).getAttribute(Attribute.thermalSpeed) * 100;
		}
		Material material = block.getMaterial();
		if(material == Material.air) return 80D;
		if(material == Material.fire) return 80D;
		if(material == Material.plants) return 81D;
		if(material == Material.water) return 90D;
		if(material == Material.wood) return 50D;
		if(material == Material.clay) return 50D;
		if(material == Material.ice) return 100D;
		if(material == Material.sand) return 46D;
		if(material == Material.glass) return 160D;
		if(material == Material.rock) return 48D;
		if(material == Material.snow) return 170D;
		if(material == Material.ground) return 70D;
		if(material == Material.lava) return 13D;
		if(material == Material.iron) return 120D;
		return 0.9D;
	}

	@Override
	public void emmitHeat(BlockPos pos) 
	{
		double[] ds = new double[6];
		if(pos.getBlockTile() instanceof IThermalTileEntity)
		{
			int i;
			for(i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
			{
				ds[i] = getEmmitHeatValue(pos, ForgeDirection.VALID_DIRECTIONS[i]);
			}
			for(i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
			{
				if(ds[i] > 0) ((IThermalTileEntity) pos.getBlockTile()).onHeatEmit(ForgeDirection.VALID_DIRECTIONS[i], ds[i]);
				else if(ds[i] < 0) ((IThermalTileEntity) pos.getBlockTile()).onHeatReceive(ForgeDirection.VALID_DIRECTIONS[i], -ds[i]);
			}
		}
			
	}
	
	private double getEmmitHeatValue(BlockPos pos, ForgeDirection dir)
	{
		if(pos.getBlockTile() instanceof IThermalTileEntity)
		{
			IThermalTileEntity te = (IThermalTileEntity) pos.getBlockTile();
			int t1 = te.getTemperature(dir);
			if(te.getThermalConductivity(dir) <= 0) return 0;
			if(pos.toPos(dir).getBlockTile() instanceof IThermalTileEntity)
			{
				IThermalTileEntity te1 = (IThermalTileEntity) pos.toPos(dir).getBlockTile();
				if(te1.getThermalConductivity(dir.getOpposite()) <= 0) return 0;
				int t2 = te1.getTemperature(dir);
				double conduct = Math.sqrt(te.getThermalConductivity(dir) * te1.getThermalConductivity(dir.getOpposite()));
				double heatLevel = Math.abs(t1 / 100D) + Math.abs(t2 / 100D);
				double conduceProgress = heatLevel / (heatLevel + 1);
				double value = (t2 - t1) * conduct * conduceProgress;
				if(t1 > t2)
				{
					te1.onHeatReceive(dir, -value);
				}
				else if(t1 < t2)
				{
					te1.onHeatEmit(dir, value);
				}
				return -value;
			}
			else
			{
				int t2;
				Block block = pos.toPos(dir).getBlock();
				if(map.containsKey(block))
				{
					t2 = map.get(block).getAttribute(Attribute.max_temp);
				}
				else
				{
					t2 = getEnvironmentTemperature(pos.toPos(dir));
				}
				double s = getBlockMaterialConductSpeed(pos.toPos(dir).getBlock());
				double conduct = Math.sqrt(te.getThermalConductivity(dir) * s);
				double heatLevel = Math.abs(t1 / 100D) + Math.abs(t2 / 100D);
				double conduceProgress = heatLevel / (heatLevel + 1);
				double value = Math.signum(t1 - t2) * Math.log(1 + Math.abs(t1 - t2)) * conduct * conduceProgress;
				FLEThermalHeatEvent evt = new FLEThermalHeatEvent(pos.toPos(dir), value * conductValue, t1 > t2);
				MinecraftForge.EVENT_BUS.post(evt);
				return evt.getHeat();
			}
		}
		return 0.0D;
	}

	@Override
	public void emmitHeatTo(BlockPos pos, ForgeDirection dir) 
	{
		if(pos.getBlockTile() instanceof IThermalTileEntity)
		{
			IThermalTileEntity te = (IThermalTileEntity) pos.getBlockTile();
			double value = getEmmitHeatValue(pos, dir);
			if(value > 0) te.onHeatEmit(dir, value);
			else if(value < 0) te.onHeatReceive(dir, -value);
		}
	}

	public static int getFTempretureToInteger(double tempreture)
	{
		return (int) (Math.pow(tempreture, 0.5D) / Math.pow(40D, 0.5D) * 40) + FleValue.WATER_FREEZE_POINT;
	}

	@Override
	public String getEnergyNetName()
	{
		return "FTN";
	}
}