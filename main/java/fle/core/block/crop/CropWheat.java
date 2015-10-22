package fle.core.block.crop;

import static fle.api.enums.EnumCropRender.O_LIKE;
import fle.api.crop.ICropTile;
import fle.api.util.DropInfo;
import net.minecraftforge.common.EnumPlantType;

public class CropWheat extends CropBase
{
	public CropWheat()
	{
		super("wheat");
		setCropLevel(1);
		setMaturationStage(8);
		setTextureName("crop/wheat");
		setCropRender(O_LIKE);
	}

	@Override
	public EnumPlantType getPlantType()
	{
		return EnumPlantType.Crop;
	}
}