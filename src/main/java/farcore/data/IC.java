package farcore.data;

import java.util.HashMap;
import java.util.Map;

import farcore.lib.block.instance.BlockRock.RockType;
import farcore.lib.material.Mat;
import farcore.lib.render.IIconLoader;
import farcore.lib.render.IIconRegister;
import farcore.lib.util.SubTag;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class IC implements IIconLoader
{
	public static final IC INSTANCE = new IC();
	
	public static final Map<Mat, Map<RockType, TextureAtlasSprite>> ROCK_ICONS = new HashMap();
	
	private IC(){}

	@Override
	public void registerIcon(IIconRegister register)
	{
		ROCK_ICONS.clear();
		for(Mat material : Mat.filt(SubTag.ROCK))
		{
			Map<RockType, TextureAtlasSprite> map = new HashMap();
			for(RockType type : RockType.values())
			{
				map.put(type, register.registerIcon(material.modid, "blocks/rock/" + material.name + "/" + type.getName()));
			}
			ROCK_ICONS.put(material, map);
		}
	}
}