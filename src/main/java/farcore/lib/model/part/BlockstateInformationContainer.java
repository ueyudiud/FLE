package farcore.lib.model.part;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.util.ResourceLocation;

public class BlockstateInformationContainer
{
	public static final BlockstateInformationContainer INSTANCE = new BlockstateInformationContainer();
	
	private static final Function<String, ModelExtData> CUSTOM_MODEL_LOSSING = (String name) ->
	{
		return ModelExtData.DEFAULT;
	};

	public String parentName = null;
	public BlockstateInformationContainer parent = null;
	
	public Map<String, ResourceLocation> modelLocation = new HashMap();

	public Function<String, ModelExtData> customModel = CUSTOM_MODEL_LOSSING;
	
	public static class ModelExtDataFunction implements Function<String, ModelExtData>
	{
		public ModelExtDataFunction()
		{
		}

		@Override
		public ModelExtData apply(String name)
		{
			return null;
		}
	}
}