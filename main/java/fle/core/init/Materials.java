package fle.core.init;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import net.minecraft.init.Blocks;
import static fle.api.enums.CompoundType.*;
import static fle.api.enums.EnumAtoms.*;
import fle.api.enums.EnumAtoms;
import fle.api.material.MaterialAbstract;
import fle.api.material.MaterialAlloy;
import fle.api.material.MaterialOre;
import fle.api.material.MaterialRock;
import fle.api.material.Matter;
import fle.api.material.Matter.AtomStack;
import fle.api.material.PropertyInfo;
import fle.api.util.FleEntry;
import fle.api.util.SubTag;
import fle.core.util.DitchInfo;
import fle.core.util.FleAlloy;

public class Materials
{
	public static MaterialAbstract Void;
	
	public static MaterialOre NativeCopper;
	public static MaterialOre Tetrahedrite;
	public static MaterialOre Enargite;
	public static MaterialOre Cuprite;
	public static MaterialOre Tenorite;
	public static MaterialOre Covellite;
	public static MaterialOre Chalcocite;
	public static MaterialOre Malachite;
	public static MaterialOre Azurite;
	public static MaterialOre Chalcopyrite;
	public static MaterialOre Bornite;
	public static MaterialOre Orpiment;
	public static MaterialOre Realgar;
	public static MaterialOre Arsenolite;
	public static MaterialOre Nickeline;
	public static MaterialOre Arsenopyrite;
	public static MaterialOre Schorodite;
	public static MaterialOre Erythrite;

	public static MaterialAbstract SoftWood;
	public static MaterialAbstract HardWood;
	public static MaterialAbstract Charcoal;
	public static MaterialAbstract Flint;
	public static MaterialAbstract Obsidian;
	public static MaterialAbstract Argil;
	
	public static MaterialRock Stone;
	public static MaterialRock CompactStone;
	public static MaterialRock Limestone;

	public static MaterialAbstract Copper;
	public static MaterialAlloy CuAs;
	public static MaterialAlloy CuAs2;
	public static MaterialAlloy CuSn;
	public static MaterialAlloy CuSn2;
	public static MaterialAlloy CuPb;
	public static MaterialAlloy CuPb2;
	public static MaterialAlloy CuSnPb;
	public static MaterialAlloy CuZn;
	public static MaterialAlloy CuZn2;

	public static DitchInfo ditch_stone;
	public static DitchInfo ditch_wood0;
	public static DitchInfo ditch_wood1;
	public static DitchInfo ditch_wood2;
	public static DitchInfo ditch_wood3;
	public static DitchInfo ditch_wood4;
	public static DitchInfo ditch_wood5;
	
