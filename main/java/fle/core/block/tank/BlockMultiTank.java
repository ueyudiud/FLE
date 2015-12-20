package fle.core.block.tank;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import flapi.FleAPI;
import flapi.block.interfaces.IDebugableBlock;
import flapi.block.interfaces.IGuiBlock;
import flapi.block.old.BlockHasTile;
import flapi.util.FleValue;
import fle.FLE;
import fle.core.gui.ContainerMTInterface;
import fle.core.gui.ContainerMTSoak;
import fle.core.gui.GuiMTInterface;
import fle.core.gui.GuiMTSoak;
import fle.core.te.tank.TileEntityMultiTank;
import fle.core.te.tank.TileEntityMultiTankIO;
import fle.core.te.tank.TileEntityMultiTankInterface;
import fle.core.te.tank.TileEntityMultiTankSoak;
import fle.core.util.TankBlockInfo;

public class BlockMultiTank extends BlockHasTile implements IDebugableBlock, IGuiBlock
{
	private IIcon[] icons;
	
	public BlockMultiTank(String aName)
	{
		super(ItemTankBlock.class, aName, Material.rock);
		lightOpacity = 0;
		setResistance(1.0F);
		GameRegistry.registerTileEntity(TileEntityMultiTank.class, "tankMulti");
		GameRegistry.registerTileEntity(TileEntityMultiTankIO.class, "tankMultiIO");
		GameRegistry.registerTileEntity(TileEntityMultiTankInterface.class, "tankMultiInterface");
		GameRegistry.registerTileEntity(TileEntityMultiTankSoak.class, "tankMultiSoak");
		FleAPI.langManager.registerLocal(getUnlocalizedName() + ":0.name", "Tank Block");
		FleAPI.langManager.registerLocal(getUnlocalizedName() + ":1.name", "Tank Valve");
		FleAPI.langManager.registerLocal(getUnlocalizedName() + ":2.name", "Tank Interface");
		FleAPI.langManager.registerLocal(getUnlocalizedName() + ":3.name", "Tank Soak Pool");
	}
	
	@Override
	public int getRenderType()
	{
		return FleValue.FLE_RENDER_ID;
	}
	
	@Override
	public float getBlockHardness(World aWorld, int x,
			int y, int z)
	{
		TileEntity tile = aWorld.getTileEntity(x, y, z);
		if(tile instanceof TileEntityMultiTank)
		{
			return ((TileEntityMultiTank) tile).getTankMaterial().getBlockHardness(aWorld, x, y, z);
		}
		return 1.0F;
	}

	@Override
	public TileEntity createNewTileEntity(World aWorld, int aMeta)
	{
		switch(aMeta & 15)
		{
		case 0 : return new TileEntityMultiTank();
		case 1 : return new TileEntityMultiTankIO();
		case 2 : return new TileEntityMultiTankInterface();
		case 3 : return new TileEntityMultiTankSoak();
		}
		return null;
	}
	
	@Override
	public boolean onBlockActivated(World aWorld, int x,
			int y, int z, EntityPlayer aPlayer,
			ForgeDirection side, float xPos, float yPos,
			float zPos)
	{
		switch(aWorld.getBlockMetadata(x, y, z))
		{
		case 2 : 
		{

			if(aWorld.getTileEntity(x, y, z) instanceof TileEntityMultiTankInterface)
			{
				if(!((TileEntityMultiTankInterface) aWorld.getTileEntity(x, y, z)).isMultiTank()) return true;
				if(aWorld.isRemote) return true;
				aPlayer.openGui(FLE.MODID, 0, aWorld, x, y, z);
			}
			return true;
		}
		case 3 : 
		{

			if(aWorld.getTileEntity(x, y, z) instanceof TileEntityMultiTankSoak)
			{
				if(!((TileEntityMultiTankSoak) aWorld.getTileEntity(x, y, z)).isMultiTank()) return true;
				if(aWorld.isRemote) return true;
				aPlayer.openGui(FLE.MODID, 0, aWorld, x, y, z);
			}
			return true;
		}
		}
		return false;
	}
	
	@Override
	public void onNeighborBlockChange(World aWorld, int x, int y, int z,
			Block block)
	{
		super.onNeighborBlockChange(aWorld, x, y, z, block);
		TileEntity tile = aWorld.getTileEntity(x, y, z);
		if(tile instanceof TileEntityMultiTank)
		{
			((TileEntityMultiTank) tile).onNeibourChange(true);
		}
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			TileEntity tile, int metadata, int fortune)
	{
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		if(tile instanceof TileEntityMultiTank)
			ret.add(new ItemStack(this, 1, metadata + (((TileEntityMultiTank) tile).getInfoData() << 4)));
		return ret;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		icons = new IIcon[4];
		icons[0] = register.registerIcon(getTextureName() + "_side");
		icons[1] = register.registerIcon(getTextureName() + "_valve");
		icons[2] = register.registerIcon(getTextureName() + "_interface");
		icons[3] = register.registerIcon(getTextureName() + "_soak_pool");
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return icons[(meta % 16) % icons.length];
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab,
			List list)
	{
		int i = 0;
		for(TankBlockInfo info : ItemTankBlock.register)
		{
			list.add(ItemTankBlock.a(ItemTankBlock.register.name(info), 0));
			list.add(ItemTankBlock.a(ItemTankBlock.register.name(info), 1));
			list.add(ItemTankBlock.a(ItemTankBlock.register.name(info), 2));
			list.add(ItemTankBlock.a(ItemTankBlock.register.name(info), 3));
			if(i++ > 3) break;
		}
	}

	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z,
			List aList)
	{
		TileEntity tile = aWorld.getTileEntity(x, y, z);
		if (tile instanceof TileEntityMultiTank)
		{
			TileEntityMultiTank tank = (TileEntityMultiTank) tile;
			aList.addAll(tank.getDebugInfo());
			aList.add("Material is " + new ItemStack(tank.getTankMaterial(), 1, tank.getTankMaterialMeta()).getDisplayName());
		}
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		switch(aWorld.getBlockMetadata(x, y, z))
		{
		case 2 : return new ContainerMTInterface(aPlayer.inventory, (TileEntityMultiTankInterface) aWorld.getTileEntity(x, y, z));
		case 3 : return new ContainerMTSoak(aPlayer.inventory, (TileEntityMultiTankSoak) aWorld.getTileEntity(x, y, z));
		}
		return null;
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		switch(aWorld.getBlockMetadata(x, y, z))
		{
		case 2 : return new GuiMTInterface(aPlayer, (TileEntityMultiTankInterface) aWorld.getTileEntity(x, y, z));
		case 3 : return new GuiMTSoak(aPlayer, (TileEntityMultiTankSoak) aWorld.getTileEntity(x, y, z));
		}
		return null;
	}
	
	@Override
	public boolean hasSubs()
	{
		return true;
	}
}