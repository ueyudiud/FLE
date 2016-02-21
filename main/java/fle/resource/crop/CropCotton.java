package fle.resource.crop;

import net.minecraftforge.common.EnumPlantType;
import flapi.enums.EnumCropRender;
import flapi.plant.ICropTile;
import flapi.recipe.DropInfo;

public class CropCotton extends CropBase
{
	public CropCotton()
	{
		super("cotton");
		setCropLevel(2);
		setMaturationStage(6);
		setTextureName("crop/cotton");
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