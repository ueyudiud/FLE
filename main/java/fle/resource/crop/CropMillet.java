package fle.resource.crop;

import flapi.enums.EnumCropRender;
import net.minecraftforge.common.EnumPlantType;

public class CropMillet extends CropBase
{
	public CropMillet()
	{
		super("millet");
		setCropLevel(1);
		setMaturationStage(8);
		setTextureName("crop/millet(setaria)");
		setCropRender(EnumCropRender.O_LIKE);
	}

	@Override
	public EnumPlantType getPlantType()
	{
		return EnumPlantType.Crop;
	}
}