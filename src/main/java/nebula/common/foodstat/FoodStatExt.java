/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.foodstat;

import java.util.UUID;

import javax.annotation.Nullable;

import nebula.common.NebulaConfig;
import nebula.common.item.IFoodStat;
import nebula.common.util.NBTs;
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
		this.foodLevel = Math.min(this.foodLevel + amount, MAX_FOOD_LEVEL);
		this.foodSaturationLevel = Math.min(this.foodSaturationLevel + saturation, this.foodLevel);
	}
	
	public void addWaterStats(float amount)
	{
		this.waterLevel = Math.min(this.waterLevel + amount, MAX_WATER_LEVEL);
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
		this.foodDigestionLevel = Math.min(MAX_FOOD_LEVEL, this.foodDigestionLevel + stat.getFoodAmount(stack));
		this.foodSaturationLevel = Math.min(this.foodLevel, this.foodSaturationLevel + stat.getSaturation(stack));
		this.waterLevel = Math.min(MAX_WATER_LEVEL, this.waterLevel + stat.getDrinkAmount(stack));
		float[] nutr = stat.getNutritionAmount(stack);
		if(nutr != null)
		{
			for(int i = 0; i < 6; ++i)
			{
				this.nutrition[i] = Math.min(MAX_NUTRITION_LEVEL, this.nutrition[i] + nutr[i]);
			}
		}
	}
	
	public void addThirsty(float thirsty)
	{
		this.waterExhaustionLevel += thirsty;
	}
	
	@Override
	public void onUpdate(EntityPlayer player)
	{
		if(player.capabilities.isCreativeMode) return;
		EnumDifficulty difficulty = player.world.getDifficulty();
		boolean flag = player.world.getGameRules().getBoolean("naturalRegeneration");
		int dim = player.world.provider.getDimension();
		long time = player.world.getWorldTime();
		this.prevFoodLevel = (int) this.foodLevel;
		this.prevWaterLevel = (int) this.waterLevel;
		updateStat(player);
		if(this.prevDim != dim)
		{
			this.prevDim = dim;
			time = player.world.getWorldTime() + 1L;
		}
		else if(time >= this.worldTimer)
		{
			this.worldTimer += 20L;
			++this.foodTimer;
			updateHeal(player, difficulty, flag);
		}
		updateNatural(player, difficulty);
	}
	
	private void updateStat(EntityPlayer player)
	{
		if (this.foodDigestionLevel > 0F && this.foodLevel >= 4)
		{
			if(++this.digestionTimer > 200)
			{
				float amt = Math.min(this.foodDigestionLevel, 1);
				this.foodDigestionLevel -= 1.0F;
				this.foodLevel += amt;
				this.digestionTimer = 0;
			}
		}
		if (this.foodExhaustionLevel >= 4.0F)
		{
			this.foodExhaustionLevel -= 4.0F;
			
			if (this.foodSaturationLevel > 0.0F)
			{
				this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0F, 0.0F);
			}
			else//if (enumdifficulty != EnumDifficulty.PEACEFUL)
			{
				this.foodLevel = Math.max(this.foodLevel - 1.0F, 0F);
			}
		}
		if (NebulaConfig.enableWaterStat && this.waterExhaustionLevel > 10.0F)
		{
			this.waterExhaustionLevel -= 10F;
			this.waterLevel = Math.max(this.waterLevel - 1, 0);
		}
	}
	
	private void updateHeal(EntityPlayer player, EnumDifficulty difficulty, boolean flag)
	{
		if (flag && this.foodLevel >= 10F && player.shouldHeal())
		{
			switch ((int) this.foodLevel)
			{
			case 20 :
				if(this.foodSaturationLevel > 0F)
				{
					float f = Math.min(this.foodSaturationLevel, 4.0F);
					player.heal(f / 4.0F);
					addExhaustion(f);
					this.foodTimer = 0;
					break;
				}
			case 19 :
				if(this.foodTimer >= 3)
				{
					player.heal(1.0F);
					addExhaustion(1.0F);
					this.foodTimer = 0;
					break;
				}
			case 18 :
			case 17 :
			case 16 :
				if(this.foodTimer >= 6)
				{
					player.heal(1.0F);
					addExhaustion(1.0F);
					this.foodTimer = 0;
					break;
				}
			case 15 :
			case 14 :
			case 13 :
			case 12 :
			case 11 :
			case 10 :
				if(this.foodTimer >= 10)
				{
					player.heal(1.0F);
					addExhaustion(1.0F);
					this.foodTimer = 0;
					break;
				}
			default : break;
			}
		}
		if (this.foodLevel <= 0)
		{
			if (++this.foodTimer >= 40)
			{
				if (player.getHealth() > 10.0F || difficulty == EnumDifficulty.HARD || player.getHealth() > 1.0F && difficulty == EnumDifficulty.NORMAL)
				{
					player.attackEntityFrom(DamageSource.starve, 1.0F);
				}
				
				this.foodTimer = 0;
			}
		}
	}
	
	private void updateNatural(EntityPlayer player, EnumDifficulty difficulty)
	{
		if (this.foodLevel < 4)
		{
			if(this.prevFoodLevel >= 4)
			{
				player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_SPEED).applyModifier(HUNGER_WEAKNESS_I);
				player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(HUNGER_WEAKNESS_II);
			}
		}
		else if(this.prevFoodLevel < 4)
		{
			player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_SPEED).removeModifier(HUNGER_WEAKNESS_I);
			player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(HUNGER_WEAKNESS_II);
		}
		if(NebulaConfig.enableWaterStat)
		{
			if(this.waterLevel < 4F)
			{
				if(this.prevWaterLevel >= 4)
				{
					player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_SPEED).applyModifier(THIRSTY_WEAKNESS_I);
					player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(THIRSTY_WEAKNESS_II);
				}
			}
			else if(this.prevWaterLevel < 4F)
			{
				player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_SPEED).removeModifier(THIRSTY_WEAKNESS_I);
				player.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(THIRSTY_WEAKNESS_II);
			}
		}
	}
	
	@Override
	public void writeNBT(NBTTagCompound compound)
	{
		compound.setInteger("dim", this.prevDim);
		compound.setLong("worldTimer", this.worldTimer);
		
		compound.setShort("foodTickTimer", (short) this.foodTimer);
		compound.setShort("digestionTimer", (short) this.digestionTimer);
		
		compound.setFloat("foodLevel", this.foodLevel);
		compound.setFloat("foodSaturationLevel", this.foodSaturationLevel);
		compound.setFloat("foodExhaustionLevel", this.foodExhaustionLevel);
		for(int i = 0; i < 6; ++i)
		{
			compound.setFloat("nutrition" + EnumNutrition.values()[i].getRegisteredName(), this.nutrition[i]);
		}
		
		compound.setFloat("waterLevel", this.waterLevel);
		compound.setFloat("waterExhaustionLevel", this.waterExhaustionLevel);
	}
	
	@Override
	public void readNBT(NBTTagCompound compound)
	{
		this.prevDim = compound.getInteger("dim");
		this.worldTimer = NBTs.getLongOrDefault(compound, "worldTimer", Long.MIN_VALUE);
		if (compound.hasKey("waterLevel", 99))
		{
			this.waterLevel = compound.getFloat("waterLevel");
			this.waterExhaustionLevel = compound.getFloat("waterExhaustionLevel");
		}
		if (compound.hasKey("foodLevel", 99))
		{
			this.foodLevel = compound.getFloat("foodLevel");
			this.foodSaturationLevel = compound.getFloat("foodSaturationLevel");
			this.foodExhaustionLevel = compound.getFloat("foodExhaustionLevel");
			for(int i = 0; i < 6; ++i)
			{
				this.nutrition[i] = compound.getFloat("nutrition" + EnumNutrition.values()[i].getRegisteredName());
			}
			
			this.foodTimer = compound.getInteger("foodTickTimer");
			this.digestionTimer = compound.getInteger("digestionTimer");
		}
	}
	
	@Override
	public void addExhaustion(float exhaustion)
	{
		this.foodExhaustionLevel += exhaustion;
	}
	
	@Override
	public void addStats(int foodLevelIn, float foodSaturationModifier)
	{
		this.foodDigestionLevel = Math.min(foodLevelIn + this.foodDigestionLevel, MAX_FOOD_LEVEL);
		this.foodSaturationLevel = Math.min(this.foodSaturationLevel + foodSaturationModifier * foodLevelIn, this.foodLevel);
	}
	
	@Override
	public boolean needFood()
	{
		return this.foodDigestionLevel < MAX_FOOD_LEVEL - this.foodLevel;
	}
	
	@Override
	public int getFoodLevel()
	{
		return (int) this.foodLevel;
	}
	
	public int getWaterLevel()
	{
		return (int) this.waterLevel;
	}
	
	@Override
	public float getSaturationLevel()
	{
		return this.foodSaturationLevel;
	}
	
	@Override
	public void setFoodLevel(int foodLevelIn)
	{
		this.foodLevel = foodLevelIn;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void setFoodSaturationLevel(float foodSaturationLevelIn)
	{
		this.foodSaturationLevel = foodSaturationLevelIn;
	}
	
	public void setWaterLevel(int waterLevel)
	{
		this.waterLevel = waterLevel;
	}
}