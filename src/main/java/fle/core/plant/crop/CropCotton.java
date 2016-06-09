package fle.core.plant.crop;

import java.util.List;
import java.util.Random;

import farcore.enums.EnumItem;
import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICropAccess;
import net.minecraft.item.ItemStack;

public class CropCotton extends CropFle
{
	public CropCotton()
	{
		super("cotton");
		setTextureName("fle:crop/cotton");
		waterWaste = 8;
		maxGrowWaterUse = 11;
		maxStage = 6;
		maxTemp = 402;
		minAliveTemp = 268;
		minGrowTemp = 280;
		minAliveWaterReq = 300;
		minGrowWaterReq = 500;
		growReq = 1400;
	}
	
	@Override
	protected void applyBaseEffect(CropInfo info)
	{
		info.coldResistance = 1;
		info.dryResistance = 1;
	}
	
	@Override
	protected void applyEffect(CropInfo info, String prop)
	{
		switch (prop)
		{
		case "xerophily" : info.dryResistance += 2;
		break;
		case "grain" : info.grain += 2;
		break;
		case "growth" : info.growth += 2;
		break;
		case "resistance" : info.weedResistance += 2;
		break;
		case "cold" : info.coldResistance += 3;
		break;
		case "hot" : info.hotResistance += 2;
		break;
		}
	}

	@Override
	public String instanceDNA(Random random)
	{
		return random.nextBoolean() ? "xerophily" : "growth";
	}

	@Override
	public void getDrops(ICropAccess access, List<ItemStack> list)
	{
		if(access.getStage() > 4)
		{
			CropInfo info = access.getInfo();
			int grain = info.grain;
			if(grain > 0)
			{
				list.add(EnumItem.plant.instance(3 + access.rng().nextInt(grain + 2), "cotton"));
			}
			list.add(applyChildSeed(access.rng().nextInt(21) == 0 ? 2 : 1, info));
		}
	}
}