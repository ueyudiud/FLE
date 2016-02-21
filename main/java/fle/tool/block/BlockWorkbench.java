package fle.tool.block;

import farcore.block.BlockFle;
import farcore.block.interfaces.IGuiBlock;
import farcore.resource.BlockTextureManager;
import farcore.util.U;
import flapi.enums.EnumWorldNBT;
import flapi.util.BlockTextureHandler;
import flapi.util.FleValue;
import flapi.world.BlockPos;
import fle.FLE;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWorkbench extends BlockFle implements IGuiBlock
{
	protected BlockTextureHandler btm;
	
	public BlockWorkbench(String aName)
	{
		super(aName, Material.wood);
		
		btm = new BlockTextureHandler(new BlockTextureManager(new String[]{"machine/workbench_side", "machine/workbench_top", "machine/workbench_top", "machine/workbench_side"}));
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		setBlockTextureName(FleValue.TEXTURE_FILE + ":" + FleValue.VOID_ICON_FILE);
		super.registerBlockIcons(register);
		btm.registerIcon(register);
	}
	
	@Override
	public void onBlockPlacedBy(World aWorld, int x, int y, int z, EntityLivingBase entity,
			ItemStack aStack)
	{
		FLE.fle.getWorldManager().setData(new BlockPos(aWorld, x, y, z), EnumWorldNBT.Facing, U.N.side(U.W.initFacing(entity)));
		super.onBlockPlacedBy(aWorld, x, y, z, entity, aStack);
	}
	
	@Override
	public IIcon getIcon(IBlockAccess world, int x,
			int y, int z, int side)
	{
		int frontSide = FLE.fle.getWorldManager().getData(new BlockPos(world, x, y, z), EnumWorldNBT.Facing);
		return btm.getIcon(this, world, x, y, z, FleValue.MACHINE_FACING[frontSide][side]);
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return btm.getIcon(this, meta, side);
	}
	
	@Override
	public boolean onBlockActivated(World aWorld, int x, int y, int z, EntityPlayer aPlayer,
			int side, float xPos, float yPos, float zPos)
	{
		if(aWorld.isRemote) return side == 1;
		else
		{
			if(side == 1)
			{
				aPlayer.openGui(FLE.MODID, 0, aWorld, x, y, z);
				return true;
			}
			return false;
		}
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		switch(aWorld.getBlockMetadata(x, y, z))
		{
//		case 0 : return new ContainerWorkbenchTire0(aWorld, x, y, z, aPlayer.inventory);
		default : return null;
		}
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		switch(aWorld.getBlockMetadata(x, y, z))
		{
//		case 0 : return new GuiWorkbenchTire0(aWorld, x, y, z, aPlayer);
		default : return null;
		}
	}
}