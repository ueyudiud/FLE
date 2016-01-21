package flapi.debug;

import java.io.File;
import java.io.IOException;

import net.minecraft.block.state.BlockState;

import net.minecraftforge.fml.common.registry.GameData;

public class RenderJsonWriter
{
	private File file;
	
	public RenderJsonWriter(File file) throws IOException
	{
		this.file = file;
		if (!file.exists())
		{
			file.createNewFile();
		}
	}
	
	public void write(BlockState state, IModelLocateProvider provider1,
			IModelProvider provider, IModelStateProvider... providers)
					throws IOException
	{
		String[] data = ((String) GameData.getBlockRegistry()
				.getNameForObject(state.getBlock())).split(":");
		if (data.length == 1)
			data = new String[]{"minecraft", data[0]};
		BlockStateJsonWriter writer1 = new BlockStateJsonWriter(new File(file,
				String.format("%s/blockstates/", data[0], data[1])));
		writer1.setBlockState(state);
		writer1.setProvider(provider1);
		for (IModelStateProvider provider2 : providers)
			writer1.addProvider(provider2);
		writer1.write(file, provider);
	}
}