package farcore.lib.model.item;

import java.util.ArrayList;
import java.util.List;

import farcore.FarCore;
import farcore.data.EnumItem;
import farcore.lib.model.ModelHelper;
import farcore.lib.model.block.ICustomItemModelSelector;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidRegistry.FluidRegisterEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelDisplayFluid
{
	@SideOnly(Side.CLIENT)
	public enum Loader implements ICustomModelLoader
	{
		instance;
		
		@Override
		public void onResourceManagerReload(IResourceManager resourceManager){	}
		
		@Override
		public boolean accepts(ResourceLocation modelLocation)
		{
			return FarCore.INNER_RENDER.equals(modelLocation.getResourceDomain()) &&
					modelLocation.getResourcePath().startsWith("display_fluid");
		}
		
		@Override
		public IModel loadModel(ResourceLocation modelLocation) throws Exception
		{
			String fluid = modelLocation.getResourcePath().split("/")[1];
			ResourceLocation location = FluidRegistry.getFluid(fluid).getStill();
			return ModelHelper.makeItemModel(location);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public enum Selector implements ICustomItemModelSelector
	{
		instance;
		
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack)
		{
			Fluid fluid = FluidRegistry.getFluid(stack.getItemDamage());
			if(fluid == null)
			{
				fluid = FluidRegistry.WATER;
			}
			return new ModelResourceLocation(FarCore.INNER_RENDER + ":display_fluid/" + fluid.getName(), null);
		}

		private List<ResourceLocation> list = new ArrayList();
		
		@Override
		public List<ResourceLocation> getAllowedResourceLocations(Item item)
		{
			list.add(new ModelResourceLocation(FarCore.INNER_RENDER + ":display_fluid/" + FluidRegistry.WATER.getName(), null));
			list.add(new ModelResourceLocation(FarCore.INNER_RENDER + ":display_fluid/" + FluidRegistry.LAVA.getName(), null));
			List<ResourceLocation> locations = list;
			list = null;
			/**
			 * The fluid might register after this model selector, so use event bus listener instead.
			 */
			return locations;
		}
		
		@SubscribeEvent
		public void onFluidRegister(FluidRegisterEvent event)
		{
			ModelResourceLocation location = new ModelResourceLocation(FarCore.INNER_RENDER + ":display_fluid/" + event.getFluidName(), null);
			if(list != null)
			{
				list.add(location);
			}
			else
			{
				ModelBakery.registerItemVariants(EnumItem.display_fluid.item, location);
			}
		}
	}
}