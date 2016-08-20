package farcore.lib.knowledge;

import farcore.lib.collection.Register;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class KnowledgeAbstract implements IKnowledge
{
	protected static final Register<KnowledgeAbstract> REGISTER = new Register();

	public static KnowledgeAbstract getKnowledge(String name)
	{
		return REGISTER.get(name);
	}

	private final String name;
	private final int id;
	
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