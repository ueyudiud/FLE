package fle.core.block.debug;

import java.util.ArrayList;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import fle.FLE;
import fle.api.FleValue;
import fle.api.block.BlockHasTile;
import fle.core.te.creative.TileEntityThermal;

public class BlockThermal extends BlockHasTile
{
	public BlockThermal()
	{
		super("debugThermal", Material.iron);
		GameRegistry.registerTileEntity(TileEntityThermal.class, "debugThermal");
	}

	@Override
	public TileEntity createNewTileEntity(World aWorld, int aMeta)
	{
		return new TileEntityThermal();
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			TileEntity tile, int metadata, int fortune)
	{
		return new ArrayList<ItemStack>();
	}
}