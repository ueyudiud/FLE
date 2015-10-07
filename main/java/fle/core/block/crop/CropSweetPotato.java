package fle.core.block.crop;

import net.minecraftforge.common.EnumPlantType;

public class CropSweetPotato extends CropBase
{
	public CropSweetPotato()
	{
		super("sweetpotato");
		setCropLevel(2);
		setGrowTick(1700);
		setMaturationStage(6);
		setTextureName("crop/sweetpotato");
	}

	@Override
	public EnumPlantType getPlantType()
	{
		return EnumPlantType.Crop;
	}
}