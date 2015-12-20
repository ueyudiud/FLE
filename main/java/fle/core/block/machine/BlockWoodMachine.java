package fle.core.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import flapi.block.interfaces.IBlockWithTileBehaviour;
import flapi.block.interfaces.IGuiBlock;
import fle.core.block.BlockSubTile;
import fle.core.block.behaviour.BehaviourBarrel;
import fle.core.block.behaviour.BehaviourPolish;
import fle.core.util.BlockTextureManager;

public class BlockWoodMachine extends BlockSubTile implements IGuiBlock
{
	public BlockWoodMachine init()
	{
		registerSub(0, "polishTable", "Polish Table", new BlockTextureManager(new String[]{"machine/polish_side", "machine/polish_top", "machine/polish_down", "machine/polish_side"}), new BehaviourPolish());
		registerSub(1, "barrel", "Barrel", new BlockTextureManager(new String[]{"machine/barrel_top", "machine/barrel_side"}), new BehaviourBarrel());
		return this;
	}
	
	public BlockWoodMachine(String aName)
	{
		super(aName, Material.wood);
		setResistance(1.0F);
		setHardness(1.0F);
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		IBlockWithTileBehaviour<BlockSubTile> behaviour = blockBehaviors.get(getDamageValue(aWorld, x, y, z));
		if(behaviour instanceof IGuiBlock)
		{
			return ((IGuiBlock) behaviour).openContainer(aWorld, x, y, z, aPlayer);
		}
		return null;
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		IBlockWithTileBehaviour<BlockSubTile> behaviour = blockBehaviors.get(getDamageValue(aWorld, x, y, z));
		if(behaviour instanceof IGuiBlock)
		{
			return ((IGuiBlock) behaviour).openGui(aWorld, x, y, z, aPlayer);
		}
		return null;
	}
}