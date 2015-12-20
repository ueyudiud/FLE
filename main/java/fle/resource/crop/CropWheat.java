package fle.resource.crop;

import net.minecraftforge.common.EnumPlantType;
import flapi.enums.EnumCropRender;
import flapi.plant.ICropTile;
import flapi.recipe.DropInfo;
import fle.core.item.ItemFleSub;

public class CropWheat extends CropBase
{
	public CropWheat()
	{
		super("wheat");
		setCropLevel(1);
		setMaturationStage(8);
		setTextureName("crop/wheat");
		setCropRender(EnumCropRender.O_LIKE);
	}

	@Override
	public EnumPlantType getPlantType()
	{
		return EnumPlantType.Crop;
	}
	
	private DropInfo saplingDrop;
	
	@Override
	public DropInfo getSeedDropsInfo(ICropTile tile)
	{
		return tile.getStage() == 1 ? getSaplingDrop() : super.getSeedDropsInfo(tile);
	}

	private DropInfo getSaplingDrop()
	{
		if (saplingDrop == null)
		{
			saplingDrop = new DropInfo(ItemFleSub.a("malt"));
		}

		return saplingDrop;
	}
}