package fle.core.init;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.entity.EntityFleFallingBlock;
import farcore.render.block.BlockRenderBase;
import farcore.render.block.CustomRenderHandler;
import farcore.render.block.StandardRenderHandler;
import farcore.render.entity.RenderFleFallingBlock;
import flapi.util.Values;
import fle.core.enums.EnumDirtState;
import fle.init.Blocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraftforge.oredict.OreDictionary;

@SideOnly(Side.CLIENT)
public class Renders
{
	public static Class<? extends BlockRenderBase> RenderStoneChip;
	public static Render RenderThrownStone;
	
	public static void init()
	{
		for(EnumDirtState state : EnumDirtState.values())
		{
			FarCore.registerColorMap(state);
		}
		RenderingRegistry.registerEntityRenderingHandler(EntityFleFallingBlock.class,
				new RenderFleFallingBlock());
		if(Blocks.stoneChip != null)
		{
			CustomRenderHandler.register(Blocks.stoneChip, OreDictionary.WILDCARD_VALUE, RenderStoneChip);
		}
		if(RenderThrownStone != null)
		{
			RenderingRegistry.registerEntityRenderingHandler(
					(Class<? extends Entity>) Entities.EntityThrowStone, 
					RenderThrownStone);
		}
//	    RenderingRegistry.registerEntityRenderingHandler(EntityFleArrow.class, new RenderFleArrow("arrow"));
	}
	
	public static void postInit()
	{
		Values.FLE_BASIC_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
		Values.FLE_CUSTOM_RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new StandardRenderHandler());
    	RenderingRegistry.registerBlockHandler(new CustomRenderHandler(false));
	}
	
//	public static void registerTESR(Class<? extends TESRBase> tesr)
//	{
//		try
//		{
//			ParameterizedType type = (ParameterizedType) tesr.getGenericSuperclass();
//			Class<?> type1 = (Class) type.getActualTypeArguments()[0];
//			if(type1.isAssignableFrom(type1))
//			{
//				ClientRegistry.bindTileEntitySpecialRenderer((Class<? extends TileEntity>) type1, tesr.newInstance());
//			}
//			else throw new RuntimeException("FLE find a class not extends TileEntity!");
//		}
//		catch(Throwable e)
//		{
//			new RuntimeException("FLE fail to register a TESR.", e).printStackTrace();
//		}
//	}
}