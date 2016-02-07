package farcore.chem;

import java.util.ArrayList;
import java.util.List;

import farcore.substance.IPhaseCondition;
import farcore.substance.SStack;
import farcore.substance.Substance;
import farcore.util.Part;

public interface IReactionSystem extends IPhaseCondition
{
	EnumReactionSystemType getType();
	
	List<SStack> getEnvironment();
	
	/**
	 * Match all substance in this system.
	 * (NOT CHECK ENVIOURMENT SUBSTANCE)
	 * @return
	 */
	List<SStack> getContain();
	
	default List<SStack> getAll()
	{
		List list = new ArrayList();
		list.addAll(getContain());
		list.addAll(getEnvironment());
		return list;
	}
	
	void markReactionCache(RecipeCache cache);
	
	/**
	 * Some recipe require a high PH level.
	 * Used Proton acid-base theory.
	 * 700 is water default temperature(298K).
	 * Lower value means more acid condition.
	 * @return
	 */
	int getPH();
	
	/**
	 * The temperature of this system.
	 * Use Kelvin degree.
	 * @return
	 */
	@Override
	long getTemperature();
	
	/**
	 * The pressure of this system.
	 * Use Pa for unit.
	 * Default value(Open system usually.) is 101325Pa.
	 * @return
	 */
	@Override
	long getPressure();
	
	default boolean contain(Substance substance)
	{
		for(SStack stack : getAll())
		{
			if(stack.contain(substance))
				return true;
		}
		return false;
	}
	
	default int amount(Substance substance)
	{
		int size = 0;
		for(SStack stack : getAll())
		{
			if(stack.contain(substance))
				size += stack.size;
		}
		return size;
	}
	
	default float specificArea(Substance substance)
	{
		int size = 0;
		float area = 0F;
		for(SStack stack : getAll())
		{
			if(stack.contain(substance))
			{
				size += stack.size;
				area += stack.part.specificArea * stack.size;
			}
		}
		return area / (float) size;
	}
	
	void remove(Substance substance, int size);
	
	void remove(Substance substance, Part part, int size);
	
	void add(Substance substance, Part part, int size);
	
	double releaseEnergy(double amount, boolean process);
	
	double absorbEnergy(double amount, boolean process);
}