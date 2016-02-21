package flapi.event;

import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.common.eventhandler.Event;

/**
 * 
 * @author ueyudiud
 *
 */
public class FluidIconRegisterEvent extends Event
{
	public IIconRegister register;
	
	public FluidIconRegisterEvent(IIconRegister aRegister) 
	{
		register = aRegister;
	}
}
