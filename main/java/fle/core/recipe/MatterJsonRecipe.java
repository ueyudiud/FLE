package fle.core.recipe;

import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import flapi.collection.ArrayStandardStackList;
import flapi.collection.abs.IStackList;
import flapi.collection.abs.Stack;
import flapi.material.IChemCondition;
import flapi.material.IChemCondition.EnumOxide;
import flapi.material.IChemCondition.EnumPH;
import flapi.material.IMolecular;
import flapi.material.Matter;
import flapi.material.Matter.AtomStack;
import flapi.material.MatterReactionRegister;
import flapi.material.MatterReactionRegister.ReactionHandler;
import flapi.util.io.IJsonLoader;
import flapi.util.io.JsonInputStream;
import fle.core.recipe.MatterPhaseChangeRecipe.MatterInfo;

public class MatterJsonRecipe implements IJsonLoader, ReactionHandler
{
	public static final MatterJsonRecipe instance = new MatterJsonRecipe();
	
	private ReactionHandler handler;
	
	private MatterJsonRecipe(){	}
	public MatterJsonRecipe(JsonInputStream obj)
	{
		String str = obj.getString("type", "hit");
		if(str == "hit")
			handler = 
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
		else if(str == "resolve")
			handler = 
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
		else if(str == "phase")
		{
			ArrayStandardStackList<IMolecular> wh = new ArrayStandardStackList(AtomStack.readMatterStackFromJson(obj.sub("output")));
			MatterPhaseChangeRecipe.register(
					new MatterInfo(obj.getBoolean("airPhase"), Matter.getMatterFromName(obj.getString("atom", "")), 
							obj.getInteger("tempNeed", 0), (float) obj.getDouble("baseEffect", 0.00001),
							(float) obj.getDouble("tempretureEffect", 0), wh.toArray()));
		}
	}
	
	@Override
	public boolean doesActive(IChemCondition condition, IStackList<Stack<IMolecular>, IMolecular> helper)
	{
		return handler == null ? false : handler.doesActive(condition, helper);
	}

	@Override
	public void doReactionResult(IChemCondition condition, IStackList<Stack<IMolecular>, IMolecular> helper)
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