/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.util;

import java.util.UUID;

import javax.annotation.Nullable;

import farcore.data.Config;
import farcore.data.EnumNutrition;
import farcore.data.V;
import farcore.energy.thermal.ThermalNet;
import farcore.event.FoodEatenEvent;
import farcore.lib.item.behavior.IFoodStat;
import farcore.util.U.NBTs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * For more usable food stat.
 * @author ueyudiud
 */
public class FoodStatExt extends FoodStats
{
	private static final AttributeModifier HUNGER_WEAKNESS_I = new AttributeModifier(UUID.fromString("150FC59A-A22B-4F8C-90A5-B76000AC4BD7"), "foodstat.effect1", -0.2F, 2);
	private static final AttributeModifier HUNGER_WEAKNESS_II = new AttributeModifier(UUID.fromString("F14348DA-C3B1-4D76-AEC7-E05BF6A672AA"), "foodstat.effect2", -0.3F, 2);
	private static final AttributeModifier THIRSTY_WEAKNESS_I = new AttributeModifier(UUID.fromString("2A2D2C19-F00F-40F4-869D-F07A713E6533"), "waterstat.effect1", -0.2F, 2);
	private static final AttributeModifier THIRSTY_WEAKNESS_II = new AttributeModifier(UUID.fromString("F6122B44-3B2B-4BB9-9294-B566E39D6AF9"), "waterstat.effect2", -0.2F, 2);
	
	private static final int MAX_FOOD_LEVEL = 20;
	private static final int START_SATURATION_LEVAL = 5;
	private static final int MAX_NUTRITION_LEVEL = 100;
	private static final int MAX_WATER_LEVEL = 20;
	
	private int prevDim;
	private long worldTimer = Long.MIN_VALUE;

	/** The player's food level. */
	private float foodLevel = MAX_FOOD_LEVEL;
	/** The player's food saturation. */
	private float foodSaturationLevel = START_SATURATION_LEVAL;
	/** The player's food exhaustion. */
	private float foodExhaustionLevel = 0;
	/** The player's food digestion. */
	private float foodDigestionLevel = 0;
	/** The player's food timer value. */
	private int foodTimer;
	private int digestionTimer;
	
	private float waterLevel = MAX_WATER_LEVEL;
	private float waterExhaustionLevel = 0;

	private float[] nutrition = {80, 80, 80, 80, 80, 80};
	private int prevFoodLevel = 20;
	private int prevWaterLevel = 20;
	
	public void addDirectStats(float amount, float saturation)
	{
		foodLevel = Math.min(foodLevel + amount, MAX_FOOD_LEVEL);
		foodSaturationLevel = Math.min(foodSaturationLevel + saturation, foodLevel);
	}

	public void addWaterStats(float amount)
	{
		waterLevel = Math.min(waterLevel + amount, MAX_WATER_LEVEL);
	}
	
	@Override
	public void addStats(@Nullable ItemFood foodItem, ItemStack stack)
	{
		addStatsUncheck(stack.getItem(), stack);
	}
	
	private void addStatsUncheck(Item item, ItemStack stack)
	{
		FoodEatenEvent event = new FoodEatenEvent(this, item, stack);
		if(MinecraftForge.EVENT_BUS.post(event)) return;
		if(event.getResult() != Result.DEFAULT) return;
		if(item instanceof ItemFood)
		{
			int amt = ((ItemFood) item).getHealAmount(stack);
			addStats(amt, ((ItemFood) item).getSaturationModifier(stack));
		}
		else if(item instanceof IFoodStat)
		{
			addFoodStats((IFoodStat) item, stack);
		}
	}
	
	private void addFoodStats(IFoodStat stat, ItemStack stack)
	{
		foodDigestionLevel = Math.min(MAX_FOOD_LEVEL, foodDigestionLevel + stat.getFoodAmount(stack));
		foodSaturationLevel = Math.min(foodLevel, foodSaturationLevel + stat.getSaturation(stack));
		waterLevel = Math.min(MAX_WATER_LEVEL, waterLevel + stat.getDrinkAmount(stack));
		float[] nutr = stat.getNutritionAmount(stack);
		if(nutr != null)
		{
			for(int i = 0; i < 6; ++i)
			{
				nutrition[i] = Math.min(MAX_NUTRITION_LEVEL, nutrition[i] + nutr[i]);
			}
		}
	}
	
