package fle.resource.crop;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;
import flapi.plant.ICropTile;
import flapi.world.BlockPos;

public class CropSugerCances extends CropBase
{
	public CropSugerCances()
	{
		super("sugar_cances");
		setCropLevel(2);
		setMaturationStage(6);
		setTextureName("crop/reed");
	}

	@Override
	public EnumPlantType getPlantType()
	{
		return EnumPlantType.Crop;
	}
	
	private ForgeDirection[] dirs = {ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST};
	
	@Override
	public boolean canCropGrow(ICropTile tile)
	{
		if(super.canCropGrow(tile))
		{
			BlockPos tPos = tile.getBlockPos().toPos(ForgeDirection.DOWN);
			for(ForgeDirection dir : dirs)
			{
				if(tPos.toPos(dir).getBlock() == Blocks.water) return true;
			}
		}
		return false;
	}
}