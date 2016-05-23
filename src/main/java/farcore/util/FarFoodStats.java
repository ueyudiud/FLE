package farcore.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCoreSetup;
import farcore.lib.net.entity.player.PacketPlayerStatUpdate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.world.EnumDifficulty;

/**
 * Far land era food stats use ASM to override minecraft food stats.
 * @author ueyudiud
 *
 */
public class FarFoodStats extends FoodStats
{	
	protected static final float maxFoodLevel = 200F;
	protected static final float maxWaterLevel = 200F;
	
	protected long tick = Long.MIN_VALUE;
	
    /** The player's food level. */
    public float foodLevel = maxFoodLevel;    
    /** The player's food saturation. */
    public float foodSaturationLevel = foodLevel;
    /** The player's food exhaustion. */
    protected float foodExhaustionLevel = 0F;
    
    protected float foodDigestionLevel = 0F;
    /** The player's food timer value. */
    protected int foodTimer;
    
    public float waterLevel = maxWaterLevel;
    
    public float waterExhaustionLevel = 0F;

    public float prevFoodLevel = foodLevel;
    public float prevFoodSaturationLevel = foodSaturationLevel;
    public float prevWaterLevel = waterLevel;
    
    public void addWaterLevel(float level)
    {
    	waterLevel = Math.min(maxWaterLevel, waterLevel + level);
    }
    
    @Deprecated
    @Override
    public void addStats(int level, float saturation)
    {
//    	addFoodDigestionStats(level * 50F, saturation * 50F);
    }

	public void addFoodDigestionStats(NutritionInfo info)
	{
		addFoodDigestionStats(info.hungerApply, info.nutrApply);
	}
    
    public void addFoodDigestionStats(float level, float saturation)
    {
    	foodDigestionLevel = Math.min(foodDigestionLevel + level, maxFoodLevel);
    	foodSaturationLevel = Math.min(foodSaturationLevel + saturation, foodLevel);
    }
    
    public void addFoodStats(float level)
    {
    	foodLevel = Math.min(foodLevel + level, maxFoodLevel);
    }
    
