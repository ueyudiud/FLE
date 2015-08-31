package fle.core.energy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.FleValue;
import fle.api.energy.IThermalTileEntity;
import fle.api.energy.ThermalNet;
import fle.api.world.BlockPos;

public class FleThermalNet extends ThermalNet
{
	@Override
	public int getEnvironmentTemperature(BlockPos pos)
	{
		BiomeGenBase biome = pos.getBiome();
		float bioTem = biome.getFloatTemperature(pos.x, pos.y, pos.z);
		int ret = (int) ((Math.pow(bioTem, 0.5D) * 40) + FleValue.WATER_FREEZE_POINT);
		if(FLE.fle.getRotationNet() != null)
		{
			ret -= FLE.fle.getRotationNet().getWindSpeed() / 5;
		}
		return ret;
	}
	
	public int getTemperatureNearBy(int environmentTem, BlockPos pos)
	{
		int ret = 0;
		for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
		{
			ForgeDirection target = ForgeDirection.VALID_DIRECTIONS[i];
			TileEntity tile = pos.toPos(target).getBlockTile();
			if(tile instanceof IThermalTileEntity)
			{
				if(((IThermalTileEntity) tile).getTemperature(target.getOpposite()) > environmentTem)
				{
					ret += ((IThermalTileEntity) tile).getThermalConductivity(target.getOpposite()) * (((IThermalTileEntity) tile).getTemperature(target.getOpposite()) - environmentTem) / getBlockMaterialSpecificHeat(pos.getBlock().getMaterial());
				}
			}
		}
		return ret;
	}
	
	private double getBlockMaterialSpecificHeat(Material material)
	{
		if(material == Material.air) return 0.2D;
		if(material == Material.fire) return 0.2D;
		if(material == Material.water) return 0.3D;
		if(material == Material.wood) return 0.4D;
		if(material == Material.clay) return 0.18D;
		if(material == Material.ice) return 0.7D;
		if(material == Material.sand) return 0.14D;
		if(material == Material.glass) return 0.4D;
		if(material == Material.rock) return 0.08D;
		if(material == Material.snow) return 0.9D;
		if(material == Material.ground) return 0.16D;
		if(material == Material.lava) return 0.01D;
		return 0.9D;
	}

	@Override
	public void emmitHeat(BlockPos pos) 
	{
		double[] ds = new double[ForgeDirection.VALID_DIRECTIONS.length];
		if(pos.getBlockTile() instanceof IThermalTileEntity)
		{
			for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
			{
				ds[i] = getEmmitHeatValue(pos, ForgeDirection.VALID_DIRECTIONS[i]);
			}
			for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
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
			if(pos.toPos(dir).getBlockTile() instanceof IThermalTileEntity)
			{
				IThermalTileEntity te1 = (IThermalTileEntity) pos.toPos(dir).getBlockTile();
				int t2 = te1.getTemperature(dir);
				double value;
				if(t1 > t2)
				{
					value = (t1 - t2) * (te.getThermalConductivity(dir) + te1.getThermalConductivity(dir.getOpposite())) / 2F;
					te1.onHeatReceive(dir, value);
					return value;
				}
				else if(t1 < t2)
				{
					value = (t2 - t1) * (te.getThermalConductivity(dir) + te1.getThermalConductivity(dir.getOpposite())) / 2F;
					te1.onHeatEmit(dir, value);
					return -value;
				}
			}
			else
			{
				if(te.getTemperature(dir) > getEnvironmentTemperature(pos))
				{
					return (te.getTemperature(dir) - getEnvironmentTemperature(pos)) * (te.getThermalConductivity(dir) + getBlockMaterialSpecificHeat(pos.toPos(dir).getBlock().getMaterial())) / 2F;
				}
				else if(te.getTemperature(dir) < getEnvironmentTemperature(pos))
				{
					return -(getEnvironmentTemperature(pos) - te.getTemperature(dir)) * (te.getThermalConductivity(dir) + getBlockMaterialSpecificHeat(pos.toPos(dir).getBlock().getMaterial())) / 2F;
				}
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
}