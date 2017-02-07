package farcore.lib.material;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import nebula.common.LanguageManager;
import nebula.common.base.Judgable;
import nebula.common.base.Register;
import nebula.common.util.ISubTagContainer;
import nebula.common.util.OreDict;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MatCondition implements Judgable<ISubTagContainer>
{
	public static final Register<MatCondition> register = new Register();
	
	public final String name;
	public final String orePrefix;
	public final String localName;
	public final String withOreLocalName;
	
	public int stackLimit = 64;
	
	public long size = 1296L;
	public long latticeSize = 1296L;
	public float specificArea = 1.0F;
	
	public boolean isFluid = false;
	public float fluidViscosityMultiply = 1.0F;
	
	public float maxTemp = 1E16F;
	public float minTemp = 0F;
	
	public Judgable<ISubTagContainer> filter = Judgable.FALSE;
	public Set<Mat> blacklist = new HashSet();
	
	public MatCondition(String prefix, String localName, String withOreLocalName)
	{
		this(prefix, prefix, localName, withOreLocalName);
	}
	public MatCondition(String name, String prefix, String localName, String withOreLocalName)
	{
		this.name = name;
		this.orePrefix = prefix;
		this.localName = localName;
		this.withOreLocalName = withOreLocalName;
		register.register(name, this);
		LanguageManager.registerLocal(getTranslateName(), localName);
		LanguageManager.registerLocal(getWithOreTranslateName(), withOreLocalName);
	}
	
	public MatCondition setFilter(Judgable<ISubTagContainer> filter)
	{
		this.filter = filter;
		return this;
	}
	
	public MatCondition addToBlackList(Mat...mats)
	{
		if(mats.length == 1)
		{
			this.blacklist.add(mats[0]);
		}
		else
		{
			this.blacklist.addAll(Arrays.asList(mats));
		}
		return this;
	}
	
	public MatCondition setStackLimit(int stackLimit)
	{
		this.stackLimit = stackLimit;
		return this;
	}
	
	public MatCondition setSize(long size)
	{
		return setSize(size, size, 1F);
	}
	public MatCondition setSize(long size, long latticSize)
	{
		return setSize(size, latticSize, (float) ((double) size / (double) latticSize));
	}
	public MatCondition setSize(long size, long latticSize, float specificArea)
	{
		this.size = size;
		this.latticeSize = latticSize;
		this.specificArea = specificArea;
		return this;
	}
	
	public MatCondition setUnsizable()
	{
		this.size = -1L;
		this.latticeSize = -1L;
		this.specificArea = -1F;
		return this;
	}
	
	public MatCondition setFluid()
	{
		return setFluid(1.0F);
	}
	public MatCondition setFluid(float viscosityMultiply)
	{
		this.isFluid = true;
		this.fluidViscosityMultiply = viscosityMultiply;
		this.size = 1;
		this.latticeSize = 1L;
		this.specificArea = 1296F;
		return this;
	}
	
	public MatCondition setTemp(float maxTemp, float minTemp)
	{
		this.maxTemp = maxTemp;
		this.minTemp = minTemp;
		return this;
	}
	
	public boolean isBelongTo(Mat material)
	{
		return this.filter.isTrue(material) && !this.blacklist.contains(material);
	}
	
	String getWithOreTranslateName()
	{
		return "matcondition." + this.name + ".a.name";
	}
	
	String getTranslateName()
	{
		return "matcondition." + this.name + ".name";
	}
	
	public void registerOre(Mat material, Item stack)
	{
		OreDict.registerValid(this.orePrefix + material.oreDictName, stack);
	}
	
	public void registerOre(Mat material, Block stack)
	{
		OreDict.registerValid(this.orePrefix + material.oreDictName, stack);
	}
	
	public void registerOre(Mat material, ItemStack stack)
	{
		OreDict.registerValid(this.orePrefix + material.oreDictName, stack);
	}
	
	public void registerOre(String prefix1, Mat material, ItemStack stack)
	{
		OreDict.registerValid(this.orePrefix + prefix1 + material.oreDictName, stack);
	}
	
	public String getOreName(Mat material)
	{
		return this.orePrefix + material.oreDictName;
	}
	
	public String translateToLocal()
	{
		return LanguageManager.translateToLocal(getTranslateName());
	}
	
	public String translateToLocal(Mat material)
	{
		return LanguageManager.translateToLocal(getWithOreTranslateName(), material.getLocalName());
	}
	
	public String getLocal(Mat material)
	{
		return String.format(this.withOreLocalName, material.localName);
	}
	
	public String translateToLocal(String ore)
	{
		return LanguageManager.translateToLocal(getWithOreTranslateName(), ore);
	}
	
	@Override
	public boolean isTrue(ISubTagContainer target)
	{
		return this.filter.isTrue(target) &&
				(!(target instanceof Mat) || isBelongTo((Mat) target));
	}
}