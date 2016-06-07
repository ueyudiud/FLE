package fle.api.damage;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class DamageSources
{
	public static DamageSource bleeding;
	
	public static DamageSource getBleedingSource()
	{
		if(bleeding == null)
		{
			bleeding = new DamageSourceBleeding();
		}
		return bleeding;
	}
	
	public static class DamageSourceBleeding extends DamageSource
	{
		public DamageSourceBleeding()
		{
			super("bleeding");
		}

		public IChatComponent func_151519_b(EntityLivingBase target)
		{
			return new ChatComponentText(EnumChatFormatting.RED + target.getCommandSenderName() + EnumChatFormatting.WHITE + " lost lots of blood.");
		}
	}
}