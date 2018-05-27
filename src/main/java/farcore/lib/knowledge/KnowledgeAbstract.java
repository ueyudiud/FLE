/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.knowledge;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class KnowledgeAbstract implements IKnowledge
{
	private final String	name;
	protected final int		id;
	
	protected KnowledgeAbstract(String name)
	{
		this.name = name;
		this.id = REGISTER.register(name, this);
	}
	
	@Override
	public String getRegisteredName()
	{
		return this.name;
	}
	
	@Override
	public boolean access(EntityPlayer player)
	{
		NBTTagCompound tag = player.getEntityData();
		if (tag.hasKey("knowledge")) return tag.getCompoundTag("knowledge").getBoolean(this.name);
		return false;
	}
	
	@Override
	public void unlock(EntityPlayer player)
	{
		NBTTagCompound tag = player.getEntityData();
		if (!tag.hasKey("knowledge"))
		{
			tag.setTag("knowledge", tag);
		}
		tag.getCompoundTag("knowledge").setBoolean(this.name, true);
	}
	
	@Override
	public void reset(EntityPlayer player)
	{
		NBTTagCompound tag = player.getEntityData();
		if (tag.hasKey("knowledge"))
		{
			tag.getCompoundTag("knowledge").setBoolean(this.name, false);
		}
	}
}
