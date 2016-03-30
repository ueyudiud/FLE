package fle.load;

import farcore.FarCoreSetup;
import farcore.util.FleLog;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;

public class Langs
{
	public static final String infoTorchMaterial = "info.torch.material";
	public static final String infoTorchBurnTime = "info.torch.burntime";
	public static final String infoTorchWet1 = "info.torch.wet1";
	public static final String infoTorchWet2 = "info.torch.wet2";
	
	public static void init()
	{
		FleLog.getLogger().info("Start register localized names.");
		FarCoreSetup.lang.registerLocal(new ItemStack(BlockItems.torch).getUnlocalizedName(), "Torch");
		
		FarCoreSetup.lang.registerLocal(infoTorchBurnTime, "Burn Time: " + EnumChatFormatting.WHITE + "%d" + EnumChatFormatting.GRAY + " unit");
		FarCoreSetup.lang.registerLocal(infoTorchMaterial, "Material : " + EnumChatFormatting.WHITE + "%s");
		FarCoreSetup.lang.registerLocal(infoTorchWet1, "This torch is wet.");
		FarCoreSetup.lang.registerLocal(infoTorchWet2, "Please dried this torch before light it.");
	}
}