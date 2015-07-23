package fla.core.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import fla.api.FlaAPI;
import fla.api.chem.Matter;
import fla.api.tech.Technology;
import fla.api.world.BlockPos;
import fla.api.world.BlockPos.ChunkPos;

public class ItemDebug extends Item
{
    public boolean onItemUse(ItemStack i, EntityPlayer p, World w, int x, int y, int z, int s, float xP, float yP, float zP)
    {
    	if(w.isRemote)
    	{
        	p.addChatMessage(new ChatComponentText("This block is named " + w.getBlock(x, y, z).getUnlocalizedName() + "."));
        	p.addChatMessage(new ChatComponentText("World Manager:"));
        	BlockPos pos = new BlockPos(w, x, y, z);
        	ChunkPos pos1 = pos.getChunkPos();

        	p.addChatMessage(new ChatComponentText("Block TYPE is " + pos.getBlock().getClass() + "."));
        	p.addChatMessage(new ChatComponentText("Block name is " + pos.getBlock().getUnlocalizedName() + ", by id " + Block.getIdFromBlock(pos.getBlock()) + "."));
        	p.addChatMessage(new ChatComponentText("Metadata: " + pos.getBlockMeta() + "."));
        	p.addChatMessage(new ChatComponentText("Hardness: " + pos.getBlock().getBlockHardness(w, x, y, z) + "."));

        	p.addChatMessage(new ChatComponentText("T:"));
        	for(Technology tech : FlaAPI.techManager.getPlayerInfo(p).getPlayerTechList())
        	{
        		p.addChatMessage(new ChatComponentText(tech.getName()));
        	}
    	}
    	return false;
    }
}
