/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.common.gui.research;

import java.io.IOException;

import fle.core.research.ResearchGraph;
import nebula.Nebula;
import nebula.common.gui.ContainerBlockPosition;
import nebula.common.gui.IGuiDataReciever;
import nebula.common.network.PacketAbstract.Encoder;
import nebula.common.network.PacketBufferExt;
import nebula.common.network.packet.PacketGuiSyncData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class ContainerResearchGraph extends ContainerBlockPosition implements IGuiDataReciever, Encoder
{
	public ResearchGraph.ResearchInstanceProxy instance;
	
	public ContainerResearchGraph(ResearchGraph.ResearchInstanceProxy instance, EntityPlayer player, World world, BlockPos pos)
	{
		super(player, world, pos);
		this.instance = instance;
	}
	
	@SideOnly(Side.CLIENT)
	public ContainerResearchGraph(ResearchGraph.ResearchInstanceProxy instance, EntityPlayer player, World world, BlockPos pos, boolean unused)
	{
		super(player, world, pos);
		this.instance = instance;
	}
	
	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
	}
	
	public void onGraphChanged()
	{
		Nebula.network.sendToPlayer(new PacketGuiSyncData(this), this.opener);
	}
	
	public void writeData(PacketBufferExt buf) throws IOException
	{
		
	}
	
	@Override
	public void readData(PacketBufferExt buf) throws IOException
	{
		
	}
	
	@Override
	public final void encode(PacketBufferExt output) throws IOException
	{
		writeData(output);
	}
}