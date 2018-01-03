/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

import nebula.common.util.Strings;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * @author ueyudiud
 */
class FSItem extends FSObject<Item>
{
	String dormain;
	String path;
	int meta;
	
	FSItem(String name)
	{
		super(null);
		String[] split = Strings.split(name, ':');
		switch (split.length)
		{
		case 1 :
			this.dormain = "minecraft";
			this.path = split[0];
			break;
		case 3 :
			this.meta = Integer.parseInt(split[2]);
		case 2 :
			this.dormain = split[0];
			this.path = split[1];
			break;
		default:
			throw new RuntimeException();
		}
		this.value = Item.REGISTRY.getObject(new ResourceLocation(this.dormain, this.path));
	}
	
	FSItem(Item item)
	{
		super(item);
	}
	
	@Override
	public String asString()
	{
		return this.dormain + ":" + this.path + ":" + this.meta;
	}
}
