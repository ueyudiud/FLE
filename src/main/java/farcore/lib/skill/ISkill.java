package farcore.lib.skill;

import farcore.lib.util.IRegisteredNameable;
import net.minecraft.entity.player.EntityPlayer;

public interface ISkill extends IRegisteredNameable
{
	int level(EntityPlayer player);
	
	void using(EntityPlayer player, float exp);

	void set(EntityPlayer player, int level);
}