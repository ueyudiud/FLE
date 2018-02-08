/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client;

import static net.minecraftforge.client.GuiIngameForge.left_height;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;

import com.google.common.collect.Lists;

import nebula.Log;
import nebula.Nebula;
import nebula.base.A;
import nebula.client.render.IIconLoader;
import nebula.client.render.IIconRegister;
import nebula.client.render.IItemCustomRender;
import nebula.client.util.Renders;
import nebula.common.NebulaConfig;
import nebula.common.foodstat.FoodStatExt;
import nebula.common.util.L;
import nebula.common.util.Players;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public final class NebulaRenderHandler implements IIconLoader
{
	public static final NebulaRenderHandler	INSTNACE			= new NebulaRenderHandler();
	public static boolean					itemRenderingFlag	= false;
	
	public static boolean isItemRendering()
	{
		return itemRenderingFlag;
	}
	
	/**
	 * Register a item use custom renderer instead of model rendering.
	 * 
	 * @param item the item using custom renderer.
	 * @param render the used renderer.
	 * @see nebula.client.render.IItemCustomRender
	 */
	public static void registerRender(Item item, IItemCustomRender render)
	{
		if (INSTNACE.renders.containsKey(item))
		{
			Log.warn("The {} has already has a custom render.", item);
			return;
		}
		INSTNACE.renders.put(item, render);
	}
	
	private final Map<Item, IItemCustomRender> renders = new HashMap<>();
	
	private NebulaRenderHandler()
	{
		NebulaTextureHandler.addIconLoader(this);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public IItemCustomRender getRender(ItemStack stack)
	{
		if (stack.getItem() == null) return null;
		return this.renders.get(stack.getItem());
	}
	
	@Override
	public void registerIcon(IIconRegister register)
	{
		this.renders.values().forEach(L.toConsumer(IItemCustomRender::registerIcon, register));
	}
	
	// Game display HUD rendering start.
	@SubscribeEvent
	public void renderHUD(RenderGameOverlayEvent.Pre event)
	{
		if (NebulaConfig.overrideIngameStat)
		{
			Minecraft mc = Minecraft.getMinecraft();
			int updateCounter = mc.ingameGUI.getUpdateCounter();
			EntityPlayer player = (EntityPlayer) mc.getRenderViewEntity();
			if (event.getType() == ElementType.HEALTH)
			{
				if (NebulaConfig.useExtPlayerStat)// Water stat need render before health rendering.
				{
					renderWaterStat(mc, player, updateCounter, event.getPartialTicks(), event.getResolution());
				}
			}
			else if (event.getType() == ElementType.ARMOR)
			{
				if (ForgeHooks.getTotalArmorValue(player) == 0)
				{
					// If level is 0, the GUI should not left a rendering
					// position to armor, but vanilla rendering did.
					// I don't know is this is a BUG or just a feature.
					left_height -= 10;
				}
			}
			else if (event.getType() == ElementType.EXPERIENCE)
			{
				// event.setCanceled(true);//Move experience information to
				// player state screen.
			}
		}
	}
	
	@SubscribeEvent
	public void renderText(RenderGameOverlayEvent.Text event)
	{
		final Minecraft mc = Minecraft.getMinecraft();
		final EntityPlayer player = Players.player();
		if (player.isCreative())
		{
			
		}
		else
		{
			if (mc.gameSettings.showDebugInfo)
			{
				List<String> left = event.getLeft(), right = event.getRight();
				left.clear();
				right.clear();
				
				long i = Runtime.getRuntime().maxMemory();
				long j = Runtime.getRuntime().totalMemory();
				long k = Runtime.getRuntime().freeMemory();
				long l = j - k;
				if (mc.isReducedDebug())
				{
					right.addAll(A.argument(new Object[] {String.format("Mem: % 2d%% %03d/%03dMB", l * 100L / i, l / (1L << 20), i / (1L << 20))}));
				}
				else
				{
					List<String> list;
					list = Lists.newArrayList(
							String.format("Java: %s %dbit", System.getProperty("java.version"), mc.isJava64bit() ? 64 : 32),
							String.format("Mem: % 2d%% %03d/%03dMB", l * 100L / i, l / (1L << 20), i / (1L << 20)),
							String.format("Allocated: % 2d%% %03dMB", j * 100L / i, j / (1L << 20)),
							"",
							String.format("CPU: %s", OpenGlHelper.getCpu()),
							"",
							String.format("Display: %dx%d (%s)", Display.getWidth(), Display.getHeight(), GlStateManager.glGetString(7936)),
							GlStateManager.glGetString(7937),
							GlStateManager.glGetString(7938));
					right.addAll(list);
					right.add("");
					right.addAll(FMLCommonHandler.instance().getBrandings(false));
				}
				left.add(Minecraft.getDebugFPS() + " FPS");
			}
		}
	}
	
	public static final ResourceLocation LOCATION = new ResourceLocation(Nebula.MODID, "textures/gui/override.png");
	
	private void renderWaterStat(Minecraft mc, EntityPlayer player, int updateCounter, float partialTicks, ScaledResolution resolution)
	{
		mc.mcProfiler.startSection("water");
		
		mc.renderEngine.bindTexture(LOCATION);
		GlStateManager.enableBlend();
		
		int left = resolution.getScaledWidth() / 2 - 91;
		int top = resolution.getScaledHeight() - left_height;
		left_height += 10;
		
		FoodStatExt stats = (FoodStatExt) player.getFoodStats();
		int level = stats.getWaterLevel();
		
		for (int i = 0; i < 10; ++i)
		{
			int idx = i * 2 + 1;
			int x = left + i * 8;
			int y = top;
			byte background = 0;
			
			// if (stats.getWaterLevel() > 10 && updateCounter % (level * 3 + 1)
			// == 0)
			// {
			// y += L.nextInt(3) - 1;
			// }
			
			Renders.drawTexturedModalRect(x, y, background * 9, 0, 9, 9, 0);
			
			if (idx < level)
				Renders.drawTexturedModalRect(x, y, 0, 9, 9, 9, 0);// Full water
			// icon.
			else if (idx == level) Renders.drawTexturedModalRect(x, y, 9, 9, 9, 9, 0);// Half
			// water
			// icon.
		}
		
		mc.renderEngine.bindTexture(GuiIngameForge.ICONS);// Bind back to
		// vanilla texture.
		GlStateManager.disableBlend();// GUI need disable blend.
		
		mc.mcProfiler.endSection();
	}
	
	// Game display HUD rendering end.
}
