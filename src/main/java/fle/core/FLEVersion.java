package fle.core;

public class FLEVersion
{
	public static final int MAJOR_VERSION = 3;
	public static final int MINOR_VERSION = 0;
	public static final int SUB_VERSION = 2;
	/**
	 * The snapshot version.
	 */
	public static final int SNAPSHOT_VERSION = 5;
	
	public static boolean isSnapshotVersion()
	{
		return SNAPSHOT_VERSION >= 0;
	}
}