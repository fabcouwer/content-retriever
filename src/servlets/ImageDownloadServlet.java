package servlets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import tools.ValidateUrl;

/*
 * ImageDownloader class.
 * Given an ID and a resource URL, downloads the resource and stores it to be retrieved later based on its ID.
 */

@WebServlet("/imagedownload")
public class ImageDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// private String baseDirectory2 =
	// ReadPropertiesFile.getProperty("retriever.imageDirectory", this);
	private String baseDirectory = "D:\\Afstuderen\\Tomcat\\content";
	private String lineSep = System.getProperty("line.separator");

	@Override
	// Handles post request.
	// Request should contain an ID to store resources with, and links to
	// resources.
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		// Extract image URLs.
		ArrayList<String> images = getImageParameters(req);

		// Get ID - this is the directory where we will store the files
		String id = req.getParameter("id");
		if (id != null) {
			String targetLoc = baseDirectory + "\\" + id;
			File targetDir = new File(targetLoc);

			if (!targetDir.exists()) {
				if (targetDir.mkdir()) {
					// Validate urls, then download and store images
					validateAndStoreImages(targetLoc, images);
				} else {
					// Could not create file.
					// TODO
					return;
				}
			}

		}

	}

	// For every image URL in images, checks if it is valid,
	// then writes it to disk in directory targetLoc
	private void validateAndStoreImages(String targetLoc,
			ArrayList<String> images) {
		ArrayList<String> logLines = new ArrayList<String>();

		// Log lines to be placed in folder
		logLines.add("Image storing log. Current date and time: ");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		logLines.add(dateFormat.format(cal.getTime()));

		// Keep a list of stored images with their original URL
		ArrayList<String[]> storedFiles = new ArrayList<String[]>();

		// Validate, download and store each image file.
		int i = 0;
		String fileName = "";
		for (String imgUrl : images) {
			if (ValidateUrl.isValidUrl(imgUrl)) {
				// Generate name for the file
				fileName = "img" + i
						+ imgUrl.substring(imgUrl.lastIndexOf("."));

				if (storeImageFromURL(targetLoc, imgUrl, fileName)) {
					// Log success
					logLines.add("Stored " + imgUrl + " successfully.");
					i++;

					// Log new file and original URL
					storedFiles.add(new String[] { fileName, imgUrl });
				} else {
					// Log error: could not retrieve or save to disk
					logLines.add("Storing " + imgUrl
							+ " FAILED. Could not retrieve or save to disk.");
				}
			} else {
				// Log error: not valid
				logLines.add("Storing " + imgUrl
						+ " FAILED. URL or extension not valid.");
			}
		}

		writeLogFile(targetLoc, "storageLog.txt", logLines);
		writeImageNameLog(targetLoc, "imageNames.txt", storedFiles);
	}

	// Stores the image at imageUrl with name imageId
	private boolean storeImageFromURL(String directory, String imageUrl,
			String imageId) {
		try {
			URL source = new URL(imageUrl);
			File destination = new File(directory + "\\" + imageId);
			FileUtils.copyURLToFile(source, destination);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// Retrieves parameters that start with "img" from a HttpServletRequest
	private ArrayList<String> getImageParameters(HttpServletRequest req) {
		ArrayList<String> atts = new ArrayList<String>();
		String curr = "";
		for (Enumeration<String> e = req.getParameterNames(); e
				.hasMoreElements();) {
			curr = e.nextElement();
			if (curr.startsWith("img"))
				atts.add(req.getParameter(curr));
		}

		return atts;
	}

	// Write information to a log file in the directory
	private void writeLogFile(String directory, String fileName,
			ArrayList<String> logs) {
		try {

			File file = new File(directory + "\\" + fileName);

			// if file doesn't exist, then create it
			if (!file.exists()) {
				if (file.createNewFile()) {

					OutputStreamWriter fw = new OutputStreamWriter(
							new FileOutputStream(file.getAbsoluteFile()),
							"UTF-8");
					BufferedWriter bw = new BufferedWriter(fw);

					for (String s : logs) {
						bw.write(s);
						bw.write(lineSep);
					}
					bw.close();
				} else {
					// Could not write log
					// TODO
					return;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeImageNameLog(String directory, String fileName,
			ArrayList<String[]> names) {
		try {

			File file = new File(directory + "\\" + fileName);

			// if file doesn't exist, then create it
			if (!file.exists()) {
				if (file.createNewFile()) {
					OutputStreamWriter fw = new OutputStreamWriter(
							new FileOutputStream(file.getAbsoluteFile()),
							"UTF-8");
					BufferedWriter bw = new BufferedWriter(fw);

					for (String[] s : names) {
						bw.write(s[0] + " : " + s[1]);
						bw.write(lineSep);
					}
					bw.close();
				} else {
					// Could not write log
					// TODO
					return;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
