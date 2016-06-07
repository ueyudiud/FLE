package farcore.energy.electric;

import java.util.ArrayList;
import java.util.List;

import farcore.enums.Direction;
import farcore.interfaces.energy.electric.IElectricTile;
import farcore.interfaces.energy.electric.IElectricTile.Linkable;
import farcore.interfaces.energy.electric.IElectricTile.Nodable;

public class ElectricHelper implements Nodable
{
	private float voltage;
	private Direction direction;
	private int channel;
	private List<Linkable> list = new ArrayList();

	public ElectricHelper(Direction direction)
	{
		this(0F, direction);
	}
	public ElectricHelper(float volatge, Direction direction)
	{
		this(volatge, 0, direction);
	}
	public ElectricHelper(float volatge, int channel, Direction direction)
	{
		this.voltage = volatge;
		this.channel = channel;
		this.direction = direction;
	}
	
	public void addLink(ElectricHelper other, float resistance)
	{
		addLink(other, resistance, true);
	}
	
	private void addLink(ElectricHelper other, float resistance, boolean isSelf)
	{
		list.add(new LinkableHelper(this, other, resistance));
		if(isSelf)
		{
			other.addLink(this, resistance, false);
		}
	}

	@Override
	public int[][] getAccessOffsetLinkPath()
	{
		return new int[][]{new int[]{direction.x, direction.y, direction.z, direction.getOpposite().ordinal()}};
	}

	@Override
	public Direction direction()
	{
		return direction;
	}

	@Override
	public int channel()
	{
		return channel;
	}

	@Override
	public float voltage()
	{
		return voltage;
	}

	@Override
	public Linkable[] getInnerLinks()
	{
		return new Linkable[0];
	}

	@Override
	public float resistanceToOther(IElectricTile tile, Nodable nodable)
	{
		return 5F;
	}
	
	@Override
	public boolean canLinkWith(IElectricTile tile, Nodable nodable)
	{
		return nodable.direction() == this.direction.getOpposite();
	}
	
	private static class LinkableHelper implements Linkable
	{
		Nodable node1;
		Nodable node2;
		float resistance;
		
		public LinkableHelper(Nodable nodable1, Nodable nodable2, float resistance)
		{
			this.node1 = nodable1;
			this.node2 = nodable2;
			this.resistance = resistance;
		}

		@Override
		public Nodable getThis()
		{
			return node1;
		}

		@Override
		public Nodable getThat()
		{
			return node2;
		}

		@Override
		public float resistance() 
		{
			return resistance;
		}
	}
}