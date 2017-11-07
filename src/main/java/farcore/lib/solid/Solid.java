package farcore.lib.solid;

import nebula.client.render.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Solid extends SolidAbstract
{
	protected ResourceLocation		location;
	@SideOnly(Side.CLIENT)
	protected TextureAtlasSprite	icon;
	
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