    public void refreshStat(EntityPlayer player)
    {
		addFoodExhaustion(2.5F);
		addWaterExhaustion(3F);
    	if (foodDigestionLevel > 0F && foodLevel > 20F)
    	{
    		float a = Math.min(foodDigestionLevel, 15F);
    		foodDigestionLevel -= a;
    		addFoodStats(a);
    	}
    	if(foodLevel >= 80F && player.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration") && player.shouldHeal())
    	{
    		++foodTimer;
    		if(foodTimer >= getHealTick())
    		{
    			player.heal(1.0F);
    			addFoodExhaustion(8.0F);
    			foodTimer = 0;
    		}
    	}
    	if (foodExhaustionLevel > 5.0F)
        {
            foodExhaustionLevel -= 5.0F;

            float a = Math.min(1F, foodSaturationLevel);
            foodSaturationLevel -= a;
            foodLevel = Math.max(foodLevel - (1F - a), 0);
        }
    	if(waterExhaustionLevel > 5.0F)
    	{
    		waterExhaustionLevel -= 5.0F;
    		waterLevel = Math.max(waterLevel - 1F, 0F);
    	}
    	FarCoreSetup.network.sendToPlayer(new PacketPlayerStatUpdate(this), player);
    }
    
    @Override
    public void onUpdate(EntityPlayer player)
    {
    	if(player.capabilities.isCreativeMode) return;
    	EnumDifficulty difficulty = player.worldObj.difficultySetting;
//    	prevFoodLevel = foodLevel;
//    	prevFoodSaturationLevel = foodSaturationLevel;
//    	prevWaterLevel = waterLevel;

    	long tick1 = U.Time.getTime(player.worldObj);
    	if(tick == Long.MIN_VALUE)
    		tick = tick1;
    	if(tick1 - tick > 200)
    	{
    		tick += 200;
    		refreshStat(player);
    	}
    	if(foodLevel <= 0F)
    	{
    		if(difficulty != EnumDifficulty.PEACEFUL)
    		{
    			player.attackEntityFrom(DamageSource.starve, 1E10F);
    		}
    		else
    		{
    			player.attackEntityFrom(DamageSource.starve, 10F);
    		}
    	}
    }
    
    protected int getHealTick()
    {
    	return (int) ((320F - foodLevel) / 40F) + 1;
    }
    
    @Override
    public void readNBT(NBTTagCompound nbt)
    {
        if (nbt.hasKey("foodLevel", 99))
        {
            this.foodLevel = nbt.getFloat("foodLevel");
            this.foodTimer = nbt.getInteger("foodTickTimer");
            this.foodSaturationLevel = nbt.getFloat("foodSaturationLevel");
            this.foodExhaustionLevel = nbt.getFloat("foodExhaustionLevel");
            this.foodDigestionLevel = nbt.getFloat("foodDigestionLevel");
            this.tick = nbt.getLong("tick");
        }
        if(nbt.hasKey("waterLevel"))
        {
        	this.waterLevel = nbt.getFloat("waterLevel");
        	this.waterExhaustionLevel = nbt.getFloat("waterExhaustionLevel");
        }
    }
    
    @Override
    public void writeNBT(NBTTagCompound nbt)
    {
        nbt.setFloat("foodLevel", this.foodLevel);
        nbt.setInteger("foodTickTimer", this.foodTimer);
        nbt.setFloat("foodSaturationLevel", this.foodSaturationLevel);
        nbt.setFloat("foodExhaustionLevel", this.foodExhaustionLevel);
        nbt.setFloat("foodDigestionLevel", this.foodDigestionLevel);
        nbt.setLong("tick", tick);
        nbt.setFloat("waterLevel", waterLevel);
        nbt.setFloat("waterExhaustionLevel", waterExhaustionLevel);
    }
    
    /**
     * Get the player's food level(For progress bar).
     */
    public int getFoodLevel()
    {
        return (int) (foodLevel / 10F);
    }

    public float getFoodLevelFar()
    {
        return foodLevel;
    }
    
    public float getWaterLevel()
    {
		return waterLevel;
	}
    
    @SideOnly(Side.CLIENT)
    public int getWaterProgress()
    {
		return (int) (getWaterLevel() / 10F);
	}

    @SideOnly(Side.CLIENT)
    public int getPrevFoodLevel()
    {
        return (int) (prevFoodLevel / 10F);
    }

    /**
     * If foodLevel is not max.
     */
    public boolean needFood()
    {
        return this.foodLevel < maxFoodLevel && this.foodDigestionLevel < maxFoodLevel;
    }

    /**
     * adds input to foodExhaustionLevel to a max of 1000
     */
    @Deprecated
    public void addExhaustion(float exhaustion)
    {
//        foodExhaustionLevel = Math.min(foodExhaustionLevel + exhaustion, 50.0F);
    }
    
    public void addFoodExhaustion(float exhaustion)
    {
        foodExhaustionLevel = Math.min(foodExhaustionLevel + exhaustion, 50.0F);
    }
    
    public void addWaterExhaustion(float thirst)
    {
    	waterExhaustionLevel = Math.min(waterExhaustionLevel + thirst, 50.0F);
    }

    /**
     * Get the player's food saturation level.
     */
    @Override
    public float getSaturationLevel()
    {
        return foodSaturationLevel;
    }
    
    @SideOnly(Side.CLIENT)
    public void setFoodLevel(float level)
    {
    	this.foodLevel = level;
    }

    @SideOnly(Side.CLIENT)
    public void setFoodLevel(int level)
    {
//        this.foodLevel = level * 50F;
    }

    @SideOnly(Side.CLIENT)
    public void setFoodSaturationLevel(float level)
    {
        this.foodSaturationLevel = level;
    }
    
    @SideOnly(Side.CLIENT)
    public void setWaterLevel(float level)
    {
    	this.waterLevel = level;
    }
}