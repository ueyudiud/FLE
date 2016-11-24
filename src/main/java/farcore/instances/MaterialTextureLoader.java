/*
 * copyright© 2016 ueyudiud
 */

package farcore.instances;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import farcore.data.M;
import farcore.data.MC;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.lib.render.IIconLoader;
import farcore.lib.render.IIconRegister;
import farcore.util.L;
import farcore.util.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class MaterialTextureLoader implements IIconLoader
{
	private static final Map<String, MTFBuilder> SPECIAL_MATERIAL_LOCATION = new HashMap();
	private static final Map<String, List<String>> VARIANTS = new HashMap();
	
	private static final Map<String, TextureAtlasSprite> ICONS = new HashMap();
	
	public static void addIconset(MatCondition condition, String...variant)
	{
		L.put(VARIANTS, condition.name, variant);
	}
	
	public static void addIconLocationVariant(MatCondition condition, String variant, Mat material, ResourceLocation location)
	{
		String key = condition.name + ":" + variant;
		MTFBuilder builder;
		if(SPECIAL_MATERIAL_LOCATION.containsKey(key))
		{
			builder = SPECIAL_MATERIAL_LOCATION.get(key);
		}
		else
		{
			SPECIAL_MATERIAL_LOCATION.put(condition.name, builder = new MTFBuilder(condition, variant));
		}
		builder.map.put(material, location);
	}

	public static void addIconLocationApplier(MatCondition condition, String variant, Function<Mat, ResourceLocation> function)
	{
		String key = condition.name + ":" + variant;
		if(function instanceof MTFBuilder)
		{
			SPECIAL_MATERIAL_LOCATION.put(key, (MTFBuilder) function);
			return;
		}
		MTFBuilder builder;
		if(SPECIAL_MATERIAL_LOCATION.containsKey(key))
		{
			builder = SPECIAL_MATERIAL_LOCATION.get(key);
		}
		else
		{
			SPECIAL_MATERIAL_LOCATION.put(condition.name, builder = new MTFBuilder(condition, variant));
		}
		builder.parent = function;
	}

	public static TextureAtlasSprite getIcon(Mat material, MatCondition condition)
	{
		return getIcon(material, condition, "");
	}
	public static TextureAtlasSprite getIcon(Mat material, MatCondition condition, String variant)
	{
		return getIcon(condition.name + ":" + variant + ":" + material.name);
	}
	
	public static TextureAtlasSprite getIcon(String name)
	{
		return ICONS.getOrDefault(name, Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite());
	}

	@Override
	public void registerIcon(IIconRegister register)
	{
		ICONS.clear();
		for(MatCondition condition : MatCondition.register)
		{
			if(VARIANTS.containsKey(condition.name))
			{
				for(String variant : VARIANTS.get(condition.name))
				{
					Function<Mat, ResourceLocation> function = SPECIAL_MATERIAL_LOCATION.get(condition.name + ":" + variant);
					if(function == null)
					{
						function = getDefaultFunction(condition, variant);
					}
					for(Mat material : Mat.filt(condition))
					{
						ResourceLocation location = function.apply(material);
						ICONS.put(condition.name + ":" + variant + ":" + material.name, register.registerIcon(location));
					}
				}
			}
		}
	}

	public static Function<Mat, ResourceLocation> getDefaultFunction(MatCondition condition, String variant)
	{
		String prefix = "iconsets/" + condition.name + (Strings.validate(variant).length() == 0 ? "" : "/" + variant) + "/";
		return material -> new ResourceLocation(material.modid, prefix + material.name);
	}

	public static class MTFBuilder implements Function<Mat, ResourceLocation>
	{
		private MatCondition condition;
		private String variant;
		private Function<Mat, ResourceLocation> parent;
		private Map<Mat, ResourceLocation> map = new HashMap();

		MTFBuilder(MatCondition condition, String variant)
		{
			this.condition = condition;
			this.variant = variant;
		}
		
		public void build()
		{
			addIconset(condition, variant);
			addIconLocationApplier(condition, variant, this);
		}

		public void put(Function<Mat, ResourceLocation> function)
		{
			parent = function;
		}
		
		public void put(Mat material, ResourceLocation location)
		{
			map.put(material, location);
		}

		@Override
		public ResourceLocation apply(Mat material)
		{
			if(parent == null)
			{
				parent = getDefaultFunction(condition, variant);
			}
			return map.containsKey(material) ? map.get(material) : parent.apply(material);
		}
	}
	
	static
	{
		MTFBuilder builder = new MTFBuilder(MC.stone, "");
		builder.put(material -> new ResourceLocation(material.modid, "blocks/rock/" + material.name + "/resource"));
		builder.put(M.stone, new ResourceLocation("minecraft", "blocks/stone"));
		builder.build();
		builder = new MTFBuilder(MC.cobble, "standard");
		builder.put(material -> new ResourceLocation(material.modid, "blocks/rock/" + material.name + "/cobble"));
		builder.put(M.stone, new ResourceLocation("minecraft", "blocks/cobblestone"));
		builder.build();
		builder = new MTFBuilder(MC.cobble, "mossy");
		builder.put(material -> new ResourceLocation(material.modid, "blocks/rock/" + material.name + "/mossy"));
		builder.put(M.stone, new ResourceLocation("minecraft", "blocks/cobblestone_mossy"));
		builder.put(M.netherrack, new ResourceLocation("minecraft", "blocks/netherrack"));
		builder.build();
		builder = new MTFBuilder(MC.brickBlock, "standard");
		builder.put(material -> new ResourceLocation(material.modid, "blocks/rock/" + material.name + "/brick"));
		builder.put(M.stone, new ResourceLocation("minecraft", "blocks/stonebrick"));
		builder.build();
		builder = new MTFBuilder(MC.brickBlock, "compacted");
		builder.put(material -> new ResourceLocation(material.modid, "blocks/rock/" + material.name + "/compacted"));
		builder.build();
		builder = new MTFBuilder(MC.brickBlock, "crushed");
		builder.put(material -> new ResourceLocation(material.modid, "blocks/rock/" + material.name + "/brick_crushed"));
		builder.put(M.stone, new ResourceLocation("minecraft", "blocks/stonebrick_cracked"));
		builder.build();
		builder = new MTFBuilder(MC.brickBlock, "mossy");
		builder.put(material -> new ResourceLocation(material.modid, "blocks/rock/" + material.name + "/brick_mossy"));
		builder.put(M.stone, new ResourceLocation("minecraft", "blocks/stonebrick_mossy"));
		builder.build();
		builder = new MTFBuilder(MC.brickBlock, "smoothed");
		builder.put(material -> new ResourceLocation(material.modid, "blocks/rock/" + material.name + "/smoothed"));
		builder.put(M.stone, new ResourceLocation("minecraft", "blocks/stonebrick_mossy"));
		builder.build();
		builder = new MTFBuilder(MC.brickBlock, "chiseled");
		builder.put(material -> new ResourceLocation(material.modid, "blocks/rock/" + material.name + "/chiseled"));
		builder.put(M.stone, new ResourceLocation("minecraft", "blocks/stonebrick_carved"));
		builder.build();
	}
}