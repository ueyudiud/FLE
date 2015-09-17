package fle.core.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;

public class FleFoodStats extends FoodStats
{
    /** The player's food level. */
    protected int foodLevel = 40;
    /** The player's food saturation. */
    protected float foodSaturationLevel = 5.0F;
    /** The player's food exhaustion. */
    protected float foodExhaustionLevel;
    /** The player's food timer value. */
    protected int foodTimer;
    protected int prevFoodLevel = 20;
    
    public FleFoodStats()
    {
    	
	}
    public FleFoodStats(FoodStats fs)
    {
    	if(fs != null)
    	{
        	foodLevel = fs.getFoodLevel();
        	foodSaturationLevel = fs.getSaturationLevel();
        	foodExhaustionLevel = 0.0F;
    	}
	}
    
    @Override
    public void addStats(int foodLevel, float foodSaturationModifier)
    {
        this.foodLevel += foodLevel;
        this.foodSaturationLevel = Math.min(this.foodSaturationLevel + (float) foodLevel * foodSaturationModifier / 10.0F, (float) this.foodLevel);
    }

    @Override
    public void func_151686_a(ItemFood food, ItemStack aStack)
    {
        addStats(food.func_150905_g(aStack), food.func_150906_h(aStack));
    }
    
    /**
     * Handles the food game logic.
     */
    @Override
    public void onUpdate(EntityPlayer aPlayer)
    {
    	if(aPlayer.capabilities.isCreativeMode) return;
        EnumDifficulty dif = aPlayer.worldObj.difficultySetting;
        prevFoodLevel = (int) Math.floor(foodLevel);
        addExhaustion(0.0015625F);

        while (foodExhaustionLevel > 4.0F)
        {
            foodExhaustionLevel -= 4.0F;

            if ((float) foodLevel - foodSaturationLevel <= 3.0F && foodSaturationLevel > 0.0F)
            {
                foodSaturationLevel -= 1.0F;
            }
            else
            {
            	foodLevel -= aPlayer.getRNG().nextBoolean() ? 1 : 0 + 1;
            }
        }

        if (aPlayer.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration") && aPlayer.shouldHeal())
        {
            ++this.foodTimer;
        	if(foodLevel >= 36)
        	{
                if (foodTimer >= 75)
                {
                    aPlayer.heal(1.0F);
                    addExhaustion(1.5F);
                    foodTimer = 0;
                }
        	}
        	else if(foodLevel >= 30)
        	{
                if (foodTimer >= 100)
                {
                    aPlayer.heal(1.0F);
                    addExhaustion(1.5F);
                    foodTimer = 0;
                }
        	}
        	else if(foodLevel >= 24)
        	{
                if (foodTimer >= 150)
                {
                    aPlayer.heal(1.0F);
                    addExhaustion(1.5F);
                    foodTimer = 0;
                }
        	}
        	else if(foodLevel >= 16)
        	{
                if (foodTimer >= 240)
                {
                    aPlayer.heal(1.0F);
                    addExhaustion(1.5F);
                    foodTimer = 0;
                }
        	}
        	else if(foodLevel >= 8)
        	{
                if (foodTimer >= 400)
                {
                    aPlayer.heal(1.0F);
                    addExhaustion(1.5F);
                    foodTimer = 0;
                }
        	}
        }
        else if (foodLevel <= 0)
        {
            ++foodTimer;
            if (foodTimer >= 20)
            {
            	float damage = dif == EnumDifficulty.PEACEFUL ? 100.0F : dif == EnumDifficulty.EASY ? 5.0F : dif == EnumDifficulty.NORMAL ? 10.0F : 20.0F;
                aPlayer.attackEntityFrom(DamageSource.starve, damage);

                foodTimer = 0;
            }
        }
        else
        {
            foodTimer = 0;
        }
    }
    
    /**
     * Reads food stats from an NBT object.
     */
    @Override
    public void readNBT(NBTTagCompound nbt)
    {
        if (nbt.hasKey("foodLevel", 99))
        {
            foodLevel = nbt.getInteger("foodLevel");
            foodTimer = nbt.getInteger("foodTickTimer");
            foodSaturationLevel = nbt.getFloat("foodSaturationLevel");
            foodExhaustionLevel = nbt.getFloat("foodExhaustionLevel");
        }
    }

    /**
     * Writes food stats to an NBT object.
     */
    @Override
    public void writeNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("foodLevel", foodLevel);
        nbt.setInteger("foodTickTimer", foodTimer);
        nbt.setFloat("foodSaturationLevel", foodSaturationLevel);
        nbt.setFloat("foodExhaustionLevel", foodExhaustionLevel);
    }
    
    /**
     * Get the player's food level.
     */
    public int getFoodLevel()
    {
        return (int) Math.floor(foodLevel / 2);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getPrevFoodLevel()
    {
        return prevFoodLevel;
    }

    /**
     * If foodLevel is not max.
     */
    @Override
    public boolean needFood()
    {
        return foodLevel < 40;
    }

    /**
     * adds input to foodExhaustionLevel to a max of 40
     */
    @Override
    public void addExhaustion(float ex)
    {
        foodExhaustionLevel = (float) Math.min(foodExhaustionLevel + ex, 40.0F);
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
    @Override
    public void setFoodLevel(int level)
    {
        foodLevel = level * 2;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void setFoodSaturationLevel(float level)
    {
        foodSaturationLevel = level;
    }
}