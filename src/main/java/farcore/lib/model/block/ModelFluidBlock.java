package farcore.lib.model.block;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import farcore.FarCore;
import farcore.lib.model.ModelHelper;
import farcore.lib.util.Log;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelFluid;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelFluidBlock
{
	@SideOnly(Side.CLIENT)
	public static enum Loader implements ICustomModelLoader
	{
		instance;

		@Override
		public void onResourceManagerReload(IResourceManager resourceManager){	}

		@Override
		public boolean accepts(ResourceLocation modelLocation)
		{
			return modelLocation instanceof FluidModelLocation;
		}

		@Override
		public IModel loadModel(ResourceLocation modelLocation) throws Exception
		{
			return "inventory".equals(((FluidModelLocation) modelLocation).getVariant()) ?
					ModelHelper.makeItemModel(((FluidModelLocation) modelLocation).fluid.getStill()):
						new ModelFluid(((FluidModelLocation) modelLocation).fluid);
		}
	}

	@SideOnly(Side.CLIENT)
	public static enum Selector implements ICustomItemModelSelector, IStateMapper
	{
		instance;
		
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack)
		{
			Fluid fluid = null;
			try
			{
				fluid = ((BlockFluidBase) Block.getBlockFromItem(stack.getItem())).getFluid();
			}
			catch(Exception exception)
			{
				Log.warn("Fail to find fluid by item " + stack.getItem().getRegistryName().toString());
			}
			if(fluid == null)
			{
				fluid = FluidRegistry.WATER;
			}
			return new FluidModelLocation(fluid, true);
		}
		
		@Override
		public List<ResourceLocation> getAllowedResourceLocations(Item item)
		{
			Fluid fluid = null;
			try
			{
				fluid = ((BlockFluidBase) Block.getBlockFromItem(item)).getFluid();
			}
			catch(Exception exception)
			{
				Log.warn("Fail to find fluid by item " + item.getRegistryName().toString());
			}
			if(fluid == null)
			{
				fluid = FluidRegistry.WATER;
			}
			return ImmutableList.of(new FluidModelLocation(fluid, true));
		}

		@Override
		public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn)
		{
			Fluid fluid = ((BlockFluidBase) blockIn).getFluid();
			if(fluid == null)
			{
				fluid = FluidRegistry.WATER;
			}
			ModelResourceLocation location = new FluidModelLocation(fluid, false);
			ImmutableMap.Builder<IBlockState, ModelResourceLocation> builder = ImmutableMap.builder();
			for(IBlockState state : blockIn.getBlockState().getValidStates())
			{
				builder.put(state, location);
			}
			return builder.build();
		}
	}
	
	@SideOnly(Side.CLIENT)
	private static class FluidModelLocation extends ModelResourceLocation
	{
		private Fluid fluid;

		public FluidModelLocation(Fluid fluid, boolean isItem)
		{
			super(FarCore.INNER_RENDER + ":" + fluid.getName(), isItem ? "inventory" : "normal");
			this.fluid = fluid;
		}
	}
}