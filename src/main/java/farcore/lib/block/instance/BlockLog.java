package farcore.lib.block.instance;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.block.BlockBase;
import farcore.lib.block.ItemBlockBase;
import farcore.lib.tree.ITree;
import farcore.lib.util.INamedIconRegister;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLog extends BlockBase
{
	protected ITree information;
	
	protected BlockLog(ITree information, String name)
	{
		super(name, Material.wood);
		this.information = information;
	}
	
	@Override
	public String getHarvestTool(int metadata)
	{
		return "axe";
	}
	
	@Override
	public int getHarvestLevel(int metadata)
	{
		return 0;
	}
	
	@Override
	public String getTranslateNameForItemStack(int metadata)
	{
		return getUnlocalizedName() + "@" + (metadata >> 2);
	}
	
	@Override
	public boolean isToolEffective(String type, int metadata)
	{
		return "axe".equals(type);
	}
	
	@SideOnly(Side.CLIENT)
	protected void registerIcon(INamedIconRegister register)
	{
		information.registerLogIcon(register);
	}
	
	@SideOnly(Side.CLIENT)
	protected IIcon getIcon(int side, int meta, INamedIconRegister register)
	{
		return information.getLogIcon(register, meta, side);
	}
	
	@Override
	public int damageDropped(int meta)
	{
		return meta & (~0x3);
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ, int meta)
	{
		meta &= (~0x3);
//		if(side == 0 || side == 1)
//		{
//			meta |= 0x0;
//		}
//		else 
			if(side == 2 || side == 3)
		{
			meta |= 0x1;
		}
		else if(side == 4 || side == 5)
		{
			meta |= 0x2;
		}
		return meta;
	}
	
	@Override
	public boolean isWood(IBlockAccess world, int x, int y, int z)
	{
		return true;
	}
}