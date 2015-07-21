package fla.core.block.cobble;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import fla.api.util.FlaValue;
import fla.api.util.SubTag;
import fla.core.block.BlockCobbleRock;
import fla.core.item.ItemSub;

public class BlockCobble1 extends BlockCobbleRock
{
	public BlockCobble1() 
	{
		super(SubTag.BLOCK_HARDNESS.copy(0.5F), SubTag.BLOCK_RESISTANCE.copy(1.0F));
		registerItem(0, ItemSub.a("flint_a", 4));
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		for(int i = 0; i < getMaxDamage(); ++i)
		{
			this.icons[i] = register.registerIcon(FlaValue.TEXT_FILE_NAME + ":chip/" + String.valueOf(i + 1));
		}
	}

	@Override
	protected int getMaxDamage() 
	{
		return 1;
	}
}
