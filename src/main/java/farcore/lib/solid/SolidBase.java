/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.solid;

import nebula.client.render.IIconRegister;
import nebula.common.LanguageManager;
import nebula.common.util.Game;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SolidBase extends Solid
{
	protected ResourceLocation		location;
	@SideOnly(Side.CLIENT)
	protected TextureAtlasSprite	icon;
	
	protected String unlocalized;
	
	public SolidBase(String name, String localizedName)
	{
		this(Game.getActiveModID(), name, localizedName);
	}
	
	public SolidBase(String modid, String name, String localizedName)
	{
		super(modid + ":" + name);
		this.location = new ResourceLocation(modid, "solids/" + name);
		this.unlocalized = name;
		LanguageManager.registerLocal(getUnlocalizedname() + ".name", localizedName);
	}
	
	@Override
	public String getUnlocalizedname()
	{
		return "solid." + this.unlocalized;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcon(IIconRegister register)
	{
		this.icon = register.registerIcon(getTextureName());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTextureName()
	{
		if (this.location == null)
		{
			this.location = super.getTextureName();
		}
		return this.location;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getIcon()
	{
		return this.icon;
	}
	
	public void setIcon(ResourceLocation location)
	{
		this.location = location;
	}
}
