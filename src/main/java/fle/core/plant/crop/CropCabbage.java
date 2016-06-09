package fle.core.plant.crop;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.enums.EnumItem;
import farcore.lib.crop.CropInfo;
import farcore.lib.crop.ICropAccess;
import farcore.util.LanguageManager;
import farcore.util.U;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class CropCabbage extends CropFle
{
	private static final String cabbage = "cabbage";
	private static final String collateral_sprouting = "collateral_sprouting";
	private static final String expand_inflorescence = "expand_inflorescence";
	private static final String rich_anthocyanin = "rich_anthocyanin";
	
	public CropCabbage()
	{
		super("cabbage");
		setTextureName("fle:crop/");
		waterWaste = 8;
		maxGrowWaterUse = 13;
		maxStage = 6;
		maxTemp = 410;
		minAliveTemp = 269;
		minGrowTemp = 283;
		minAliveWaterReq = 200;
		minGrowWaterReq = 500;
		growReq = 1500;
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack)
	{
		return super.getIcon(stack);
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ICropAccess access)
	{
		int stage = access.getStage();
		int type = access.getInfo().map.get("type");
		return stage == 0 ? icons[0] : 
			(type & 8) != 0 ? (stage == 1 ? icons[maxStage + 5] : 
				(type & 1) != 0 ? icons[maxStage + stage + 4] : icons[stage]) :
					stage < 3 ? icons[stage] : 
						type == 3 ? icons[maxStage + stage - 3] :
							(type & 8) != 0 ? icons[maxStage + stage + 4] :
								stage != 3 ? icons[stage] :
									(type & 1) != 0 ? icons[maxStage + 3] :
										(type & 4) != 0 ? icons[maxStage + 4] :
											icons[3];
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcon(IIconRegister register)
	{
		int l = getMaxStage();
		icons = new IIcon[l + 10];
		int i = 0;
		for(; i < l; ++i)
		{
			icons[i] = register.registerIcon(textureName + "cabbage_universal_stage_" + i);
		}
		icons[l] = register.registerIcon(textureName + "brussels_sprouts_stage_insert_3");
		icons[l + 1] = register.registerIcon(textureName + "brussels_sprouts_stage_insert_4");
		icons[i + 2] = register.registerIcon(textureName + "brussels_sprouts_stage_insert_5");
		icons[i + 3] = register.registerIcon(textureName + "cabbage_stage_insert_3");
		icons[i + 4] = register.registerIcon(textureName + "cauliflower_stage_insert_3");
		icons[i + 5] = register.registerIcon(textureName + "purple_cabbage_stage_insert_1");
		icons[i + 6] = register.registerIcon(textureName + "purple_cabbage_stage_insert_2");
		icons[i + 7] = register.registerIcon(textureName + "purple_cabbage_stage_insert_3");
		icons[i + 8] = register.registerIcon(textureName + "purple_cabbage_stage_insert_4");
		icons[i + 9] = register.registerIcon(textureName + "purple_cabbage_stage_insert_5");
	}
	
	@Override
	public String getName(String dna)
	{
		List<String> list = Arrays.asList(U.Lang.split(dna, ','));
		if(!list.contains("docile"))
		{
			return "wild cabbage";
		}
		else 
		{
			if(list.contains(cabbage))
			{
				if(list.contains(collateral_sprouting))
				{
					return "brussels sprouts";
				}
				return "cabbage";
			}
			if(list.contains(expand_inflorescence))
			{
				return "cauliflower";
			}
			return "kale";
		}
	}
	
	@Override
	public String getTranslateName(String dna)
	{
		List<String> list = Arrays.asList(U.Lang.split(dna, ','));
		if(!list.contains("docile"))
		{
			return "wild.cabbage";
		}
		else 
		{
			if(list.contains(cabbage))
			{
				if(list.contains(rich_anthocyanin))
				{
					return "purple.cabbage";
				}
				if(list.contains(collateral_sprouting))
				{
					return "brussels.sprouts";
				}
				return "cabbage";
			}
			if(list.contains(expand_inflorescence))
			{
				return "cauliflower";
			}
			return "kale";
		}
	}
	
	@Override
	public void registerLocalizedName(LanguageManager manager)
	{
		manager.registerLocal("crop.wild.cabbage.name", "Wild Cabbage");
		manager.registerLocal("crop.brussels.sprouts.name", "Brussels Sprouts");
		manager.registerLocal("crop.cabbage.name", "Cabbage");
		manager.registerLocal("crop.cauliflower.name", "Cauliflower");
		manager.registerLocal("crop.kale.name", "Kale");
		manager.registerLocal("crop.purple.cabbage.name", "Purple Cabbage");
	}
	
	@Override
	protected void applyBaseEffect(CropInfo info)
	{
		info.hotResistance = 1;
		info.dryResistance = 1;
	}
	
	@Override
	protected void applyEffect(CropInfo info, String prop)
	{
		int type;
		switch (prop)
		{
		case "docile" : info.grain += 1;
		break;
		case cabbage : info.grain += 1;
		type = info.map.get("type");
		info.map.put("type", type | 1);
		break;
		case collateral_sprouting : info.grain -= 1;
		type = info.map.get("type");
		info.map.put("type", type | 2);
		break;
		case expand_inflorescence : ;
		type = info.map.get("type");
		info.map.put("type", type | 4);
		break;
		case rich_anthocyanin : ;
		type = info.map.get("type");
		info.map.put("type", type | 8);
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
			if(harmonicCheck(random, generation, 0.5D, 0.3D))
			{
				list.add("docile");
			}
		}
		else if(!list.contains(expand_inflorescence))
		{
			if(!list.contains(cabbage))
			{
				if(harmonicCheck(random, generation, 0.09F, 0.12F))
				{
					list.add(expand_inflorescence);
				}
				else
				{
					if(harmonicCheck(random, generation, 0.11F, 0.14F))
					{
						list.add(cabbage);
					}
					if(!list.contains(collateral_sprouting) && !list.contains(rich_anthocyanin))
					{
						if(harmonicCheck(random, generation, 0.07F, 0.09F))
						{
							list.add(rich_anthocyanin);
						}
						else if(harmonicCheck(random, generation, 0.12F, 0.17F))
						{
							list.add(collateral_sprouting);
						}
					}
				}
			}
		}
		super.addMutation(generation, list);
	}

	@Override
	public String instanceDNA(Random random)
	{
		return random.nextBoolean() ? "xerophily" : "cold";
	}

	@Override
	public void getDrops(ICropAccess access, List<ItemStack> list)
	{
		CropInfo info = access.getInfo();
		int grain = info.grain;
		switch (access.getStage())
		{
		case 3 :
			int type = info.map.get("type");
			if((type & 0x1) != 0)
			{
				if((type & 0x2) != 0)
				{
					list.add(EnumItem.plant.instance(2 + access.rng().nextInt(grain + 1), "brussels_sprouts"));
				}
				else if((type & 0x8) != 0)
				{
					list.add(EnumItem.plant.instance(1 + access.rng().nextInt(grain / 2 + 1), "purple_cabbage"));
				}
				else
				{
					list.add(EnumItem.plant.instance(1 + access.rng().nextInt(grain / 2 + 1), "cabbage"));
				}
			}
			else if((type & 0x4) != 0)
			{
				list.add(EnumItem.plant.instance(1 + access.rng().nextInt(grain / 2 + 1), "cauliflower"));
			}
			else
			{
				list.add(EnumItem.plant.instance(1 + access.rng().nextInt(grain / 2 + 1), "wild_cabbage_leaf"));
			}
			break;
		case 4 :
			break;
		case 5 :
			list.add(applyChildSeed(access.rng().nextInt(grain + 2) + 2, info));
			break;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public EnumRenderType getRenderType()
	{
		return EnumRenderType.cross;
	}
}