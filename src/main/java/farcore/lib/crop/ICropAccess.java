/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.crop;

import java.util.Random;

import farcore.data.EnumBlock;
import farcore.lib.bio.BioData;
import farcore.lib.bio.IBio;
import nebula.common.util.L;
import nebula.common.world.ICoord;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public interface ICropAccess extends ICoord, IBio, IPlantable
{
	default Random rng()
	{
		return L.random();
	}
	
	ICropSpecie getSpecie();
	
	default ICropFamily<?> getFamily()
	{
		return getSpecie().getFamily();
	}
	
	CropInfo info();
	
	Biome biome();
	
	boolean isWild();
	
	int stage();
	
	float stageBuffer();
	
	int getWaterLevel();
	
	void grow(int growth);
	
	void setStage(int stage);
	
	int useWater(int amount);
	
	float temp();
	
	void killCrop();
	
	/**
	 * Pollinating pollen or spore to spread genetic material to this crop.
	 * @param gm the genetic material of pollen.
	 */
	void pollinate(boolean self, BioData gm);
	
	@Override
	default IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return EnumBlock.crop.block.getDefaultState();
	}
	
	@Override
	default EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return getSpecie().getPlantType(this);
	}
}
