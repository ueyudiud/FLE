package fle.core.energy.electrical;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.BlockPos;
import farcore.util.Direction;
import flapi.energy.ElectricalEnergyPacket;
import flapi.energy.IEleTile;
import flapi.energy.IEleTile.Nodable;
import flapi.energy.IElectricalVoltageProvider;

public class Node
{
	public IEleTile tile;
	Nodable nodes;
	
	public Node(IEleTile tile)
	{
		this.tile = tile;
		this.nodes = tile.getNodes();
	}
		
	public Direction getLinkDirection(BlockPos from)
	{
		if(from == null) return Direction.UNKNOWN;
		BlockPos pos;
		for(Direction dir : Direction.BASIC)
		{
			pos = nodes.getTransTarget(dir);
			if(from.equals(pos)) return dir;
		}
		return Direction.UNKNOWN;
	}
	
	public double getResitance(BlockPos to)
	{
		Direction dir;
		return (dir = getLinkDirection(to)) == Direction.UNKNOWN ?
				Double.MAX_VALUE :
					nodes.getResistance(dir);
	}
	
	public Link[] getAccessLinks(ElectricalNetLocal net)
	{
		List<Link> ret = new ArrayList();
		for(Direction dir : Direction.values())
		{
			BlockPos p = nodes.getTransTarget(dir);
			Node node = net.getNode(p);
			ret.add(new Link(this, dir, node, node.getLinkDirection(tile.getBlockPos())));
		}
		return ret.toArray(new Link[ret.size()]);
	}

	public boolean isConnect(BlockPos pos)
	{
		return nodes.isConnect(getLinkDirection(pos));
	}
	
	public boolean isConnectValid(BlockPos pos)
	{
		Direction dir;
		return (dir = getLinkDirection(pos)) == Direction.UNKNOWN ? 
				false :
					nodes.isConnectable(dir);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Node ? 
				((Node) obj).tile.getBlockPos().equals(tile.getBlockPos()) : false;
	}

	public double getBasicCurrent()
	{
		return tile instanceof IElectricalVoltageProvider ? ((IElectricalVoltageProvider) tile).getVoltage() / nodes.getResistance(Direction.UNKNOWN) : 0;
	}

	public void update(ElectricalNetLocal local)
	{
		int i = 0;
		double I = 0;
		double V = local.getVoltage(tile.getBlockPos());
		for(Direction dir : Direction.values())
		{
			if(nodes.isConnect(dir))
			{
				BlockPos pos = nodes.getTransTarget(dir);
				I += Math.abs(V - local.getVoltage(pos)) / getResitance(pos);
				i++;
			}
		}
		if(i > 0 && V > 0 && I > 0)
		{
			tile.onElectricalUpdate(new ElectricalEnergyPacket(V, I / i));
		}
	}
}