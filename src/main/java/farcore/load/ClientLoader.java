package farcore.load;

import farcore.FarCore;
import farcore.handler.FarCoreGuiHandler;
import farcore.lib.entity.EntityFallingBlockExtended;
import farcore.lib.entity.EntityProjectileItem;
import farcore.lib.model.block.ModelFluidBlock;
import farcore.lib.model.entity.RenderFallingBlockExt;
import farcore.lib.model.entity.RenderProjectileItem;
import farcore.lib.model.item.FarCoreItemModelLoader;
import farcore.lib.render.Colormap.ColormapFactory;
import farcore.lib.render.FontRenderExtend;
import farcore.lib.render.instance.FontMap;
import farcore.lib.tesr.TESRCarvedRock;
import farcore.lib.tile.instance.TECustomCarvedStone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientLoader extends CommonLoader
{
	@Override
	public void preload()
	{
		super.preload();
		//Register color map loader.
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(ColormapFactory.INSTANCE);
		//Register client side handler.
		MinecraftForge.EVENT_BUS.register(new FarCoreGuiHandler());
		FontRenderExtend.addFontMap(new FontMap(new ResourceLocation(FarCore.ID, "textures/font/greeks.png"), "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρστυφχψω"));
		//Register entity rendering handlers.
		RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlockExtended.class, RenderFallingBlockExt.Factory.instance);
		RenderingRegistry.registerEntityRenderingHandler(EntityProjectileItem.class, RenderProjectileItem.Factory.instance);
		//Register model loaders, state mappers and item model selectors.
		//The base item model loader.
		ModelLoaderRegistry.registerLoader(FarCoreItemModelLoader.instance);
		//The custom block model loaders.
		ModelLoaderRegistry.registerLoader(ModelFluidBlock.Loader.instance);
		//Register TESR.
		ClientRegistry.bindTileEntitySpecialRenderer(TECustomCarvedStone.class, new TESRCarvedRock());
	}
}