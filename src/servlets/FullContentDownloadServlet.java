package servlets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.ContentExtractor;

/*
 * FullContentDownload class.
 * Given an ID and a resource URL, downloads the resource and stores it to be retrieved later based on its ID.
 */

@WebServlet("/fulldownload")
public class FullContentDownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1001349189L;

	// private String baseDirectory =
	// ReadPropertiesFile.getProperty("retriever.fullDownloadDirectory", this);

	private String baseDirectory = "D:\\Afstuderen\\Tomcat\\content";
	private String lineSep = System.getProperty("line.separator");

	@Override
	// Handles post request.
	// Request should contain an ID to store resources with, and links to
	// resources.
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		// Get ID - this is the directory where we will store the files
		String id = req.getParameter("id");
		String url = req.getParameter("url");
		if (id != null) {
			String targetLoc = baseDirectory + "\\" + id;
			File targetDir = new File(targetLoc);

			if (!targetDir.exists()) {
				if (targetDir.mkdir()) {
					// Retrieve the HTML page at the url and store it
					String targetFileName = id + ".htm";
					if (!url.endsWith(".htm")) {
						targetFileName += "l";
					}
					if (ContentExtractor.storeFileFromURL(targetLoc, url,
							targetFileName)) {
						// Successfully stored HTML page - now extract content
						ContentExtractor.extractContentInHtmlFile(targetLoc,
								targetFileName, url);
					} else {
						// Could not retrieve web page
						// TODO error log
					}

				} else {
					// Could not create directory.
					// TODO error log
					return;
				}
			}

		}

	}

	// Write information to a log file in the directory
	// TODO move to other class
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

	// TODO move to other class
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
