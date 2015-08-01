package tools;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ContentExtractor {

	// Extracts css, javascript and images in the HTML file at the location
	// provided and stores them in the same folder
	public static void extractContentInHtmlFile(String targetDir,
			String fileName, String originalUrl) {

		try {
			// Make JSoup Document out of the file
			File inputFile = new File(targetDir + "\\" + fileName);
			Document doc = Jsoup.parse(inputFile, "UTF-8", originalUrl);

			// Identify all resources
			ArrayList<String> resourceUrls = identifyExternalResources(doc);

			// Download all resources
			downloadExternalResources(targetDir, resourceUrls);

			// Substitute links in original document
			Document newDoc = substituteLinks(doc);

			// Write new version of original document
			FileUtils.writeStringToFile(inputFile, newDoc.outerHtml(), "UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Stores the file at sourceUrl with name targetFileName
	// targetFileName should also include file extension!
	public static boolean storeFileFromURL(String directory, String sourceUrl,
			String targetFileName) {
		if (sourceUrl != null && !sourceUrl.isEmpty()) {
			try {
				URL source = new URL(sourceUrl);
				File destination = new File(directory + "\\" + targetFileName);
				FileUtils.copyURLToFile(source, destination);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	private static ArrayList<String> identifyExternalResources(Document doc) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			String current = "";

			// CSS
			Elements styles = doc
					.select("link[type=text/css], link[rel=stylesheet");
			int counter = 0;
			for (Element style : styles) {
				current = style.absUrl("href");
				if (current.contains("?")) {
					current = current.substring(0, current.indexOf("?"));
				}

				result.add("css" + counter + ":" + current);
				counter++;
			}

			// JavaScript
			Elements script = doc.select("script[src]");
			counter = 0;
			for (Element el : script) {
				current = el.absUrl("src");
				if (current.contains("?")) {
					current = current.substring(0, current.indexOf("?"));
				}
				result.add("js" + counter + ":" + current);
				counter++;
			}

			// Images
			Elements img = doc.getElementsByTag("img");
			counter = 0;
			for (Element el : img) {
				current = el.absUrl("src");
				if (current.contains("?")) {
					current = current.substring(0, current.indexOf("?"));
				}
				result.add("js" + counter + ":" + current);
				counter++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	// Downloads all the resources in resourceUrls to targetDir
	private static void downloadExternalResources(String targetDir,
			ArrayList<String> resourceUrls) {
		String resourceLoc = "";
		String targetName = "";
		int index = 0;
		for (String resource : resourceUrls) {
			index = resource.indexOf(":");
			// The original URL
			resourceLoc = resource.substring(index + 1);
			// The target filename
			targetName = resourceLoc
					.substring(resourceLoc.lastIndexOf("/") + 1);

			storeFileFromURL(targetDir, resourceLoc, targetName);
		}
	}

	// Substitutes all resource links with only the filename
	private static Document substituteLinks(Document doc) {
		String current = "";

		// CSS
		Elements styles = doc
				.select("link[type=text/css], link[rel=stylesheet");
		for (Element style : styles) {
			current = style.absUrl("href");
			current = current.substring(current.lastIndexOf("/") + 1);
			if (current != null && !current.isEmpty())
				style.attr("href", current);
		}

		// JavaScript
		Elements script = doc.select("script[src]");
		for (Element el : script) {
			current = el.absUrl("src");
			if (current != null && !current.isEmpty()) {
				current = current.substring(current.lastIndexOf("/") + 1);
				if (current != "" && current != null)
					el.attr("src", current);
			}
		}

		// Images
		Elements img = doc.getElementsByTag("img");
		for (Element el : img) {
			current = el.absUrl("src");
			if (current != null && !current.isEmpty()) {
				current = current.substring(current.lastIndexOf("/") + 1);
				if (current != "" && current != null)
					el.attr("src", current);
			}
		}
		return doc;
	}
}
