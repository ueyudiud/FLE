package fle.core.block.crop;

import fle.api.crop.ICropTile;
import fle.api.enums.EnumCropRender;
import fle.api.util.DropInfo;
import net.minecraftforge.common.EnumPlantType;

public class CropCotton extends CropBase
{
	public CropCotton()
	{
		super("cotton");
		setCropLevel(2);
		setMaturationStage(6);
		setTextureName("crop/cotton");
		setGrowTick(1500);
		setCropRender(EnumCropRender.O_LIKE);
	}
	
	@Override
	public DropInfo getSeedDropsInfo(ICropTile tile)
	{
		return tile.getStage() < 5 ? super.getSeedDropsInfo(tile) : getHarvestDropsInfo(tile);
	}
	
	@Override
	public EnumPlantType getPlantType()
	{
		return EnumPlantType.Crop;
	}
}