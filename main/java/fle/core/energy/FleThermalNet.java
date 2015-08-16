package fle.core.energy;

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
			else if(pos.toPos(target).getBlock() == Blocks.fire)
			{
				ret += 1;
			}
		}
		return ret;
	}
	
	private double getBlockMaterialSpecificHeat(Material material)
	{
		if(material == Material.air) return 1.0D;
		if(material == Material.water) return 4.2D;
		if(material == Material.wood) return 2.0D;
		if(material == Material.clay) return 0.75D;
		if(material == Material.ice) return 3.4D;
		if(material == Material.sand) return 0.54D;
		if(material == Material.glass) return 0.84D;
		if(material == Material.rock) return 0.38D;
		if(material == Material.snow) return 3.5D;
		if(material == Material.ground) return 0.84D;
		return 0.9D;
	}

	@Override
	public void emmitHeat(BlockPos pos) 
	{
		for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
			emmitHeatTo(pos, ForgeDirection.VALID_DIRECTIONS[i]);
	}

	@Override
	public void emmitHeatTo(BlockPos pos, ForgeDirection dir) 
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
				if(t1 > t2 + 1)
				{
					value = (t1 - t2) * Math.min(te.getThermalConductivity(dir), te1.getThermalConductivity(dir.getOpposite()));
					te.onHeatEmmit(dir, value);
					te1.onHeatReceive(dir.getOpposite(), value);
				}
				else if(t1 + 1 < t2)
				{
					value = (t2 - t1) * Math.min(te.getThermalConductivity(dir), te1.getThermalConductivity(dir.getOpposite()));
					te1.onHeatEmmit(dir.getOpposite(), value);
					te.onHeatReceive(dir, value);
				}
			}
			else
			{
				if(te.getTemperature(dir) > getEnvironmentTemperature(pos) + 1)
				{
					te.onHeatEmmit(dir, (te.getTemperature(dir) - getEnvironmentTemperature(pos)) * Math.min(te.getThermalConductivity(dir), getBlockMaterialSpecificHeat(pos.getBlock().getMaterial())));
				}
				else if(te.getTemperature(dir) < getEnvironmentTemperature(pos) - 1)
				{
					te.onHeatReceive(dir, (getEnvironmentTemperature(pos) - te.getTemperature(dir)) * Math.min(te.getThermalConductivity(dir), getBlockMaterialSpecificHeat(pos.getBlock().getMaterial())));
				}
			}
		}
	}

	public static int getFTempretureToInteger(double tempreture)
	{
		return (int) (Math.pow(tempreture, 0.5D) / Math.pow(40D, 0.5D) * 40) + FleValue.WATER_FREEZE_POINT;
	}
}