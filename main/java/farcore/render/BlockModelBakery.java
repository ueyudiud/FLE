package farcore.render;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;

import farcore.util.FleLog;

/**
 * 
 * @author ueyudiud
 *
 */
@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class BlockModelBakery implements ICustomModelLoader
{
	private static Set<ResourceLocation> blockSet = new HashSet();
	
	public BlockModelBakery()
	{
		ModelLoaderRegistry.registerLoader(this);
	}
	
	private IResourceManager manager;
	
	@Override
	public void onResourceManagerReload(IResourceManager manager)
	{
		this.manager = manager;
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return blockSet.contains(modelLocation);
	}

	@Override
	public IModel loadModel(ResourceLocation location) throws IOException
	{
        String s = location.getResourcePath();

        Object object;

        if (s.startsWith("builtin/"))
        {
            String s1 = s.substring("builtin/".length());
            String s2 = null;

            if (s2 == null)
            {
                throw new FileNotFoundException(location.toString());
            }

            object = new StringReader(s2);
        }
        else
        {
            IResource iresource = this.manager.getResource(getModelLocation(location));
            object = new InputStreamReader(iresource.getInputStream(), Charsets.UTF_8);
        }

        ModelBlock modelblock1;

        try
        {
            ModelBlockFLE modelblock = null;//ModelBlockFLE.deserialize((Reader) object);
            //modelblock.name = location.toString();
            modelblock1 = modelblock;
        }
        finally
        {
            ((Reader)object).close();
        }

        return null; 
	}	
	
    protected ResourceLocation getModelLocation(ResourceLocation location)
    {
        return new ResourceLocation(location.getResourceDomain(), "models/" + location.getResourcePath() + ".json");
    }
	
	public static class ModelBlockFLE extends ModelBlock
	{
	    public static ModelBlock deserialize(Reader reader)
	    {
	        return (ModelBlockFLE)SERIALIZER.fromJson(reader, ModelBlockFLE.class);
	    }
	    
		public static final String NO_TEXTURE = TextureMap.LOCATION_MISSING_TEXTURE.getResourceDomain();
		static final Gson SERIALIZER = new GsonBuilder()
				.registerTypeAdapter(ModelBlockFLE.class, BlockDeserializer.newBlockModelDeserializer())
				.registerTypeAdapter(BlockPart.class, BlockDeserializer.newBlockPartDeserializer())
				.registerTypeAdapter(BlockPartFace.class, BlockDeserializer.newBlockFaceDeserializer())
				.registerTypeAdapter(BlockFaceUV.class, BlockDeserializer.newBlockFaceUVDeserializer())
				.registerTypeAdapter(ItemTransformVec3f.class, BlockDeserializer.newItemTransformVec3fDeserializer())
				.registerTypeAdapter(ItemCameraTransforms.class, BlockDeserializer.newItemCameraTransformsDeserializer())
				.create();
		protected ModelBlockFLE parent;
	    protected final List elements;
	    protected final boolean gui3d;
	    protected final boolean ambientOcclusion;
	    protected ItemCameraTransforms cameraTransforms;
	    
	    protected ModelBlockFLE(List list, Map map,
				boolean ambientOcclusion, boolean gui3d,
				ItemCameraTransforms ict)
		{
			super(list, map, ambientOcclusion, gui3d, ict);
			this.elements = list;
			this.ambientOcclusion = ambientOcclusion;
			this.gui3d = gui3d;
			this.cameraTransforms = ict;
		}

		protected ModelBlockFLE(ResourceLocation locate, Map map,
				boolean ambientOcclusion, boolean gui3d,
				ItemCameraTransforms ict)
		{
			super(locate, map, ambientOcclusion, gui3d, ict);
			this.elements = Collections.emptyList();
			this.ambientOcclusion = ambientOcclusion;
			this.gui3d = gui3d;
			this.cameraTransforms = ict;
		}

		public ModelBlockFLE(ResourceLocation locate, List list,
				Map map, boolean ambientOcclusion, boolean gui3d,
				ItemCameraTransforms ict)
		{
			super(locate, list, map, ambientOcclusion, gui3d, ict);
			this.elements = list;
			this.ambientOcclusion = ambientOcclusion;
			this.gui3d = gui3d;
			this.cameraTransforms = ict;
		}
		
		private static ThreadLocal<ItemCameraTransforms> cache = new ThreadLocal<ItemCameraTransforms>();
		
		private static ItemCameraTransforms cached(ModelBlock model)
		{
			ItemCameraTransforms item = new ItemCameraTransforms(
					model.getThirdPersonTransform(), 
					model.getFirstPersonTransform(), 
					model.getHeadTransform(),
					model.getInGuiTransform());
			cache.set(item);
			return item;
		}
		
		private ModelBlockFLE(ModelBlock model)
		{
			super(model.getParentLocation(), model.getElements(), model.textures, model.isAmbientOcclusion(), model.isGui3d(), 
					cached(model));
			elements = model.getElements();
			ambientOcclusion = model.isAmbientOcclusion();
			gui3d = model.isGui3d();
			cameraTransforms = cache.get();
		}
		
		@Override
		public String resolveTextureName(String name)
		{
			return resolveTextureName("#" + name, new Bookkeep());
		}
		
		@Override
		public boolean isTexturePresent(String name)
		{
			return !NO_TEXTURE.equals(resolveTextureName(name));
		}
		
		@Override
		public List<BlockPart> getElements()
		{
			return super.getElements();
		}
		
		@Override
		public void getParentFromMap(Map map)
		{
			super.getParentFromMap(map);
			if(super.parent != null)
			{
				this.parent = new ModelBlockFLE(super.parent);
			}
		}
		
		protected boolean hasParent()
		{
			return parent != null;
		}
		
		protected String resolveTextureName(String name, Bookkeep keep)
	    {
			String n;
	        if (isParentModifier(name))
	        {
	            if (this == keep.modelExt)
	            {
	                FleLog.warn("Unable to resolve texture due to upward reference: " + name + " in " + this.name);
	                return NO_TEXTURE;
	            }
	            else
	            {
	                n = (String) this.textures.get(name.substring(1));

	                if (n == null && hasParent())
	                {
	                    n = parent.resolveTextureName(name, keep);
	                }
	                else if(n != null && isParentModifier(n))
	                {
	                	keep.modelExt = this;
	                    n = parent.resolveTextureName(n, keep);
	                }

	                return n != null && !isParentModifier(n) && !isSubModifier(n) ? n : NO_TEXTURE;
	            }
	        }
	        else if(isSubModifier(name))
	        {
                n = keep.model.resolveTextureName(name, keep);
                return n != null && !isParentModifier(n) && !isSubModifier(n) ? n : NO_TEXTURE;
	        }
	        else
	        {
	            return name;
	        }
	    }

	    protected boolean isSubModifier(String name)
	    {
	        return name.charAt(0) == '@';
	    }

	    protected boolean isParentModifier(String name)
	    {
	        return name.charAt(0) == '#';
	    }
		
		final class Bookkeep
	    {
	        public final ModelBlockFLE model;
	        public ModelBlockFLE modelExt;

	        private Bookkeep()
	        {
	            this.model = ModelBlockFLE.this;
	        }
	    }
	}
}