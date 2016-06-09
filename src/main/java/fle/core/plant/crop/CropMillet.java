package fle.core.plant.crop;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.enums.EnumItem;
import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICropAccess;
import farcore.util.LanguageManager;
import farcore.util.U;
import fle.core.item.ItemCropSeed;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class CropMillet extends CropFle
{
	public CropMillet()
	{
		super("millet");
		setTextureName("fle:crop/millet(setaria)");
		waterWaste = 4;
		maxGrowWaterUse = 10;
		maxStage = 8;
		maxTemp = 410;
		minAliveTemp = 269;
		minGrowTemp = 283;
		minAliveWaterReq = 200;
		minGrowWaterReq = 500;
		growReq = 1500;
	}
	
	@Override
	public String getName(String dna)
	{
		if(!Arrays.asList(U.Lang.split(dna, ',')).contains("docile"))
		{
			return "bristlegrass";
		}
		else
		{
			return "millet";
		}
	}
	
	@Override
	public void registerLocalizedName(LanguageManager manager)
	{
		manager.registerLocal("crop.bristlegrass.name", "Bristlegrass");
		manager.registerLocal("crop.millet.name", "Millet");
	}
	
	@Override
	protected void applyBaseEffect(CropInfo info)
	{
		info.growth = 2;
		info.dryResistance = 1;
	}
	
	@Override
	protected void applyEffect(CropInfo info, String prop)
	{
		switch (prop)
		{
		case "docile" : info.grain += 2;
		break;
		case "xerophily" : info.dryResistance += 3;
		break;
		case "grain" : info.grain += 1;
		break;
		case "growth" : info.growth += 1;
		break;
		case "resistance" : info.weedResistance += 2;
		break;
		}
	}
	
	@Override
	protected void addMutation(int generation, List<String> list)
	{
		if(!list.contains("docile"))
		{
			if(harmonicCheck(random, generation, 0.3D, 0.2D));
			{
				list.add("docile");
			}
		}
		super.addMutation(generation, list);
	}

	@Override
	public String instanceDNA(Random random)
	{
		return random.nextBoolean() ? "xerophily" : "resistance";
	}

	@Override
	public void getDrops(ICropAccess access, List<ItemStack> list)
	{
		if(access.getStage() > 6)
		{
			CropInfo info = access.getInfo();
			int grain = info.grain;
			if(grain > 0)
			{
				list.add(EnumItem.plant.instance(2 + access.rng().nextInt(grain), "millet"));
			}
			list.add(applyChildSeed(access.rng().nextInt(12) == 0 ? 2 : 1, info));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public EnumRenderType getRenderType()
	{
		return EnumRenderType.lattice;
	}
}