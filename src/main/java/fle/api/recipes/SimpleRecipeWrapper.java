package fle.api.recipes;

import java.util.Arrays;

public abstract class SimpleRecipeWrapper implements IRecipe
{
	final IRecipeInput[] inputs;
	final IRecipeOutput[] outputs;
	final String registeredName;
	boolean isFakeRecipe = false;
	
	public SimpleRecipeWrapper(IRecipeInput[] inputs, IRecipeOutput[] outputs)
	{
		this.inputs = inputs;
		this.outputs = outputs;
		registeredName = Arrays.toString(inputs) + "->" + Arrays.toString(outputs);
	}
	
	public SimpleRecipeWrapper markFakeRecipe()
	{
		isFakeRecipe = true;
		return this;
	}

	@Override
	public String getRegisteredName()
	{
		return registeredName;
	}
	
	@Override
	public int getRecipeInputSize()
	{
		return inputs.length;
	}
	
	@Override
	public IRecipeInput getInput(int index)
	{
		return inputs[index];
	}
	
	@Override
	public int getRecipeOutputSize()
	{
		return outputs.length;
	}
	
	@Override
	public IRecipeOutput getOutput(int index)
	{
		return outputs[index];
	}
	
	@Override
	public boolean isFakeRecipe()
	{
		return isFakeRecipe;
	}
	
	@Override
	public boolean isValid()
	{
		for(IRecipeInput input : inputs)
		{
			if(!input.isValid()) return false;
		}
		for(IRecipeOutput output : outputs)
		{
			if(!output.isValid()) return false;
		}
		return true;
	}
}