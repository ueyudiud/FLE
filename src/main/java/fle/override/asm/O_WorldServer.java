package fle.override.asm;

import static com.sun.org.apache.bcel.internal.Constants.*;

import java.util.Arrays;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import net.minecraft.client.multiplayer.WorldClient;

public class O_WorldServer extends ClassTransformer
{
	public O_WorldServer()
	{
		super("net.minecraft.world.WorldServer", "mt");
		mcpMethods.put("func_147456_g|()V", Arrays.asList(
				new OperationInfo(OperationType.Replace, 11, 366,
						new FieldInsnNode(GETSTATIC, "farcore/enums/EnumBlock", "ice", "Lfarcore/enums/EnumBlock;"), 
						new MethodInsnNode(INVOKEVIRTUAL, "farcore/enums/EnumBlock", "block", "()Lnet/minecraft/block/Block;", false)),
				new OperationInfo(OperationType.Replace, 9, 371, 
						new FieldInsnNode(GETSTATIC, "farcore/enums/EnumBlock", "snow", "Lfarcore/enums/EnumBlock;"), 
						new MethodInsnNode(INVOKEVIRTUAL, "farcore/enums/EnumBlock", "block", "()Lnet/minecraft/block/Block;", false))));
		mcpMethods.put("g|()V", Arrays.asList(
				new OperationInfo(OperationType.Replace, 11, 366,
						new FieldInsnNode(GETSTATIC, "farcore/enums/EnumBlock", "ice", "Lfarcore/enums/EnumBlock;"), 
						new MethodInsnNode(INVOKEVIRTUAL, "farcore/enums/EnumBlock", "block", "()Lnet/minecraft/block/Block;", false)),
				new OperationInfo(OperationType.Replace, 9, 371, 
						new FieldInsnNode(GETSTATIC, "farcore/enums/EnumBlock", "snow", "Lfarcore/enums/EnumBlock;"), 
						new MethodInsnNode(INVOKEVIRTUAL, "farcore/enums/EnumBlock", "block", "()Lnet/minecraft/block/Block;", false))));
	}

}