	public static void init()
	{
		Void = new MaterialAbstract("Void", new PropertyInfo(1, 1.0F, 1.0F, 1.0F, 0.0F, 10000000));
		NativeCopper = new MaterialOre("NativeCopper", EnumAtoms.Cu.asMatter(), new PropertyInfo(0xFF834C, 721, 1735, 698, 500, 1200, 1.2F, 0.8F, 1.0F, 0.2F, 12000000, 18.0F, 0.18F, 0.7F), SubTag.ORE_native);
		Tetrahedrite = new MaterialOre("Tetrahedrite", Matter.mCu10Fe2Sb4S13, new PropertyInfo(0xDCBC74, 9, 2.1F, 0.3F, 1.0F, 0.4F, 6800000, -1.0F, 0.21F, 0.7F), SubTag.ORE_sulfide);
		Enargite = new MaterialOre("Enargite", Matter.mCu3AsS4, new PropertyInfo(0x7F6A68, 10, 1.3F, 1.0F, 0.4F, 0.5F, 6000000, -1.0F, 0.32F, 0.58F), SubTag.ORE_sulfide);
		Cuprite = new MaterialOre("Cuprite", Matter.mCu2O, new PropertyInfo(0xD83E26, 8, 1.0F, 0.4F, 1.0F, 0.9F, 8000000, 52.0F, 0.43F, 0.5F), SubTag.ORE_oxide);
		Tenorite = new MaterialOre("Tenorite", Matter.mCuO, new PropertyInfo(0x5F6A73, 9, 1.2F, 0.6F, 1.0F, 1.0F, 9000000, 59.0F, 0.43F, 0.52F), SubTag.ORE_oxide);
		Covellite = new MaterialOre("Covellite", Matter.mCuS, new PropertyInfo(new int[]{0x3FC2F8, 0x643EB7}, 11, 2.0F, 0.5F, 1.0F, 1.1F, 7500000, -1.0F, 0.35F, 0.45F), SubTag.ORE_sulfide);
		Chalcocite = new MaterialOre("Chalcocite", Matter.mCu2S, new PropertyInfo(0xA5B2C0, 11, 1.3F, 0.2F, 1.0F, 0.6F, 4800000, -1.0F, 0.38F, 0.55F), SubTag.ORE_sulfide);
		Malachite = new MaterialOre("Malachite", Matter.mCu_OH2_CO3, new PropertyInfo(0x43D393, 6, 0.8F, 0.3F, 0.9F, 0.7F, 7200000, -1.0F, 0.12F, 0.64F), SubTag.ORE_hydroxide, SubTag.ORE_oxide, SubTag.ORE_gem);
		Azurite = new MaterialOre("Azurite", Matter.mCu_OH2_2CO3, new PropertyInfo(0x434BD3, 7, 1.0F, 0.3F, 0.9F, 0.8F, 8000000, -1.0F, 0.21F, 0.56F), SubTag.ORE_hydroxide, SubTag.ORE_oxide, SubTag.ORE_gem);
		Chalcopyrite = new MaterialOre("Chalcopyrite", Matter.mCuFeS2, new PropertyInfo(0xF8E3A2, 8, 1.0F, 0.4F, 1.0F, 0.7F, 8400000, -1.0F, 0.28F, 0.59F), SubTag.ORE_sulfide);
		Bornite = new MaterialOre("Bornite", Matter.mCu5FeS4, new PropertyInfo(new int[]{0xDCBC74, 0x9D7797}, 9, 1.4F, 0.4F, 1.0F, 0.8F, 9200000, -1.0F, 0.30F, 0.6F), SubTag.ORE_sulfide);
		Orpiment = new MaterialOre("Orpiment", Matter.mAs2S3, new PropertyInfo(0xF7DA6E, 2, 0.8F, 0.1F, 1.0F, 0.3F, 5900000, -1.0F, 0.32F, 0.7F), SubTag.ORE_sulfide);
		Realgar = new MaterialOre("Realgar", Matter.mAs4S4, new PropertyInfo(0xEF6C6B, 3, 0.9F, 0.1F, 1.0F, 0.4F, 7800000, -1.0F, 0.31F, 0.8F), SubTag.ORE_sulfide);
		Arsenolite = new MaterialOre("Arsenolite", Matter.mAs4S6, new PropertyInfo(0xEBEFC8, 3, 1.6F, 0.1F, 1.0F, 0.3F, 8200000, -1.0F, 0.32F, 0.7F), SubTag.ORE_sulfide);
		Nickeline = new MaterialOre("Nickeline", Matter.mNiAs, new PropertyInfo(0xC1B67A, 18, 3.8F, 0.1F, 1.3F, 0.2F, 10200000, -1.0F, 0.33F, 0.7F), SubTag.ORE_TYPE_default);
		Arsenopyrite = new MaterialOre("Arsenopyrite", Matter.mFeAsS, new PropertyInfo(0xD7D5CB, 9, 2.1F, 0.1F, 1.1F, 0.2F, 8100000, -1.0F, 0.31F, 0.7F), SubTag.ORE_TYPE_default, SubTag.ORE_sulfide);
		Schorodite = new MaterialOre("Schorodite", Matter.mFeAsO4_2H2O, new PropertyInfo(new int[]{0xB9DCCE, 0x4268A6}, 12, 2.5F, 0.1F, 1.2F, 0.2F, 8900000, -1.0F, 0.32F, 0.7F), SubTag.ORE_TYPE_default);
		Erythrite = new MaterialOre("Erythrite", Matter.mCo3_AsO4_2_8H2O, new PropertyInfo(0xE180B0, 19, 4.1F, 0.1F, 1.2F, 0.2F, 12300000, -1.0F, 0.34F, 0.6F), SubTag.ORE_TYPE_default);
		
		Flint = new MaterialAbstract("Flint", Matter.mSiO2, new PropertyInfo(0x5A5A5A, 12, 0.6F, 0.0F, 1.1F, 1.5F, 13500000), SubTag.TOOL_stone, SubTag.TOOL_flint);
		Obsidian = new MaterialAbstract("Obsidian", Matter.mSiO2, new PropertyInfo(0x33344F, 16, 2.1F, 0.0F, 1.0F, 3.0F, 7500000), SubTag.TOOL_stone, SubTag.TOOL_flint);
		HardWood = new MaterialAbstract("HardWood", new PropertyInfo(0x7F643D, 20, 0.4F, 1.0F, 0.1F, 0.1F, 4600000, -1F, 0.5F, 2.0F), SubTag.TOOL_wood);
		SoftWood = new MaterialAbstract("SoftWood", new PropertyInfo(0x8F744D, 6, 0.2F, 1.1F, 0.05F, 0.07F, 2800000, -1F, 0.3F, 1.9F));
		Charcoal = new MaterialAbstract("Charcoal", EnumAtoms.C.asMatter(), new PropertyInfo(0x35322A, 2, 0.03F, 0.0F, 0.08F, 0.5F, 4000000, 98F, 0.6F, 1.2F));
		Argil = new MaterialAbstract("Argil", new PropertyInfo(0xAE9789, 29, 0.7F, 0.0F, 1.0F, 1.9F, 10800000, 59.1F, 0.2F, 2.3F));
		Stone = new MaterialRock("Stone", new PropertyInfo(0x626262, 16, 1.2F, 0.0F, 1.0F, 1.4F, 10000000, -1.0F, 0.38F, 2.1F), SubTag.TOOL_stone, SubTag.TOOL_stone_real);
		CompactStone = new MaterialRock("CompactStone", new PropertyInfo(0x686868, 21, 1.2F, 0.1F, 2.0F, 1.3F, 12800000), SubTag.TOOL_stone, SubTag.TOOL_stone_real);
		Limestone = new MaterialRock("Limestone", new PropertyInfo(0xE4E4E5, 2, 0.8F, 0.2F, 1.0F, 1.8F, 5600000));
		Copper = new MaterialAbstract("Copper", EnumAtoms.Cu.asMatter(), new PropertyInfo(0xDB4E31, 52, 857, 1735, 698, 500, 0.8F, 1600, 2.3F, 8.0F, 1.2F, 0.0F, 48000000, 4F, 1F, 0.19F), SubTag.TOOL_metal_tier0, SubTag.CRAFTING_cold_wought);
		MaterialAbstract.pureMaterials.register(Copper, "Cu");
		CuAs = new MaterialAlloy("CuAs", new Matter(Alloy, new AtomStack(Cu, 9), new AtomStack(As)), 
				new PropertyInfo(0xA37C68, 175, 684, 1735, 702, 7086, 0.2F, 1500, 1.6F, 1.7F, 1.1F, 0.2F, 92000000, 102F, 0.97F, 0.21F), 
				new FleAlloy(FleEntry.asMap(new FleEntry(EnumAtoms.Cu, new double[]{0.8D, 0.95D}), new FleEntry(EnumAtoms.As, new double[]{0.05D, 0.2D}))), SubTag.TOOL_metal_tier1);
		CuAs2 = new MaterialAlloy("CuAs2", new Matter(Alloy, new AtomStack(Cu, 3), new AtomStack(As)), 
				new PropertyInfo(0x6A6353, 135, 573, 1735, 689, 6894, 0.1F, 1650, 1.4F, 0.5F, 1.1F, 0.4F, 86000000, 100F, 0.96F, 0.22F), 
				new FleAlloy(FleEntry.asMap(new FleEntry(EnumAtoms.Cu, new double[]{0.5D, 0.8D}), new FleEntry(EnumAtoms.As, new double[]{0.1D, 0.5D}))), SubTag.TOOL_metal_tier1);
		CuPb = new MaterialAlloy("CuPb", new Matter(Alloy, new AtomStack(Cu, 17), new AtomStack(Pb, 2)), 
				new PropertyInfo(0x8E8741, 200, 671, 1735, 702, 8291, 0.2F, 1500, 1.8F, 1.6F, 1.2F, 0.1F, 97000000, 98F, 0.94F, 0.22F), 
				new FleAlloy(FleEntry.asMap(new FleEntry(EnumAtoms.Cu, new double[]{0.8D, 0.95D}), new FleEntry(EnumAtoms.Pb, new double[]{0.05D, 0.2D}))), SubTag.TOOL_metal_tier1);
		CuPb2 = new MaterialAlloy("CuPb2", new Matter(Alloy, new AtomStack(Cu, 3), new AtomStack(Pb, 2)), 
				new PropertyInfo(0x817E66, 160, 628, 1735, 702, 8028, 0.3F, 1500, 1.7F, 1.2F, 1.3F, 0.3F, 82000000, 101F, 0.99F, 0.21F), 
				new FleAlloy(FleEntry.asMap(new FleEntry(EnumAtoms.Cu, new double[]{0.4D, 0.8D}), new FleEntry(EnumAtoms.Pb, new double[]{0.2D, 0.6D}))), SubTag.TOOL_metal_tier1);
		CuSn = new MaterialAlloy("CuSn", new Matter(Alloy, new AtomStack(Cu, 17), new AtomStack(Sn, 2)), 
				new PropertyInfo(0x936436, 215, 648, 1735, 702, 8028, 0.3F, 1500, 1.9F, 1.2F, 1.1F, 0.1F, 102000000, 103F, 0.93F, 0.23F), 
				new FleAlloy(FleEntry.asMap(new FleEntry(EnumAtoms.Cu, new double[]{0.8D, 0.95D}), new FleEntry(EnumAtoms.Sn, new double[]{0.05D, 0.2D}))), SubTag.TOOL_metal_tier1);
		CuSn2 = new MaterialAlloy("CuSn2", new Matter(Alloy, new AtomStack(Cu, 3), new AtomStack(Sn, 2)), 
				new PropertyInfo(0xA28A72, 175, 629, 1735, 702, 7960, 0.26F, 1500, 1.5F, 1.1F, 1.13F, 0.18F, 93000000, 109F, 0.91F, 0.27F), 
				new FleAlloy(FleEntry.asMap(new FleEntry(EnumAtoms.Cu, new double[]{0.4D, 0.8D}), new FleEntry(EnumAtoms.Sn, new double[]{0.2D, 0.6D}))), SubTag.TOOL_metal_tier1);
		CuSnPb = new MaterialAlloy("CuSnPb", new Matter(Alloy, new AtomStack(Cu, 8), new AtomStack(Sn, 1), new AtomStack(Pb, 1)), 
				new PropertyInfo(0xD0B36A, 208, 682, 1735, 689, 5839, 0.26F, 1600, 1.6F, 1.2F, 1.11F, 0.19F, 99000000, 129F, 0.87F, 0.29F), 
				new FleAlloy(FleEntry.asMap(new FleEntry(EnumAtoms.Cu, new double[]{0.8D, 0.9D}), new FleEntry(EnumAtoms.Sn, new double[]{0.05D, 0.15D}), new FleEntry(EnumAtoms.Pb, new double[]{0.05D, 0.15D}))), SubTag.TOOL_metal_tier1);
		ditch_stone = new DitchInfo("Stone", Stone, Blocks.stone, 0.01F, 9000, 1500);
		ditch_wood0 = new DitchInfo("Wood0", HardWood, Blocks.planks, 0, 0.03F, 4000, 380);
		ditch_wood1 = new DitchInfo("Wood1", SoftWood, Blocks.planks, 1, 0.06F, 5000, 375);
		ditch_wood2 = new DitchInfo("Wood2", HardWood, Blocks.planks, 2, 0.03F, 4000, 380);
		ditch_wood3 = new DitchInfo("Wood3", HardWood, Blocks.planks, 3, 0.03F, 4000, 380);
		ditch_wood4 = new DitchInfo("Wood4", SoftWood, Blocks.planks, 4, 0.06F, 5000, 375);
		ditch_wood5 = new DitchInfo("Wood5", HardWood, Blocks.planks, 5, 0.03F, 4000, 380);
	}
	
