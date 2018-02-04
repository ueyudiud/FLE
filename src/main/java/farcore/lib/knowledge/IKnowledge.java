/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.knowledge;

import nebula.base.register.Register;
import nebula.common.util.IRegisteredNameable;
import net.minecraft.entity.player.EntityPlayer;

public interface IKnowledge extends IRegisteredNameable
{
	Register<KnowledgeAbstract> REGISTER = new Register<>();
	
	static KnowledgeAbstract getKnowledge(String name)
	{
		return REGISTER.get(name);
	}
	
	boolean access(EntityPlayer player);
	
	void unlock(EntityPlayer player);
	
	void reset(EntityPlayer player);
}
