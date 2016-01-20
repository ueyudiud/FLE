package fle.core.render.model;

public class ModelPart
{
	public String[] textures;
	
	/**
	 * Vertexes,
	 * each vertex provide {x, y, z, color, u, v, <unused>} elemtents.
	 */
	private int[][] vertexes;
	
	/**
	 * Facing map, each provide {x1, x2, x3, x4, facing} four point.
	 */
	private int[][] conntection;
	
	public ModelPart()
	{
		
	}
	
	public int[][] getConntection()
	{
		return conntection;
	}
	
	public int[] getVertex(int id)
	{
		return vertexes[id];
	}
}