package fle.resource.crop;

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