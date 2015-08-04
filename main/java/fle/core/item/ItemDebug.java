package fle.core.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.block.IDebugableBlock;
import fle.api.enums.EnumDamageResource;
import fle.api.item.ItemFle;
import fle.api.world.BlockPos;
import fle.api.world.BlockPos.ChunkPos;

public class ItemDebug extends ItemFle
{
	public ItemDebug(String aUnlocalized)
	{
		super(aUnlocalized);
	}
	
	@Override
	public boolean onItemUse(ItemStack aStack, EntityPlayer aPlayer,
			World aWorld, int x, int y, int z,
			int aSide, float xPos, float yPos,
			float zPos)
	{
    	if(aWorld.isRemote)
    	{
        	aPlayer.addChatMessage(new ChatComponentText("This block is named " + aWorld.getBlock(x, y, z).getUnlocalizedName() + "."));
        	BlockPos pos = new BlockPos(aWorld, x, y, z);
        	ChunkPos pos1 = pos.getChunkPos();

        	aPlayer.addChatMessage(new ChatComponentText("Block TYPE is " + pos.getBlock().getClass() + "."));
        	aPlayer.addChatMessage(new ChatComponentText("Block name is " + pos.getBlock().getUnlocalizedName() + ", by id " + Block.getIdFromBlock(pos.getBlock()) + "."));
        	aPlayer.addChatMessage(new ChatComponentText("Metadata: " + pos.getBlockMeta() + "."));
        	aPlayer.addChatMessage(new ChatComponentText("Hardness: " + pos.getBlock().getBlockHardness(aWorld, x, y, z) + "."));
    		if(aWorld.getBlock(x, y, z) instanceof IDebugableBlock)
    		{
    			List<String> tList = new ArrayList();
    			((IDebugableBlock) aWorld.getBlock(x, y, z)).addInfomationToList(aWorld, x, y, z, tList);
    			for(String str : tList)
    			{
    				aPlayer.addChatMessage(new ChatComponentText(str));
    			}
    		}
    	}
		return true;
	}

	@Override
	protected void damageItem(ItemStack stack, EntityLivingBase aUser,
			EnumDamageResource aReource, int aDamage) 
	{
		
	}
	
	@Override
	public void registerIcons(IIconRegister register)
	{
		super.registerIcons(register);
	}
}