package fle.core.block.crop;

import static fle.api.enums.EnumCropRender.O_LIKE;
import net.minecraftforge.common.EnumPlantType;

public class CropMillet extends CropBase
{
	public CropMillet()
	{
		super("millet");
		setCropLevel(1);
		setMaturationStage(8);
		setTextureName("crop/millet(setaria)");
		setCropRender(O_LIKE);
	}

	@Override
	public EnumPlantType getPlantType()
	{
		return EnumPlantType.Crop;
	}
}