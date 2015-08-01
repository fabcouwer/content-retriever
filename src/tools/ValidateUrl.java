package tools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.validator.routines.UrlValidator;

public class ValidateUrl {

	private static final Set<String> ALLOWED_FILE_EXTENSIONS_SET;
	static {
		final List<String> extensions = Arrays.asList("jpg", "jpeg", "gif",
				"png", "bmp", "tif", "tiff");
		ALLOWED_FILE_EXTENSIONS_SET = new HashSet<String>(extensions);
	}

	// Checks url to see if it is a valid URL pointing to something with an
	// image extension. URL must be prepended by http:// or https://!
	public static boolean isValidUrl(String url) {
		String[] schemes = { "http", "https" };
		UrlValidator urlValidator = new UrlValidator(schemes);
		String fileExtension = url.substring(url.lastIndexOf(".") + 1);
		return urlValidator.isValid(url)
				&& ALLOWED_FILE_EXTENSIONS_SET.contains(fileExtension);
	}

	public static Set<String> getAllowedFileExtensions() {
		return ALLOWED_FILE_EXTENSIONS_SET;
	}

}
