/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DLOAD;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.FSTORE;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFGE;
import static org.objectweb.asm.Opcodes.IFGT;
import static org.objectweb.asm.Opcodes.IFLE;
import static org.objectweb.asm.Opcodes.IFLT;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.IFNONNULL;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.IF_ACMPEQ;
import static org.objectweb.asm.Opcodes.IF_ACMPNE;
import static org.objectweb.asm.Opcodes.IF_ICMPEQ;
import static org.objectweb.asm.Opcodes.IF_ICMPGE;
import static org.objectweb.asm.Opcodes.IF_ICMPGT;
import static org.objectweb.asm.Opcodes.IF_ICMPLE;
import static org.objectweb.asm.Opcodes.IF_ICMPLT;
import static org.objectweb.asm.Opcodes.IF_ICMPNE;
import static org.objectweb.asm.Opcodes.IINC;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INSTANCEOF;
import static org.objectweb.asm.Opcodes.INVOKEDYNAMIC;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.JSR;
import static org.objectweb.asm.Opcodes.LDC;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LOOKUPSWITCH;
import static org.objectweb.asm.Opcodes.LSTORE;
import static org.objectweb.asm.Opcodes.MULTIANEWARRAY;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.NEWARRAY;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RET;
import static org.objectweb.asm.Opcodes.SIPUSH;
import static org.objectweb.asm.Opcodes.TABLESWITCH;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import nebula.common.util.Jsons;
import net.minecraftforge.fml.relauncher.IFMLCallHook;

/**
 * @author ueyudiud
 */
public class NebulaSetup implements IFMLCallHook
{
	/**
	 * The ASM file version, uses to determine if it need replaced ASM files.
	 */
	private static final int VERSION = 16;
	
