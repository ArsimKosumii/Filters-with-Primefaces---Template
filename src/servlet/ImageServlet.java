package servlet;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Dao;
import model.User;

@WebServlet("/ImageServlet")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final int DEFFAULT_BUFFER_SIZE = 10240000;
	private String filePath;
	@Inject
	private Dao dao;
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.filePath = "C:\\Users\\Arsim\\OneDrive\\Desktop\\Images";
		
		String requestedFile = request.getPathInfo();
		
		String username = request.getRemoteUser();
		User user = dao.findUserByUsername(username);
		int id = user.getId();

		if (requestedFile == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
//		URLDecoder.decode(requestedFile, "UTF-8")

		File file = new File(filePath+"/"+user.getId()+"/"+requestedFile);
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
		response.setHeader("Content-Length", String.valueOf(file.length()));
		response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

		BufferedInputStream input = null;
		OutputStream output = null;

		try {
			input = new BufferedInputStream(new FileInputStream(file));
			output = response.getOutputStream();

			byte[] buffer = new byte[DEFFAULT_BUFFER_SIZE];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
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
