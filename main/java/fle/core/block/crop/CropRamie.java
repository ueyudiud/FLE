package fle.core.block.crop;

import static fle.api.enums.EnumCropRender.O_LIKE;
import net.minecraftforge.common.EnumPlantType;

public class CropRamie extends CropBase
{
	public CropRamie()
	{
		super("ramie");
		setCropLevel(1);
		setMaturationStage(7);
		setTextureName("crop/ramie");
		setCropRender(O_LIKE);
	}

	@Override
	public EnumPlantType getPlantType()
	{
		return EnumPlantType.Crop;
	}
}