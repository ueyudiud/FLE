package fle.core;

public class FLEVersion
{
	public static final int MAJOR_VERSION = 3;
	public static final int MINOR_VERSION = 0;
	public static final int SUB_VERSION = 1;

	public static final int SNAPSHOT_VERSION = 7;

	public static boolean isSnapshotVersion()
	{
		return SNAPSHOT_VERSION >= 0;
	}
}