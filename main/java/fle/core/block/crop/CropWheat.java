package fle.core.block.crop;

import static fle.api.enums.EnumCropRender.O_LIKE;
import net.minecraftforge.common.EnumPlantType;

public class CropWheat extends CropBase
{
	public CropWheat()
	{
		super("wheat");
		setCropLevel(1);
		setMaturationStage(8);
		setTextureName("crop/wheat");
		setGrowTick(1000);
		setCropRender(O_LIKE);
	}

	@Override
	public EnumPlantType getPlantType()
	{
		return EnumPlantType.Crop;
	}
}
