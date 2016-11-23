package farcore.lib.skill;

import farcore.lib.collection.Register;
import farcore.lib.util.IRegisteredNameable;
import net.minecraft.entity.player.EntityPlayer;

public interface ISkill extends IRegisteredNameable
{
	Register<SkillAbstract> REGISTER = new Register();

	static SkillAbstract getSkill(String name)
	{
		return REGISTER.get(name);
	}

	int level(EntityPlayer player);

	void using(EntityPlayer player, float exp);
	
	void set(EntityPlayer player, int level);
}