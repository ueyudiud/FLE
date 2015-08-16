package fle.api.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class DamageResources
{
	public static DamageSource getArrowDamageSource(Entity aEntity)
	{
		return new FleArrowAttackDamageSource(aEntity);
	}
	
	public static class FleArrowAttackDamageSource extends EntityDamageSource
	{
		public FleArrowAttackDamageSource(Entity aEntity)
		{
			super("fle.arrow", aEntity);
		}
		
	    public IChatComponent func_151519_b(EntityLivingBase aTarget)
	    {
	    	return new ChatComponentText(EnumChatFormatting.RED + aTarget.getCommandSenderName() + 
	    			EnumChatFormatting.WHITE + " try to pickup an arrow didn't on the ground.");
	    }
	}
}
