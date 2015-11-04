package fle.api.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * The action handler of a block.
 * Register in sub block.
 * @author ueyudiud
 *
 * @param <E> The class this behavior belong to.
 */
public interface IBlockBehaviour<E extends Block>
{
    public boolean onBlockActivated(E block, World aWorld, int x, int y, int z, EntityPlayer aPlayer, ForgeDirection aSide, float xPos, float yPos, float zPos);

    public boolean canBlockStay(E block, World aWorld, int x, int y, int z);
    
    public void onBlockClicked(E block, World aWorld, int x, int y, int z, EntityPlayer aPlayer);

    public void onBlockPlaced(E block, World aWorld, int x, int y, int z, ForgeDirection aSide, float xPos, float yPos, float zPos);

    public void onBlockPlacedBy(E block, World aWorld, int x, int y, int z, ItemStack aStack, EntityLivingBase aEntity);
   
    public void onBlockBreak(E block, World aWorld, int x, int y, int z, Block aBlock, int aMeta);
    
    public void onEntityCollidedWithBlock(E block, World aWorld, int x, int y, int z, Entity aEntity);

    public void onFallenUpon(E block, World aWorld, int x, int y, int z, Entity aEntity, float aHeight);

	public void getAdditionalToolTips(E block, List<String> list, ItemStack aStack);
	
	public void onRenderUpdate(E block, World aWorld, int x, int y, int z, Random aRand);
	
	public ArrayList<ItemStack> getHarvestDrop(E block, World aWorld, int aMeta, TileEntity aTile, int aFotune);
}