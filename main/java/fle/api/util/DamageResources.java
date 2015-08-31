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
	public static DamageSource getHeatDamageSource()
	{
		return new FleHeatDamageSource();
	}
	public static DamageSource getBleedingDamageSource()
	{
		return new FleBleedingDamageSource();
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
	
	public static class FleHeatDamageSource extends DamageSource
	{
	    public FleHeatDamageSource()
	    {
			super("fle.heat");
		}

		public IChatComponent func_151519_b(EntityLivingBase aTarget)
	    {
	    	return new ChatComponentText(EnumChatFormatting.RED + aTarget.getCommandSenderName() + 
	    			EnumChatFormatting.WHITE + " felt hot!");
	    }
	}
	
	public static class FleBleedingDamageSource extends DamageSource
	{
	    public FleBleedingDamageSource()
	    {
			super("fle.bleed");
		}

		public IChatComponent func_151519_b(EntityLivingBase aTarget)
	    {
	    	return new ChatComponentText(EnumChatFormatting.RED + aTarget.getCommandSenderName() + 
	    			EnumChatFormatting.WHITE + " lost lots of blood.");
	    }
	}
}