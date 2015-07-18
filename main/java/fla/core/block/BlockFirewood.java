package fla.core.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import fla.api.world.BlockPos;
import fla.core.tileentity.TileEntityFirewood;

public class BlockFirewood extends BlockBaseHasTile
{
	private IIcon burningSideIcon;
	private IIcon burningTopIcon;
	private IIcon sideIcon;
	
	public BlockFirewood()
	{
		super(Material.wood);
		setHardness(1.0F);
		setResistance(3.0F);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		blockIcon = register.registerIcon(getTextureName() + "_top");
		sideIcon= register.registerIcon(getTextureName() + "_side");
		burningTopIcon = register.registerIcon(getTextureName() + "_burning_top");
		burningSideIcon= register.registerIcon(getTextureName() + "_burning_side");
	}

	@Override
	public ForgeDirection getBlockDirection(BlockPos pos) 
	{
		return ForgeDirection.UP;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int x)
	{
		return new TileEntityFirewood();
	}

	@Override
	public IIcon getIcon(BlockPos pos, ForgeDirection side) 
	{
		return pos.getBlockMeta() == 1 ? 
				(side == UP || side == DOWN ? burningTopIcon : burningSideIcon) : 
					(side == UP || side == DOWN ? blockIcon : sideIcon);
	}

	@Override
	public IIcon getIcon(int meta, ForgeDirection side) 
	{
		return side == UP || side == DOWN ? blockIcon : sideIcon;
	}

	@Override
	public int getRenderType() 
	{
		return 0;
	}

	@Override
	public boolean isNormalCube() 
	{
		return true;
	}

	@Override
	public boolean hasSubs() 
	{
		return false;
	}

	@Override
	protected boolean canRecolour(World world, BlockPos pos,
			ForgeDirection side, int colour) 
	{
		return false;
	}
}