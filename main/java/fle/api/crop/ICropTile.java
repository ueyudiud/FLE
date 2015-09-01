package fle.api.crop;

import net.minecraft.block.Block;
import fle.api.world.BlockPos;

public interface ICropTile 
{
	public int getStage();
	
	public BlockPos getBlockPos();
	
	public boolean isBlockUnder(Block target);
	
	public double getAirLevel();
	
	public double getWaterLevel();
	
	public double getTempretureLevel();
	
	public int getLightValue();
}