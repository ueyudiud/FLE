package fle.override.asm;

import static com.sun.org.apache.bcel.internal.Constants.*;

import java.util.Arrays;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.entity.player.EntityPlayer;

public class O_EntityPlayer extends ClassTransformer
{
	public O_EntityPlayer()
	{
		super("net.minecraft.entity.player.EntityPlayer", "yz");
		
		mcpMethods.put("<init>|(Lnet/minecraft/world/World;Lcom/mojang/authlib/GameProfile;)V", Arrays.asList(
				new OperationInfo(OperationType.Replace, 2, 109, 
						new TypeInsnNode(NEW, "farcore/util/FarFoodStats")),
				new OperationInfo(OperationType.Replace, 4, 109, 
						new MethodInsnNode(INVOKESPECIAL, "farcore/util/FarFoodStats", "<init>", "()V", false))));
		mcpMethods.put("jump|()V", Arrays.asList(
				new OperationInfo(OperationType.InsertBefore, 0, 1840, 
						new VarInsnNode(ALOAD, 0),
						new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Player", "jump", "(Lnet/minecraft/entity/player/EntityPlayer;)V", false))));
		obfMethods.put("<init>|(Lahb;Lcom/mojang/authlib/GameProfile;)V", Arrays.asList(
				new OperationInfo(OperationType.Replace, 2, 109, 
						new TypeInsnNode(NEW, "farcore/util/FarFoodStats")),
				new OperationInfo(OperationType.Replace, 4, 109, 
						new MethodInsnNode(INVOKESPECIAL, "farcore/util/FarFoodStats", "<init>", "()V", false))));
		obfMethods.put("bj|()V", Arrays.asList(
				new OperationInfo(OperationType.InsertBefore, 0, 1840, 
						new VarInsnNode(ALOAD, 0),
						new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Player", "jump", "(Lyz;)V", false))));
	}
}