package farcore.lib.knowledge;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class KnowledgeAbstract implements IKnowledge
{
	private final String name;
	protected final int id;

	protected KnowledgeAbstract(String name)
	{
		this.name = name;
		id = REGISTER.register(name, this);
	}

	@Override
	public String getRegisteredName()
	{
		return name;
	}
	
	@Override
	public boolean access(EntityPlayer player)
	{
		NBTTagCompound tag = player.getEntityData();
		if(tag.hasKey("knowledge"))
			return tag.getCompoundTag("knowledge").getBoolean(name);
		return false;
	}
	
	@Override
	public void unlock(EntityPlayer player)
	{
		NBTTagCompound tag = player.getEntityData();
		if(!tag.hasKey("knowledge"))
		{
			tag.setTag("knowledge", tag);
		}
		tag.getCompoundTag("knowledge").setBoolean(name, true);
	}
	
	@Override
	public void reset(EntityPlayer player)
	{
		NBTTagCompound tag = player.getEntityData();
		if(tag.hasKey("knowledge"))
		{
			tag.getCompoundTag("knowledge").setBoolean(name, false);
		}
	}
}