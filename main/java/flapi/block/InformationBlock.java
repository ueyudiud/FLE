package flapi.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import farcore.collection.Register;
import farcore.nbt.ToolInfo.ToolType;
import farcore.util.DropInfo;
import farcore.util.ISubTagContainer;
import farcore.util.SubTag;
import net.minecraft.item.ItemStack;

public class InformationBlock implements ISubTagContainer
{
	public static final InformationBlock DEFAULT = new InformationBlock("null");
	
	final int meta;
	private final String name;
	private float hardness = 1.0F;
	private float resistance = 1.0F;
	private Set<String> effectiveTools;
	private int harvestLevel;
	private String textureName;
	private DropInfo info;
	private final List<SubTag> list = new ArrayList();
	
	public InformationBlock(Register register, String name)
	{
		meta = register.register(this, this.name = name);
	}
	public InformationBlock(String name, int meta)
	{
		this.name = name;
		this.meta = meta;
	}
	public InformationBlock(String name)
	{
		this(name, 0);
	}
	
	public InformationBlock setHardness(float hardness)
	{
		this.hardness = hardness;
		return this;
	}
	
	public InformationBlock setResistance(float resistance)
	{
		this.resistance = resistance;
		return this;
	}
	
	public InformationBlock setHarvestLevel(int harvestLevel)
	{
		this.harvestLevel = harvestLevel;
		return this;
	}
	
	public InformationBlock setTextureName(String textureName)
	{
		this.textureName = textureName;
		return this;
	}
	
	public InformationBlock setEffectiveTools(String...tools)
	{
		effectiveTools = ImmutableSet.copyOf(tools);
		return this;
	}
	
	public InformationBlock setEffectiveTools(ToolType...tools)
	{
		Builder builder = ImmutableSet.builder();
		for(ToolType type : tools)
			builder.add(type.name);
		effectiveTools = builder.build();
		return this;
	}
	
	public float getHardness()
	{
		return hardness;
	}
	
	public float getResistance()
	{
		return resistance;
	}
	
	public int getHarvestLevel()
	{
		return harvestLevel;
	}

	public String getIconName()
	{
		return textureName;
	}
	
	public Set<String> getEffectiveTools()
	{
		return effectiveTools;
	}
	
	public List<ItemStack> getDrops(int fortune)
	{
		return info == null ? new ArrayList() : info.drops();
	}
	
	public DropInfo getDropInfo()
	{
		return info == null ? DropInfo.NULL : info;
	}
	
	@Override
	public boolean contain(SubTag tag)
	{
		return list.contains(tag);
	}
	
	@Override
	public void add(SubTag... tag)
	{
		list.addAll(Arrays.asList(tag));
	}
	
	@Override
	public void remove(SubTag tag)
	{
		list.remove(tag);
	}
}