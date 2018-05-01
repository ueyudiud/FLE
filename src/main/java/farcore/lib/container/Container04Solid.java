/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.container;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import farcore.lib.solid.SolidSlot;
import farcore.lib.solid.SolidStackWatcher;
import nebula.common.gui.Container03TileEntity;
import nebula.common.gui.ISlot;
import nebula.common.network.PacketBufferExt;
import nebula.common.tile.IGuiTile;
import nebula.common.tile.TE00Base;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author ueyudiud
 */
public class Container04Solid<T extends TE00Base & IGuiTile> extends Container03TileEntity<T>
{
	public List<SolidSlot> solidSlots = new ArrayList<>(4);
	protected SolidStackWatcher solidStackWatcher = SolidStackWatcher.newSolidStackWatcher(this.solidSlots);
	
	public Container04Solid(T tile, EntityPlayer player)
	{
		super(tile, player);
	}
	
	protected class Initalizer2 extends Initalizer
	{
		protected Initalizer2(Map<String, TransferLocation> map)
		{
			super(map);
		}
		
		@Override
		public <S> void addSlot(ISlot<S> slot)
		{
			if (slot instanceof SolidSlot)
			{
				addSlot((SolidSlot) slot);
			}
			else
			{
				super.addSlot(slot);
			}
		}
		
		@Override
		protected void initalizeContainer(T tile)
		{
			super.initalizeContainer(tile);
			appendLocations(Container04Solid.this.tsPlayerBag, this.bag);
			appendLocations(Container04Solid.this.tsPlayerHand, this.hand);
		}
		
		@Override
		protected void build()
		{
			super.build();
		}
	}
	
	@Override
	protected void initalizeFromTileEntity()
	{
		try
		{
			Initalizer2 initalizer = new Initalizer2(ImmutableMap.of("bag", this.locationBag, "hand", this.locationHand, "player", this.locationPlayer));
			initalizer.initalizeContainer(this.tile);
			initalizer.build();
		}
		catch (Throwable t)
		{
			throw new InternalError(t);
		}
	}
	
	protected SolidSlot addSlotToContainer(SolidSlot slot)
	{
		slot.slotNumber = this.solidSlots.size();
		this.solidSlots.add(slot);
		return slot;
	}
	
	@Override
	protected void serializeAllToPacket(PacketBufferExt buf)
	{
		super.serializeAllToPacket(buf);
		this.solidStackWatcher.serializeAll(buf);
	}
	
	@Override
	protected void deserializeAllFromPacket(PacketBufferExt buf) throws IOException
	{
		super.deserializeAllFromPacket(buf);
		this.solidStackWatcher.deserializeAny(buf);
	}
	
	@Override
	protected boolean updatePacket(PacketBufferExt buf)
	{
		boolean flag = super.updatePacket(buf);
		flag |= this.solidStackWatcher.update(buf);
		return flag;
	}
}
