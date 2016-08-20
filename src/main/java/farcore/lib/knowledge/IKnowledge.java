package farcore.lib.knowledge;

import farcore.lib.util.IRegisteredNameable;
import net.minecraft.entity.player.EntityPlayer;

public interface IKnowledge extends IRegisteredNameable
{
	boolean access(EntityPlayer player);
	
	void unlock(EntityPlayer player);

	void reset(EntityPlayer player);
}