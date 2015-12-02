package fle.core.block.crop;

import static fle.api.enums.EnumCropRender.O_LIKE;
import net.minecraftforge.common.EnumPlantType;

public class CropFlax extends CropBase
{
	public CropFlax()
	{
		super("flax");
		setCropLevel(2);
		setMaturationStage(6);
		setTextureName("crop/flax");
	}

	@Override
	public EnumPlantType getPlantType()
	{
		return EnumPlantType.Crop;
	}
}