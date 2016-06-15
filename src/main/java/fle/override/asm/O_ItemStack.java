package fle.override.asm;

import static com.sun.org.apache.bcel.internal.Constants.*;

import java.util.Arrays;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class O_ItemStack extends ClassTransformer
{
	public O_ItemStack()
	{
		super("net.minecraft.item.ItemStack", "?");

//		mcpMethods.put("<init>|(Lnet/minecraft/item/Item;II)V", Arrays.asList(
//				new OperationInfo(OperationType.Remove, 1, 84),
//				new OperationInfo(OperationType.Remove, 2, 84),
//				new OperationInfo(OperationType.Replace, 3, 84, 
//						new VarInsnNode(ALOAD, 1),
//						new VarInsnNode(ALOAD, 0),
//						new VarInsnNode(ILOAD, 3),
//						new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/Item", "setDamage", "(Lnet/minecraft/item/ItemStack;I)V", false))));
	}

}
