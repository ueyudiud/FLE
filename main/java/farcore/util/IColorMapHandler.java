package farcore.util;

public interface IColorMapHandler
{
	/**
	 * Called when reload resources.<br>
	 * Active only in reload resource.<br>
	 * DO NOT USE THIS METHOD BY 
	 * <code>
	 * FarCore.mod.getColorMapHandler().registerColorMap();
	 * </code>
	 * @param name
	 * @return
	 */
	ColorMap registerColorMap(String name);
	
	/**
	 * Add this provider as resources reload listener.
	 * @param provider
	 */
	void addColorMapProvider(IColorMapProvider provider);
}
