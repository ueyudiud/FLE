package farcore.lib.potion;

import farcore.util.U.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MobEffect extends Potion
{
	public MobEffect(String name, boolean isBadEffectIn, int liquidColorIn)
	{
		super(isBadEffectIn, liquidColorIn);
		setPotionName(name);
		setRegistryName(Mod.getActiveModID(), name);
		GameRegistry.register(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldRenderInvText(PotionEffect effect)
	{
		return super.shouldRenderInvText(effect);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldRenderHUD(PotionEffect effect)
	{
		return super.shouldRenderHUD(effect);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
	{
		super.renderInventoryEffect(x, y, effect, mc);
	}
}