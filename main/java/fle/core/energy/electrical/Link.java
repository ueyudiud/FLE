package fle.core.energy.electrical;

import farcore.util.Direction;

public class Link
{
	public Node tile1;
	private Direction dir1;
	public Node tile2;
	private Direction dir2;
	
	public Link(Node tile1, Direction dir1, Node tile2, Direction dir2)
	{
		this.tile1 = tile1;
		this.dir1 = dir1;
		this.tile2 = tile2;
		this.dir2 = dir2;
	}
	
	public double getResistance()
	{
		return tile1.nodes.getResistance(dir1) + 
				tile2.nodes.getResistance(dir2);
	}

	public boolean contain(Node node)
	{
		return tile1.equals(node) || tile2.equals(node);
	}
}