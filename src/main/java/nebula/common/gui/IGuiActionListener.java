/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.gui;

/**
 * The GUI action listener.
 * @see nebula.common.network.packet.PacketGuiAction
 * @see nebula.client.gui.GuiContainerBase#sendGuiData(int,long,boolean)
 * @author ueyudiud
 */
public interface IGuiActionListener
{
	void onRecieveGUIAction(byte type, long value);
}
