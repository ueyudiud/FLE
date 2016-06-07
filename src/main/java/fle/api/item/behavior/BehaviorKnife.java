package fle.api.item.behavior;

import farcore.enums.EnumDamageResource;
import farcore.util.U;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class BehaviorKnife extends BehaviorDigableTool
{
	public BehaviorKnife()
	{
		this.craftingDamage = 0.125F;
		this.hitEntityDamage = 0.25F;
	}
}