package flapi.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.Fluid;
import farcore.block.fluid.BlockFluidBase;

public class DamageResources
{
	public static DamageSource getArrowDamageSource(Entity entity)
	{
		return new FleArrowAttackDamageSource(entity);
	}
	public static DamageSource getHeatDamageSource()
	{
		return new FleHeatDamageSource();
	}
	public static DamageSource getBleedingDamageSource()
	{
		return new FleBleedingDamageSource();
	}
	public static DamageSource getBurnDamageSource()
	{
		return new FleBurnDamageSource();
	}
	public static DamageSource getFluidHeatDamage(BlockFluidBase fluid)
	{
		return new FleFluidBurnDamageSource(fluid.getFluid());
	}
	
	public static class FleArrowAttackDamageSource extends EntityDamageSource
	{
		public FleArrowAttackDamageSource(Entity aEntity)
		{
			super("fle.arrow", aEntity);
		}

		@Override
	    public IChatComponent getDeathMessage(EntityLivingBase target)
	    {
	    	return new ChatComponentText(EnumChatFormatting.RED + target.getCommandSenderEntity().getName() + 
	    			EnumChatFormatting.WHITE + " try to pickup an arrow didn't on the ground.");
	    }
	}
	
	public static class FleHeatDamageSource extends DamageSource
	{
	    public FleHeatDamageSource()
	    {
			super("fle.heat");
		}

	    @Override
	    public IChatComponent getDeathMessage(EntityLivingBase target)
	    {
	    	return new ChatComponentText(EnumChatFormatting.RED + target.getCommandSenderEntity().getName() + 
	    			EnumChatFormatting.WHITE + " felt hot!");
	    }
	}
	
	public static class FleBleedingDamageSource extends DamageSource
	{
	    public FleBleedingDamageSource()
	    {
			super("fle.bleed");
		}

	    @Override
	    public IChatComponent getDeathMessage(EntityLivingBase target)
	    {
	    	return new ChatComponentText(EnumChatFormatting.RED + target.getCommandSenderEntity().getName() + 
	    			EnumChatFormatting.WHITE + " lost lots of blood.");
	    }
	}
	
	public static class FleBurnDamageSource extends DamageSource
	{
	    public FleBurnDamageSource()
	    {
			super("fle.burn");
		}

	    @Override
	    public IChatComponent getDeathMessage(EntityLivingBase target)
	    {
	    	return new ChatComponentText(EnumChatFormatting.RED + target.getCommandSenderEntity().getName() + 
	    			EnumChatFormatting.WHITE + " carbonized.");
	    }
	}
	
	public static class FleFluidBurnDamageSource extends DamageSource
	{
		private Fluid fluid;
		
		public FleFluidBurnDamageSource(Fluid fluid)
		{
			super("fle.fluid.burn");
			this.fluid = fluid;
		}

		@Override
	    public IChatComponent getDeathMessage(EntityLivingBase target)
	    {
			return new ChatComponentText(EnumChatFormatting.RED + target.getCommandSenderEntity().getName() + 
					EnumChatFormatting.WHITE + " try to swim in the " + fluid.getName());
	    }
	}
}