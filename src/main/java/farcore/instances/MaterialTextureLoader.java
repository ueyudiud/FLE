/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.instances;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.ImmutableMap;

import farcore.FarCore;
import farcore.data.EnumRockType;
import farcore.data.M;
import farcore.data.MC;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.client.render.IIconLoader;
import nebula.client.render.IIconRegister;
import nebula.common.util.L;
import nebula.common.util.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The material texture loader.
 * 
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class MaterialTextureLoader implements IIconLoader
{
	private static final Map<String, MTFBuilder>	SPECIAL_MATERIAL_LOCATION	= new HashMap<>();
	private static final Map<String, List<String>>	VARIANTS					= new HashMap<>();
	
	private static final Map<String, TextureAtlasSprite> ICONS = new HashMap<>();
	
	public static void addIconset(MatCondition condition, String...variants)
	{
		L.put(VARIANTS, condition.name, variants);
		for (String variant : variants)
		{
			NebulaModelLoader.registerTextureSet(
					new ResourceLocation(FarCore.ID, "material/" + condition.name + "/" + variant),
					() -> {
						ImmutableMap.Builder<String, ResourceLocation> builder = ImmutableMap.builder();
						for (Mat material : Mat.materials())
						{
							builder.put(material.name, getResource(material, condition, variant));
						}
						return builder.build();
					});
		}
	}
	
	public static void addIconLocationVariant(MatCondition condition, String variant, Mat material, ResourceLocation location)
	{
		String key = condition.name + ":" + variant;
		MTFBuilder builder;
		if (SPECIAL_MATERIAL_LOCATION.containsKey(key))
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
		if (function instanceof MTFBuilder)
		{
			SPECIAL_MATERIAL_LOCATION.put(key, (MTFBuilder) function);
			return;
		}
		MTFBuilder builder;
		if (SPECIAL_MATERIAL_LOCATION.containsKey(key))
		{
			builder = SPECIAL_MATERIAL_LOCATION.get(key);
		}
		else
		{
			SPECIAL_MATERIAL_LOCATION.put(condition.name, builder = new MTFBuilder(condition, variant));
		}
		builder.parent = function;
	}
	
	public static ResourceLocation getResource(Mat material, MatCondition condition, String variant)
	{
		Function<Mat, ResourceLocation> function = SPECIAL_MATERIAL_LOCATION.get(condition.name + ":" + variant);
		if (function == null)
		{
			return new ResourceLocation(getPrefix(condition, variant) + material.name);
		}
		else
		{
			return function.apply(material);
		}
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
		for (MatCondition condition : MatCondition.register)
		{
			if (VARIANTS.containsKey(condition.name))
			{
				for (String variant : VARIANTS.get(condition.name))
				{
					String compute = condition.name + ":" + variant;
					Function<Mat, ResourceLocation> function = SPECIAL_MATERIAL_LOCATION.get(compute);
					if (function == null)
					{
						function = getDefaultFunction(condition, variant);
					}
					for (Mat material : Mat.filt(condition))
					{
						ICONS.put(compute + ":" + material.name, register.registerIcon(function.apply(material)));
					}
				}
			}
		}
	}
	
	private static String getPrefix(MatCondition condition, String variant)
	{
		return "iconsets/" + condition.name + (Strings.validate(variant).length() == 0 ? "" : "/" + variant) + "/";
	}
	
	private static Function<Mat, ResourceLocation> getDefaultFunction(MatCondition condition, String variant)
	{
		String prefix = getPrefix(condition, variant);
		return material -> new ResourceLocation(material.modid, prefix + material.name);
	}
	
	public static class MTFBuilder implements Function<Mat, ResourceLocation>
	{
		private MatCondition					condition;
		private String							variant;
		private Function<Mat, ResourceLocation>	parent;
		private Map<Mat, ResourceLocation>		map	= new HashMap<>();
		
		MTFBuilder(MatCondition condition, String variant)
		{
			this.condition = condition;
			this.variant = variant;
		}
		
		public void build()
		{
			addIconset(this.condition, this.variant);
			addIconLocationApplier(this.condition, this.variant, this);
		}
		
		public void put(String prefix, String postfix)
		{
			put(material -> new ResourceLocation(material.modid, prefix + material.name + postfix));
		}
		
		public void put(Function<Mat, ResourceLocation> function)
		{
			this.parent = function;
		}
		
		public void put(Mat material, ResourceLocation location)
		{
			this.map.put(material, location);
		}
		
		@Override
		public ResourceLocation apply(Mat material)
		{
			if (this.parent == null) this.parent = getDefaultFunction(this.condition, this.variant);
			return this.map.containsKey(material) ? this.map.get(material) : this.parent.apply(material);
		}
	}
	
	static
	{
		MTFBuilder builder;
		
		builder = new MTFBuilder(MC.plankBlock, "");
		builder.put(M.oak, new ResourceLocation("minecraft", "blocks/planks_oak"));
		builder.put(M.spruce, new ResourceLocation("minecraft", "blocks/planks_spruce"));
		builder.put(M.birch, new ResourceLocation("minecraft", "blocks/planks_birch"));
		builder.put(M.ceiba, new ResourceLocation("minecraft", "blocks/planks_jungle"));
		builder.put(M.acacia, new ResourceLocation("minecraft", "blocks/planks_acacia"));
		builder.put(M.oak_black, new ResourceLocation("minecraft", "blocks/planks_big_oak"));
		builder.build();
		
		for (EnumRockType type : EnumRockType.values())
		{
			if (type == EnumRockType.cobble_art) continue;
			builder = new MTFBuilder(type.condition, type.variant);
			builder.put("blocks/rock/", "/" + type.name());
			switch (type)
			{
			case resource:
				builder.put(M.stone, new ResourceLocation("minecraft", "blocks/stone"));
				break;
			case cobble:
				builder.put(M.stone, new ResourceLocation("minecraft", "blocks/cobblestone"));
				builder.put(M.whitestone, new ResourceLocation("minecraft", "blocks/end_stone"));
				break;
			case mossy:
				builder.put(M.stone, new ResourceLocation("minecraft", "blocks/cobblestone_mossy"));
				builder.put(M.netherrack, new ResourceLocation("minecraft", "blocks/netherrack"));
				break;
			case brick:
				builder.put(M.stone, new ResourceLocation("minecraft", "blocks/stonebrick"));
				break;
			case brick_crushed:
				builder.put(M.stone, new ResourceLocation("minecraft", "blocks/stonebrick_cracked"));
				break;
			case brick_mossy:
				builder.put(M.stone, new ResourceLocation("minecraft", "blocks/stonebrick_mossy"));
				break;
			case smoothed:
				builder.put(M.stone, new ResourceLocation("minecraft", "blocks/stonebrick_mossy"));
				break;
			case chiseled:
				builder.put(M.stone, new ResourceLocation("minecraft", "blocks/stonebrick_carved"));
				break;
			case brick_compacted:
			default:
				break;
			}
			builder.build();
		}
	}
}
