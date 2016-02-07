package flapi.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.block.interfaces.ITextureSwitcher;
import flapi.block.item.ItemFleMultipassRender;
import flapi.util.Values;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

public class BlockFleMultipassRender extends BlockFle implements ITextureSwitcher
{
	protected BlockFleMultipassRender(Class<? extends ItemFleMultipassRender> clazz, String unlocalized, Material Material)
	{
		super(clazz, unlocalized, Material);
	}
	protected BlockFleMultipassRender(String unlocalized, Material Material)
	{
		super(ItemFleMultipassRender.class, unlocalized, Material);
	}
	
	private boolean rendering;
	private boolean enableOverride;
	private int pass;

	@SideOnly(Side.CLIENT)
	protected boolean hasMultipassRender()
	{
		return false;
	}

	@SideOnly(Side.CLIENT)
	protected int renderingPass()
	{
		return pass;
	}

	@SideOnly(Side.CLIENT)
	public int getRenderPasses(IBlockAccess world, int x, int y, int z)
	{
		return 1;
	}

	@SideOnly(Side.CLIENT)
	public int getRenderPasses(ItemStack stack)
	{
		return 1;
	}

	@SideOnly(Side.CLIENT)
	public final void switchRender(int render)
	{
		rendering = true;
		enableOverride = render > 0;
		pass = render;
	}

	@SideOnly(Side.CLIENT)
	public final void setRenderToDefault()
	{
		rendering = false;
		enableOverride = false;
		pass = 0;
	}
	
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass()
	{
		return hasMultipassRender() && enableOverride ? 
				1 : 0;
	}

	public int getRenderType()
	{
		return hasMultipassRender() && !rendering ? 
				Values.FLE_BASIC_RENDER_ID : 0;
	}
}