package farcore.resource;

import java.util.HashMap;
import java.util.Map;

import farcore.collection.Register;
import flapi.util.BTI;
import flapi.util.ITextureHandler;

public class TileMetaTextureManager implements ITextureHandler<BTI>
{
	private Register<BlockTextureManager> managers = new Register();
	private Map<Integer, Long> iconMap = new HashMap();
	
	public TileMetaTextureManager()
	{
		
	}
	
	public void addTexture(String name, int id, BlockTextureManager manager)
	{
		managers.register(id, manager, name);
		for(int i = 0; i < manager.getLocateSize(); ++i)
		{
			iconMap.put(new Integer(iconMap.size()), new Long((long) id << 16 + i));
		}
	}
	
	@Override
	public int getLocateSize()
	{
		return iconMap.size();
	}

	@Override
	public String getTextureFileName(int index)
	{
		long l = iconMap.get(index);
		return managers.get((int) l >> 16).getTextureFileName((int) (l & 0xFFFFFFFF));
	}

	@Override
	public String getTextureName(int index)
	{
		long l = iconMap.get(index);
		return managers.get((int) l >> 16).getTextureName((int) (l & 0xFFFFFFFF));
	}

	@Override
	public int getIconIndex(BTI infomation)
	{
		return managers.get(infomation.meta).getIconIndex(infomation);
	}
}