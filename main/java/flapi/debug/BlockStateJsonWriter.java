package flapi.debug;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonWriter;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;

public class BlockStateJsonWriter
{
	private static JsonWriter $(JsonWriter writer)
	{
		writer.setIndent(" ");
		return writer;
	}
	
	private boolean build;
	private JsonWriter writer;
	
	private IModelLocateProvider provider;
	private List<IModelStateProvider> providers = new ArrayList();
	private BlockState state;
	
	public BlockStateJsonWriter(File file) throws IOException
	{
		this(new BufferedWriter(new FileWriter(file)));
		if (!file.exists())
		{
			file.mkdirs();
		}
	}
	
	public BlockStateJsonWriter(Writer writer)
	{
		this($(new JsonWriter(writer)));
	}
	
	public BlockStateJsonWriter(JsonWriter writer)
	{
		this.writer = writer;
	}
	
	public BlockStateJsonWriter setProvider(IModelLocateProvider provider)
	{
		check();
		this.provider = provider;
		return this;
	}
	
	public BlockStateJsonWriter addProvider(IModelStateProvider provider)
	{
		check();
		this.providers.add(provider);
		return this;
	}
	
	public BlockStateJsonWriter setBlockState(BlockState state)
	{
		check();
		if (state != null)
			throw new IllegalArgumentException(
					"This writer is already contain a block state.");
		this.state = state;
		return this;
	}
	
	public BlockModel apply(IBlockState state)
	{
		if (!this.state.getValidStates().contains(state))
			throw new IllegalArgumentException("Can not indentify state.");
		String locate = provider.apply(state);
		BlockModel model = new BlockModel(locate, state);
		for (IModelStateProvider provider : providers)
			provider.set(state, model);
		return model;
	}
	
	private void check()
	{
		if (build)
			throw new IllegalArgumentException("Can not init two times!");
	}
	
	public void write(File file, IModelProvider provider) throws IOException
	{
		check();
		try
		{
			writer.beginObject();
			build = true;
			writer.name("variants");
			writer.beginObject();
			for (Object stateRaw : this.state.getValidStates())
			{
				IBlockState state = (IBlockState) stateRaw;
				String name = "";
				Set<Entry<IProperty, Comparable>> set = (Set<Entry<IProperty, Comparable>>) state
						.getProperties().entrySet();
				Iterator<Entry<IProperty, Comparable>> itr = set.iterator();
				while (itr.hasNext())
				{
					Entry<IProperty, Comparable> entry = itr.next();
					name += entry.getKey().getName() + ":"
							+ entry.getKey().getName(entry.getValue());
					if (itr.hasNext())
						name += ", ";
				}
				writer.name(name);
				writer.beginObject();
				BlockModel model = apply(state);
				writer.name("model");
				writer.value(model.model);
				if (model.x != 0)
				{
					writer.name("x");
					writer.value(model.x);
				}
				if (model.y != 0)
				{
					writer.name("y");
					writer.value(model.y);
				}
				if (model.uvLock)
				{
					writer.name("uvlock");
					writer.value(model.uvLock);
				}
				writer.endObject();
				String[] data = model.model.split(":");
				if (data.length == 1)
					data = new String[]{"minecraft", data[0]};
				File file2 = new File(file,
						String.format("%s/models/%s.json", data[0], data[1]));
				if (file2.exists())
					continue;
				file2.mkdirs();
				ModelJsonWriter writer2 = new ModelJsonWriter(file2);
				provider.provide(writer2, model);
				writer2.write();
			}
			writer.endObject();
			writer.endObject();
		}
		catch (JsonIOException exception)
		{
			throw new IOException(exception);
		}
	}
	
	public class BlockModel
	{
		final String model;
		final IBlockState state;
		boolean build;
		int x;
		int y;
		boolean uvLock;
		
		public BlockModel(String model, IBlockState state)
		{
			this.model = model;
			this.state = state;
		}
		
		public final IBlockState getState()
		{
			return state;
		}
		
		private void check()
		{
			if (build)
				throw new IllegalArgumentException(
						"Can not init a built model locate.");
		}
		
		public BlockModel lockUV()
		{
			check();
			uvLock = true;
			return this;
		}
		
		public BlockModel rotate(int x, int y)
		{
			check();
			if (x != 0)
				this.x = x * 90;
			if (y != 0)
				this.y = y * 90;
			return this;
		}
		
		public BlockStateJsonWriter build()
		{
			check();
			build = true;
			return BlockStateJsonWriter.this;
		}
	}
}