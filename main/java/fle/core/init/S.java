package fle.core.init;

import static farcore.collection.CollectionUtil.*;
import static fle.init.Substances.*;
import static farcore.chem.StandardReactionHandler.addRecipe;
import static farcore.chem.EnumReactionLevel.*;

import java.util.Map.Entry;

import com.sun.swing.internal.plaf.metal.resources.metal_zh_TW;

import farcore.chem.EnumReactionLevel;
import farcore.substance.Atom;
import farcore.substance.Matter;
import farcore.substance.PDPhase;
import farcore.substance.PhaseDiagram;
import farcore.substance.PhaseDiagram.Phase;
import farcore.substance.Substance;
import farcore.substance.SubstanceRegistry;
import farcore.util.Part;
import farcore.util.SubTag;
import flapi.FleResource;
import fle.core.recipe.chem.ReactionRecipe;
import fle.core.util.phase.PhaseCarbon1;
import fle.core.util.phase.PhaseCarbon2;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class S
{
	public static void init()
	{
		Matter c = Matter.matter("carbon", "C");
		graphite = new Substance("graphite")
				.setColor(Phase.SOLID, 0x030303FF)
				.setSubstance(new PhaseCarbon1())
				.setPhasePhaseDiagram(new PhaseCarbon2())
				.setBlockInfo(1, 80)
				.setElectrologyCfg(7837F, 0.2F)
				.setThermalCfg(852, 132F)
				.setDensity(228)
				.setToolInfo(0, 0.2F, 1, 0.3F)
				.setCrafting(381, 100)
				.setFurnaceBurnTime(1200)
				.setMatter(c);
		coal = new Substance("coal")
				.setColor(Phase.SOLID, 0x101010FF)
				.setPhasePhaseDiagram(new PDPhase(3718, 3718))
				.setBlockInfo(4, 100)
				.setThermalCfg(928, 102F)
				.setDensity(172)
				.setFurnaceBurnTime(1200)
				.setMatter("[C]9[S]1");
		diamond = new Substance("diamond")
				.setColor(Phase.SOLID, 0xEFEFEF55)
				.setSubstance(new PhaseCarbon1())
				.setPhasePhaseDiagram(new PhaseCarbon2())
				.setBlockInfo(81, 120)
				.setThermalCfg(616, 1722F)
				.setDensity(351)
				.setToolInfo(93, 17.5F, 1721, 38F)
				.setCrafting(478000000, 1050000000)
				.setFurnaceBurnTime(1350)
				.setMatter(c);
		carbonLiquid = new Substance("carbon liquid")
				.setColor(Phase.LIQUID, 0xFEFFCEAA)
				.setSubstance(new PhaseCarbon1())
				.setPhasePhaseDiagram(new PhaseCarbon2())
				.setDensity(173)
				.setViscosity(371)
				.setFurnaceBurnTime(1200)
				.setMatter(c);
		carbonVapor = new Substance("carbon vapor")
				.setColor(Phase.LIQUID, 0xFFFFFF66)
				.setSubstance(new PhaseCarbon1())
				.setPhasePhaseDiagram(new PhaseCarbon2())
				.setDensity(-29)
				.setViscosity(182)
				.setFurnaceBurnTime(1200)
				.setMatter(c);
		nitrogen = new Substance("nitrogen")
				.setColor(Phase.GAS, 0xFFFFFF00)
				.setDensity(-31).setViscosity(38)
				.setPhasePhaseDiagram(new PDPhase(63, 77))
				.setThermalCfg(2912, 31.3F)
				.setMatter("N2");
		oxygen = new Substance("oxygen")
				.setColor(Phase.GAS, 0xFFFFFF00)
				.setColor(Phase.LIQUID, 0x3881FF97)
				.setColor(Phase.SOLID, 0x3881FFB2)
				.setDensity(-39).setViscosity(44)
				.setPhasePhaseDiagram(new PDPhase(54, 90))
				.setThermalCfg(2937, 37.2F)
				.setMatter("O2");
		copper = new Substance("copper")
				.setColor(Phase.SOLID , 0xDB4E31FF)
				.setColor(Phase.LIQUID, 0xFF7096FF)
				.setColor(Phase.GAS   , 0xFFFFFF37)
				.setBlockInfo(17, 500)
				.setToolInfo(71, 4.6F, 172, 0.0F)
				.setViscosity(1281)
				.setXReflectLevel((short) 38)
				.setElectrologyCfg(16.78F, 0.1F)
				.setThermalCfg(2444, 2831F)
				.setDensity(896)
				.setCrafting(48000000L, 117000000L)
				.setMatter("Cu");
		
		Matter H2O = Matter.matter("water", "H2O");
		
		$void = new Substance("void").setColor(Phase.SOLID, 0xFFFFFFFF)
				.setBlockInfo(0, 0).setDensity(0).setViscosity(0)
				.setToolInfo(0, 100.0F, 0, 0).addTag(SubTag.MATERIAL_MINERAL,
						SubTag.MATERIAL_ROCK, SubTag.MATERIAL_SAND);
						
		ice = new Substance("ice").setColor(Phase.SOLID, 0x77A9FFA0)
				.setDensity(90).setBlockInfo(3, 200)
				.setElectrologyCfg(1.28E7F, 0.64E-4F)
				.setThermalCfg(3638, 1.28F)
				.setPhasePhaseDiagram(new PDPhase(273, 373, Integer.MAX_VALUE));
		water = new Substance("water").setColor(Phase.LIQUID, 0x1F55FF8F)
				.setDensity(100).setViscosity(1000)
				.setThermalCfg(7535, 0.58F)
				.setPhasePhaseDiagram(new PDPhase(273, 373, Integer.MAX_VALUE));
		vapor = new Substance("vapor").setColor(Phase.GAS, 0xFFFCFC4F)
				.setDensity(-21).setViscosity(364)
				.setThermalCfg(3729, 71.3F)
				.setPhasePhaseDiagram(new PDPhase(273, 373, Integer.MAX_VALUE));
		saltyWater = new Substance("salty water")
				.setColor(Phase.LIQUID, 0x1F55FF8F).setDensity(105)
				.setViscosity(1050).setThermalCfg(8122, 3.52F)
				.setPhasePhaseDiagram(new PDPhase(259, 391, Integer.MAX_VALUE));
		ice.setMatter(H2O);
		water.setMatter(H2O);
		vapor.setMatter(H2O);
		saltyWater.setMatter("[H2O]99[NaCl]");
		setSubstance(e(Phase.SOLID, ice), e(Phase.LIQUID, water),
				e(Phase.GAS, vapor));
				
		sulfuricAcid = new Substance("sulfuric acid")
				.setColor(Phase.LIQUID, 0xFFFFFF2F)
				.setDensity(180).setViscosity(1750)
				.setThermalCfg(13887, 0.84F)
				.setPH(-27)
				.setPhasePhaseDiagram(new PDPhase(281, 610))
				.setMatter("H2(SO4)");
		
		carbonDioxide = new Substance("carbon dioxide")
				.setColor(Phase.GAS, 0xFFFFFF00)
				.setDensity(-371).setViscosity(38)
				.setThermalCfg(3713, 11.37F)
				.setPhasePhaseDiagram(new PDPhase(194, 194))
				.setMatter("CO2");
		sulfurDioxide = new Substance("sulfur dioxide")
				.setColor(Phase.GAS, 0xFFFFFF00)
				.setDensity(-173).setViscosity(17)
				.setThermalCfg(3418, 0.91F)
				.setPH(371)
				.setPhasePhaseDiagram(new PDPhase(201, 263))
				.setMatter("SO2");
		
		sodiumChloride = new Substance("sodium chloride")
				.setColor(Phase.SOLID, 0xFFFFFFAF)
				.setDensity(2165).setViscosity(975)
				.setThermalCfg(3679, 1.48F)
				.setMatter("NaCl");
		potassiumChloride = new Substance("potassium chloride")
				.setColor(Phase.SOLID, 0xFFFFFFAF)
				.setDensity(1984).setViscosity(1092)
				.setThermalCfg(3719, 1.38F)
				.setMatter("KCl");
		
		cupricOxide = new Substance("cupric oxide")
				.setColor(Phase.SOLID, 0x332516FF)
				.setDensity(632).setViscosity(1174)
				.setThermalCfg(3619, 2.2F)
				.setPhasePhaseDiagram(new PDPhase(1599, 2270))
				.setMatter("CuO");
		cuprousOxide = new Substance("cuprous oxide")
				.setColor(Phase.SOLID, 0xB51E13FF)
				.setDensity(600).setViscosity(1201)
				.setThermalCfg(3271, 2.5F)
				.setPhasePhaseDiagram(new PDPhase(1505, 2070))
				.setMatter("Cu2O");
		copperMonosulfide = new Substance("copper monosulfide")
				.setColor(Phase.SOLID, 0x04040FF)
				.setDensity(476).setViscosity(1201)
				.setThermalCfg(4728, 1.3F)
				.setPhasePhaseDiagram(new PDPhase(770))
				.setMatter("CuS");
		cuprousSulfide = new Substance("cuprous sulfide")
				.setColor(Phase.SOLID, 0x030303FF)
				.setDensity(560).setViscosity(1231)
				.setThermalCfg(3891, 2.3F)
				.setPhasePhaseDiagram(new PDPhase(1400))
				.setMatter("Cu2S");
		basicCopperCarbonate = new Substance("basic copper carbonate")
				.setColor(Phase.SOLID, 0x30CE88)
				.setDensity(480).setViscosity(1401)
				.setThermalCfg(2849, 2.5F)
				.setMatter("Cu2(OH)2(CO3)");
		ironSulfide = new Substance("iron sulfide")
				.setColor(Phase.SOLID, 0x020202FF)
				.setDensity(484).setViscosity(1028)
				.setThermalCfg(2719, 2.4F)
				.setPhasePhaseDiagram(new PDPhase(1467))
				.setMatter("FeS");
		
		slag = new Substance("slag")
				.setColor(Phase.SOLID, 0x828282FF)
				.setColor(Phase.LIQUID, 0xDB0000EF)
				.setBlockInfo(4, 300)
				.setThermalCfg(1732, 23.6F)
				.setViscosity(1600);
		
		stone = new Substance("stone")
				.setColor(Phase.SOLID, 0x7F7F7FFF).setBlockInfo(6, 250)
				.setThermalCfg(2839, 28.3F).setToolInfo(10, 3.5F, 1600, 0.85F)
				.setDensity(200).setViscosity(1500)
				.addTag(SubTag.MATERIAL_ROCK, SubTag.PLANTABLE_CAVE);
		compactStone = new Substance("compact stone")
				.setColor(Phase.SOLID, 0x707070FF).setBlockInfo(8, 375)
				.setThermalCfg(7182, 37.1F).setToolInfo(10, 3.8F, 2100, 0.85F)
				.setDensity(280).setViscosity(1750)
				.addTag(SubTag.MATERIAL_ROCK);
		limestone = new Substance("limestone").setColor(Phase.SOLID, 0xE0E0E0FF)
				.setBlockInfo(4, 185).setThermalCfg(3829, 27.6F)
				.setToolInfo(10, 3.5F, 850, 0.91F).setDensity(200)
				.setViscosity(1500).addTag(SubTag.MATERIAL_ROCK);
		rhyolite = new Substance("rhyolite").setColor(Phase.SOLID, 0x818387FF)
				.setBlockInfo(11, 600).setThermalCfg(2018, 17.3F)
				.setToolInfo(10, 4.0F, 4800, 0.85F).setDensity(1000)
				.setViscosity(1500).addTag(SubTag.MATERIAL_ROCK);
		andesite = new Substance("andesite").setColor(Phase.SOLID, 0x89897DFF)
				.setBlockInfo(9, 485).setThermalCfg(1937, 9.3F)
				.setToolInfo(10, 3.7F, 4100, 0.85F).setDensity(200)
				.setViscosity(1500).addTag(SubTag.MATERIAL_ROCK);
		basalt = new Substance("basalt").setColor(Phase.SOLID, 0x888989FF)
				.setBlockInfo(10, 525).setThermalCfg(1728, 10.3F)
				.setToolInfo(10, 3.7F, 4100, 0.85F).setDensity(200)
				.setViscosity(1500).addTag(SubTag.MATERIAL_ROCK);
		peridotite = new Substance("peridotite")
				.setColor(Phase.SOLID, 0x87926EFF).setBlockInfo(14, 705)
				.setThermalCfg(1829, 12.8F).setToolInfo(10, 4.2F, 4900, 0.85F)
				.setDensity(200).setViscosity(1500)
				.addTag(SubTag.MATERIAL_ROCK);
				
		nativeCopper = new Substance("native copper")
				.setColor(Phase.SOLID, 0xFF834CFF).setBlockInfo(12, 680)
				.setThermalCfg(2371, 391.2F).setDensity(310).setViscosity(1150)
				.addTag(SubTag.MATERIAL_MINERAL);
		nativeCopper.setPhasePhaseDiagram(new PDPhase(1200, 2700));
		nativeCopper.setMatter("Cu");
//		nativeCopper.setSubstance(Phase.LIQUID, copper);
		tetrahedrite = new Substance("tetrahedrite")
				.setColor(Phase.SOLID, 0xDCBC74FF).setBlockInfo(14, 910)
				.setThermalCfg(3728, 21.8F).setDensity(125).setViscosity(1150)
				.addTag(SubTag.MATERIAL_MINERAL);
		tetrahedrite.setPhasePhaseDiagram(new PDPhase(980, 2550));
		tetrahedrite.setMatter("Cu10Fe2Sb4S13");
		enargite = new Substance("enargite").setColor(Phase.SOLID, 0x7F6A68FF)
				.setBlockInfo(13, 875).setThermalCfg(3719, 18.6F)
				.setDensity(150).setViscosity(1125)
				.addTag(SubTag.MATERIAL_MINERAL)
				.setPhasePhaseDiagram(new PDPhase(1020, 2780))
				.setMatter("Cu3AsS4");
		cuprite = new Substance("cuprite").setColor(Phase.SOLID, 0xD83E26FF)
				.setBlockInfo(8, 618).setThermalCfg(2718, 5.7F)
				.setDensity(105).setViscosity(1075)
				.addTag(SubTag.MATERIAL_MINERAL)
				.setPhasePhaseDiagram(new PDPhase(1280, 2630))
				.setMatter("Cu2O");
		tenorite = new Substance("tenorite").setColor(Phase.SOLID, 0x5F6A73FF)
				.setBlockInfo(9, 705).setThermalCfg(2718, 21.1F)
				.setDensity(107).setViscosity(1100)
				.addTag(SubTag.MATERIAL_MINERAL)
				.setPhasePhaseDiagram(new PDPhase(1180, 2170))
				.setMatter("CuO");
		covellite = new Substance("covellite").setColor(Phase.SOLID, 0x3FC2F8FF)
				.setBlockInfo(11, 675).setThermalCfg(3182, 19.3F)
				.setDensity(121).setViscosity(1125)
				.addTag(SubTag.MATERIAL_MINERAL);
		covellite.setPhasePhaseDiagram(new PDPhase(1480, 2370));
		covellite.setMatter("CuS");
		chalcocite = new Substance("chalcocite")
				.setColor(Phase.SOLID, 0xA5B2C0FF).setBlockInfo(11, 680)
				.setThermalCfg(2518, 31.4F).setDensity(120).setViscosity(1130)
				.addTag(SubTag.MATERIAL_MINERAL);
		chalcocite.setPhasePhaseDiagram(new PDPhase(1445, 2572));
		chalcocite.setMatter("Cu2S");
		malachite = new Substance("malachite").setColor(Phase.SOLID, 0x43D393FF)
				.setBlockInfo(6, 420).setThermalCfg(2938, 81.1F)
				.setDensity(100).setViscosity(1125)
				.addTag(SubTag.MATERIAL_MINERAL);
		malachite.setPhasePhaseDiagram(new PDPhase(1028));
		malachite.setMatter("Cu2(OH)2(CO3)");
		azurite = new Substance("azurite").setColor(Phase.SOLID, 0x434BD3FF)
				.setBlockInfo(7, 475).setThermalCfg(2649, 73.2F)
				.setDensity(105).setViscosity(1140)
				.addTag(SubTag.MATERIAL_MINERAL);
		azurite.setPhasePhaseDiagram(new PDPhase(1210));
		azurite.setMatter("Cu2(OH)2(CO3)2");
		bornite = new Substance("bornite").setColor(Phase.SOLID, 0xDCBC74FF)
				.setBlockInfo(9, 625).setThermalCfg(1927, 41.4F)
				.setDensity(120).setViscosity(1085)
				.addTag(SubTag.MATERIAL_MINERAL)
				.setPhasePhaseDiagram(new PDPhase(1410, 2711))
				.setMatter("Cu4FeS5");
		chalcopyrite = new Substance("chalcopyrite")
				.setColor(Phase.SOLID, 0xF8E3A2FF).setBlockInfo(8, 605)
				.setThermalCfg(2638, 26.4F).setDensity(122).setViscosity(1095)
				.addTag(SubTag.MATERIAL_MINERAL);
		chalcopyrite.setPhasePhaseDiagram(new PDPhase(1372, 2711));
		chalcopyrite.setMatter("CuFeS2");
		orpiment = new Substance("orpiment").setColor(Phase.SOLID, 0xF7DA6EFF)
				.setBlockInfo(9, 645).setThermalCfg(1618, 22.8F)
				.setDensity(129).setViscosity(1100)
				.addTag(SubTag.MATERIAL_MINERAL);
		orpiment.setPhasePhaseDiagram(new PDPhase(827, 1387));
		orpiment.setMatter("As2S3");
		realgar = new Substance("realgar").setColor(Phase.SOLID, 0xEF6C6BFF)
				.setBlockInfo(8, 625).setThermalCfg(2651, 41.8F)
				.setDensity(127).setViscosity(1150)
				.addTag(SubTag.MATERIAL_MINERAL);
		realgar.setPhasePhaseDiagram(new PDPhase(917, 1648));
		realgar.setMatter("As4S4");
		arsenolite = new Substance("arsenolite")
				.setColor(Phase.SOLID, 0xEBEFC8FF).setBlockInfo(16, 845)
				.setThermalCfg(2037, 4.8F).setDensity(125).setViscosity(1125)
				.addTag(SubTag.MATERIAL_MINERAL);
		arsenolite.setPhasePhaseDiagram(new PDPhase(829, 1372));
		arsenolite.setMatter("As4S6");
		nickeline = new Substance("nickeline").setColor(Phase.SOLID, 0xC1B67AFF)
				.setBlockInfo(31, 1025).setThermalCfg(2381, 25.0F)
				.setDensity(122).setViscosity(1118)
				.addTag(SubTag.MATERIAL_MINERAL);
		nickeline.setPhasePhaseDiagram(new PDPhase(1023, 2381));
		nickeline.setMatter("NiS");
		arsenopyrite = new Substance("arsenopyrite")
				.setColor(Phase.SOLID, 0xD7D5CBFF).setBlockInfo(17, 875)
				.setThermalCfg(3618, 51.1F).setDensity(119).setViscosity(1128)
				.addTag(SubTag.MATERIAL_MINERAL);
		arsenopyrite.setPhasePhaseDiagram(new PDPhase(1381, 2371));
		arsenopyrite.setMatter("Fe(AsS)");
		scorodite = new Substance("scorodite").setColor(Phase.SOLID, 0xB9DCCEFF)
				.setBlockInfo(17, 875).setThermalCfg(2618, 37.1F)
				.setDensity(117).setViscosity(1128)
				.addTag(SubTag.MATERIAL_MINERAL);
		scorodite.setPhasePhaseDiagram(new PDPhase(1292, 2417));
		scorodite.setMatter("Fe(AsO4)[H2O]2");
		erythrite = new Substance("erythrite").setColor(Phase.SOLID, 0x4268A6FF)
				.setBlockInfo(37, 1217).setThermalCfg(3028, 19.4F)
				.setDensity(110).setViscosity(1097)
				.addTag(SubTag.MATERIAL_MINERAL);
		erythrite.setPhasePhaseDiagram(new PDPhase(1182, 2471));
		erythrite.setMatter("Co3(AsO4)2[H2O]8");
		gelenite = new Substance("gelenite").setColor(Phase.SOLID, 0x9899A4FF)
				.setBlockInfo(11, 725).setThermalCfg(3391, 41.4F)
				.setDensity(129).setViscosity(1248)
				.addTag(SubTag.MATERIAL_MINERAL);
		gelenite.setPhasePhaseDiagram(new PDPhase(736, 1762));
		gelenite.setMatter("PbS");
		sphalerite = new Substance("sphalerite")
				.setColor(Phase.SOLID, 0xD6CCCAFF).setBlockInfo(22, 950)
				.setThermalCfg(2410, 35.6F).setDensity(102).setViscosity(1128)
				.addTag(SubTag.MATERIAL_MINERAL);
		sphalerite.setPhasePhaseDiagram(new PDPhase(938, 2619));
		sphalerite.setMatter("ZnS");
		cassiterite = new Substance("cassiterite")
				.setColor(Phase.SOLID, 0xD1CBB0FF).setBlockInfo(13, 792)
				.setThermalCfg(2103, 52.2F).setDensity(114).setViscosity(1183)
				.addTag(SubTag.MATERIAL_MINERAL);
		cassiterite.setPhasePhaseDiagram(new PDPhase(638, 1639));
		cassiterite.setMatter("SnO2");
		stannite = new Substance("stannite").setColor(Phase.SOLID, 0xD6CCCAFF)
				.setBlockInfo(17, 846).setThermalCfg(2394, 35.4F)
				.setDensity(109).setViscosity(1099)
				.addTag(SubTag.MATERIAL_MINERAL);
		stannite.setPhasePhaseDiagram(new PDPhase(839, 1749));
		stannite.setMatter("Cu2FeSnS4");
		
		beachSand = new Substance("beach sand")
				.setColor(Phase.SOLID, 0xF7F6EFFF).setBlockInfo(2, 75)
				.setThermalCfg(1734, 117.2F).setDensity(875)
				.addTag(SubTag.MATERIAL_SAND, SubTag.PLANTABLE_DESERT,
						SubTag.PLANTABLE_BEACH);
		beachSand.setPhasePhaseDiagram(new PDPhase(1381, 3719));
		saltSand = new Substance("salt sand").setColor(Phase.SOLID, 0xEFE9C4FF)
				.setBlockInfo(2, 95).setThermalCfg(2117, 128.8F)
				.setDensity(90)
				.addTag(SubTag.MATERIAL_SAND, SubTag.PLANTABLE_DESERT);
		saltSand.setPhasePhaseDiagram(new PDPhase(1271, 3719));
		sand = new Substance("sand").setColor(Phase.SOLID, 0xEAE09DFF)
				.setBlockInfo(1, 65).setThermalCfg(1692, 130.6F)
				.setDensity(85)
				.addTag(SubTag.MATERIAL_SAND, SubTag.PLANTABLE_DESERT);
		sand.setPhasePhaseDiagram(new PDPhase(1482, 3719));
		soulSand = new Substance("soul sand").setColor(Phase.SOLID, 0x5F483AFF)
				.setBlockInfo(2, 105).setThermalCfg(10937, 98.3F)
				.setDensity(130)
				.addTag(SubTag.MATERIAL_SAND, SubTag.PLANTABLE_NETHER);
		soulSand.setPhasePhaseDiagram(new PDPhase(2849, 5719));
				
		dirt = new Substance("dirt").setColor(Phase.SOLID, 0x966C4AFF)
				.setBlockInfo(3, 115).setThermalCfg(4817, 9.2F)
				.setDensity(71)
				.addTag(SubTag.MATERIAL_DIRT, SubTag.PLANTABLE_PLAINS);
	}
	
	public static void postInit()
	{
		SubstanceRegistry.register(coal, Part.chunkLarge, new ItemStack(Items.coal, 1, 0));
		
		addRecipe(spontaneous, new ReactionRecipe("nattive copper->Cu", 
				asMap(e(nativeCopper, 4)), asMap(e(copper, 1), e(slag, 3))).setTemperature(1027).setEnergyUse(12000));
		addRecipe(spontaneous, new ReactionRecipe("covellite->CuS", 
				asMap(e(covellite, 4)), asMap(e(copperMonosulfide, 1), e(slag, 3))).setTemperature(682).setEnergyUse(12000));
		addRecipe(spontaneous, new ReactionRecipe("chalcocite->Cu2S", 
				asMap(e(chalcocite, 4)), asMap(e(cuprousSulfide, 1), e(slag, 3))).setTemperature(1362).setEnergyUse(12000));
		addRecipe(spontaneous, new ReactionRecipe("cuprite->Cu2O", 
				asMap(e(cuprite, 4)), asMap(e(cuprousOxide, 1), e(slag, 3))).setTemperature(1427).setEnergyUse(12000));
		addRecipe(spontaneous, new ReactionRecipe("tenorite->CuO", 
				asMap(e(tenorite, 4)), asMap(e(cupricOxide, 1), e(slag, 3))).setTemperature(1462).setEnergyUse(12000));
		addRecipe(spontaneous, new ReactionRecipe("chalcopyrite->CuS+FeS", 
				asMap(e(chalcopyrite, 8)), asMap(e(copperMonosulfide, 1), e(ironSulfide, 1), e(slag, 6))).setTemperature(1027).setEnergyUse(12000));
		addRecipe(spontaneous, new ReactionRecipe("bornite->4CuS+FeS", 
				asMap(e(bornite, 20)), asMap(e(copperMonosulfide, 4), e(ironSulfide, 1), e(slag, 15))).setTemperature(1109).setEnergyUse(12000));
		addRecipe(unstable, new ReactionRecipe("malachite", 
				asMap(e(malachite, 4)), asMap(e(basicCopperCarbonate, 1), e(slag, 3))).setTemperature(637).setEnergyUse(2000));

		addRecipe(requirement, new ReactionRecipe("Cu2(OH)2CO3->2CuO+H2O+CO2", 
				asMap(e(basicCopperCarbonate, 10)), asMap(e(cupricOxide, 4), e(water, 3), e(carbonDioxide, 3))).setTemperature(762).setEnergyUse(3801));
		addRecipe(requirement, new ReactionRecipe("2CuS+3O2->2CuO+2SO2", 
				asMap(e(copperMonosulfide, 4), e(oxygen, 6)), asMap(e(cupricOxide, 4), e(sulfurDioxide, 6))).setTemperature(817).setEnergyUse(-7810));
		addRecipe(requirement, new ReactionRecipe("Cu2S+O2->2CuO+SO2", 
				asMap(e(cuprousSulfide, 3), e(oxygen, 2)), asMap(e(cupricOxide, 4), e(sulfurDioxide, 3))).setTemperature(746).setEnergyUse(-10380));
		addRecipe(requirement, new ReactionRecipe("4CuO+C->Cu2O+CO2", 
				asMap(e(cupricOxide, 8), e(coal, 1)), asMap(e(cupricOxide, 3), e(carbonDioxide, 3))).setTemperature(616).setEnergyUse(28500));
		addRecipe(requirement, new ReactionRecipe("2Cu2O+C->4Cu+CO2", 
				asMap(e(cuprousOxide, 6), e(coal, 1)), asMap(e(copper, 4), e(carbonDioxide, 3))).setTemperature(1218).setEnergyUse(31900));
		
		for (Substance substance : Substance.getRegister().targetSet())
		{
			if (!substance.contain(SubTag.NOT_REGISTER))
			{
				FleResource.registerSubstance(substance);
			}
		}
	}
	
	private static void setSubstance(Entry<Phase, Substance>... entrys)
	{
		for (Entry<Phase, Substance> entry1 : entrys)
			for (Entry<Phase, Substance> entry2 : entrys)
			{
				entry1.getValue().setSubstance(entry2.getKey(),
						entry2.getValue());
			}
	}
}