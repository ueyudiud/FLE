package farcore.lib.potion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.util.FleLog;
import farcore.util.U;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;

public class PotionBase extends Potion
{
	private static boolean inited = false;
	
	public static int getNextPotionId(int start)
	{
		init();
		if (Potion.potionTypes != null)
		{
			while ((start <= 0) || (start >= Potion.potionTypes.length) || (Potion.potionTypes[start] != null))
			{
				start++;
				if (start >= 256)
				{
					return -1;
				}
			}
			return start;
		}
		return -1;
	}
	
	private static void init()
	{
		if(inited)
		{
			return;
		}
		try
		{
			Potion[] ps = Potion.potionTypes;
			Potion[] ret = new Potion[256];
			if (ps.length < 256)
			{
				for (int i = 0; i < ps.length; i++)
				{
					ret[i] = ps[i];
				}
			}
			else
			{
				ret = (Potion[])ps.clone();
			}
			U.Reflect.overrideFinalField(Potion.class, Arrays.asList("potionTypes", "field_76425_a"), ret, false);
		}
	    catch (Throwable e)
	    {
	    	FleLog.getLogger().warn("Fle fail to change potion array to 256.");
	    }
		inited = true;
	}
	
	public static final List<PotionBase> list = new ArrayList();
	
	protected String textureName;
	@SideOnly(Side.CLIENT)
	private IIcon icon;
	private Map<String, UUID> attributeUUID = new HashMap();
	
	public PotionBase(String name, boolean isBadPotionEffect, int color)
	{
		this(getNextPotionId(32), name, isBadPotionEffect, color);
	}
	public PotionBase(int id, String name, boolean isBadPotionEffect, int color)
	{
		super(id, isBadPotionEffect, color);
		setPotionName("potion." + name);
		setEffectiveness(0.25D);
		list.add(this);
	}
	
	public PotionBase setTextureName(String name)
	{
		textureName = name;
		return this;
	}
	
	public PotionBase setAttribute(IAttribute attribute, UUID uuid, double value)
	{
		if (!this.attributeUUID.containsKey(attribute.getAttributeUnlocalizedName()))
		{
			this.attributeUUID.put(attribute.getAttributeUnlocalizedName(), uuid);
		}
		super.func_111184_a(attribute, ((UUID)this.attributeUUID.get(attribute.getAttributeUnlocalizedName())).toString(), value, 2);
		return this;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcon(IIconRegister register)
	{
		icon = register.registerIcon(getTextureName());
	}

	public String getTextureName()
	{
		return textureName;
	}

	@SideOnly(Side.CLIENT)
	public boolean hasStatusIcon()
	{
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
	{
		if (icon != null)
		{
			mc.renderEngine.bindTexture(FarCore.potionTextureMap.location);
			mc.currentScreen.drawTexturedModelRectFromIcon(x + 6 + 1, y + 7 + 1, icon, 16, 16);
		}
	}
	
	@Override
	public boolean isReady(int tick, int level)
	{
		return false;
	}
}