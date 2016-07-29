package farcore.lib.model.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import farcore.lib.model.part.BlockstateInformationContainer;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelLoader implements ICustomModelLoader, IStateMapper
{
	private static final Map<Block, Object> registerCustomBlocks = new HashMap();
	private static final Map<ResourceLocation, BlockstateInformationContainer> map = new HashMap();

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
		ProgressBar bar = ProgressManager.push("Loading farcore custom blockstate", registerCustomBlocks.size());
		Map<Block, List<Exception>> exceptionCache = new HashMap();
		for(Entry<Block, Object> entry : registerCustomBlocks.entrySet())
		{
			boolean loadSuccess = false;
			BlockstateInformationContainer container = null;
			try
			{
				IResource resource = resourceManager.getResource(entry.getKey().getRegistryName());
				InputStream stream = resource.getInputStream();
				container = ModelStateSelector.loadFromStream(stream);
				loadSuccess = true;
			}
			catch(IOException exception)
			{
				U.L.put(exceptionCache, entry.getKey(), exception);
				continue;
			}
			if(!loadSuccess && entry.getValue() instanceof BlockstateInformationContainer)
			{
				container = (BlockstateInformationContainer) entry.getValue();
			}
		}
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn)
	{
		// TODO Auto-generated method stub
		return null;
	}
}