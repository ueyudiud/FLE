package farcore.chem;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import farcore.substance.PhaseDiagram.Phase;
import farcore.substance.SStack;
import farcore.substance.Substance;
import farcore.util.FleLog;
import farcore.util.Part;
import farcore.util.Util;
import net.minecraft.util.MathHelper;

public class StandardReactionHandler implements IReactionRecipeHandler
{
	private static final EnumMap<EnumReactionLevel, List<IReactionRecipe>> map = 
			new EnumMap(EnumReactionLevel.class);
	static final Map<String, IReactionRecipe> recipes = new HashMap();
	
	public static final StandardReactionHandler instance = new StandardReactionHandler();
	
	public static void addRecipe(EnumReactionLevel level, IReactionRecipe recipe)
	{
		if(recipes.containsKey(recipe.name()))
		{
			FleLog.warn("Some mod has already registered this recipe key " + recipe.name());
			return;
		}
		if(!map.containsKey(level)) map.put(level, new ArrayList());
		map.get(level).add(recipe);
		recipes.put(recipe.name(), recipe);
	}
	
	public static void updateReaction(IReactionSystem system)
	{
		for(SStack stack : new ArrayList<SStack>(system.getContain()))
		{
			Phase phase = stack.getSubstance().getPhase(system);
			if(stack.part.phase != phase)
			{
				double changeRequire = (phase.energy - stack.part.phase.energy) * stack.getSubstance().specificHeat;
				double e = changeRequire * stack.size;
				int reactSize = stack.size;
				if(e > 0)
				{
					double e1 = system.releaseEnergy(e, false);
					reactSize = (int) (e1 / changeRequire);
					system.releaseEnergy(e1, true);
				}
				else if(e < 0)
				{
					double e1 = system.absorbEnergy(-e, false);
					reactSize = (int) (e1 / changeRequire);
					system.absorbEnergy(e1, true);
				}
				system.remove(stack.getSubstance(), stack.part, stack.size);
				system.add(stack.getSubstance(), getPart(stack.getSubstance(), system), reactSize);
				continue;
			}
			Substance substance = stack.getSubstance().getSubstance(system);
			if(stack.getSubstance() != substance)
			{
				system.remove(stack.getSubstance(), stack.part, 1);
				system.add(substance, getPart(substance, system), 1);
			}
			int i0;
			if((i0 = phase.isStaybleInSystem(system.getType())) != 0)
			{
				if(Util.nextInt(i0) == 0)
				{
					system.remove(stack.getSubstance(), stack.part, 1);
				}
				continue;
			}
		}
		IReactionRecipeHandler handler;
		for(int i = 0; i < EnumReactionLevel.values().length; ++i)
		{
			if(!map.containsKey(EnumReactionLevel.values()[i])) continue;
			for(IReactionRecipe recipe : 
				map.get(EnumReactionLevel.values()[i]))
			{
				handler = recipe.getHandler();
				if(handler == null) handler = instance;
				try
				{
					if(handler.matchRecipe(system, recipe))
					{
						handler.handleRecipe(system, recipe);
					}
				}
				catch(Exception exception)
				{
					throw new RuntimeException("Fail to match the recipe.");
				}
			}
		}
	}
	
	private StandardReactionHandler(){}
	
	@Override
	public boolean matchRecipe(IReactionSystem system, IReactionRecipe recipe)
	{
		long temp = system.getTemperature();
		long pressure = system.getPressure();
		int ph = system.getPH();
		if(recipe.getMinTemp() > temp || recipe.getMaxTemp() < temp ||
				recipe.getMinPressure() > pressure || recipe.getMaxPressure() < pressure ||
				recipe.getMinPH() > ph || recipe.getMaxPH() < ph)
			return false;

		Map<Substance, Integer> material = recipe.material();
		for(Entry<Substance, Integer> substance : material.entrySet())
		{
			if(system.amount(substance.getKey()) < substance.getValue())
				return false;
		}
		return true;
	}
	
	@Override
	public void handleRecipe(IReactionSystem system, IReactionRecipe recipe)
	{
		Map<Substance, Integer> material = recipe.material();
		Map<Substance, Integer> product = recipe.product();
		float b = recipe.reactionCoefficient(system);
		float a1 = 0F;
		float a2 = 0F;
		float area = 0F;
		int reactAmount = Integer.MAX_VALUE;
		int a;
		for(Entry<Substance, Integer> entry : material.entrySet())
		{
			a1 += (a = system.amount(entry.getKey()));
			area += 1F / system.specificArea(entry.getKey());
			reactAmount = Math.min(reactAmount, a / entry.getValue().intValue());
		}
		List<SStack> output = new ArrayList();
		for(Entry<Substance, Integer> entry : product.entrySet())
		{
			a2 += system.amount(entry.getKey());
			output.add(new SStack(entry.getKey(), getPart(entry.getKey(), system), entry.getValue() * reactAmount));
		}
		b *= a1 / (a2 + 1E-2F);//Prevent divide 0.
		b /= reactAmount;
		b *= a1 / area;
		long energy = recipe.energyChange();
		if(energy > 0)
		{
			system.releaseEnergy(- energy * reactAmount, true);
		}
		else if(energy < 0)
		{
			system.absorbEnergy(energy * reactAmount, true);
		}
		for(Entry<Substance, Integer> entry : material.entrySet())
		{
			system.remove(entry.getKey(), entry.getValue() * reactAmount);
		}
		RecipeCache cache = new RecipeCache(recipe, output, (long) (energy * reactAmount * 100 / b));
		system.markReactionCache(cache);
	}

	private static Part getPart(Substance substance, IReactionSystem system)
	{
		switch (substance.getPhase(system))
		{
		case SOLID:	return Part.dust;
		case LIQUID: return Part.liquid;
		case GAS: return Part.gas;
		default: return Part.ingot;
		}
	}
}