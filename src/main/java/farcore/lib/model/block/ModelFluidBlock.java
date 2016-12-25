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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
	public static enum Loader implements IFarCustomModelLoader
	{
		INSTANCE;
		
		@Override
		public String getLoaderPrefix()
		{
			return "fluid.";
		}

		@Override
		public IModel loadModel(ResourceLocation modelLocation) throws Exception
		{
			if (!(modelLocation instanceof ModelResourceLocation))
				return ModelFluid.WATER;
			String key = modelLocation.getResourcePath().substring("fluid.".length());
			Fluid fluid = FluidRegistry.getFluid(key);
			if (fluid == null)
				return ModelFluid.WATER;
			String variant = ((ModelResourceLocation) modelLocation).getVariant();
			switch (variant)
			{
			case "inventory" : return ModelHelper.makeItemModel(fluid.getStill());//Make still icon for item in inventory.
			default : return new ModelFluid(fluid);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static enum Selector implements ICustomItemModelSelector, IStateMapper
	{
		instance;

		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack)
		{
			try
			{
				return getLocationFromFluid(((BlockFluidBase) Block.getBlockFromItem(stack.getItem())).getFluid(), "inventory");
			}
			catch(Exception exception)
			{
				Log.warn("Fail to find fluid by item " + stack.getItem().getRegistryName().toString());
			}
			return new ModelResourceLocation(stack.getItem().getRegistryName(), "inventory");
		}

		@Override
		public List<ResourceLocation> getAllowedResourceLocations(Item item)
		{
			try
			{
				return ImmutableList.of(getLocationFromFluid(((BlockFluidBase) Block.getBlockFromItem(item)).getFluid(), "inventory"));
			}
			catch(Exception exception)
			{
				Log.warn("Fail to find fluid by item " + item.getRegistryName().toString());
			}
			return ImmutableList.of();
		}

		private ModelResourceLocation getLocationFromFluid(Fluid fluid, String variants)
		{
			if(fluid == null)
			{
				fluid = FluidRegistry.WATER;
			}
			return new ModelResourceLocation(FarCore.INNER_RENDER + ":fluid." + fluid.getName(), variants);
		}
		
		@Override
		public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn)
		{
			ModelResourceLocation location = getLocationFromFluid(((BlockFluidBase) blockIn).getFluid(), "normal");
			ImmutableMap.Builder<IBlockState, ModelResourceLocation> builder = ImmutableMap.builder();
			for(IBlockState state : blockIn.getBlockState().getValidStates())
			{
				builder.put(state, location);
			}
			return builder.build();
		}
	}
}