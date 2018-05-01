/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.solid;

import java.io.IOException;
import java.util.List;

import farcore.lib.inventory.SolidStackHandler;
import nebula.common.gui.StacksWatcher;
import nebula.common.network.PacketBufferExt;

/**
 * @author ueyudiud
 */
public class SolidStackWatcher extends StacksWatcher<SolidStack>
{
	public static SolidStackWatcher newSolidStackWatcher(List<? extends SolidSlot> slots)
	{
		return new SolidStackWatcher(slots);
	}
	
	SolidStackWatcher(List<? extends SolidSlot> slots)
	{
		super(SolidStackHandler.SOLIDSTACK_HANDLER, slots);
	}
	
	@Override
	protected void serialize(PacketBufferExt buf, SolidStack value)
	{
		SolidStack.BS.write(buf, value);
	}
	
	@Override
	protected SolidStack deserialize(PacketBufferExt buf) throws IOException
	{
		return SolidStack.BS.read(buf);
	}
}
