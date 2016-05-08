package farcore.lib.substance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.interfaces.ISubTagContainer;
import farcore.lib.collection.Register;
import farcore.util.IDataChecker;
import farcore.util.SubTag;

public class SubstanceTool implements ISubstance, ISubTagContainer
{
	private static final Register<SubstanceTool> register = new Register();

	public static final SubstanceTool VOID_TOOL = new SubstanceTool(0, "void");
	
	private static Map<IDataChecker<ISubTagContainer>, List<SubstanceTool>> subToolList = new HashMap();
	
	public static List<SubstanceTool> getSubstances(IDataChecker<ISubTagContainer> tag)
	{
		if(subToolList.containsKey(tag))
		{
			return subToolList.get(tag);
		}
		Builder<SubstanceTool> builder = ImmutableList.builder();
		for(SubstanceTool substance : register)
		{
			if(tag.isTrue(substance))
			{
				builder.add(substance);
			}
		}
		List<SubstanceTool> list;
		subToolList.put(tag, list = builder.build());
		return list;
	}
	
	public static SubstanceTool getSubstance(String tag)
	{
		return register.get(tag, VOID_TOOL);
	}
	
	public static SubstanceTool getSubstance(int id)
	{
		return register.get(id, VOID_TOOL);
	}
	
	public static Register<SubstanceTool> getTools()
	{
		return register;
	}
	
	private List<SubTag> tags = new ArrayList();
	protected final String name;

	public int maxUses;
	public int harvestLevel;
	public float digSpeed;
	public float brittleness;
	/**
	 * The color of tool material, render in items.
	 */
	public int color = 0xFFFFFF;

	public SubstanceTool(int id, String name, String local)
	{
		this(id, name);
		registerLocalName(local);
	}
	public SubstanceTool(int id, String name)
	{
		this.name = name;
		register.register(id, name, this);
	}
	public SubstanceTool(String name)
	{
		this.name = name;
		register.register(name, this);
	}
	
	public SubstanceTool setMaxUses(int maxUses)
	{
		this.maxUses = maxUses;
		return this;
	}
	
	public SubstanceTool setHarvestLevel(int harvestLevel)
	{
		this.harvestLevel = harvestLevel;
		return this;
	}
	
	public SubstanceTool setDigSpeed(float digSpeed)
	{
		this.digSpeed = digSpeed;
		return this;
	}
	
	public SubstanceTool setBrittleness(float brittleness)
	{
		this.brittleness = brittleness;
		return this;
	}
	
	public SubstanceTool setColor(int color)
	{
		this.color = color;
		return this;
	}

	public SubstanceTool setTag(SubTag... tags)
	{
		add(tags);
		return this;
	}
	
	//Override method
	public final String getName(){return name;}
	public final int getID(){return register.id(this);}
	public final Register<SubstanceTool> getRegister(){return register;}
	public final String getType(){return "tool";}

	@Override
	public void add(SubTag... tags)
	{
		this.tags.addAll(Arrays.asList(tags));
	}

	@Override
	public boolean contain(SubTag tag)
	{
		return tags.contains(tag);
	}
}