	final JsonDeserializer<OpInfo>				DESERIALIZER1	= (json, typeOfT, context) -> {
		if (!json.isJsonObject()) throw new JsonParseException("The json should be an object.");
		JsonObject object = json.getAsJsonObject();
		OpInfo information = new OpInfo(object.get("name").getAsString());
		try
		{
			if (!object.has("modification"))
			{
				NebulaASMLogHelper.LOG.warn("No modification of " + information.mcpname + " detected.");
			}
			else
			{
				if (object.has("values"))
				{
					JsonArray array = object.getAsJsonArray("values");
					for (JsonElement json1 : array)
						context.deserialize(json1, Function.class);
				}
				JsonArray array = object.getAsJsonArray("modification");
				for (JsonElement json1 : array)
				{
					JsonObject object1 = json1.getAsJsonObject();
					String name = object1.get("name").getAsString();
					if (Jsons.getOrDefault(object1, "remove", false))
					{
						information.modifies.put(name, ImmutableList.of());
						continue;
					}
					
					{
						List<OpLabel> list = Jsons.getAsList(object1.getAsJsonArray("labels"), json2 -> context.deserialize(json2, OpLabel.class));
						if (!list.isEmpty())
						{
							information.modifies.put(name, list);
						}
					}
				}
			}
		}
		catch (RuntimeException exception)
		{
			throw new JsonParseException("Can not parse asm config of " + information.mcpname, exception);
		}
		return information;
	};
	final JsonDeserializer<OpLabel>				DESERIALIZER2	= (json, typeOfT, context) -> {
		if (!json.isJsonObject()) throw new JsonParseException("The json should be an object.");
		JsonObject object = json.getAsJsonObject();
		OpType type = OpType.parseValue(object.get("type").getAsString());
		int off = Jsons.getOrDefault(object, "off", 0);
		int len = Jsons.getOrDefault(object, "len", 1);
		List<AbstractInsnNode> nodes;
		if (object.has("nodes"))
		{
			nodes = new ArrayList<>();
			for (JsonElement element : object.getAsJsonArray("nodes"))
			{
				AbstractInsnNode node = context.deserialize(element, AbstractInsnNode.class);
				if (node == null) throw new JsonParseException("No node exist!");
				nodes.add(node);
			}
		}
		else
			nodes = null;
		OpLabel label;
		if (object.has("name"))
		{
			String owner = object.get("owner").getAsString();
			String name = object.get("name").getAsString();
			String desc = object.get("desc").getAsString();
			int count = Jsons.getOrDefault(object, "count", 1);
			// Each prefer first for replacement.
			label = new OpLabel.OpLabelMethodAsTag(count, owner, name, desc, off, len, type, nodes);
		}
		else
			switch (Jsons.getOrDefault(object, "marker", -1))
			{
			case 1:
				label = new OpLabel.OpLabelBegining(off, len, type, nodes);
				break;
			default:
				int line = Jsons.getOrDefault(object, "line", 0);
				label = new OpLabel.OpLabelLineNumber(line, off, len, type, nodes);
				break;
			}
		return label;
	};
	final JsonDeserializer<AbstractInsnNode>	DESERIALIZER3	= (json, typeOfT, context) -> {
		if (!json.isJsonObject())
		{
			return new InsnNode(json.getAsInt());
		}
		JsonObject object = json.getAsJsonObject();
		int val = object.get("opcode").getAsInt();
		switch (val)
		{
		case BIPUSH:
		case SIPUSH:
		case NEWARRAY:
			int operand = object.get("operand").getAsInt();
			return new IntInsnNode(val, operand);
		case LDC:
			Object value;
			switch (Jsons.getOrDefault(object, "type", "string"))
			{
			case "int":
				value = object.get("cst").getAsInt();
				break;
			case "long":
				value = object.get("cst").getAsLong();
				break;
			case "float":
				value = object.get("cst").getAsFloat();
				break;
			case "double":
				value = object.get("cst").getAsDouble();
				break;
			case "string":
				value = object.get("cst").getAsString();
				break;
			default:
				throw new RuntimeException("Unknown type LDC node got, type: " + object.get("type").getAsString());
			}
			return new LdcInsnNode(value);
		case ILOAD:
		case LLOAD:
		case FLOAD:
		case DLOAD:
		case ALOAD:
		case ISTORE:
		case LSTORE:
		case FSTORE:
		case ASTORE:
		case RET:
			int var = object.get("var").getAsInt();
			return new VarInsnNode(val, var);
		case IINC:
			int incr = object.get("incr").getAsInt();
			return new IincInsnNode(val, incr);
		case IFEQ:
		case IFNE:
		case IFLT:
		case IFGE:
		case IFGT:
		case IFLE:
		case IF_ICMPEQ:
		case IF_ICMPNE:
		case IF_ICMPLT:
		case IF_ICMPGE:
		case IF_ICMPGT:
		case IF_ICMPLE:
		case IF_ACMPEQ:
		case IF_ACMPNE:
		case GOTO:
		case JSR:
		case IFNULL:
		case IFNONNULL:
			throw new RuntimeException("This node can not used, sorry.");
		case TABLESWITCH:
			throw new RuntimeException("This node can not used, sorry.");
		case LOOKUPSWITCH:
			throw new RuntimeException("This node can not used, sorry.");
		case GETSTATIC:
		case PUTSTATIC:
		case GETFIELD:
		case PUTFIELD:
			String owner = object.get("owner").getAsString();
			String name = object.get("name").getAsString();
			String desc = object.get("desc").getAsString();
			return new FieldInsnNode(val, owner, name, desc);
		case INVOKEVIRTUAL:
		case INVOKESPECIAL:
		case INVOKESTATIC:
		case INVOKEINTERFACE:
			owner = object.get("owner").getAsString();
			name = object.get("name").getAsString();
			desc = object.get("desc").getAsString();
			boolean itf = object.get("itf").getAsBoolean();
			return new MethodInsnNode(val, owner, name, desc, itf);
		case INVOKEDYNAMIC:
			name = object.get("name").getAsString();
			desc = object.get("desc").getAsString();
			throw new RuntimeException("This node can not used, sorry.");
		case NEW:
		case ANEWARRAY:
		case CHECKCAST:
		case INSTANCEOF:
			desc = object.get("desc").getAsString();
			return new TypeInsnNode(val, desc);
		case MULTIANEWARRAY:
			desc = object.get("desc").getAsString();
			int dims = object.get("dims").getAsInt();
			return new MultiANewArrayInsnNode(desc, dims);
		default:
			return new InsnNode(val);
		}
	};
	
	private final Gson gson = new GsonBuilder().registerTypeAdapter(OpInfo.class, this.DESERIALIZER1).registerTypeAdapter(OpLabel.class, this.DESERIALIZER2).registerTypeAdapter(AbstractInsnNode.class, this.DESERIALIZER3).create();
	
	private static File		mcPath;
	private static boolean	runtimeDeobf;
	
	public static File getMcPath()
	{
		return mcPath;
	}
	
