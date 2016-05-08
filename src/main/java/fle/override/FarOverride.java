package fle.override;

import java.util.Arrays;
import java.util.List;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import farcore.FarCore;
import farcore.util.FleLog;
import farcore.util.U;
import net.minecraft.item.Item.ToolMaterial;

@IFMLLoadingPlugin.MCVersion("1.7.10")
public class FarOverride extends DummyModContainer
{
	public static boolean isFleLoad;
	
	@Instance(FarCore.OVERRIDE_ID)
	public static FarOverride override;
	
	public FarOverride()
	{
		super(new ModMetadata());
		override = this;
		ModMetadata meta = getMetadata();
		meta.modId = FarCore.OVERRIDE_ID;
		meta.name = "Far Override";
		meta.version = "0.2";
		meta.credits = "ueyudiud";
		meta.authorList = Arrays.asList("ueyudiud");
		meta.description = "Far land era child mod, use to override minecraft objects.";
		meta.screenshots = new String[0];
		meta.logoFile = "";
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller)
	{
		bus.register(this);
		return true;
	}
	
	@EventHandler
	public void load(FMLPreInitializationEvent event)
	{
		isFleLoad = U.Mod.isModLoaded("Far Land Era");
		overrideMaterialProperties();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		
	}
	
	@EventHandler
	public void load(FMLPostInitializationEvent event)
	{
		
	}
	
	@EventHandler
	public void load(FMLLoadCompleteEvent event)
	{
		
	}
	
	public void overrideMaterialProperties()
	{
		List<String> override = Arrays.asList("field_78001_f", "harvestLevel");
		try
		{
			U.Reflect.resetReflectCache();
			U.Reflect.overrideFinalField(ToolMaterial.class, override, ToolMaterial.WOOD, 3, true);
			U.Reflect.overrideFinalField(ToolMaterial.class, override, ToolMaterial.STONE, 10, true);
			U.Reflect.overrideFinalField(ToolMaterial.class, override, ToolMaterial.IRON, 19, true);
			U.Reflect.overrideFinalField(ToolMaterial.class, override, ToolMaterial.GOLD, 4, true);
			U.Reflect.overrideFinalField(ToolMaterial.class, override, ToolMaterial.EMERALD, 102, true);
			U.Reflect.resetReflectCache();
		}
		catch(Throwable throwable)
		{
			FleLog.getCoreLogger().warn("Fail to override tool material properties.", throwable);
		}
	}
}