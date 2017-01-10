/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.data;

import farcore.lib.potion.MobEffect;
import farcore.lib.potion.MobEffectBleeding;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;

/**
 * Potion type.
 * @author ueyudiud
 *
 */
public class Potions
{
	public static final Potion JUMP_REDUCE = new MobEffect("jump_reduce", true, 0x2D9199);
	public static final Potion BLEED = new MobEffectBleeding("bleeding", 0xC63429);
	public static final Potion INFECT_ZV = new MobEffectBleeding("infect_zv", 0x3E872D);
	public static final Potion FRACTURE = new MobEffect("fracture", true, 0xF2F2E7).registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "d0d89379-9296-4bb3-9d73-85af744c0317", -0.15F, 2);
	
	public static void init(){}
}