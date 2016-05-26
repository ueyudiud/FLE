package farcore.lib.stat;

import net.minecraft.stats.IStatType;
import net.minecraft.stats.StatBase;
import net.minecraft.util.IChatComponent;

public class FleStat extends StatBase
{
	public FleStat(String id, IChatComponent name)
	{
		super(id, name);
	}
	public FleStat(String id, IChatComponent name, IStatType type)
	{
		super(id, name, type);
	}
}
