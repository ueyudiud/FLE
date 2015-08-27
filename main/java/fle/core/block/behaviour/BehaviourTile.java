package fle.core.block.behaviour;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.block.BlockHasTile;
import fle.api.block.IBlockWithTileBehaviour;
import fle.api.block.IGuiBlock;
import fle.core.block.BlockSubTile;

public abstract class BehaviourTile extends BehaviourBase<BlockSubTile> implements IBlockWithTileBehaviour<BlockSubTile>, IGuiBlock
{
	private Class<? extends TileEntity> clazz;

	public BehaviourTile(Class<? extends TileEntity> tileClass) 
	{
		clazz = tileClass;
		GameRegistry.registerTileEntity(tileClass, tileClass.getName());
	}
	public BehaviourTile(Class<? extends TileEntity> tileClass, boolean flag) 
	{
		clazz = tileClass;
		if(flag)
			GameRegistry.registerTileEntity(tileClass, tileClass.getName());
	}
	
	@Override
	public boolean onBlockActivated(BlockSubTile block, World aWorld, int x,
			int y, int z, EntityPlayer aPlayer, ForgeDirection aSide,
			float xPos, float yPos, float zPos) 
	{
		if(aWorld.isRemote) return true;
		TileEntity tile = aWorld.getTileEntity(x, y, z);
		if(tile != null) 
		{
			aPlayer.openGui(FLE.MODID, 0, aWorld, x, y, z);
			return true;
		}
		return false;
	}

	@Override
	public void onBlockClicked(BlockSubTile block, World aWorld, int x, int y,
			int z, EntityPlayer aPlayer) 
	{
		
	}

	@Override
	public void onBlockPlaced(BlockSubTile block, World aWorld, int x, int y,
			int z, ForgeDirection aSide, float xPos, float yPos, float zPos)
	{
		
	}

	@Override
	public void onBlockBreak(BlockSubTile block, World aWorld, int x, int y,
			int z, Block aBlock, int aMeta)
	{
		
	}

	@Override
	public void onEntityCollidedWithBlock(BlockSubTile block, World aWorld,
			int x, int y, int z, Entity aEntity)
	{
		
	}

	@Override
	public void onFallenUpon(BlockSubTile block, World aWorld, int x, int y,
			int z, Entity aEntity, float aHeight)
	{
		
	}

	@Override
	public void getAdditionalToolTips(BlockSubTile block, List<String> list,
			ItemStack aStack)
	{
		
	}

	@Override
	public TileEntity createNewTileEntity(BlockSubTile block, World aWorld,
			int aMeta)
	{
		try
		{
			return clazz.newInstance();
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			return null;
		}
	}
}