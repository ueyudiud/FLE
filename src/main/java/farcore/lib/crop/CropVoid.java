package farcore.lib.crop;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.data.M;
import farcore.lib.material.Mat;
import farcore.lib.util.INamedIconRegister;
import net.minecraft.util.IIcon;

public class CropVoid extends CropBase
{
	public CropVoid()
	{
		super(M.VOID);
		maxStage = 1;
	}

	@Override
	public String getLocalName(String dna)
	{
		return "VOID";
	}
	
	@Override
	public long tickUpdate(ICropAccess access)
	{
		return Integer.MAX_VALUE;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcon(INamedIconRegister register) {	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ICropAccess access, INamedIconRegister register)
	{
		return FarCore.voidBlockIcon;
	}
}