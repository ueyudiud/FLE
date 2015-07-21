package fla.core.world;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fla.api.energy.heat.IHeatManager;
import fla.api.energy.heat.IHeatTileEntity;
import fla.api.world.BlockPos;
import fla.core.Fla;

public class HeatManager implements IHeatManager
{
	@Override
	public int getHeat(BlockPos pos) 
	{
		if(pos.getBlockTile() instanceof IHeatTileEntity)
		{
			IHeatTileEntity tile = (IHeatTileEntity) pos.getBlockTile();
			return tile.getHeatCorrect(ForgeDirection.UNKNOWN);
		}
		Block block = pos.getBlock();
		if(block == Blocks.air)
		{
			return 0;
		}
		if(block == Blocks.torch)
		{
			return 10;
		}
		if(block == Blocks.fire)
		{
			return 50;
		}
		if(block == Blocks.furnace)
		{
			TileEntityFurnace tile = (TileEntityFurnace) pos.getBlockTile();
			return tile.isBurning() ? 200 : 0;
		}
		if(block == Blocks.netherrack || block == Blocks.nether_brick || block == Blocks.soul_sand)
		{
			return 20;
		}
		return 0;
	}

	@Override
	public int getHeatAsk(BlockPos pos) 
	{
		return 0;
	}

	@Override
	public void emmitHeat(World world, BlockPos pos, ForgeDirection to, int pkg) 
	{
		ForgeDirection a = to.getOpposite();
		BlockPos pos1 = pos.toPos(to);
		int ask = getHeatAsk(pos1);

		if(pos.getBlockTile() instanceof IHeatTileEntity)
		{
			((IHeatTileEntity) pos.getBlockTile()).catchHeat(to, -pkg);
		}
		if(pos1.getBlockTile() instanceof IHeatTileEntity)
		{
			((IHeatTileEntity) pos1.getBlockTile()).catchHeat(a, pkg - 1);
		}
		if(pos1.getBlockTile() instanceof TileEntityFurnace)
		{
			if(!((TileEntityFurnace) pos1.getBlockTile()).isBurning())
			{
				if(((TileEntityFurnace) pos1.getBlockTile()).currentItemBurnTime == 0)
				{
					((TileEntityFurnace) pos1.getBlockTile()).currentItemBurnTime = 100;
				}
				((TileEntityFurnace) pos1.getBlockTile()).furnaceBurnTime += Math.max(2, pkg / 10 - 5);
			}
		}
	}
}