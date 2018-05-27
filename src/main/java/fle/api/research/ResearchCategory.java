/*
 * copyright 2016-2018 ueyudiud
 */
package fle.api.research;

import java.util.List;

import nebula.base.Cache;
import nebula.base.collection.HashIntMap;
import nebula.base.register.Register;
import nebula.common.LanguageManager;
import nebula.common.util.IRegisteredNameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class ResearchCategory implements IRegisteredNameable
{
	private static final Register<ResearchCategory> RESEARCHS = new Register<>();
	
	protected List<ResearchCategory>		requiredBefore;
	protected HashIntMap<ResearchObject>	objects;
	protected int							minAvailableWeight	= 1;
	private final String					name;
	
	public ResearchCategory(String name)
	{
		this.name = name;
		RESEARCHS.register(name, this);
	}
	
	public boolean isAvailable(EntityPlayer player)
	{
		Cache<Integer> cache = new Cache<>(0);
		this.objects.forEach((ro, w) -> {
			if (ro.getKnowledge().access(player)) cache.set(cache.get() + w);
		});
		return cache.get() >= this.minAvailableWeight;
	}
	
	public boolean isUsable(EntityPlayer player)
	{
		return this.requiredBefore.stream().allMatch(rc -> rc.isAvailable(player));
	}
	
	public String getTranslationName()
	{
		return "research.category." + this.name + ".name";
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
