package farcore.alpha.mat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.interfaces.ISubTagContainer;
import farcore.interfaces.ITreeGenerator;
import farcore.lib.collection.Register;
import farcore.lib.crop.CropCard;
import farcore.lib.recipe.DropHandler;
import farcore.lib.world.gen.tree.TreeGenEmpty;
import farcore.util.SubTag;
import farcore.util.V;
import farcore.util.Values;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class Mat implements ISubTagContainer
{
	private static final TreeGenEmpty VOID_GEN = new TreeGenEmpty();
	
	public static final Register<Mat> materialRegister = new Register();
	
	public final int id;
	public final String name;
	public final String oreDictName;
	//General configuration.
	public float molMass;
	public boolean hasSolidType;
	public int maxStableTemp1;
	public int maxStableTemp2;
	public boolean canMelt;
	public Fluid liquid;
	public boolean canBoil;
	public Fluid gas;
	public float heatCap;
	public float thermalConduct;
	public float dielectricConstant;
	public float electrialResistance;
	public float redstoneResistance;
	//Wood matter configuration.
	public boolean isWood = false;
	public float woodHardness;//Useless prop
	public float ashcontent;
	public float woodBurnHeat;
	public boolean hasTree = false;
	public ITreeGenerator treeGenerator = VOID_GEN;
	/**
	 * This two block is for tree generation.
	 */
	public Block log;
	/**
	 * The log which is already cut off from tree.
	 */
	public Block logArt;
	public Block leaves;
	public DropHandler leafDrop = DropHandler.EMPTY;
	//Block configuration.
	public boolean isRock = false;
	public boolean isSand = false;
	public boolean isDirt = false;
	public String blockHarvestTool;
	public int blockHarvestLevel;
	public float blockExplosionResistance;
	public float blockHardness;
	//Dirt extra configuration.
	//Rock extra configuration.
	public float minDetHeatForExplosion;
	//Tool configuration.
	public boolean canMakeTool = false;
	public int toolMaxUse = 1;
	public float toolHardness = 1.0F;
	public float toolDamageToEntity;
	public int toolEnchantability;
	//Handle configuration.
	public boolean canMakeHandle = false;
	public float handleUseMul = 1.0F;
	//Crop configuration.
	public boolean isCrop = false;
	public CropCard cropCard = CropCard.VOID;
	//Liquid configuration.
	public int liquidLuminosity;
	public int liquidDensity;
	public int liquidViscosity;
	//Gas configuration.
	public int gasLuminosity;
	public int gasDensity;
	public int gasViscosity;
	
	private final List<SubTag> tagList = new ArrayList();
	
	protected Mat(int id, String name, String oreDictName, String localName)
	{
		materialRegister.register(id, name, this);
		this.id = id;
		this.name = name;
		this.oreDictName = oreDictName;
		FarCoreSetup.lang.registerLocal(getTranslatedName(), localName);
	}
	
	protected String getTranslatedName()
	{
		return "mat." + name + ".name";
	}
	
	public String getLocalName()
	{
		return FarCore.translateToLocal(getTranslatedName());
	}
	
	public Mat setGeneralProp(float molMass, float heatCap, float thermalConduct, float dielectricConstant, float electrialResistance, float redstoneResistance)
	{
		this.molMass = molMass;
		this.heatCap = heatCap;
		this.thermalConduct = thermalConduct;
		this.dielectricConstant = dielectricConstant;
		this.electrialResistance = electrialResistance;
		this.redstoneResistance = redstoneResistance;
		return this;
	}
	
	public Mat setFluid(int meltTemp, int boilTemp, int luminosity, int density, int viscosity)
	{
		setLiquid(meltTemp, luminosity, density, viscosity);
		setGas(boilTemp, luminosity + 1, density / 100 - 20, viscosity / 100);
		return this;
	}
	
	public Mat setLiquid(int meltTemp, int luminosity, int density, int viscosity)
	{
		return setLiquid(new Fluid("liquid." + name), meltTemp, luminosity, density, viscosity);
	}	
	public Mat setLiquid(Fluid liquid, int meltTemp, int luminosity, int density, int viscosity)
	{
		if(!FluidRegistry.isFluidRegistered(liquid))
		{
			FluidRegistry.registerFluid(liquid);
		}
		this.canMelt = true;
		this.liquid = liquid
				.setTemperature(Math.max(Values.ROOM_Point_INT, this.maxStableTemp1 = meltTemp))
				.setLuminosity(this.liquidLuminosity = luminosity)
				.setDensity(this.liquidDensity = density)
				.setViscosity(this.liquidViscosity = viscosity);
		return this;
	}

	public Mat setGas(int boilTemp, int luminosity, int density, int viscosity)
	{
		return setGas(new Fluid("gas." + name), boilTemp, luminosity, density, viscosity);
	}	
	public Mat setGas(Fluid gas, int boilTemp, int luminosity, int density, int viscosity)
	{
		if(!FluidRegistry.isFluidRegistered(gas))
		{
			FluidRegistry.registerFluid(gas);
		}
		this.canBoil = true;
		this.gas = gas
				.setTemperature(Math.max(Values.ROOM_Point_INT, this.maxStableTemp2 = boilTemp))
				.setLuminosity(this.gasLuminosity = luminosity)
				.setDensity(this.gasDensity = density)
				.setViscosity(this.gasViscosity = viscosity)
				.setGaseous(true);
		return this;
	}

	public Mat setWood(float woodHardness, float ashcontent, float woodBurnHeat)
	{
		this.blockHardness = 1.5F + woodHardness / 4F;
		this.blockExplosionResistance = 0.4F + woodHardness / 8F;
		this.blockHarvestLevel = 1;
		this.blockHarvestTool = "axe";
		this.woodHardness = woodHardness;
		this.ashcontent = ashcontent;
		this.woodBurnHeat = woodBurnHeat;
		return this;
	}
	
	
	
	public Mat addProp(SubTag...tags)
	{
		add(tags);
		return this;
	}
	
	@Override
	public void add(SubTag... tags)
	{
		if(tags.length == 1)
		{
			tagList.add(tags[0]);
		}
		else
		{
			tagList.addAll(Arrays.asList(tags));
		}
	}

	@Override
	public boolean contain(SubTag tag)
	{
		return tagList.contains(tag);
	}
}