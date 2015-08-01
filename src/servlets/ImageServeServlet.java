package servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/*
 * ImageServer class.
 * Given a specific ID, serves the requester URLs to the image files relating to that ID.
 */

@WebServlet("/imageserve")
public class ImageServeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String lineSep = System.getProperty("line.separator");

	//private String baseDirectory = ReadPropertiesFile.getProperty("retriever.imageDirectory", this);
	private String baseDirectory = "D:\\Afstuderen\\Tomcat\\content";

	@Override
	// Handles get request.
	// Request should contain an ID for which to retrieve resources.
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		// Get request attribute.
		String id = req.getParameter("id");
		// Verify if ID parameter was entered..
		if (id != null) {
			res.setContentType("text/plain");
			PrintWriter writer = res.getWriter();
			String directory = baseDirectory + "\\" + id;

			// Verify the directory exists.
			File f = new File(directory);
			if (f.exists() && f.isDirectory()) {
				// If it exists -> get list of urls for files in its folder.
				// Send the list as a response.
				for (File current : f.listFiles()) {
					// TODO either serve the images directly or have it give a
					// web link rather than the path
					writer.write(current.getAbsolutePath());
					writer.write(lineSep);
				}
			} else {
				// If not -> send negative response.
				writer.write("Directory " + directory + " does not exist.");
			}
			writer.close();
		}
		// If not -> send negative response.
		else {
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

}
