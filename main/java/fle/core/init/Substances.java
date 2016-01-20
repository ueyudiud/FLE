package fle.core.init;

import static farcore.collection.CollectionUtil.e;
import static fle.init.Substance1.*;
import static fle.init.Substance2.ice;
import static fle.init.Substance2.saltyWater;
import static fle.init.Substance2.vapor;
import static fle.init.Substance2.water;

import java.util.Map.Entry;

import farcore.substance.Matter;
import farcore.substance.PDPhase;
import farcore.substance.PhaseDiagram.Phase;
import farcore.substance.Substance;
import farcore.util.SubTag;
import flapi.FleResource;

public class Substances
{
	public static void init()
	{
		Matter H2O = Matter.matter("water", "H2O");
		
		$void = new Substance("void").setColor(Phase.SOLID, 0xFFFFFFFF)
				.setBlockInfo(0, 0).setDensity(0).setViscosity(0)
				.setToolInfo(0, 100.0F, 0, 0).addTag(SubTag.MATERIAL_MINERAL,
						SubTag.MATERIAL_ROCK, SubTag.MATERIAL_SAND);
						
		ice = new Substance("ice").setColor(Phase.SOLID, 0x77A9FFA0)
				.setDensity(90).setBlockInfo(3, 200)
				.setElectrologyCfg(1.28E7F, 0.64E-4F)
				.setThermalCfg(24000000, 4.1F)
				.setPhasePhaseDiagram(new PDPhase(273, 373, Integer.MAX_VALUE));
		water = new Substance("water").setColor(Phase.LIQUID, 0x1F55FF8F)
				.setDensity(100).setViscosity(1000)
				.setThermalCfg(48000000, 3.5F)
				.setPhasePhaseDiagram(new PDPhase(273, 373, Integer.MAX_VALUE));
		vapor = new Substance("vapor").setColor(Phase.GAS, 0xFFFCFC4F)
				.setDensity(-21).setViscosity(364).setThermalCfg(1640000, 71.3F)
				.setPhasePhaseDiagram(new PDPhase(273, 373, Integer.MAX_VALUE));
		saltyWater = new Substance("salty water")
				.setColor(Phase.LIQUID, 0x1F55FF8F).setDensity(105)
				.setViscosity(1050).setThermalCfg(48000000, 3.52F)
				.setPhasePhaseDiagram(new PDPhase(259, 391, Integer.MAX_VALUE));
		ice.setMatter(H2O);
		water.setMatter(H2O);
		vapor.setMatter(H2O);
		saltyWater.setMatter("[H2O]99[NaCl]");
		setSubstance(e(Phase.SOLID, ice), e(Phase.LIQUID, water),
				e(Phase.GAS, vapor));
				
		stone = new Substance("stone").setColor(Phase.SOLID, 0x7F7F7FFF)
				.setBlockInfo(6, 250).setThermalCfg(1800000, 1.8F)
				.setToolInfo(10, 3.5F, 1600, 0.85F).setDensity(1000)
				.setViscosity(1500)
				.addTag(SubTag.MATERIAL_ROCK, SubTag.PLANTABLE_CAVE);
		compactStone = new Substance("compact stone")
				.setColor(Phase.SOLID, 0x707070FF).setBlockInfo(8, 375)
				.setThermalCfg(2400000, 1.8F).setToolInfo(10, 3.8F, 2100, 0.85F)
				.setDensity(1250).setViscosity(1750)
				.addTag(SubTag.MATERIAL_ROCK);
		limestone = new Substance("limestone").setColor(Phase.SOLID, 0xE0E0E0FF)
				.setBlockInfo(4, 185).setThermalCfg(1900000, 1.8F)
				.setToolInfo(10, 3.5F, 850, 0.91F).setDensity(1000)
				.setViscosity(1500).addTag(SubTag.MATERIAL_ROCK);
		rhyolite = new Substance("rhyolite").setColor(Phase.SOLID, 0x818387FF)
				.setBlockInfo(11, 600).setThermalCfg(1900000, 1.8F)
				.setToolInfo(10, 4.0F, 4800, 0.85F).setDensity(1000)
				.setViscosity(1500).addTag(SubTag.MATERIAL_ROCK);
		andesite = new Substance("andesite").setColor(Phase.SOLID, 0x89897DFF)
				.setBlockInfo(9, 485).setThermalCfg(1900000, 1.8F)
				.setToolInfo(10, 3.7F, 4100, 0.85F).setDensity(1000)
				.setViscosity(1500).addTag(SubTag.MATERIAL_ROCK);
		basalt = new Substance("basalt").setColor(Phase.SOLID, 0x888989FF)
				.setBlockInfo(10, 525).setThermalCfg(1900000, 1.8F)
				.setToolInfo(10, 3.7F, 4100, 0.85F).setDensity(1000)
				.setViscosity(1500).addTag(SubTag.MATERIAL_ROCK);
		peridotite = new Substance("peridotite")
				.setColor(Phase.SOLID, 0x87926EFF).setBlockInfo(14, 705)
				.setThermalCfg(1900000, 1.8F).setToolInfo(10, 4.2F, 4900, 0.85F)
				.setDensity(1000).setViscosity(1500)
				.addTag(SubTag.MATERIAL_ROCK);
				
		nativeCopper = new Substance("native copper")
				.setColor(Phase.SOLID, 0xFF834CFF).setBlockInfo(12, 680)
				.setThermalCfg(320000, 5.2F).setDensity(1100).setViscosity(1150)
				.addTag(SubTag.MATERIAL_MINERAL);
		nativeCopper.setMatter("Cu");
		tetrahedrite = new Substance("tetrahedrite")
				.setColor(Phase.SOLID, 0xDCBC74FF).setBlockInfo(14, 910)
				.setThermalCfg(480000, 5.8F).setDensity(1125).setViscosity(1150)
				.addTag(SubTag.MATERIAL_MINERAL);
		tetrahedrite.setMatter("Cu10Fe2Sb4S13");
		enargite = new Substance("enargite").setColor(Phase.SOLID, 0x7F6A68FF)
				.setBlockInfo(13, 875).setThermalCfg(450000, 5.6F)
				.setDensity(1250).setViscosity(1125)
				.addTag(SubTag.MATERIAL_MINERAL);
		enargite.setMatter("Cu3AsS4");
		cuprite = new Substance("cuprite").setColor(Phase.SOLID, 0xD83E26FF)
				.setBlockInfo(8, 618).setThermalCfg(420000, 5.7F)
				.setDensity(1050).setViscosity(1075)
				.addTag(SubTag.MATERIAL_MINERAL);
		cuprite.setMatter("Cu2O");
		tenorite = new Substance("tenorite").setColor(Phase.SOLID, 0x5F6A73FF)
				.setBlockInfo(9, 705).setThermalCfg(475000, 5.1F)
				.setDensity(1075).setViscosity(1100)
				.addTag(SubTag.MATERIAL_MINERAL);
		tenorite.setMatter("CuO");
		covellite = new Substance("covellite").setColor(Phase.SOLID, 0x3FC2F8FF)
				.setBlockInfo(11, 675).setThermalCfg(460000, 5.3F)
				.setDensity(1215).setViscosity(1125)
				.addTag(SubTag.MATERIAL_MINERAL);
		covellite.setMatter("CuS");
		chalcocite = new Substance("chalcocite")
				.setColor(Phase.SOLID, 0xA5B2C0FF).setBlockInfo(11, 680)
				.setThermalCfg(452000, 5.4F).setDensity(1200).setViscosity(1130)
				.addTag(SubTag.MATERIAL_MINERAL);
		chalcocite.setMatter("Cu2S");
		malachite = new Substance("malachite").setColor(Phase.SOLID, 0x43D393FF)
				.setBlockInfo(6, 420).setThermalCfg(418000, 5.1F)
				.setDensity(1000).setViscosity(1125)
				.addTag(SubTag.MATERIAL_MINERAL);
		malachite.setMatter("Cu2(OH)2(CO3)");
		azurite = new Substance("azurite").setColor(Phase.SOLID, 0x434BD3FF)
				.setBlockInfo(7, 475).setThermalCfg(425000, 5.2F)
				.setDensity(1050).setViscosity(1140)
				.addTag(SubTag.MATERIAL_MINERAL);
		azurite.setMatter("Cu2(OH)2(CO3)2");
		bornite = new Substance("bornite").setColor(Phase.SOLID, 0xDCBC74FF)
				.setBlockInfo(9, 625).setThermalCfg(435000, 5.4F)
				.setDensity(1203).setViscosity(1085)
				.addTag(SubTag.MATERIAL_MINERAL);
		bornite.setMatter("Cu4FeS5");
		chalcopyrite = new Substance("chalcopyrite")
				.setColor(Phase.SOLID, 0xF8E3A2FF).setBlockInfo(8, 605)
				.setThermalCfg(448000, 5.3F).setDensity(1220).setViscosity(1095)
				.addTag(SubTag.MATERIAL_MINERAL);
		chalcopyrite.setMatter("CuFeS2");
		orpiment = new Substance("orpiment").setColor(Phase.SOLID, 0xF7DA6EFF)
				.setBlockInfo(9, 645).setThermalCfg(395000, 4.7F)
				.setDensity(1290).setViscosity(1100)
				.addTag(SubTag.MATERIAL_MINERAL);
		orpiment.setMatter("As2S3");
		realgar = new Substance("realgar").setColor(Phase.SOLID, 0xEF6C6BFF)
				.setBlockInfo(8, 625).setThermalCfg(398000, 4.8F)
				.setDensity(1270).setViscosity(1150)
				.addTag(SubTag.MATERIAL_MINERAL);
		realgar.setMatter("As4S4");
		arsenolite = new Substance("arsenolite")
				.setColor(Phase.SOLID, 0xEBEFC8FF).setBlockInfo(16, 845)
				.setThermalCfg(397500, 4.8F).setDensity(1235).setViscosity(1125)
				.addTag(SubTag.MATERIAL_MINERAL);
		arsenolite.setMatter("As4S6");
		nickeline = new Substance("nickeline").setColor(Phase.SOLID, 0xC1B67AFF)
				.setBlockInfo(31, 1025).setThermalCfg(432000, 5.0F)
				.setDensity(1215).setViscosity(1118)
				.addTag(SubTag.MATERIAL_MINERAL);
		nickeline.setMatter("NiS");
		arsenopyrite = new Substance("arsenopyrite")
				.setColor(Phase.SOLID, 0xD7D5CBFF).setBlockInfo(17, 875)
				.setThermalCfg(427000, 5.1F).setDensity(1197).setViscosity(1128)
				.addTag(SubTag.MATERIAL_MINERAL);
		arsenopyrite.setMatter("Fe(AsS)");
		scorodite = new Substance("scorodite").setColor(Phase.SOLID, 0xB9DCCEFF)
				.setBlockInfo(17, 875).setThermalCfg(427000, 5.1F)
				.setDensity(1197).setViscosity(1128)
				.addTag(SubTag.MATERIAL_MINERAL);
		scorodite.setMatter("Fe(AsO4)[H2O]2");
		erythrite = new Substance("erythrite").setColor(Phase.SOLID, 0x4268A6FF)
				.setBlockInfo(37, 1217).setThermalCfg(438000, 5.4F)
				.setDensity(1103).setViscosity(1097)
				.addTag(SubTag.MATERIAL_MINERAL);
		erythrite.setMatter("Co3(AsO4)2[H2O]8");
		gelenite = new Substance("gelenite").setColor(Phase.SOLID, 0x9899A4FF)
				.setBlockInfo(11, 725).setThermalCfg(418000, 5.4F)
				.setDensity(1295).setViscosity(1248)
				.addTag(SubTag.MATERIAL_MINERAL);
		gelenite.setMatter("PbS");
		sphalerite = new Substance("sphalerite")
				.setColor(Phase.SOLID, 0xD6CCCAFF).setBlockInfo(22, 950)
				.setThermalCfg(438000, 5.6F).setDensity(1102).setViscosity(1128)
				.addTag(SubTag.MATERIAL_MINERAL);
		sphalerite.setMatter("ZnS");
		cassiterite = new Substance("cassiterite")
				.setColor(Phase.SOLID, 0xD1CBB0FF).setBlockInfo(13, 792)
				.setThermalCfg(451000, 5.2F).setDensity(1147).setViscosity(1183)
				.addTag(SubTag.MATERIAL_MINERAL);
		cassiterite.setMatter("SnO2");
		stannite = new Substance("stannite").setColor(Phase.SOLID, 0xD6CCCAFF)
				.setBlockInfo(17, 846).setThermalCfg(430000, 5.4F)
				.setDensity(1092).setViscosity(1099)
				.addTag(SubTag.MATERIAL_MINERAL);
		stannite.setMatter("Cu2FeSnS4");
		
		beachSand = new Substance("beach sand")
				.setColor(Phase.SOLID, 0xF7F6EFFF).setBlockInfo(2, 75)
				.setThermalCfg(274000, 11.2F).setDensity(875)
				.addTag(SubTag.MATERIAL_SAND, SubTag.PLANTABLE_DESERT,
						SubTag.PLANTABLE_BEACH);
		saltSand = new Substance("salt sand").setColor(Phase.SOLID, 0xEFE9C4FF)
				.setBlockInfo(2, 95).setThermalCfg(294000, 12.8F)
				.setDensity(900)
				.addTag(SubTag.MATERIAL_SAND, SubTag.PLANTABLE_DESERT);
		sand = new Substance("sand").setColor(Phase.SOLID, 0xEAE09DFF)
				.setBlockInfo(1, 65).setThermalCfg(236000, 13.6F)
				.setDensity(850)
				.addTag(SubTag.MATERIAL_SAND, SubTag.PLANTABLE_DESERT);
		soulSand = new Substance("soul sand").setColor(Phase.SOLID, 0x5F483AFF)
				.setBlockInfo(2, 105).setThermalCfg(830000, 9.3F)
				.setDensity(1300)
				.addTag(SubTag.MATERIAL_SAND, SubTag.PLANTABLE_NETHER);
				
		dirt = new Substance("dirt").setColor(Phase.SOLID, 0x966C4AFF)
				.setBlockInfo(3, 115).setThermalCfg(471000, 9.2F)
				.setDensity(710)
				.addTag(SubTag.MATERIAL_DIRT, SubTag.PLANTABLE_PLAINS);
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
	
	public static void postInit()
	{
		for (Substance substance : Substance.getRegister().targetSet())
		{
			if (!substance.contain(SubTag.NOT_REGISTER))
			{
				FleResource.registerSubstance(substance);
			}
		}
	}
}