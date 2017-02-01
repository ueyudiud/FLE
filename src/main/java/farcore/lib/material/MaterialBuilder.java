/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.material;

import farcore.data.MP;
import farcore.data.SubTags;
import farcore.lib.material.prop.PropertyBasic;
import nebula.common.util.Game;

/**
 * @author ueyudiud
 */
public class MaterialBuilder
{
	final String modid;
	final String name;
	final String oreDictName;
	final String localName;
	final short id;
	final boolean register;
	final PropertyBasic property = new PropertyBasic();
	boolean isTool = false;
	int toolMaxUse = 1;
	int toolHarvestLevel;
	float toolHardness = 1.0F;
	float toolBrittleness;
	float toolDamageToEntity;
	float toolAttackSpeedMutiple;
	String chemicalFormula;
	short[] RGBa = {255, 255, 255, 255};
	
	public MaterialBuilder(int id, String name, String oreDict, String localized)
	{
		this(id, Game.getActiveModID(), name, oreDict, localized);
	}
	public MaterialBuilder(int id, String modid, String name, String oreDict, String localized)
	{
		this(id, true, modid, name, oreDict, localized);
	}
	public MaterialBuilder(int id, boolean register, String modid, String name, String oreDict, String localized)
	{
		if(Mat.material(id) != null)
		{
			throw new RuntimeException("The material '" + name + "' id " + id + " has already be registered by " + Mat.material(id).name + ".");
		}
		this.id = (short) id;
		this.register = register;
		this.modid = modid;
		this.name = name;
		this.oreDictName = oreDict;
		this.localName = localized;
	}
	
	public MaterialBuilder setChemicalFormula(String chemicalFormula)
	{
		this.chemicalFormula = chemicalFormula;
		return this;
	}
	
	public MaterialBuilder setRGBa(int colorIndex)
	{
		this.RGBa[0] = (short) ((colorIndex >> 24)       );
		this.RGBa[1] = (short) ((colorIndex >> 16) & 0xFF);
		this.RGBa[2] = (short) ((colorIndex >> 8 ) & 0xFF);
		this.RGBa[3] = (short) ((colorIndex      ) & 0xFF);
		return this;
	}
	
	public MaterialBuilder setRGBa(short[] colorIndex)
	{
		this.RGBa = colorIndex;
		return this;
	}
	
	public MaterialBuilder setGeneralProp(float heatCap, float thermalConduct, float maxSpeed, float maxTorque, float dielectricConstant, float electrialResistance, float redstoneResistance)
	{
		this.property.heatCap = heatCap;
		this.property.thermalConduct = thermalConduct;
		this.property.dielectricConstant = dielectricConstant;
		this.property.maxSpeed = maxSpeed;
		this.property.maxTorque = maxTorque;
		this.property.electrialResistance = electrialResistance;
		this.property.redstoneResistance = redstoneResistance;
		return this;
	}
	
	public MaterialBuilder setToolProp(int maxUses, int harvestLevel, float hardness, float brittleness, float damageToEntity, float attackSpeedMutiple)
	{
		this.isTool = true;
		this.toolMaxUse = maxUses;
		this.toolHarvestLevel = harvestLevel;
		this.toolHardness = hardness;
		this.toolBrittleness = brittleness;
		this.toolDamageToEntity = damageToEntity;
		this.toolAttackSpeedMutiple = attackSpeedMutiple;
		return this;
	}
	
	public Mat build()
	{
		Mat material = new Mat(this.id, this.register, this.modid, this.name, this.oreDictName, this.localName);
		material.RGBa = this.RGBa;
		material.RGB = this.RGBa[0] << 16 | this.RGBa[1] << 8 | this.RGBa[2];
		material.chemicalFormula = this.chemicalFormula;
		material.thermalConductivity = this.property.thermalConduct;
		material.heatCapacity = this.property.heatCap;
		material.dielectricConstant = this.property.dielectricConstant;
		material.electrialResistance = this.property.electrialResistance;
		material.maxSpeed = this.property.maxSpeed;
		material.maxTorque = this.property.maxTorque;
		if(this.isTool)
		{
			material.add(SubTags.TOOL);
			material.toolMaxUse = this.toolMaxUse;
			material.toolHarvestLevel = this.toolHarvestLevel;
			material.toolHardness = this.toolHardness;
			material.toolDamageToEntity = this.toolDamageToEntity;
			material.toolBrittleness = this.toolBrittleness;
			material.addProperty(MP.tool_attackspeed, this.toolAttackSpeedMutiple);
		}
		return material;
	}
}