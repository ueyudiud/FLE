package fle.override.asm;

import static com.sun.org.apache.bcel.internal.Constants.*;

import java.util.Arrays;

import org.objectweb.asm.tree.FieldInsnNode;

import net.minecraft.client.multiplayer.WorldClient;

public class O_WorldServer extends ClassTransformer
{
	public O_WorldServer()
	{
		super("net.minecraft.world.WorldServer", "mt");
		mcpMethods.put("func_147456_g|()V", Arrays.asList(
				new OperationInfo(OperationType.Replace, 11, 366, 
						new FieldInsnNode(GETSTATIC, "fle/load/BlockItems", "ice", "Lnet/minecraft/block/Block;")),
				new OperationInfo(OperationType.Replace, 9, 371, 
						new FieldInsnNode(GETSTATIC, "fle/load/BlockItems", "snow", "Lnet/minecraft/block/Block;"))));
		mcpMethods.put("g|()V", Arrays.asList(
				new OperationInfo(OperationType.Replace, 11, 366, 
						new FieldInsnNode(GETSTATIC, "fle/load/BlockItems", "ice", "Laji;")),
				new OperationInfo(OperationType.Replace, 9, 371, 
						new FieldInsnNode(GETSTATIC, "fle/load/BlockItems", "snow", "Laji;"))));
	}

}