	private void extractASMFile(File file, String location, File destination) throws IOException
	{
		if (file.getName().endsWith(".jar"))
		{
			JarFile jarFile = new JarFile(file);
			location += "/";
			if (jarFile.getEntry(location) == null)
			{
				NebulaASMLogHelper.LOG.warn("Asm file does not exist or invalid!");
			}
			else
			{
				Enumeration<JarEntry> enumeration = jarFile.entries();
				while (enumeration.hasMoreElements())
				{
					JarEntry entry = enumeration.nextElement();
					if (!entry.isDirectory() && entry.getName().startsWith(location))
					{
						NebulaASMLogHelper.LOG.info("Copy asm data from " + entry.getName());
						FileUtils.copyInputStreamToFile(jarFile.getInputStream(entry), new File(destination, entry.getName().substring(location.length())));
					}
				}
			}
			jarFile.close();
		}
		else
		{
			file = new File(file, location);
			if (file.exists())
			{
				NebulaASMLogHelper.LOG.info("Copy asm data from :" + file.getPath());
				FileUtils.copyDirectory(file, destination);
			}
			else
			{
				NebulaASMLogHelper.LOG.warn("Asm file does not exist or invalid!");
			}
		}
	}
	
	private void searchASMFileFromOptional(File file)
	{
		try
		{
			NebulaASMLogHelper.LOG.info("Searching modifications at {}", file.getCanonicalPath());
		}
		catch (IOException exception)
		{
			NebulaASMLogHelper.LOG.warn("Unknown fil path.");
		}
		try
		{
			for (File file2 : file.listFiles(name -> name.getName().endsWith(".json")))
			{
				try (BufferedReader reader = new BufferedReader(new FileReader(file2)))
				{
					OpInfo information = this.gson.fromJson(reader, OpInfo.class);
					information.put();
					NebulaASMLogHelper.LOG.info("Loaded {} modifications.", information.mcpname);
				}
				catch (RuntimeException exception)
				{
					NebulaASMLogHelper.LOG.error("Fail to parse OperationInformation at " + file2.getPath(), exception);
				}
				catch (IOException exception)
				{
					NebulaASMLogHelper.LOG.error("Fail to load OperationInformation", exception);
				}
			}
		}
		catch (RuntimeException exception)
		{
			NebulaASMLogHelper.LOG.error("Failed to searching ASM files.", exception);
		}
	}
	
	private boolean markVersion(File destination) throws IOException
	{
		File file = new File(destination, "version.txt");
		if (!file.exists())
		{
			try (OutputStream stream = new BufferedOutputStream(new FileOutputStream(file)))
			{
				file.createNewFile();
				stream.write(VERSION);
				return true;
			}
			catch (Exception exception)
			{
				NebulaASMLogHelper.LOG.error("Fail to add version file.", exception);
			}
		}
		else
		{
			try (InputStream stream1 = new BufferedInputStream(new FileInputStream(file)))
			{
				int version = stream1.read();
				if (version != VERSION)
				{
					NebulaASMLogHelper.LOG.warn("The Nebula ASM version and your config ASM version are not same, " +
							"there may cause some bug, it is suggested that you should clean your ./asm file if " +
							"you don't known what is happening and your game got crashed.");
				}
				stream1.close();
				return false;
			}
			catch (Exception exception)
			{
				NebulaASMLogHelper.LOG.error("Fail to check version file.", exception);
			}
		}
		return true;
	}
	
	@Override
	public Void call() throws Exception
	{
		File destination = new File(mcPath, "asm/" + (runtimeDeobf ? "obf" : "mcp"));
		File file;
		
		if (!destination.exists())
		{
			if (!destination.mkdirs())
			{
				throw new RuntimeException("Can't create asm file.");
			}
		}
		else if (!destination.isDirectory())
		{
			throw new RuntimeException("Can't read custom asm file.");
		}
		if (markVersion(destination))
		{
			final String suffix = runtimeDeobf ? "obf" : "mcp";
			final URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
			file = new File(url.toURI());
			for (String str : NebulaCoreAPI.ASM_SEARCHING_DIRECTION)
			{
				String targetLocation = "asm/" + str + "/" + suffix;
				try // Insert Operation Files source to location.
				{
					extractASMFile(file, targetLocation, destination);
				}
				catch (IOException exception)
				{
					throw new RuntimeException("Fail to extract source files.", exception);
				}
			}
		}
		searchASMFileFromOptional(destination);
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data)
	{
		mcPath = (File) data.get("mcLocation");
		runtimeDeobf = ((Boolean) data.get("runtimeDeobfuscationEnabled")).booleanValue();
	}
}
