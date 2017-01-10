/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.asm;

import java.util.Arrays;

import com.google.common.eventbus.EventBus;

import farcore.FarCore;
import farcore.util.U;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.10.2")
public class FarOverride extends DummyModContainer
{
	public static boolean isFleLoad;
	
	@Instance(FarCore.OVERRIDE_ID)
	public static FarOverride override;
	
	public static final String VERSION = "1.1";
	
	public FarOverride()
	{
		super(new ModMetadata());
		override = this;
		ModMetadata meta = getMetadata();
		meta.modId = FarCore.OVERRIDE_ID;
		meta.name = "Far Override";
		meta.version = VERSION;
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
	}
}