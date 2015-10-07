package fle.api.crop;

import net.minecraft.block.Block;
import fle.api.crop.IFertilableBlock.FertitleLevel;
import fle.api.world.BlockPos;

public interface ICropTile 
{
	int getStage();
	
	BlockPos getBlockPos();
	
	boolean isBlockUnder(Block target);
	
	double getAirLevel();
	
	double getWaterLevel();
	
	double getTempretureLevel();
	
	FertitleLevel getFertitleLevel();
	
	int getLightValue();
}
