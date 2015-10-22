package fle.core.tech;

import net.minecraft.util.IIcon;
import fle.api.tech.ITechTag;
import fle.api.tech.PlayerTechInfo;

public abstract class FlaTechTag extends ITechTag
{
	public FlaTechTag(String name)
	{
		super(name);
	}
	
	@Override
	public boolean isTechEnable(PlayerTechInfo aInfo)
	{
		return true;
	}

	@Override
	public abstract IIcon getIconForTag();
}