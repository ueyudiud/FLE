package farcore.lib.material;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import farcore.lib.block.instance.BlockCoreLeaves;
import farcore.lib.block.instance.BlockLeaves;
import farcore.lib.block.instance.BlockLogArtificial;
import farcore.lib.block.instance.BlockLogNatural;
import farcore.lib.collection.Register;
import farcore.lib.crop.ICrop;
import farcore.lib.tree.ITree;
import farcore.lib.util.IRegisteredNameable;
import farcore.lib.util.ISubTagContainer;
import farcore.lib.util.SubTag;
import farcore.util.U;
import farcore.util.U.OreDict;
import net.minecraft.block.Block;
import net.minecraftforge.oredict.OreDictionary;

public class Mat implements ISubTagContainer, IRegisteredNameable
{
	public static final Register<Mat> register = new Register(32768);
	
	public final String modid;
	public final String name;
	public final String oreDictName;
	public final String localName;
	public final int id;
	public short[] RGBa = {255, 255, 255, 255};
	public int RGB = 0xFFFFFF;
	public float heatCap;
	public float thermalConduct;
	public float maxSpeed;
	public float maxTorque;
	public float dielectricConstant;
	public float electrialResistance;
	public float redstoneResistance;
	//Block configuration.
	public boolean isRock = false;
	public boolean isSand = false;
	public boolean isDirt = false;
	public String blockHarvestTool;
	public int blockHarvestLevel;
	public float blockExplosionResistance;
	public float blockHardness;
	//Wood matter configuration.
	public boolean isWood = false;
	public float woodHardness;//Useless prop
	public float ashcontent;
	public float woodBurnHeat;
	public boolean hasTree = false;
	public ITree tree;
	/**
	 * This two block is for tree generation.
	 */
	public Block log;
	/**
	 * The log which is already cut off from tree.
	 */
	public Block logArt;
	public Block leaves;
	//Crop configuration.
	public boolean isCrop = false;
	public ICrop crop = ICrop.VOID;
	
	private Set<SubTag> subTags = new HashSet();

	public Mat(int id, String name, String oreDict, String localized)
	{
		this(id, U.Mod.getActiveModID(), name, oreDict, localized);
	}
	public Mat(int id, String modid, String name, String oreDict, String localized)
	{
		this(id, true, modid, name, oreDict, localized);
	}
	public Mat(int id, boolean register, String modid, String name, String oreDict, String localized)
	{
		this.id = id;
		this.modid = modid;
		this.name = name;
		this.oreDictName = oreDict;
		this.localName = localized;
		if(register)
		{
			Mat.register.register(id, name, this);
		}
	}
	
	@Override
	public final String getRegisteredName()
	{
		return name;
	}
	
	public Mat setRGBa(int colorIndex)
	{
		this.RGBa[0] = (short) ((colorIndex >> 24)       );
		this.RGBa[1] = (short) ((colorIndex >> 16) & 0xFF);
		this.RGBa[2] = (short) ((colorIndex >> 8 ) & 0xFF);
		this.RGBa[3] = (short) ((colorIndex      ) & 0xFF);
		this.RGB = colorIndex >> 8;
		return this;
	}
	
	public Mat setRGBa(short[] colorIndex)
	{
		this.RGBa = colorIndex;
		this.RGB = colorIndex[0] << 16 | colorIndex[1] << 8 | colorIndex[2];
		return this;
	}
	
	public Mat setGeneralProp(float heatCap, float thermalConduct, float maxSpeed, float maxTorque, float dielectricConstant, float electrialResistance, float redstoneResistance)
	{
		this.heatCap = heatCap;
		this.thermalConduct = thermalConduct;
		this.dielectricConstant = dielectricConstant;
		this.maxSpeed = maxSpeed;
		this.maxTorque = maxTorque;
		this.electrialResistance = electrialResistance;
		this.redstoneResistance = redstoneResistance;
		return this;
	}

	public Mat setWood(float woodHardness, float ashcontent, float woodBurnHeat)
	{
		this.isWood = true;
		this.blockHardness = 1.5F + woodHardness / 4F;
		this.blockExplosionResistance = 0.4F + woodHardness / 8F;
		this.blockHarvestLevel = 1;
		this.blockHarvestTool = "axe";
		this.woodHardness = woodHardness;
		this.ashcontent = ashcontent;
		this.woodBurnHeat = woodBurnHeat;
		add(SubTag.WOOD);
		return this;
	}
	
	public Mat setTree(ITree tree)
	{
		this.hasTree = true;
		this.tree = tree;
		BlockLogNatural logNatural = new BlockLogNatural(this);
		BlockLogArtificial logArtificial = new BlockLogArtificial(this);
		BlockLeaves leaves = new BlockLeaves(this);
		BlockCoreLeaves coreLeaves = new BlockCoreLeaves(this, leaves);
		OreDict.registerValid("logWood", logArtificial);
		OreDict.registerValid("leaves", leaves);
		tree.initInfo(logNatural, logArtificial, leaves, coreLeaves);
		return this;
	}
	
	public Mat setCrop(ICrop crop)
	{
		this.isCrop = true;
		this.crop = crop;
		add(SubTag.CROP);
		return this;
	}
	
	public Mat setTag(SubTag...tags)
	{
		add(tags);
		return this;
	}
	
	@Override
	public void add(SubTag... tags)
	{
		subTags.addAll(Arrays.asList(tags));
	}
	
	@Override
	public boolean contain(SubTag tag)
	{
		return subTags.contains(tag);
	}
}