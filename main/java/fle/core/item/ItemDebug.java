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
import net.minecraftforge.common.util.ForgeDirection;
import fle.FLE;
import fle.api.FleAPI;
import fle.api.FleValue;
import fle.api.block.IDebugableBlock;
import fle.api.energy.IThermalTileEntity;
import fle.api.enums.EnumDamageResource;
import fle.api.item.ItemFle;
import fle.api.util.FleLog;
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
        	aPlayer.addChatMessage(new ChatComponentText("Harvest Level: " + pos.getBlock().getHarvestLevel(pos.getBlockMeta()) + "."));
    		aPlayer.addChatMessage(new ChatComponentText("Hardness: " + pos.getBlock().getBlockHardness(aWorld, x, y, z) + "."));
    		String str1 = "";
        	for(int i = 0; i < 8; ++i)
    			str1 += FLE.fle.getWorldManager().getData(pos, i) + " ";
        	aPlayer.addChatMessage(new ChatComponentText("FWM: " + str1 + "."));
        	if(pos.getBlockTile() instanceof IThermalTileEntity)
        	{
        		IThermalTileEntity tile = (IThermalTileEntity) pos.getBlockTile();
        		aPlayer.addChatMessage(new ChatComponentText(String.format("Tempreture: %s.", FleValue.format_K.format_c(tile.getTemperature(ForgeDirection.VALID_DIRECTIONS[aSide])))));
        		aPlayer.addChatMessage(new ChatComponentText(String.format("Heat Currect: %s.", FleValue.format_MJ.format_c(tile.getThermalEnergyCurrect(ForgeDirection.VALID_DIRECTIONS[aSide])))));
            	
        	}
        	if(pos.getBlock() instanceof IDebugableBlock)
    		{
    			List<String> tList = new ArrayList();
    			((IDebugableBlock) pos.getBlock()).addInfomationToList(aWorld, x, y, z, tList);
    			for(String str : tList)
    			{
    				aPlayer.addChatMessage(new ChatComponentText(str));
    			}
    		}
    	}
    	else
    	{
        	BlockPos pos = new BlockPos(aWorld, x, y, z);
        	ChunkPos pos1 = pos.getChunkPos();

        	FleLog.logger.debug("Block TYPE is " + pos.getBlock().getClass() + ".");
        	FleLog.logger.info("Block name is " + pos.getBlock().getUnlocalizedName() + ", by id " + Block.getIdFromBlock(pos.getBlock()) + ".");
        	FleLog.logger.debug("Metadata: " + pos.getBlockMeta() + ".");
        	FleLog.logger.debug("Hardness: " + pos.getBlock().getBlockHardness(aWorld, x, y, z) + ".");
    		String str1 = "";
        	for(int i = 0; i < 8; ++i)
    			str1 += FLE.fle.getWorldManager().getData(pos, i) + " ";
        	FleLog.logger.info("FWM: " + str1 + ".");
        	if(aWorld.getBlock(x, y, z) instanceof IDebugableBlock)
    		{
    			List<String> tList = new ArrayList();
    			((IDebugableBlock) aWorld.getBlock(x, y, z)).addInfomationToList(aWorld, x, y, z, tList);
    			for(String str : tList)
    			{
    				FleLog.logger.info(str);
    			}
    		}
        }
		return true;
	}
	
	@Override
	public void registerIcons(IIconRegister register)
	{
		super.registerIcons(register);
	}

	@Override
	public void damageItem(ItemStack stack, EntityLivingBase aUser,
			EnumDamageResource aReource, float aDamage)
	{
		
	}
}