	public void addThirsty(float thirsty)
	{
		waterExhaustionLevel += thirsty;
	}

	@Override
	public void onUpdate(EntityPlayer player)
	{
		if(player.capabilities.isCreativeMode) return;
		EnumDifficulty difficulty = player.worldObj.getDifficulty();
		boolean flag = player.worldObj.getGameRules().getBoolean("naturalRegeneration");
		int dim = player.worldObj.provider.getDimension();
		long time = player.worldObj.getWorldTime();
		prevFoodLevel = (int) foodLevel;
		prevWaterLevel = (int) waterLevel;
		updateStat(player);
		if(prevDim != dim)
		{
			prevDim = dim;
			time = player.worldObj.getWorldTime() + 1L;
		}
		else if(time >= worldTimer)
		{
			worldTimer += 20L;
			++foodTimer;
			updateHeal(player, difficulty, flag);
		}
		updateNatural(player, difficulty);
	}

	private void updateStat(EntityPlayer player)
	{
		if (foodDigestionLevel > 0F && foodLevel >= 4)
		{
			if(++digestionTimer > 200)
			{
				float amt = Math.min(foodDigestionLevel, 1);
				foodDigestionLevel -= amt;
				foodLevel += amt;
			}
		}
		if (foodExhaustionLevel >= 4.0F)
		{
			foodExhaustionLevel -= 4.0F;

			if (foodSaturationLevel > 0.0F)
			{
				foodSaturationLevel = Math.max(foodSaturationLevel - 1.0F, 0.0F);
			}
			else//if (enumdifficulty != EnumDifficulty.PEACEFUL)
			{
				foodLevel = Math.max(foodLevel - 1, 0);
			}
		}
		if (Config.enableWaterStat && waterExhaustionLevel > 10.0F)
		{
			waterExhaustionLevel -= 10F;
			waterLevel = Math.max(waterLevel - 1, 0);
		}
	}
	
	private void updateHeal(EntityPlayer player, EnumDifficulty difficulty, boolean flag)
	{
		if(Config.enableWaterStat)
		{
			float amt = 0.01F;
			if (ThermalNet.getTemperature(player.worldObj, player.getPosition(), true) > V.WATER_FREEZE_POINT_F + 32)
			{
				amt += 0.02F;
			}
			addThirsty(amt);
		}
		if (flag && foodLevel >= 10F && player.shouldHeal())
		{
			switch ((int) foodLevel)
			{
			case 20 :
				if(foodSaturationLevel > 0F)
				{
					float f = Math.min(foodSaturationLevel, 4.0F);
					player.heal(f / 4.0F);
					addExhaustion(f);
					foodTimer = 0;
					break;
				}
			case 19 :
				if(foodTimer >= 3)
				{
					player.heal(1.0F);
					addExhaustion(1.0F);
					foodTimer = 0;
					break;
				}
			case 18 :
			case 17 :
			case 16 :
				if(foodTimer >= 6)
				{
					player.heal(1.0F);
					addExhaustion(1.0F);
					foodTimer = 0;
					break;
				}
			case 15 :
			case 14 :
			case 13 :
			case 12 :
			case 11 :
			case 10 :
				if(foodTimer >= 10)
				{
					player.heal(1.0F);
					addExhaustion(1.0F);
					foodTimer = 0;
					break;
				}
			default : break;
			}
		}
		if (foodLevel <= 0)
		{
			if (++foodTimer >= 40)
			{
				if (player.getHealth() > 10.0F || difficulty == EnumDifficulty.HARD || player.getHealth() > 1.0F && difficulty == EnumDifficulty.NORMAL)
				{
					player.attackEntityFrom(DamageSource.starve, 1.0F);
				}

				foodTimer = 0;
			}
		}
	}
	
