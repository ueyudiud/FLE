/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.load;

import com.google.common.collect.ImmutableMap;

import farcore.FarCore;
import farcore.handler.FarCoreGuiHandler;
import farcore.instances.MaterialTextureLoader;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.lib.model.block.ModelPartRedstoneCircuitPlate;
import farcore.lib.solid.SolidTextureLoader;
import nebula.client.NebulaTextureHandler;
import nebula.client.model.flexible.NebulaModelDeserializer;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.client.render.FontMap;
import nebula.client.render.FontRenderExtend;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class ClientLoader extends CommonLoader
{
	@Override
	public void preload()
	{
		super.preload();
		//Register client side handler.
		MinecraftForge.EVENT_BUS.register(new FarCoreGuiHandler());
		
		NebulaTextureHandler.addIconLoader(new MaterialTextureLoader());
		SolidTextureLoader.init();
		
		/** For uses this font map provided all Greek character. */
		FontRenderExtend.addFontMap(new FontMap(new ResourceLocation(FarCore.ID, "textures/font/greeks.png"), "ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρστυφχψω"));
		//Register model loaders, state mappers and item model selectors.
		NebulaModelDeserializer.registerBlockDeserializer("farcore:circuitplate", ModelPartRedstoneCircuitPlate.DESERIALIZER);
		
		NebulaModelLoader.registerModel(new ResourceLocation(FarCore.ID, "models/block/fence_base"), new ResourceLocation(FarCore.ID, "models/block1/fence.json"), NebulaModelDeserializer.BLOCK);
		
		NebulaModelLoader.registerItemMetaGenerator(new ResourceLocation(FarCore.ID, "material"), stack -> "material:" + ItemMulti.getMaterial(stack).name);
		NebulaModelLoader.registerItemMetaGenerator(new ResourceLocation(FarCore.ID, "material.sub"), stack ->
		"material:" + ItemMulti.getMaterial(stack).name + "." + ItemMulti.getSubMeta(stack));
		
		for(MatCondition condition : MatCondition.register)
		{
			NebulaModelLoader.registerTextureSet(new ResourceLocation(FarCore.ID, "group/" + condition.name), () ->
			{
				ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
				for (Mat material : Mat.filt(condition))
				{
					builder.put("material:" + material.name, new ResourceLocation(material.modid, "items/group/" + condition.name + "/" + material.name));
				}
				return builder.build();
			});
		}
		NebulaModelLoader.registerItemColorMultiplier(new ResourceLocation(FarCore.ID, "material"), stack -> ItemMulti.getMaterial(stack).RGB);
	}
}