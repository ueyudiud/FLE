package fle.resource.crop;

import net.minecraftforge.common.EnumPlantType;
import flapi.enums.EnumCropRender;

public class CropRamie extends CropBase
{
	public CropRamie()
	{
		super("ramie");
		setCropLevel(1);
		setMaturationStage(7);
		setTextureName("crop/ramie");
		setCropRender(EnumCropRender.O_LIKE);
	}

	@Override
	public EnumPlantType getPlantType()
	{
		return EnumPlantType.Crop;
	}
}