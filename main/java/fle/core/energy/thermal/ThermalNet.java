package fle.core.energy.thermal;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

import farcore.util.Direction;
import farcore.util.FleLog;
import farcore.util.Vs;
import flapi.energy.EnergyPacket;
import flapi.energy.EnergyType;
import flapi.energy.IThermalNet;
import flapi.energy.IThermalStorage;

public class ThermalNet implements IThermalNet
{
	@Override
	public EnergyType getType()
	{
		return EnergyType.Thermal;
	}
	
	@Override
	public int getEnviormentTemperature(IBlockAccess world, BlockPos pos)
	{
		BiomeGenBase biome = world.getBiomeGenForCoords(pos);
		float bioTem = biome.getFloatTemperature(pos);
		int ret = floatTempretureToInteger(bioTem);
		int c = 1;
		int t = 0;
		// for (Direction dir : Direction.BASIC)
		// {
		// IBlockState block = world.getBlockState(pos.offset(dir.toFacing());
		// if (map.containsKey(block))
		// {
		// c++;
		// int temp = map.get(block).getAttribute(Attribute.max_temp);
		// if (temp > ret)
		// t += temp;
		// else
		// t += (temp + ret) / 2;
		// }
		// }
		ret += t;
		ret /= c;
		return ret;
	}
	
	private double getBlockMaterialConductSpeed(Block block)
	{
		// if (map.containsKey(block))
		// {
		// return map.get(block).getAttribute(Attribute.thermalSpeed) * 100;
		// }
		Material material = block.getMaterial();
		if (material == Material.air)
			return 80D;
		if (material == Material.fire)
			return 80D;
		if (material == Material.plants)
			return 81D;
		if (material == Material.water)
			return 90D;
		if (material == Material.wood)
			return 50D;
		if (material == Material.clay)
			return 50D;
		if (material == Material.ice)
			return 100D;
		if (material == Material.sand)
			return 46D;
		if (material == Material.glass)
			return 160D;
		if (material == Material.rock)
			return 48D;
		if (material == Material.snow)
			return 170D;
		if (material == Material.ground)
			return 70D;
		if (material == Material.lava)
			return 13D;
		if (material == Material.iron)
			return 120D;
		return 0.9D;
	}
	
	@Override
	public void emmit(World world, BlockPos pos)
	{
		double[] differEnergy = new double[6];
		long enviorTemp = getEnviormentTemperature(world, pos);
		if (world.getTileEntity(pos) instanceof IThermalStorage)
		{
			IThermalStorage storage = (IThermalStorage) world
					.getTileEntity(pos);
			IThermalStorage storage1;
			for (Direction direction : Direction.BASIC)
			{
				BlockPos offset = pos.offset(direction.toFacing());
				try
				{
					if (world.getTileEntity(offset) instanceof IThermalStorage)
					{
						storage1 = (IThermalStorage) world
								.getTileEntity(offset);
						differEnergy[direction.ordinal()] = (storage1
								.getTemperature(direction.getOpposite())
								- storage.getTemperature(direction))
								* (storage1.getThermalConductivity(
										direction.getOpposite())
										+ storage.getThermalConductivity(
												direction))
								/ 2;
					}
					else
					{
						differEnergy[direction
								.ordinal()] = (storage.getTemperature(direction)
										- enviorTemp)
										* storage.getThermalConductivity(
												direction);
					}
				}
				catch (Exception exception)
				{
					FleLog.addExceptionToCache(exception);
				}
			}
			for (Direction direction : Direction.BASIC)
			{
				BlockPos offset = pos.offset(direction.toFacing());
				if (differEnergy[direction.ordinal()] == 0)
					continue;
				EnergyPacket packet = new EnergyPacket(
						differEnergy[direction.ordinal()]);
				storage.emmitThermalTo(direction, packet, true);
				if (world.getTileEntity(offset) instanceof IThermalStorage)
				{
					((IThermalStorage) world.getTileEntity(offset))
							.recieveThermalFrom(direction.getOpposite(), packet,
									true);
				}
			}
		}
		FleLog.resetAndCatchException(
				"Catching exception during caculating thermal current.");
	}
	
	public static int floatTempretureToInteger(double tempreture)
	{
		return (int) (Math.pow(tempreture, 0.27403) * 49.6204)
				+ Vs.water_freeze_point - 19;
	}
}