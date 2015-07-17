package fla.core.block.ore;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.util.FlaValue;
import fla.api.world.BlockPos;
import fla.core.block.BlockBaseOre;

public class BlockOre1 extends BlockBaseOre
{	
	public BlockOre1()
	{
		this.setStepSound(soundTypeStone);
		this.setHardness(2.0F);
		this.setResistance(2.0F);
		this.setHarvestLevel("pickaxe", 1, 0);
		this.setHarvestLevel("pickaxe", 2, 1);
		this.setHarvestLevel("pickaxe", 0, 2);
		this.setHarvestLevel("pickaxe", 0, 3);
		this.setHarvestLevel("pickaxe", 2, 4);
		this.setHarvestLevel("pickaxe", 1, 5);
	}

	protected int getMaxDamage()
	{
		return 6;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		icons = new IIcon[16];
		for(int i = 0; i < getMaxDamage(); ++i)
		{
			icons[i] = register.registerIcon(FlaValue.TEXT_FILE_NAME + ":ore/" + String.valueOf(i + 1));
		}
	}

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess access, int x, int y, int z)
    {
    	return this.getRenderColor(access.getBlockMetadata(x, y, z));
    }
}
