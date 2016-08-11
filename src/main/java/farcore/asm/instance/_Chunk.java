package farcore.asm.instance;

import static com.sun.org.apache.bcel.internal.Constants.ALOAD;
import static com.sun.org.apache.bcel.internal.Constants.INVOKESTATIC;

import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import farcore.asm.ClassTransformer;

public class _Chunk extends ClassTransformer
{
	public _Chunk()
	{
		super("net.minecraft.world.chunk.Chunk", "apx");
		
		List<OperationInfo> list =
				Arrays.asList(
						new OperationInfo(OperationType.Replace, 3, 1278,
								new VarInsnNode(ALOAD, 1),
								new VarInsnNode(ALOAD, 2),
								new MethodInsnNode(INVOKESTATIC, "farcore/util/U$Worlds", "regetBiome", "(ILnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/BiomeProvider;)Lnet/minecraft/world/biome/Biome;)V", false)));
		mcpMethods.put("getBiome|(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/BiomeProvider;)Lnet/minecraft/world/biome/Biome;", list);
	}
}