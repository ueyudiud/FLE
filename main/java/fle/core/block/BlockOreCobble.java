package fle.core.block;

import java.util.ArrayList;
import java.util.List;

import fle.FLE;
import fle.api.FleValue;
import fle.api.material.MaterialOre;
import fle.api.world.BlockPos;
import fle.core.block.behaviour.BehaviourBase;
import fle.core.block.behaviour.BehaviourTile;
import fle.core.te.TileEntityOreCobble;
import fle.core.util.BlockTextureManager;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOreCobble extends BlockSubTile
{
	public BlockOreCobble init()
	{
		boolean flag = true;
		for(MaterialOre ore : MaterialOre.getOres())
		{
			registerSub(MaterialOre.getOreID(ore), ore.getOreName().toLowerCase(), new BlockTextureManager("void"), new BehaviourTile(TileEntityOreCobble.class, flag)
			{
				public Container openContainer(World aWorld, int x, int y,
						int z, EntityPlayer aPlayer) {return null;}

				public GuiContainer openGui(World aWorld, int x, int y, int z,
						EntityPlayer aPlayer) {return null;}
			});
			flag = false;
		}
		return this;
	}
	
	public BlockOreCobble(String aName)
	{
		super(ItemOreCobble.class, aName, Material.rock);
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return true;
	}
	
	@Override
	public int getRenderType()
	{
		return FleValue.FLE_RENDER_ID;
	}
	
	@Override
	public int getRenderPasses()
	{
		return 2;
	}
	
	IIcon blockIcon1;
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		blockIcon = register.registerIcon(FleValue.TEXTURE_FILE + ":iconsets/ore_cobble");
		blockIcon1 = register.registerIcon(FleValue.TEXTURE_FILE + ":iconsets/ore_cobble2");
	}
	
	@Override
	public IIcon getIcon(int aPass, BlockPos aPos, int aSide)
	{
		return aPass == 0 ? Blocks.cobblestone.getBlockTextureFromSide(aSide) : aPass == 1 ? blockIcon : blockIcon1;
	}
	
	@Override
	public IIcon getIcon(int aPass, int aMeta, int aSide)
	{
		return aPass == 0 ? Blocks.cobblestone.getBlockTextureFromSide(aSide) : aPass == 1 ? blockIcon : blockIcon1;
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		return Blocks.cobblestone.getIcon(world, x, y, z, side);
	}

	@Override
	public IIcon getIcon(int side, int meta)
	{
		return Blocks.cobblestone.getIcon(side, meta);
	}

	public byte renderFlag = 2;
	
	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z)
	{
		if(renderFlag == 2) return 0xFFFFFF;
		try
		{
			return ((TileEntityOreCobble) world.getTileEntity(x, y, z)).getOre().getPropertyInfo().getColors()[renderFlag];
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			return 0xFFFFFF;
		}
	}
	
	@Override
	public int getHarvestLevel(int metadata)
	{
		return 0;
	}
	
	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		return 1F + getOre(world, x, y, z).getPropertyInfo().getHardness();
	}
	
	@Override
	public boolean onBlockActivated(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer, int aSide, float xPos, float yPos, float zPos)
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World aWorld, int aMeta)
	{
		return new TileEntityOreCobble();
	}
	
	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			int metadata, int fortune)
	{
		return ((TileEntityOreCobble) tileThread.get()).getDrops();
	}
	
	public static void setOre(IBlockAccess world, int x, int y, int z, int meta)
	{
		FLE.fle.getWorldManager().setData(new BlockPos(world, x, y, z), 0, meta);
	}
	
	public static MaterialOre getOre(IBlockAccess world, int x, int y, int z)
	{
		return MaterialOre.getOreFromID(FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), 0));
	}
	
	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList)
	{
		TileEntityOreCobble tile = (TileEntityOreCobble) aWorld.getTileEntity(x, y, z);
		if(tile != null)
		{
			if(tile.isSmelting())
			{
				aList.add("Smelted Progress : " + FleValue.format_progress.format_c(tile.getProgress() * 100D));
			}
		}
		super.addInfomationToList(aWorld, x, y, z, aList);
	}
}