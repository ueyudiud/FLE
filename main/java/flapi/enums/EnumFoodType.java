package flapi.enums;

public enum EnumFoodType
{
	Drink(50),
	Resource(40),
	Snack(80),
	Refection(140),
	Meal(180),
	Feast(300);
	
	int eatTick;
	
	EnumFoodType(int aEatTick)
	{
		eatTick = aEatTick;
	}
	
	public int getEatTick()
	{
		return eatTick;
	}
}
