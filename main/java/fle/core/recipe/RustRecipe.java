package fle.core.recipe;

import java.util.HashMap;
import java.util.Map;

import fle.api.material.MaterialAbstract;

public class RustRecipe
{
	private static final Map<MaterialAbstract, RustRecipe> map = new HashMap();
	
	public static RustRecipe getRustRecipe(MaterialAbstract aInput)
	{
		return map.get(aInput);
	}
	
	public static void registryRecipe(MaterialAbstract aInput, float aBaseSpeed, MaterialAbstract aOutput)
	{
		if(map.containsKey(aInput)) return;
		new RustRecipe(aInput, aBaseSpeed, aOutput);
	}
	
	private float baseSpeed;
	private MaterialAbstract input;
	private MaterialAbstract output;
	
	public RustRecipe(MaterialAbstract aInput, float aBaseSpeed, MaterialAbstract aOutput) 
	{
		baseSpeed = aBaseSpeed;
		input = aInput;
		output = aOutput;
		map.put(aInput, this);
	}
	
	public float getBaseSpeed(int temperature, float wetness)
	{
		return baseSpeed;
	}
	
	public MaterialAbstract getOutput()
	{
		return output;
	}
}