package farcore.alpha.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.alpha.interfaces.INamedIconRegister;

public class ItemSimilar extends ItemBase
{
	protected ItemSimilar(String unlocalized)
	{
		super(unlocalized);
	}
	protected ItemSimilar(String modid, String unlocalized)
	{
		super(modid, unlocalized);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(INamedIconRegister register)
	{
		super.registerIcons(register);
	}
}