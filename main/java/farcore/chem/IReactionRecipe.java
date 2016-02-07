package farcore.chem;

import java.util.Map;

import farcore.substance.Substance;

public interface IReactionRecipe
{
	String name();
	
	/**
	 * Use standard reaction handler to match the recipe or use custom to matching.
	 * @return
	 */
	default IReactionRecipeHandler getHandler()
	{
		return StandardReactionHandler.instance;
	}
	
	long getMaxTemp();
	
	long getMinTemp();
	
	long getMaxPressure();
	
	long getMinPressure();
	
	int getMaxPH();
	
	int getMinPH();
	
	/**
	 * Get inputs.
	 * @return
	 */
	Map<Substance, Integer> material();
	
	/**
	 * Get outputs.
	 * The default output use these property.
	 * SOLID for dust or powder.
	 * LIQUID for liquid.
	 * GAS for gas.
	 * @return
	 */
	Map<Substance, Integer> product();
	
	/**
	 * A coefficient to balance how much does this
	 * reaction will happened.<br>
	 * Higher value cause forward reaction more
	 * thorough.
	 * @return
	 */
	float reactionCoefficient(IReactionSystem system);
	
	/**
	 * Get energy change in this reaction.
	 * @return
	 */
	long energyChange();
}