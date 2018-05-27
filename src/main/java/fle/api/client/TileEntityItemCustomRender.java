/*
 * copyright 2016-2018 ueyudiud
 */
package fle.api.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nebula.client.NebulaTextureHandler;
import nebula.client.render.IIconLoader;
import nebula.client.render.IIconRegister;
import nebula.common.util.L;
import nebula.common.world.ICoord;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class TileEntityItemCustomRender implements IIconLoader
{
	@SideOnly(Side.CLIENT)
	public static interface IRender extends IIconLoader
	{
		boolean access(ItemStack stack);
		
		void render(ICoord tile, ItemStack stack);
	}
	
	public static final TileEntityItemCustomRender SIMPLY_KILN = new TileEntityItemCustomRender();
	
	private final List<IRender> list = new ArrayList<>();
	
	{
		NebulaTextureHandler.addIconLoader(this);
	}
	
	public void registerItemRender(IRender render)
	{
		this.list.add(render);
	}
	
	public void registerIcon(IIconRegister register)
	{
		for (IRender render : this.list)
		{
			render.registerIcon(register);
		}
	}
	
	public boolean renderContainItem(ItemStack stack, ICoord tile)
	{
		Optional<IRender> render = this.list.stream().filter(L.toPredicate(IRender::access, stack)).findFirst();
		if (render.isPresent())
		{
			render.get().render(tile, stack);
			return true;
		}
		return false;
	}
}
