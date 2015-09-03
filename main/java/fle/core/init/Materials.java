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
		Flint = new MaterialAbstract("Flint", Matter.mSiO2, new PropertyInfo(0x5A5A5A, 12, 0.6F, 0.0F, 1.1F, 1.5F, 13500000), SubTag.TOOL_stone, SubTag.TOOL_flint);
		Obsidian = new MaterialAbstract("Obsidian", Matter.mSiO2, new PropertyInfo(0x33344F, 16, 2.1F, 0.0F, 1.0F, 3.0F, 7500000), SubTag.TOOL_stone, SubTag.TOOL_flint);
		HardWood = new MaterialAbstract("HardWood", new PropertyInfo(0x7F643D, 20, 0.4F, 1.0F, 0.1F, 0.1F, 4600000, -1F, 0.5F, 2.0F), SubTag.TOOL_wood);
		SoftWood = new MaterialAbstract("SoftWood", new PropertyInfo(0x8F744D, 6, 0.2F, 1.1F, 0.05F, 0.07F, 2800000, -1F, 0.3F, 1.9F));
		Charcoal = new MaterialAbstract("Charcoal", EnumAtoms.C.asMatter(), new PropertyInfo(0x35322A, 2, 0.03F, 0.0F, 0.08F, 0.5F, 4000000, 98F, 0.6F, 1.2F));
		Argil = new MaterialAbstract("Argil", new PropertyInfo(0xAE9789, 29, 0.7F, 0.0F, 1.0F, 1.9F, 10800000, 59.1F, 0.2F, 2.3F));
		Stone = new MaterialRock("Stone", new PropertyInfo(0x626262, 16, 1.2F, 0.0F, 1.0F, 1.4F, 10000000, -1.0F, 0.38F, 2.1F), SubTag.TOOL_stone, SubTag.TOOL_stone_real);
		CompactStone = new MaterialRock("CompactStone", new PropertyInfo(0x686868, 21, 1.2F, 0.1F, 2.0F, 1.3F, 12800000), SubTag.TOOL_stone, SubTag.TOOL_stone_real);
		Limestone = new MaterialRock("Limestone", new PropertyInfo(0xE4E4E5, 2, 0.8F, 0.2F, 1.0F, 1.8F, 5600000));
		Copper = new MaterialAbstract("Copper", EnumAtoms.Cu.asMatter(), new PropertyInfo(0xDB4E31, 52, 857, 1735, 698, 500, 0.8F, 1600, 2.3F, 8.0F, 1.2F, 0.0F, 48000000, 4F, 1F, 0.19F), SubTag.TOOL_metal, SubTag.CRAFTING_cold_wought);
		MaterialAbstract.pureMaterials.register(Copper, "Cu");
		CuAs = new MaterialAlloy("CuAs", new Matter(Alloy, new AtomStack(Cu, 9), new AtomStack(As)), 
				new PropertyInfo(0xA37C68, 175, 684, 1735, 702, 7086, 0.2F, 1500, 1.6F, 1.7F, 1.1F, 0.2F, 92000000, 102F, 0.97F, 0.21F), 
				new FleAlloy(FleEntry.asMap(new FleEntry(EnumAtoms.Cu, new double[]{0.8D, 0.95D}), new FleEntry(EnumAtoms.As, new double[]{0.05D, 0.2D}))), SubTag.TOOL_metal);
		CuAs2 = new MaterialAlloy("CuAs2", new Matter(Alloy, new AtomStack(Cu, 3), new AtomStack(As)), 
				new PropertyInfo(0x6A6353, 135, 573, 1735, 689, 6894, 0.1F, 1650, 1.4F, 0.5F, 1.1F, 0.4F, 86000000, 100F, 0.96F, 0.22F), 
				new FleAlloy(FleEntry.asMap(new FleEntry(EnumAtoms.Cu, new double[]{0.5D, 0.8D}), new FleEntry(EnumAtoms.As, new double[]{0.1D, 0.5D}))), SubTag.TOOL_metal);
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