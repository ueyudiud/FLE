package fle.core.recipe;

import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fle.api.config.IJsonLoader;
import fle.api.config.JsonInputStream;
import fle.api.material.IAtoms;
import fle.api.material.Matter;
import fle.api.material.Matter.AtomStack;
import fle.api.material.MatterReactionRegister;
import fle.api.material.MatterReactionRegister.ReactionHandler;
import fle.api.util.IChemCondition;
import fle.api.util.IChemCondition.EnumOxide;
import fle.api.util.IChemCondition.EnumPH;
import fle.api.util.WeightHelper;
import fle.api.util.WeightHelper.Stack;
import fle.core.recipe.MatterPhaseChangeRecipe.MatterInfo;

public class MatterJsonRecipe implements IJsonLoader, ReactionHandler
{
	public static final MatterJsonRecipe instance = new MatterJsonRecipe();
	
	private ReactionHandler handler;
	
	private MatterJsonRecipe(){	}
	public MatterJsonRecipe(JsonInputStream obj)
	{
		switch(obj.getString("type", "hit"))
		{
		case "hit" : handler = 
				new MatterReactionRecipe(
						Matter.getMatterFromName(obj.getString("atom1", "")), 
						Matter.getMatterFromName(obj.getString("atom2", "")), 
						obj.getInteger("tempNeed", 0), 
						EnumPH.valueOf(obj.getString("hPH", "MaxPH")),
						EnumPH.valueOf(obj.getString("lPH", "MinPH")),
						EnumOxide.valueOf(obj.getString("hOxide", "Highest")), 
						EnumOxide.valueOf(obj.getString("lOxide", "Lowest")), 
						obj.getDouble("phEffect", 0), 
						obj.getDouble("oxideEffect", 0), 
						obj.getDouble("tempretureEffect", 0), 
						obj.getDouble("baseEffect", 0.00001), 
						AtomStack.readMatterStackFromJson(obj.sub("output")));
		break;
		case "resolve" :  handler = 
				new MatterSingleRecipe(
						Matter.getMatterFromName(obj.getString("atom", "")), 
						obj.getInteger("tempNeed", 0), 
						EnumPH.valueOf(obj.getString("hPH", "MaxPH")),
						EnumPH.valueOf(obj.getString("lPH", "MinPH")),
						EnumOxide.valueOf(obj.getString("hOxide", "Highest")), 
						EnumOxide.valueOf(obj.getString("lOxide", "Lowest")), 
						obj.getDouble("phEffect", 0), 
						obj.getDouble("oxideEffect", 0), 
						obj.getDouble("tempretureEffect", 0), 
						obj.getDouble("baseEffect", 0.00001), 
						AtomStack.readMatterStackFromJson(obj.sub("output")));
		break;
		case "phase" : ;
		WeightHelper<IAtoms> wh = new WeightHelper(AtomStack.readMatterStackFromJson(obj.sub("output")));
		MatterPhaseChangeRecipe.register(
				new MatterInfo(obj.getBoolean("airPhase"), Matter.getMatterFromName(obj.getString("atom", "")), 
						obj.getInteger("tempNeed", 0), (float) obj.getDouble("baseEffect", 0.00001),
						(float) obj.getDouble("tempretureEffect", 0), wh.getArray(new IAtoms[wh.allWeight()])));
		break;
		}
	}
	
	@Override
	public boolean doesActive(IChemCondition condition, WeightHelper helper)
	{
		return handler == null ? false : handler.doesActive(condition, helper);
	}

	@Override
	public void doReactionResult(IChemCondition condition, WeightHelper helper)
	{
		handler.doReactionResult(condition, helper);
	}

	@Override
	public void readJson(JsonElement e) throws IOException
	{
		if(!e.isJsonObject()) return;
		if(!e.getAsJsonObject().has("recipes")) return;
		if(!e.getAsJsonObject().get("recipes").isJsonArray()) return;
		for(JsonElement raw : e.getAsJsonObject().get("recipes").getAsJsonArray())
		{
			JsonObject obj = (JsonObject) raw;
			MatterReactionRegister.registerReactionHandler(new MatterJsonRecipe(new JsonInputStream(obj)));
		}
	}
}