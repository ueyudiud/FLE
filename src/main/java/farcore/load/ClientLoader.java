package farcore.load;

import com.google.common.collect.ImmutableMap;

import farcore.FarCore;
import farcore.handler.FarCoreGuiHandler;
import farcore.handler.FarCoreTextureHandler;
import farcore.instances.MaterialTextureLoader;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import nebula.client.model.ColorMultiplier;
import nebula.client.model.FlexibleItemSubmetaGetterLoader;
import nebula.client.model.FlexibleTextureSet;
import nebula.client.render.FontMap;
import nebula.client.render.FontRenderExtend;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientLoader extends CommonLoader
{
	@Override
	public void preload()
	{
		super.preload();
		//Register client side handler.
		MinecraftForge.EVENT_BUS.register(new FarCoreGuiHandler());
		MinecraftForge.EVENT_BUS.register(new FarCoreTextureHandler());
		
		FarCoreTextureHandler.addIconLoader(new MaterialTextureLoader());
		
		FontRenderExtend.addFontMap(new FontMap(new ResourceLocation(FarCore.ID, "textures/font/greeks.png"), "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρστυφχψω"));
		//Register model loaders, state mappers and item model selectors.
		FlexibleItemSubmetaGetterLoader.registerSubmetaGetter(new ResourceLocation(FarCore.ID, "material"), stack -> "material:" + ItemMulti.getMaterial(stack).name);
		
		for(MatCondition condition : MatCondition.register)
		{
			FlexibleTextureSet.registerTextureSetApplier(new ResourceLocation(FarCore.ID, "group/" + condition.name), () ->
			{
				ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
				for (Mat material : Mat.filt(condition))
				{
					builder.put("material:" + material.name, new ResourceLocation(material.modid, "items/group/" + condition.name + "/" + material.name));
				}
				return builder.build();
			});
		}
		ColorMultiplier.registerColorMultiplier(new ResourceLocation(FarCore.ID, "material"), stack -> ItemMulti.getMaterial(stack).RGB);
	}
}