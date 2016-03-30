package fle.override.asm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

public class O_ItemFlintAndSteel extends ClassTransformer
{
	public O_ItemFlintAndSteel()
	{
		super("net.minecraft.item.ItemFlintAndSteel", "?");
		methods.put("onItemUse|(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;IIIIFFF)Z", 
				Arrays.asList(new OperationInfo(OperationType.Replace, 131, new FieldInsnNode(131, "fle/load/BlockItems", "fire", "Lnet/minecraft/block/Block"))));
	}
}