/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.solid;

import nebula.base.register.IRegisterDelegate;
import nebula.base.register.IRegisterElement;
import nebula.base.register.IdAllocatableRegister;
import nebula.client.render.IIconRegister;
import nebula.common.LanguageManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The basic solid type.
 * @author ueyudiud
 */
public abstract class Solid implements IRegisterElement<Solid>
{
	/**
	 * The registry of solid, allocating network id of solids and store registryName-solid pair.
	 */
	public static final IdAllocatableRegister<Solid> REGISTRY = new IdAllocatableRegister<>(true);
	
	public final IRegisterDelegate<Solid>	delegate;
	private ResourceLocation				name;
	private String							unlocalizedName;
	
	public Solid(String name)
	{
		setRegistryName(name);
		this.delegate = REGISTRY.register(this);
	}
	
	/**
	 * The class to serialize.
	 */
	@Override
	public final Class<Solid> getTargetClass()
	{
		return Solid.class;
	}
	
	/**
	 * Set the registry name of solid.<p>
	 * Also take its <tt>path</tt> name as unlocalized name.<p>
	 * It is already called by constructor, do not call it again.
	 * @see #setUnlocalizedName(String)
	 */
	@Override
	public final void setRegistryName(String name)
	{
		if (this.name != null) throw new IllegalStateException("The name of " + this.name + " already exist!");
		this.name = new ResourceLocation(name);
		this.unlocalizedName = this.name.getResourcePath();
	}
	
	@Override
	public String getRegisteredName()
	{
		return this.name.toString();
	}
	
	public void setUnlocalizedName(String unlocalizedName)
	{
		this.unlocalizedName = unlocalizedName;
	}
	
	public String getUnlocalizedName()
	{
		return this.unlocalizedName == null ? "MISSING_UNLOCALIZED_NAME_" + this.name.getResourcePath() : this.unlocalizedName;
	}
	
	public String getLocalizedName()
	{
		return LanguageManager.translateLocal(getUnlocalizedName() + ".name");
	}
	
	/**
	 * Returned aRGB color of solid stack.
	 * 
	 * @param stack the stack to get color.
	 * @return the aRGB color.
	 */
	public int getColor(SolidStack stack)
	{
		return 0xFFFFFFFF;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcon(IIconRegister register)
	{
	}
	
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTextureName()
	{
		return new ResourceLocation(this.name.getResourceDomain(), "solids/" + this.name.getResourcePath());
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
