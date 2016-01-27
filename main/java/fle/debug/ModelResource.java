package fle.debug;

import java.io.File;
import java.io.IOException;

import net.minecraft.block.Block;

import farcore.util.FleLog;
import flapi.debug.IModelLocateProvider;
import flapi.debug.IModelProvider;
import flapi.debug.IModelStateProvider;
import flapi.debug.RenderJsonWriter;

public class ModelResource
{
	private static RenderJsonWriter writer;
	
	public static void add(Block block, IModelLocateProvider provider1,
			IModelProvider provider, IModelStateProvider... providers)
	{
		try
		{
			if (writer == null)
				writer = new RenderJsonWriter(new File("./assets"));
			writer.write(block.getBlockState(), provider1, provider, providers);
		}
		catch (IOException exception)
		{
			FleLog.error(
					"Can not write block model " + block.getUnlocalizedName(),
					exception);
		}
	}
}