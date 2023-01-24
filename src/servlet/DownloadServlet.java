package servlet;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String filePath;
	private static final int DEFFAULT_BUFFER_SIZE = 10240000;

	public void init() throws ServletException {
		this.filePath = "C:\\Users\\Arsim\\OneDrive\\Desktop\\Images";
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Called download");
		String requestedFile = request.getPathInfo();
		if (requestedFile == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		File file = new File(filePath, URLDecoder.decode(requestedFile, "UTF-8"));
		if (!file.exists()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		String contentType = getServletContext().getMimeType(file.getName());
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		response.reset();
		response.setBufferSize(DEFFAULT_BUFFER_SIZE);
		response.setContentType(contentType);
		response.setContentLength(DEFFAULT_BUFFER_SIZE);
		String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
        response.setHeader(headerKey, headerValue);
		
		FileInputStream input = null;
		OutputStream output = null;

		try {
			input = new FileInputStream(file);
			output = response.getOutputStream();

			byte[] buffer = new byte[DEFFAULT_BUFFER_SIZE];
	        int bytesRead = -1;
	         
	        while ((bytesRead = input.read(buffer)) != -1) {
	            output.write(buffer, 0, bytesRead);
	        }
			
		} finally {
			close(output);
			close(input);
		}
	}

	public static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
