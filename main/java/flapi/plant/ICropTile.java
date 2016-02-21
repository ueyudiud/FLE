package flapi.plant;

import net.minecraft.block.Block;
import flapi.plant.IFertilableBlock.FertitleLevel;
import flapi.world.BlockPos;

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
