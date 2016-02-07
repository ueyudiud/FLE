package farcore.util;

import net.minecraft.entity.player.EntityPlayer;

public interface IKeyBoard
{
	boolean isKeyDown(EntityPlayer player, IKey key);
}