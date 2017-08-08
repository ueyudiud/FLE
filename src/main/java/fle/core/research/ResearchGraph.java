/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.research;

import java.util.Arrays;

import nebula.common.util.A;
import nebula.common.util.L;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class ResearchGraph
{
	public static final int GRAPH_SIZE = 24;
	
	public static class ResearchInstanceProxy
	{
		private boolean isFinished;
		
		public void setFinished(boolean isFinished)
		{
			this.isFinished = isFinished;
		}
		
		public boolean isFinished()
		{
			return this.isFinished;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ResearchInstanceProxyClient extends ResearchInstanceProxy
	{
		public int clickingNode = -1;
		
		EntityPlayer player;
		ResearchInstance instance;
		
		public ResearchInstanceProxyClient(EntityPlayer player, ResearchInstance instance)
		{
			this.instance = instance;
			this.player = player;
		}
		
		@Override
		public boolean isFinished()
		{
			return this.instance.isFinished();
		}
		
		public int getClickingNodeID(int posX, int posY)
		{
			for (int i = 0; i < this.instance.nodeSize; ++i)
			{
				if (this.instance.positions[i][0] == posX && this.instance.positions[i][1] == posY)
				{
					return i;
				}
			}
			return -1;
		}
		
		public void moveNodePos(int id, int x, int y)
		{
			this.instance.moveNodePos(id, x, y);
		}
		
		public void refreshGraph()
		{
			while (this.instance.removeIsolatedNodes());
		}
		
		public int[][] getPositions()
		{
			return this.instance.positions;
		}
		
		public int[][] getLinks()
		{
			return this.instance.links;
		}
	}
	
	public static class ResearchInstance
	{
		int nodeSize;
		int[][] positions;
		int[][] idToLinks;
		int[][] links;
		
		public ResearchInstance (int[][] p, int[][] l)
		{
			this.nodeSize = p.length;
			for (int i = 0; i < p.length; ++i)
			{
				if (!L.inRange(GRAPH_SIZE-1, 0, p[i][0]) || !L.inRange(GRAPH_SIZE-1, 0, p[i][1]))
				{
					throw new IllegalArgumentException("Illegal node : (" + p[i][0] + "," + p[i][1] + ")");
				}
			}
			this.positions = p.clone();
			this.links = l.clone();
			/**
			 * The id to links map buffer.
			 * For each int[] in array,
			 * the first array is buffer current length and afters are datas.
			 */
			int[][] map = new int[p.length][p.length];
			for (int i = 0; i < l.length; ++i)
			{
				int[] is = l[i];
				if (is.length != 2 || !L.inRange(p.length-1, 0, is[0]) || !L.inRange(p.length-1, 0, is[1]) ||
						is[0] == is[1])
					throw new IllegalArgumentException("Illegal link : (" + is[0] + "," + is[1] + ")");
				map[is[0]][++map[is[0]][0]] = i;
				map[is[1]][++map[is[1]][0]] = i;
			}
			this.idToLinks = new int[p.length][];
			for (int i = 0; i < p.length; ++i)
			{
				this.idToLinks[i] = Arrays.copyOfRange(map[i], 1, map[i].length);
			}
		}
		
		public int getNodeSize()
		{
			return this.nodeSize;
		}
		
		public void moveNodePos(int id, int x, int y)
		{
			this.positions[id][0] = x;
			this.positions[id][1] = y;
		}
		
		public boolean isFinished()
		{
			return this.nodeSize == 0;
		}
		
		public boolean removeIsolatedNodes()
		{
			int[] markremove = new int[this.nodeSize+1];
			for (int i = 0; i < this.nodeSize; ++i)
			{
				if (isIsolated(i))
				{
					markremove[++markremove[0]] = i;
				}
			}
			for (int i = 1; i <= markremove[0]; ++i)
			{
				this.positions[markremove[i]] = null;
			}
			refreshNodesAndLinks();
			return markremove[0] > 0;
		}
		
		void refreshNodesAndLinks()
		{
			int[] noderemapid = new int[this.positions.length];
			int[][] posbuf = new int[this.positions.length][];
			int nodebuflen = 0;
			//Relist nodes.
			for (int i = 0; i < this.positions.length; ++i)
			{
				if (this.positions[i] == null)//The node were removed.
				{
					noderemapid[i] = -1;
				}
				else
				{
					noderemapid[i] = nodebuflen;
					posbuf[nodebuflen] = this.positions[i];
					nodebuflen++;
				}
			}
			//Relist links.
			int[] linkremapid = new int[this.links.length];
			int[][] linksbuf = new int[this.links.length][];
			int linkbuflen = 0;
			for (int i = 0; i < this.links.length; ++i)
			{
				if (noderemapid[this.links[i][0]] == -1 || noderemapid[this.links[i][1]] == -1)
				{
					linkremapid[i] = -1;
				}
				else
				{
					linkremapid[i] = linkbuflen;
					linksbuf[linkbuflen] = this.links[i];
					linksbuf[linkbuflen][0] = noderemapid[linksbuf[linkbuflen][0]];
					linksbuf[linkbuflen][1] = noderemapid[linksbuf[linkbuflen][1]];
					linkbuflen++;
				}
			}
			//Remap links.
			int[][] itlbuf = new int[this.idToLinks.length][nodebuflen];
			for (int i = 0; i < this.idToLinks.length; ++i)
			{
				if (noderemapid[i] == -1) continue;
				int[] is = itlbuf[noderemapid[i]];
				for (int j = 0; j < this.idToLinks[i].length; ++j)
				{
					if (linkremapid[this.idToLinks[i][j]] != -1)
					{
						is[++is[0]] = linkremapid[this.idToLinks[i][j]];
					}
				}
				itlbuf[noderemapid[i]] = Arrays.copyOfRange(is, 1, is[0]+1);
			}
			this.nodeSize = nodebuflen;
			this.positions = A.copyToLength(posbuf, nodebuflen);
			this.idToLinks = A.copyToLength(itlbuf, nodebuflen);
			this.links = A.copyToLength(linksbuf, linkbuflen);
		}
		
		public boolean isIsolated(int id)
		{
			int x1 = this.positions[id][0];
			int y1 = this.positions[id][1];
			int x2, y2, x3, y3, x4, y4;
			for (int j : this.idToLinks[id])
			{
				int[] is1 = this.links[j];
				int k = is1[is1[0] == id ? 1 : 0];
				x2 = this.positions[k][0];
				y2 = this.positions[k][1];
				for (int[] is : this.links)
				{
					if (is[0] == id || is[1] == id || is[0] == k || is[1] == k) continue;
					x3 = this.positions[is[0]][0];
					y3 = this.positions[is[0]][1];
					x4 = this.positions[is[1]][0];
					y4 = this.positions[is[1]][1];
					int d1 = (x1-x3)*(y3-y4)+(x4-x3)*(y1-y3);
					int d2 = (x1-x3)*(y1-y2)+(x2-x1)*(y1-y3);
					int d3 = (x1-x2)*(y3-y4)+(x3-x4)*(y2-y1);
					if (d3 != 0 && (d3 > 0 ?
							L.inRange(d3, 0, d1) && L.inRange(d3, 0, d2) :
								L.inRange(0, d3, d1) && L.inRange(0, d3, d2)))
					{
						return false;
					}
				}
			}
			return true;
		}
		
		@Override
		public String toString()
		{
			return "Graph: { pos: " + Arrays.deepToString(this.positions) + ", links: " + Arrays.deepToString(this.links) + "}";
		}
	}
}