package farcore.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
	protected static final float maxFoodLevel = 1000F;
	
    /** The player's food level. */
    protected float foodLevel = 1000F;
    /** The player's food saturation. */
    protected float foodSaturationLevel = 250F;
    /** The player's food exhaustion. */
    protected float foodExhaustionLevel = 0F;
    
    protected float foodDigestionLevel = 0F;
    /** The player's food timer value. */
    protected int foodTimer;
    
    protected int hungerTimer;
    
    protected int digestTimer;
    protected float prevFoodLevel = 1000F;
    
    @Override
    public void addStats(int level, float saturation)
    {
    	addFoodDigestionStats(level * 50F, saturation * 50F);
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
    
    @Override
    public void onUpdate(EntityPlayer player)
    {
    	if(player.capabilities.isCreativeMode) return;
    	EnumDifficulty difficulty = player.worldObj.difficultySetting;
    	prevFoodLevel = foodLevel;

    	++hungerTimer;
    	if (hungerTimer >= 1500)
    	{
    		hungerTimer = 0;
    		addExhaustion(12.5F);
    	}
    	if (foodDigestionLevel > 0F && foodLevel > 100F)
    	{
    		digestTimer++;
    		if(digestTimer >= 8)
    		{
    			digestTimer = 0;
    			float a = Math.min(foodDigestionLevel, 1F);
    			foodDigestionLevel -= a;
    			addFoodStats(a);
    		}
    	}
    	if (foodExhaustionLevel > 1.0F)
        {
            foodExhaustionLevel -= 1.0F;

            float a = Math.min(1F, foodSaturationLevel);
            foodSaturationLevel -= a;
            foodLevel = Math.max(foodLevel - (1F - a), 0);
        }
    	if(player.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration") && foodLevel >= 400F && player.shouldHeal())
    	{
    		++foodTimer;
    		if(foodTimer >= getHealTick())
    		{
    			player.heal(1.0F);
    			addExhaustion(2.5F);
    			foodTimer = 0;
    		}
    	}
    	else if(foodLevel <= 0F)
    	{
    		++foodTimer;
    		if(foodTimer >= 3000)
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
    		foodTimer = 0;
    	}
    }
    
    protected int getHealTick()
    {
    	return (int) ((1800F - foodLevel) / 4F);
    }
    
    @Override
    public void readNBT(NBTTagCompound nbt)
    {
        if (nbt.hasKey("foodLevel", 99))
        {
            this.foodLevel = nbt.getFloat("foodLevel");
            this.foodTimer = nbt.getInteger("foodTickTimer");
            this.hungerTimer = nbt.getInteger("hungerTimer");
            this.foodSaturationLevel = nbt.getFloat("foodSaturationLevel");
            this.foodExhaustionLevel = nbt.getFloat("foodExhaustionLevel");
            this.foodDigestionLevel = nbt.getFloat("foodDigestionLevel");
        }
    }
    
    @Override
    public void writeNBT(NBTTagCompound nbt)
    {
        nbt.setFloat("foodLevel", this.foodLevel);
        nbt.setInteger("foodTickTimer", this.foodTimer);
        nbt.setInteger("hungerTimer", this.hungerTimer);
        nbt.setFloat("foodSaturationLevel", this.foodSaturationLevel);
        nbt.setFloat("foodExhaustionLevel", this.foodExhaustionLevel);
        nbt.setFloat("foodDigestionLevel", this.foodDigestionLevel);
    }
    
    /**
     * Get the player's food level.
     */
    public int getFoodLevel()
    {
        return (int) (foodLevel / 50F);
    }

    @SideOnly(Side.CLIENT)
    public int getPrevFoodLevel()
    {
        return (int) (prevFoodLevel / 50F);
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
    public void addExhaustion(float exhaustion)
    {
        foodExhaustionLevel = Math.min(foodExhaustionLevel + exhaustion, 50.0F);
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
    public void setFoodLevel(int level)
    {
        this.foodLevel = level * 50F;
    }

    @SideOnly(Side.CLIENT)
    public void setFoodSaturationLevel(float level)
    {
        this.foodSaturationLevel = level;
    }
}