	private void updateNatural(EntityPlayer player, EnumDifficulty difficulty)
	{
		if (foodLevel < 4)
		{
			if(prevFoodLevel >= 4)
			{
				player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_SPEED).applyModifier(HUNGER_WEAKNESS_I);
				player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(HUNGER_WEAKNESS_II);
			}
		}
		else if(prevFoodLevel < 4)
		{
			player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_SPEED).removeModifier(HUNGER_WEAKNESS_I);
			player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(HUNGER_WEAKNESS_II);
		}
		if(Config.enableWaterStat)
		{
			if(waterLevel < 4F)
			{
				if(prevWaterLevel >= 4)
				{
					player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_SPEED).applyModifier(THIRSTY_WEAKNESS_I);
					player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(THIRSTY_WEAKNESS_II);
				}
			}
			else if(prevWaterLevel < 4F)
			{
				player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_SPEED).removeModifier(THIRSTY_WEAKNESS_I);
				player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(THIRSTY_WEAKNESS_II);
			}
		}
	}
	
	@Override
	public void writeNBT(NBTTagCompound compound)
	{
		compound.setInteger("dim", prevDim);
		compound.setLong("worldTimer", worldTimer);

		compound.setShort("foodTickTimer", (short) foodTimer);
		compound.setShort("digestionTimer", (short) digestionTimer);
		
		compound.setFloat("foodLevel", foodLevel);
		compound.setFloat("foodSaturationLevel", foodSaturationLevel);
		compound.setFloat("foodExhaustionLevel", foodExhaustionLevel);
		for(int i = 0; i < 6; ++i)
		{
			compound.setFloat("nutrition" + EnumNutrition.values()[i].getRegisteredName(), nutrition[i]);
		}

		compound.setFloat("waterLevel", waterLevel);
		compound.setFloat("waterExhaustionLevel", waterExhaustionLevel);
	}

	@Override
	public void readNBT(NBTTagCompound compound)
	{
		prevDim = compound.getInteger("dim");
		worldTimer = NBTs.getLongOrDefault(compound, "worldTimer", Long.MIN_VALUE);
		if (compound.hasKey("waterLevel", 99))
		{
			waterLevel = compound.getFloat("waterLevel");
			waterExhaustionLevel = compound.getFloat("waterExhaustionLevel");
		}
		if (compound.hasKey("foodLevel", 99))
		{
			foodLevel = compound.getFloat("foodLevel");
			foodSaturationLevel = compound.getFloat("foodSaturationLevel");
			foodExhaustionLevel = compound.getFloat("foodExhaustionLevel");
			for(int i = 0; i < 6; ++i)
			{
				nutrition[i] = compound.getFloat("nutrition" + EnumNutrition.values()[i].getRegisteredName());
			}

			foodTimer = compound.getInteger("foodTickTimer");
			digestionTimer = compound.getInteger("digestionTimer");
		}
	}

	@Override
	public void addExhaustion(float exhaustion)
	{
		foodExhaustionLevel += exhaustion;
	}
	
	@Override
	public void addStats(int foodLevelIn, float foodSaturationModifier)
	{
		foodDigestionLevel = Math.min(foodLevelIn + foodDigestionLevel, MAX_FOOD_LEVEL);
		foodSaturationLevel = Math.min(foodSaturationLevel + foodSaturationModifier * foodLevelIn, foodLevel);
	}

	@Override
	public boolean needFood()
	{
		return foodDigestionLevel < MAX_FOOD_LEVEL - foodLevel;
	}
	
	@Override
	public int getFoodLevel()
	{
		return (int) foodLevel;
	}

	public int getWaterLevel()
	{
		return (int) waterLevel;
	}
	
	@Override
	public float getSaturationLevel()
	{
		return foodSaturationLevel;
	}
	
	@Override
	public void setFoodLevel(int foodLevelIn)
	{
		foodLevel = foodLevelIn;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void setFoodSaturationLevel(float foodSaturationLevelIn)
	{
		foodSaturationLevel = foodSaturationLevelIn;
	}

	public void setWaterLevel(int waterLevel)
	{
		this.waterLevel = waterLevel;
	}
}