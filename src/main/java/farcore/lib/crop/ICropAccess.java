/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.crop;

import farcore.data.EnumBlock;
import farcore.lib.bio.GeneticMaterial;
import farcore.lib.bio.IBiology;
import farcore.lib.bio.IFamily;
import nebula.common.world.ICoord;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public interface ICropAccess extends ICoord, IBiology, IPlantable
{
	@Override
	default ICrop getSpecie()
	{
		return crop();
	}
	
	@Override
	default IFamily<ICropAccess> getFamily()
	{
		return crop().getFamily();
	}
	
	ICrop crop();
	
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
	 * 
	 * @param gm
	 */
	void pollinate(GeneticMaterial gm);
	
	@Override
	default IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return EnumBlock.crop.block.getDefaultState();
	}
	
	@Override
	default EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return crop().getPlantType(this);
	}
}
