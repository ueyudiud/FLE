package fle.core.block.wire;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;
import fle.api.FleValue;
import fle.api.block.BlockHasTile;
import fle.api.material.MaterialAbstract;
import fle.api.util.SubTag;
import fle.core.block.machine.ItemThermalWire;
import fle.core.te.TileEntityThermalWire;

public class BlockThermalWire extends BlockHasTile
{	
	public BlockThermalWire(String aName, String aLocalized)
	{
		super(ItemThermalWire.class, aName, aLocalized, Material.iron);
		GameRegistry.registerTileEntity(TileEntityThermalWire.class, "thermalWire");
	}
	
	@Override
	public int colorMultiplier(IBlockAccess world, int x,
			int y, int z)
	{
		return world.getTileEntity(x, y, z) == null ? 0xFFFFFF : ((TileEntityThermalWire) world.getTileEntity(x, y, z)).getColor();
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public int getRenderType()
	{
		return FleValue.FLE_NOINV_RENDER_ID;
	}

	@Override
	public TileEntity createNewTileEntity(World aWorld, int aMeta)
	{
		return new TileEntityThermalWire();
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			TileEntity tile, int metadata, int fortune)
	{
		ArrayList<ItemStack> list = new ArrayList();
		if(tile instanceof TileEntityThermalWire)
		{
			list.add(ItemThermalWire.a(((TileEntityThermalWire) tile).getMaterial()));
		}
		return list;
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		int i = 0;
		for(MaterialAbstract material : MaterialAbstract.getMaterialRegistry())
		{
			if(material.contain(SubTag.MATERIAL_heatwire))
			{
				list.add(ItemThermalWire.a(material));
				if(i++ > 5) break;
			}
		}
	}
}