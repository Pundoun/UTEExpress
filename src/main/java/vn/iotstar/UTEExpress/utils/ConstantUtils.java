package vn.iotstar.UTEExpress.utils;

import java.nio.file.Paths;

public class ConstantUtils {

	//public static String UPLOAD_PATH = "C:/Users/dinhk/Downloads/UTEExpress/UTEExpress/src/main/resources/static/assets/images";
	public static final String UPLOAD_PATH = Paths.get("src/main/resources/static/assets/images").toAbsolutePath().toString();
}
