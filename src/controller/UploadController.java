package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dao.Dao;
import model.User;

@Named
@ViewScoped
public class UploadController implements Serializable {
	private static final long serialVersionUID = 1L;

	private String photo = "";
	private Part part;
	private List<File> images;
	private final String PATH_TO = "C:\\Users\\User\\Desktop\\Images";
	private final String LIMIT_TYPE_FILE = "gif|jpg|png|jpeg";
	private final int LIMIT_MAX_SIZE = 10240000;
	
	
	@Inject
	private Dao dao;

	@PostConstruct
	public void init() {
		getAllImages();
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public List<File> getImages() {
		return images;
	}

	public void setImages(List<File> images) {
		this.images = images;
	}

	public List<File> getAllImages() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();
		String username = request.getRemoteUser();
		User user = dao.findUserByUsername(username);
		int id = user.getId();
		
		images = new ArrayList<>();
		File folder = new File(this.PATH_TO+"\\"+id);

		if (folder.exists()) {
			for (File f : folder.listFiles()) {
				if (checkFileType(f.getName())) {
					images.add(f);
				}
			}
		}

		return images;
	}

	public String processUpload() {
		String fileSaveData = "noimages.jpg";
		
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();
		String username = request.getRemoteUser();
		User user = dao.findUserByUsername(username);
		int id = user.getId();
		
		if (part != null) {
			try {
				if (part.getSize() > 0) {
					String submittedFileName = getFileName(part);
					if (checkFileType(submittedFileName)) {
						if (part.getSize() > this.LIMIT_MAX_SIZE) {
							FacesContext.getCurrentInstance().addMessage("File size too large!", new FacesMessage());
						} else {
							String currentFileName = submittedFileName;
							String extendsion = currentFileName.substring(currentFileName.lastIndexOf("."),
									currentFileName.length());
							Long nameRadom = Calendar.getInstance().getTimeInMillis();
							String newFileName = nameRadom + extendsion;
							fileSaveData = newFileName;

							try {
								byte[] fileContent = new byte[(int) part.getSize()];
								InputStream in = part.getInputStream();
								in.read(fileContent);

								File fileToCreate = new File(this.PATH_TO+"\\"+id, newFileName);
								File folder = new File(this.PATH_TO+"\\"+id);
								if (!folder.exists()) {
									folder.mkdirs();
								}

								FileOutputStream fileOutStream = new FileOutputStream(fileToCreate);
								fileOutStream.write(fileContent);
								fileOutStream.flush();
								fileOutStream.close();
								fileSaveData = newFileName;

							} catch (IOException e) {
								fileSaveData = "noimages.jpg";
							}
						}
					} else {
						fileSaveData = "noimages.jpg";
					}
				}
			} catch (Exception ex) {
				fileSaveData = "noimages.jpg";
			}
		}
		getAllImages();
		return fileSaveData;
	}

	private boolean checkFileType(String fileName) {
		if (fileName.length() > 0) {
			String[] parts = fileName.split("\\.");
			if (parts.length > 0) {
				String extention = parts[parts.length - 1];

				return this.LIMIT_TYPE_FILE.contains(extention);
			}
		}
		return false;
	}

	private String getFileName(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");

				return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1);
			}
		}
		return null;
	}

}