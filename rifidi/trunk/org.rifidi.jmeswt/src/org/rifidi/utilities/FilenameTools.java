package org.rifidi.utilities;

/**
 * This class contains utility functions for filenames
 * @author Dan West
 */
public class FilenameTools {

	/**
	 * Gets the extension of the given file
	 * @param filename the file to find the extension of
	 * @return the extension of the file
	 */
	public static String getExtension( String filename ) {
		int last = filename.lastIndexOf('.');
		return filename.substring(last+1);
	}

	/**
	 * Makes sure that the extension of the given filename is extension
	 * @param filename the file whose extension to check
	 * @param extension the preferred extension of this file
	 * @return a filename with the extension added, if it wasn't already there
	 */
	public static String assertExtension( String filename, String extension ) {
		String ext = getExtension(filename);

		if ( !ext.equals(extension) ){
			filename = filename.concat("."+extension);
		}
		return filename;
	}

	/**
	 * Get the directory that the given file is in
	 * @param filename the file to find the directory of
	 * @return a string representing the directory
	 */
	public static String getDirectory( String filename ) {
		int last = filename.lastIndexOf('/');
		return filename.substring(0, last + 1);
	}

	/**
	 * returns the name of the file given its full path
	 * @param pathname
	 * @return
	 */
	public static String getFileFromPath( String pathname ) {
		int last = Math.max(pathname.lastIndexOf('/'),pathname.lastIndexOf('\\'));
		return pathname.substring(last+1);
	}

	/**
	 * returns the base filename of a file
	 */
	public static String getBaseFilename( String fname ) {
		int lastslash = Math.max(fname.lastIndexOf('/'),fname.lastIndexOf('\\'));
		int lastdot = fname.lastIndexOf('.');
		if ( lastdot == lastslash )				// fname == "filename"
			return fname;
		else if ( lastdot > lastslash )			// fname == "/folder.package/file.s"
			return fname.substring(lastslash+1,lastdot);
		else									// fname == "/folder.package/file"
			return fname.substring(lastslash+1);
	}

	/**
	 * tester function
	 */
	public static void main(String[] args) {
		// test assertExtension
		String filename = "/the/path/to/some/file.type";
		System.out.println(assertExtension(filename, "cheese"));

		System.out.println();

		// test getdirectory
		System.out.println("file: "+filename);
		System.out.println("base: "+getBaseFilename(filename));
		System.out.println("dir:  "+getDirectory(filename));
		System.out.println("swap: "+swapExtension(filename, "foo"));

		// advanced basefilename test
		filename = "/path/to/com.something/testfile";
		System.out.println("base: "+getBaseFilename(filename));
		filename = "/path/to/com.something/testfile.test";
		System.out.println("base: "+getBaseFilename(filename));
		filename = "testfile.test";
		System.out.println("base: "+getBaseFilename(filename));
		filename = "testfile";
		System.out.println("base: "+getBaseFilename(filename));
	}

	/**
	 * 
	 * @param file
	 * @param extension
	 * @return
	 */
	public static String swapExtension(String file, String extension){
		return (file.substring(0, file.lastIndexOf(".")) + "." + extension);
	}
}