	public static void postInit()
	{
		if(MaterialAbstract.property != null)
		{
			BufferedWriter fw = null;
			try
			{
				fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
						MaterialAbstract.property), "UTF-8"), 1024);
				fw.write("Material Name, Color A, Color B, Max Uses, Melting Point, "
						+ "Boiling Point, Viscosity, Hardness, Toughness, Brittleness, "
						+ "Denseness, Shear Strength, Resistance, Specific Heat, Thermal Conductivity"
						+ "\r\n");
				for(MaterialAbstract material : MaterialAbstract.getMaterialRegistry())
				{
					PropertyInfo i = material.getPropertyInfo();
					fw.write(String.format("%s, %d, %d, %d, %d, %d, %d, %g, %g, %g, %g, %d, %g, %g, %g\r\n", 
							MaterialAbstract.getMaterialRegistry().name(material),
							i.getColors()[0],
							i.getColors()[1],
							i.getMaxUses(),
							i.getMeltingPoint(),
							i.getBoilingPoint(),
							i.getViscosity(),
							i.getHardness(),
							i.getToughness(),
							i.getBrittleness(),
							i.getDenseness(),
							(int) Math.pow(10, i.getShearStrength()),
							i.getResistance(),
							(float) i.getSpecificHeat(),
							i.getThermalConductivity()));
				}
				fw.flush();
				fw.close();
			}
			catch(Throwable e)
			{
				e.printStackTrace();
			}
			finally
			{
				if(fw != null)
				{
					try
					{
						fw.close();
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
}