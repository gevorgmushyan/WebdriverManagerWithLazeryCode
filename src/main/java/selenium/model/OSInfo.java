package selenium.model;

public abstract class OSInfo {
    public static final Boolean arch32 = System.getProperty("os.arch").contains("32");
    public static final Boolean arch64 = System.getProperty("os.arch").contains("64");
    public static final Boolean Windows = System.getProperty("os.name").contains("Windows");
    public static final Boolean Linux = System.getProperty("os.name").contains("Linux");

    public static String getOsName() {
        if(Windows)
            return "windows";
        if(Linux)
            return "linux";
        throw new IllegalStateException("The OS is not detected.");
    }

    public static String getOsType() {
        if(arch64)
            return "64bit";
        if(arch32)
            return "32bit";
        throw new IllegalStateException("The OS arch type is not detected.");
    }
}
