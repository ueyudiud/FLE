package fle.override.asm;

import static com.sun.org.apache.bcel.internal.Constants.*;

import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class O_EntityPlayer extends ClassTransformer
{
	public O_EntityPlayer()
	{
		super("net.minecraft.entity.player.EntityPlayer", "yz");
		
		List<OperationInfo> list = 
//				Arrays.asList(new OperationInfo(OperationType.InsertAfter, 4, 175, 
//						new VarInsnNode(ALOAD, 0),
//						new MethodInsnNode(INVOKESTATIC, "fle/override/helper/Player", "initStat", "(Lnet/minecraft/entity/player/EntityPlayer;)V", false)));
				Arrays.asList(
				new OperationInfo(OperationType.Replace, 2, 109, 
						new TypeInsnNode(NEW, "farcore/util/FarFoodStats")),
				new OperationInfo(OperationType.Replace, 4, 109, 
						new MethodInsnNode(INVOKESPECIAL, "farcore/util/FarFoodStats", "<init>", "()V", false)));
		OperationInfo info1;
		mcpMethods.put("<init>|(Lnet/minecraft/world/World;Lcom/mojang/authlib/GameProfile;)V", list);
		list = Arrays.asList(
				new OperationInfo(OperationType.Replace, 2, 105, 
						new TypeInsnNode(NEW, "farcore/util/FarFoodStats")),
				new OperationInfo(OperationType.Replace, 4, 105, 
						new MethodInsnNode(INVOKESPECIAL, "farcore/util/FarFoodStats", "<init>", "()V", false)));
//		list = Arrays.asList(new OperationInfo(OperationType.InsertBefore, 4, 152, 
//				new VarInsnNode(ALOAD, 0),
//				new MethodInsnNode(INVOKESTATIC, "fle/override/helper/Player", "initStat", "(Lyz;)V", false)));
		obfMethods.put("<init>|(Lahb;Lcom/mojang/authlib/GameProfile;)V", list);
		info1 = new OperationInfo(OperationType.InsertBefore, 0, 1840, 
				new VarInsnNode(ALOAD, 0),
				new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Player", "jump", "(Lnet/minecraft/entity/player/EntityPlayer;)V", false));
		mcpMethods.put("jump|()V", Arrays.asList(info1));
		info1 = new OperationInfo(OperationType.InsertBefore, 0, 1637, 
				new VarInsnNode(ALOAD, 0),
				new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Player", "jump", "(Lyz;)V", false));
		obfMethods.put("bj|()V", Arrays.asList(info1));
	}
}