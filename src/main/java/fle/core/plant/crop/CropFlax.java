package fle.core.plant.crop;

import java.util.List;
import java.util.Random;

import farcore.enums.EnumItem;
import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICropAccess;
import net.minecraft.item.ItemStack;

public class CropFlax extends CropFle
{
	public CropFlax()
	{
		super("flax");
		setTextureName("fle:crop/flax");
		waterWaste = 8;
		maxGrowWaterUse = 12;
		maxStage = 6;
		maxTemp = 400;
		minAliveTemp = 265;
		minGrowTemp = 279;
		minAliveWaterReq = 280;
		minGrowWaterReq = 520;
		growReq = 1500;
	}
	
	@Override
	protected void applyBaseEffect(CropInfo info)
	{
		info.coldResistance = 1;
		info.growth = 1;
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
		case "resistance" : info.weedResistance += 1;
		break;
		case "cold" : info.coldResistance += 3;
		break;
		case "hot" : info.hotResistance += 3;
		break;
		}
	}

	@Override
	public String instanceDNA(Random random)
	{
		return random.nextBoolean() ? "hot" : "cold";
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
//				list.add(EnumItem.plant.instance(3 + access.rng().nextInt(grain + 2), "flax_fiber"));
			}
			list.add(applyChildSeed(access.rng().nextInt(21) == 0 ? 2 : 1, info));
		}
	}
}