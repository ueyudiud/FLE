package farcore.util;

import farcore.enums.EnumFoodType;
import farcore.enums.EnumNutrition;

public final class NutritionInfo
{
	int hungerApply;
	float nutrApply;
	float[] amountN;
	int[] amountT;
	
	public static NutritionInfo make()
	{
		return new NutritionInfo();
	}
	
	NutritionInfo()
	{
		amountN = new float[EnumNutrition.values().length];
		amountT = new int[EnumFoodType.values().length];
	}
	
	public NutritionInfo apply(int hungerApply, float nutrApply)
	{
		this.hungerApply = hungerApply;
		this.nutrApply = nutrApply;
		return this;
	}
	
	public NutritionInfo apply(EnumNutrition nutrition, float amount)
	{
		amountN[nutrition.ordinal()] = amount;
		return this;
	}
	
	public NutritionInfo apply(EnumFoodType type, int amount)
	{
		amountT[type.ordinal()] = amount;
		return this;
	}
	
	public NutritionInfo apply(EnumFoodType type)
	{
		return apply(type, 100);
	}
	
	public NutritionInfo copy()
	{
		NutritionInfo info = make();
		info.amountN = amountN.clone();
		info.amountT = amountT.clone();
		return info;
	}
}