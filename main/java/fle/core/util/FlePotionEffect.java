package fle.core.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.util.DamageResources;
import fle.api.util.FleLog;

public class FlePotionEffect
{
	private static int start = Potion.potionTypes.length;
	public static Potion bleeding;
	public static Potion fracture;
	public static Potion burn;
	public static Potion recovery;
	
	public static void init()
	{
		try
		{
			Potion[] ps = Potion.potionTypes;
			Potion[] ret = new Potion[256];
			if(ps.length < 256)
			{
				for(int i = 0; i < ps.length; ++i)
					ret[i] = ps[i];
			}
			else
			{
				ret = ps.clone();
			}
			Util.overrideStaticFinalField(Potion.class, Arrays.asList("potionTypes", "field_76425_a"), ret, false);
		}
		catch(Throwable e)
		{
			FleLog.getLogger().warn("Fle fail to change potion array to 256.");
		}
		bleeding = new PotionFLEBleeding(FleAPI.getNextPotionId(start), "bleeding", 0xC40F0F, new int[]{0, 0});
		fracture = new PotionFLEFracture(FleAPI.getNextPotionId(start), "fracture", 0xFFFDE8, new int[]{1, 0});
		burn = new PotionFLEBurn(FleAPI.getNextPotionId(start), "burn", 0x922700, new int[]{2, 0});
		recovery = new PotionFLERecovery(FleAPI.getNextPotionId(start), "recovery", 0x83FF00, new int[]{3, 0});
	}

	private static class PotionFLE extends Potion
	{
		private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/misc/potions.png");
		
		public PotionFLE(int id, String name, boolean badEffect, int color, int[] iconIndex)
		{
			super(id, badEffect, color);
			setPotionName("potion.fle." + name.toLowerCase());
			setIconIndex(iconIndex[0], iconIndex[1]);
			setEffectiveness(0.25D);
		}
		
		@Override
		public boolean hasStatusIcon()
		{
			return false;
		}
		
		int[] iconPos;
		
		@Override
		protected PotionFLE setIconIndex(int x, int y)
		{
			iconPos = new int[]{x, y};
			return this;
		}
		
		@Override
		public void renderInventoryEffect(int x, int y, PotionEffect effect,
				Minecraft mc)
		{
			if(iconPos != null)
			{
				mc.renderEngine.bindTexture(locate);
				mc.currentScreen.drawTexturedModalRect(x + 6, y + 7, iconPos[0] * 18, iconPos[1] * 18, 18, 18);
			}
		}
		
		@Override
		public int getStatusIconIndex()
		{
			return -1;
		}

		private Map<String, UUID> attributeUUID = new HashMap();
		
		public PotionFLE setAttribute(IAttribute aA, UUID uuid, double value)
		{
			if(!attributeUUID.containsKey(aA.getAttributeUnlocalizedName())) attributeUUID.put(aA.getAttributeUnlocalizedName(), uuid);
			super.func_111184_a(aA, attributeUUID.get(aA.getAttributeUnlocalizedName()).toString(), value, 2);
			return this;
		}
		
		@Override
		public boolean isReady(int tick, int level)
		{
			return false;
		}
	}

	private static class PotionFLEBleeding extends PotionFLE
	{
		public PotionFLEBleeding(int id, String name, int color, int[] iconIndex)
		{
			super(id, name, true, color, iconIndex);
		}
		
		@Override
		public void performEffect(EntityLivingBase entity, int level)
		{
			entity.attackEntityFrom(DamageResources.getBleedingDamageSource(), 1.0F);
		}
		
		@Override
		public boolean isReady(int tick, int level)
		{
			return tick % (96 / (level + 1)) == 0;
		}
	}

	private static class PotionFLERecovery extends PotionFLE
	{
		public PotionFLERecovery(int id, String name, int color, int[] iconIndex)
		{
			super(id, name, false, color, iconIndex);
		}
		
		@Override
		public void performEffect(EntityLivingBase entity, int level)
		{
			entity.heal(1.0F);
			if(entity instanceof EntityPlayer)
			{
				((EntityPlayer) entity).getFoodStats().addExhaustion(0.5F);
			}
		}
		
		@Override
		public boolean isReady(int tick, int level)
		{
			return tick % 600 == 0;
		}
	}

	private static class PotionFLEBurn extends PotionFLE
	{
		public PotionFLEBurn(int id, String name, int color, int[] iconIndex)
		{
			super(id, name, true, color, iconIndex);
		}
		
		@Override
		public void performEffect(EntityLivingBase entity, int level)
		{
			entity.attackEntityFrom(DamageResources.getBurnDamageSource(), 1.0F);
		}
		
		@Override
		public boolean isReady(int tick, int level)
		{
			return tick % (120 / (level + 1)) == 0;
		}
	}
	
	private static class PotionFLEFracture extends PotionFLE
	{
		public PotionFLEFracture(int id, String name, int color, int[] iconIndex)
		{
			super(id, name, true, color, iconIndex);
			setAttribute(SharedMonsterAttributes.movementSpeed, new UUID(184829284141L, 4182453251241L), -0.25D);
		}
	}
}