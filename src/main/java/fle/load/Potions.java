package fle.load;

import static net.minecraft.entity.SharedMonsterAttributes.*;

import java.util.UUID;

import farcore.lib.potion.PotionBase;
import fle.api.potion.PotionBleeding;

public class Potions
{
	public static PotionBase fracture;
	public static PotionBase bleeding;
	
	public static void init()
	{
		fracture = new PotionBase("fracture", true, 16776680).setTextureName("fle:fracture").setAttribute(movementSpeed, new UUID(184829284141L, 4182453251241L), -0.25D);
		bleeding = new PotionBleeding("bleeding", 12848911).setTextureName("fle:bleeding");
	}
}