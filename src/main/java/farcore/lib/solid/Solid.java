/*
 * copyright© 2016-2017 ueyudiud
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

public abstract class Solid implements IRegisterElement<Solid>
{
	public static final IdAllocatableRegister<Solid> REGISTRY = new IdAllocatableRegister<>(true);
	
	public final IRegisterDelegate<Solid>	delegate;
	private ResourceLocation				name;
	private String							unlocalizedName;
	
	public Solid(String name)
	{
		setRegistryName(name);
		this.delegate = REGISTRY.register(this);
	}
	
	@Override
	public final Class<Solid> getTargetClass()
	{
		return Solid.class;
	}
	
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
	
	public String getUnlocalizedname()
	{
		return "MISSING_UNLOCALIZED_NAME_" + this.unlocalizedName;
	}
	
	public String getLocalizedname()
	{
		return LanguageManager.translateToLocal(getUnlocalizedname() + ".name");
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
