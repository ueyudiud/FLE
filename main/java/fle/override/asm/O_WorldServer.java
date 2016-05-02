package fle.override.asm;

import java.util.Arrays;

import org.objectweb.asm.tree.FieldInsnNode;

public class O_WorldServer extends ClassTransformer
{
	public O_WorldServer()
	{
		super("net.minecraft.world.WorldServer", "?");
		mcpMethods.put("func_147456_g|()V", Arrays.asList(
				new OperationInfo(OperationType.Replace, 11, 366, 
						new FieldInsnNode(178, "fle/load/BlockItems", "ice", "Lnet/minecraft/block/Block;")),
				new OperationInfo(OperationType.Replace, 9, 371, 
						new FieldInsnNode(178, "fle/load/BlockItems", "snow", "Lnet/minecraft/block/Block;"))));
	}

}