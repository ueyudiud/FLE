package fla.core.block.ore;

import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.chem.OreInfo;
import fla.api.util.FlaValue;
import fla.api.util.SubTag;
import fla.api.world.BlockPos;
import fla.core.FlaItems;
import fla.core.block.BlockBaseOre;

public class BlockOre1 extends BlockBaseOre
{	
	public static final OreInfo[] oreInfo = 
		{
		OreInfo.Cuprite,
		OreInfo.NativeCopper,
		OreInfo.Malachite,
		OreInfo.Azurite,
		OreInfo.Chalcocite,
		OreInfo.Tenorite,
		OreInfo.Chalcopyrite,
		OreInfo.Bornite,
		OreInfo.Tetrahedrite,
		OreInfo.Covellite,
		OreInfo.Enargite
		};
	
	public BlockOre1()
	{
		super(SubTag.BLOCK_HARDNESS.copy(2.0F), SubTag.BLOCK_RESISTANCE.copy(2.0F));
		this.setStepSound(soundTypeStone);
		this.setHarvestLevel("pickaxe", 1, 0);
		this.setHarvestLevel("pickaxe", 2, 1);
		this.setHarvestLevel("pickaxe", 0, 2);
		this.setHarvestLevel("pickaxe", 0, 3);
		this.setHarvestLevel("pickaxe", 2, 4);
		this.setHarvestLevel("pickaxe", 1, 5);
		this.setHarvestLevel("pickaxe", 1, 6);
		this.setHarvestLevel("pickaxe", 2, 7);
		this.setHarvestLevel("pickaxe", 1, 8);
		this.setHarvestLevel("pickaxe", 2, 9);
		this.setHarvestLevel("pickaxe", 1, 10);
	}

	protected int getMaxDamage()
	{
		return 11;
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
    
    @Override
    public int damageDropped(int meta) 
    {
    	return meta;
    }
}