package fle.core.energy.electrical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import farcore.util.Direction;
import farcore.util.FleLog;
import flapi.energy.IEleTile;
import flapi.energy.IEleTile.Nodable;
import fle.core.util.Matrix;

public class ElectricalNetLocal
{
	World world;
	private List<Node> dirtyNode = new ArrayList();
	private Map<BlockPos, Node> nodeList = new HashMap();
	private Map<Node, Integer> nodeIndexMap = new HashMap();
	private Routes route = new Routes();
	List<Node> cache = new ArrayList();
	private Map<Node, Double> voltageCache = new HashMap();
	private Matrix matrix = new Matrix();
	private boolean recaculate = false;
	private boolean refind = false;
	
	public ElectricalNetLocal(World world)
	{
		this.world = world;
	}
	
	public void addTileEntity(IEleTile tile)
	{
		Node n;
		BlockPos from;
		nodeList.put(from = tile.getBlockPos(), n = new Node(tile));
        List<Link> neighborMap = new ArrayList();
		Nodable node = tile.getNodes();
        for(Direction dir : Direction.BASIC)
        {
        	BlockPos locate;
        	if(node.isConnectable(dir) && (locate = node.getTransTarget(dir)) != null)
        	{
        		Node node1 = getNode(locate);
        		neighborMap.add(new Link(n, dir, node1, node1.getLinkDirection(from)));
        	}
        }

        for (Link neighbor : neighborMap)
        {
            route.addLink(neighbor);
        }

        mark();
	}
	
	public void removeTileEntity(IEleTile tile)
	{
		route.remove(nodeList.get(tile.getBlockPos()));
		nodeList.remove(tile.getBlockPos());
		mark();
	}
	
	public void rejoinTileEntity(IEleTile tile)
	{
		removeTileEntity(tile);
		addTileEntity(tile);
	}
	
	public void markTileEntity(IEleTile tile)
	{
		recaculate = true;
		dirtyNode.add(nodeList.get(tile.getBlockPos()));
	}
	
	public Node getNode(BlockPos pos)
	{
		return nodeList.get(pos);
	}
	
	private void mark()
	{
		recaculate = refind = true;
	}
	
	public void refresh()
	{
		mark();
		updateWorld();
	}

	public void updateWorld()
	{
		if(recaculate)
		{
			if(refind)
			{
				refind();
			}
			else
			{
				recaculate();
			}
			reset();
			for (Node node : route.nodeSet())
			{
				try
    			{
                    //Call onEnergyNetUpdate()
                    node.tile.onNetUpdate();
                }
    			catch (Exception ignored)
    			{
    				FleLog.addExceptionToCache(ignored);
    			}
			}
		}
		for(Node node : route.nodeSet())
		{
			try
			{
				node.update(this);
			}
			catch(Exception ignored)
			{
				FleLog.addExceptionToCache(ignored);
			}
		}
		FleLog.resetAndCatchException("Fle : Catching exception during updating electrical tile in net.");
	}
	
	private void reset()
	{
		recaculate = refind = false;
		dirtyNode.clear();
	}
	
	public void refind()
	{
		//Clear all cache.
		cache.clear();
		nodeIndexMap.clear();
		voltageCache.clear();
		//Add all route to cache.
		cache.addAll(route.nodeSet());
		int size = cache.size();
		double[] b = new double[size];
		for(int i = 0; i < size; ++i)
		{
			Node node = cache.get(i);
			assert(node != null);
			nodeIndexMap.put(node, i);
			b[i] = node.getBasicCurrent();
			Link[] neighbour = node.getAccessLinks(this);
			for(int r = 0; r < size; ++r)
			{
				matrix.push(getCoefficient(r, neighbour, i, node));
			}
			matrix.enter();
		}
		
		matrix.finalized();
		solveMatrix();
		b = matrix.result;
		
		for(int i = 0; i < size; ++i)
			voltageCache.put(cache.get(i), b[i]);
	}
	
	private void recaculate()
	{
        if (nodeIndexMap.size() == 0) {
            refind();
            return;
        }

        List<Node> updateList = new LinkedList<Node>();
        for (Node c : dirtyNode)
        {
            if (!updateList.contains(c)) updateList.add(c);
            for (Link neighbor : route.link(c))
            {
            	if (!updateList.contains(neighbor.tile1)) updateList.add(neighbor.tile1);
            	if (!updateList.contains(neighbor.tile2)) updateList.add(neighbor.tile2);
            }
        }

        int matrixSize = cache.size();
        double[] b = new double[matrixSize];
        boolean bCalcutated = false;
        for (Node node : updateList)
        {
            int index = nodeIndexMap.get(node);
            matrix.selectY(index);

            List<Link> list;
            Link[] neighborList = (list = route.link(node)).toArray(new Link[list.size()]);
            for (int rowIndex = 0; rowIndex < matrixSize; rowIndex++)
            {
                Node node1 = cache.get(rowIndex);

                //Calculate the right hand side of the matrix in the first traverse
                if (!bCalcutated) b[rowIndex] = node1.getBasicCurrent();

                matrix.push(getCoefficient(rowIndex, neighborList, index, node));
            }
            bCalcutated = true;

            index++;
        }

        solveMatrix();
        b = matrix.result;

        voltageCache.clear();
        for (int i = 0; i < matrixSize; i++) {
            voltageCache.put(cache.get(i), b[i]);
        }
    }
	
	private void solveMatrix()
	{
		try
		{
			if(matrix.solve() == null) throw new RuntimeException("Fle : The matrix require invilid value.");
		}
		catch(RuntimeException e)
		{
			throw e;
		}
	}
	
	private double getCoefficient(int r, Link[] neighbour, int i, Node node)
	{
		double cellData = 0;
		if (i == r)
		{ 
			//Key cell
			//Add neighbor resistance
			for (Link link : neighbour)
			{
				cellData += 1.0D / link.getResistance();  // IConductor next to IConductor
			}
		}
		else
		{
			Node node1 = cache.get(r);
			for(Link link : neighbour)
			{
				if(link.contain(node1))
				{
					cellData = -1.0D / link.getResistance();
					break;
				}
			}
		}
		return cellData;
	}

	public double getVoltage(BlockPos pos)
	{
		if(pos == null) return 0;
		if(!nodeList.containsKey(pos)) return 0;
		double value = voltageCache.get(nodeList.get(pos)).doubleValue();
		return Double.isNaN(value) ? 0 : value;
	}

}