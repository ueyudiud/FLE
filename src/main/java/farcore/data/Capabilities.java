package farcore.data;

import farcore.lib.tile.IItemHandlerIO;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class Capabilities
{
	//Pre-pre initialization
	static
	{
		CapabilityManager.INSTANCE.register(IItemHandlerIO.class, new IItemHandlerIO.ItemHandlerIOStorage(), IItemHandlerIO.Instance.class);
	}
	
	@CapabilityInject(IItemHandlerIO.class)
	public static Capability<IItemHandlerIO> ITEM_HANDLER_IO;
}