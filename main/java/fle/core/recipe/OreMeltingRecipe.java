package fle.core.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import flapi.material.MaterialOre;
import fle.core.init.Materials;

public class OreMeltingRecipe
{
	private static List<OreMeltingRecipe> list = new ArrayList();
	private static Map<MaterialOre, OreMeltingRecipe> map = new HashMap();
	
	static
	{
		registerRecipe(new OreMeltingRecipe(Materials.Cuprite, 550, 640000, Materials.NativeCopper, 0.5F));
		registerRecipe(new OreMeltingRecipe(Materials.Tenorite, 600, 800000, Materials.NativeCopper, 0.3F));
		registerRecipe(new OreMeltingRecipe(Materials.Chalcocite, 450, 180000, Materials.Cuprite, 0.95F));
		registerRecipe(new OreMeltingRecipe(Materials.Covellite, 500, 102000, Materials.Tenorite, 0.95F));
		registerRecipe(new OreMeltingRecipe(Materials.Malachite, 400, 86000, Materials.Tenorite, 0.3F));
		registerRecipe(new OreMeltingRecipe(Materials.Azurite, 400, 88000, Materials.Tenorite, 0.3F));
	}
	
	public static void registerRecipe(OreMeltingRecipe recipe)
	{
		list.add(recipe);
		map.put(recipe.input, recipe);
	}

	public static OreMeltingRecipe matchRecipe(MaterialOre ore)
	{
		if(map.containsKey(ore))
		{
			return map.get(ore);
		}
		return null;
	}
	
	public int minTempreture;
	public int energyRequire;
	MaterialOre input;
	public MaterialOre output;
	public float productivity;
	
	public OreMeltingRecipe(MaterialOre aInput, int aTempreture, int aEnergy, MaterialOre aOutput, float aProductivity)
	{
		input = aInput;
		minTempreture = aTempreture;
		energyRequire = aEnergy;
		output = aOutput;
		productivity = aProductivity;
	}
}