package farcore.render.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.util.Values;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class ItemTextureHandler
{
	private final int renderPass;
	private final String fileName;
	private final String[] textureNames;
	private IIcon[] icons;

	public ItemTextureHandler(String file, String[] strings)
	{
		this.renderPass = strings.length;
		this.fileName = file;
		this.textureNames = strings;
	}
	public ItemTextureHandler(String...strings)
	{
		this(Values.TEXTURE_FILE, strings);
	}

	@SideOnly(Side.CLIENT)
	public int getRenderPass()
	{
		return renderPass;
	}

	@SideOnly(Side.CLIENT)
	public void onIconRegister(IIconRegister register)
	{
		icons = new IIcon[renderPass];
		for(int i = 0; i < renderPass; ++i)
		{
			icons[i] = register.registerIcon(fileName + ":" + textureNames[i]);
		}
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int pass)
	{
		return icons[pass % renderPass];
	}
}