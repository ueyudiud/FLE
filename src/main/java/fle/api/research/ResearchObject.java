/*
 * copyright 2016-2018 ueyudiud
 */
package fle.api.research;

import java.util.List;

import farcore.lib.knowledge.IKnowledge;
import nebula.common.LanguageManager;
import nebula.common.util.IRegisteredNameable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class ResearchObject implements IRegisteredNameable
{
	protected final String				name;
	protected final ResearchCategory	category;
	
	protected List<ResearchTheorem>	theoremRequired;
	protected IKnowledge			knowledge;
	
	public ResearchObject(ResearchCategory category, String name)
	{
		this.category = category;
		this.name = name;
	}
	
	public ResearchCategory getCategory()
	{
		return this.category;
	}
	
	public IKnowledge getKnowledge()
	{
		return this.knowledge;
	}
	
	public String getTranslationName()
	{
		return "research.object." + this.name + ".name";
	}
	
	public String getLocalizedName()
	{
		return LanguageManager.translateLocal(getTranslationName());
	}
	
	@SideOnly(Side.CLIENT)
	public void renderIcon(int x, int y, int u, int v)
	{
		
	}
	
	@Override
	public String getRegisteredName()
	{
		return this.name;
	}
}
