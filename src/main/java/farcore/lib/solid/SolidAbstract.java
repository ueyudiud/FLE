/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.solid;

import nebula.client.render.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;
import net.minecraftforge.fml.common.registry.RegistryDelegate;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class SolidAbstract implements IForgeRegistryEntry<SolidAbstract>
{
	public static final ResourceLocation SOILDS = new ResourceLocation("farcore:soild");
	
	public static final IForgeRegistry<SolidAbstract> REGISTRY;
	
	static
	{
		REGISTRY = PersistentRegistryManager.createRegistry(SOILDS, SolidAbstract.class, new ResourceLocation("farcore", "void"), 0, 32768, true, null, null, null);
	}
	
	public final RegistryDelegate<SolidAbstract>	delegate		= PersistentRegistryManager.makeDelegate(this, SolidAbstract.class);
	private ResourceLocation						registryName	= null;
	
	public SolidAbstract()
	{
	}
	
	@Override
	public Class<SolidAbstract> getRegistryType()
	{
		return SolidAbstract.class;
	}
	
	public final SolidAbstract setRegistryName(String name)
	{
		return setRegistryName(new ResourceLocation(name));
	}
	
	public final SolidAbstract setRegistryName(String modid, String name)
	{
		return setRegistryName(new ResourceLocation(modid, name));
	}
	
	@Override
	public final SolidAbstract setRegistryName(ResourceLocation name)
	{
		if (getRegistryName() != null) throw new IllegalStateException("Attempted to set registry name with existing registry name! New: " + name + " Old: " + getRegistryName());
		
		this.registryName = name;
		return this;
	}
	
	@Override
	public final ResourceLocation getRegistryName()
	{
		return this.registryName;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcon(IIconRegister register)
	{
	}
	
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTextureName()
	{
		return new ResourceLocation(getRegistryName().getResourceDomain(), "solids/" + getRegistryName().getResourcePath());
	}
	
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getIcon()
	{
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getIcon(SolidStack stack)
	{
		return getIcon();
	}
}
