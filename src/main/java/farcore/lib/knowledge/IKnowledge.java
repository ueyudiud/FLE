package farcore.lib.knowledge;

import farcore.lib.collection.Register;
import farcore.lib.util.IRegisteredNameable;
import net.minecraft.entity.player.EntityPlayer;

public interface IKnowledge extends IRegisteredNameable
{
	Register<KnowledgeAbstract> REGISTER = new Register();

	static KnowledgeAbstract getKnowledge(String name)
	{
		return REGISTER.get(name);
	}
	
	boolean access(EntityPlayer player);
	
	void unlock(EntityPlayer player);

	void reset(EntityPlayer player);
}