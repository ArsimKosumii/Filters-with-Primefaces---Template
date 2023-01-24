//package helper;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;
//import javax.servlet.http.Part;
//
//public class UploadHelper {
//	private final int LIMIT_MAX_SIZE = 10240000;
//	private final String LIMIT_TYPE_FILE = "gif|jpg|png|jpeg";
//	private final String PATH_TO = "C:\\Users\\Arsim\\OneDrive\\Desktop\\Images";
//
//	public UploadHelper() {
//	}
//
//	public String processUpload(Part fileUpload) {
//		String fileSaveData = "noimages.jpg";
//
//		if (fileUpload != null) {
//			try {
//				if (fileUpload.getSize() > 0) {
//					String submittedFileName = getFileName(fileUpload);
//					if (checkFileType(submittedFileName)) {
//						if (fileUpload.getSize() > this.LIMIT_MAX_SIZE) {
//							FacesContext.getCurrentInstance().addMessage("File size too large!", new FacesMessage());
//						} else {
//							String currentFileName = submittedFileName;
//							String extendsion = currentFileName.substring(currentFileName.lastIndexOf("."),
//									currentFileName.length());
//							Long nameRadom = Calendar.getInstance().getTimeInMillis();
//							String newFileName = nameRadom + extendsion;
//							fileSaveData = newFileName;
//
//							String fileSavePath = this.PATH_TO;
//
//							try {
//								byte[] fileContent = new byte[(int) fileUpload.getSize()];
//								InputStream in = fileUpload.getInputStream();
//								in.read(fileContent);
//
//								File fileToCreate = new File(fileSavePath, newFileName);
//								File folder = new File(fileSavePath);
//								if (!folder.exists()) {
//									folder.mkdirs();
//								}
//
//								FileOutputStream fileOutStream = new FileOutputStream(fileToCreate);
//								fileOutStream.write(fileContent);
//								fileOutStream.flush();
//								fileOutStream.close();
//								fileSaveData = newFileName;
//
//							} catch (IOException e) {
//								fileSaveData = "noimages.jpg";
//							}
//						}
//					} else {
//						fileSaveData = "noimages.jpg";
//					}
//				}
//			} catch (Exception ex) {
//				fileSaveData = "noimages.jpg";
//			}
//		}
//
//		return fileSaveData;
//	}
//
//	private String getFileName(Part part) {
//		for (String cd : part.getHeader("content-disposition").split(";")) {
//			if (cd.trim().startsWith("filename")) {
//				String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
//
//				return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1);
//			}
//		}
//		return null;
//	}
//
//	private boolean checkFileType(String fileName) {
//		if (fileName.length() > 0) {
//			String[] parts = fileName.split("\\.");
//			if (parts.length > 0) {
//				String extention = parts[parts.length - 1];
//
//				return this.LIMIT_TYPE_FILE.contains(extention);
//			}
//		}
//		return false;
//	}
//
//	public List<File> getImages() {
//		List<File> images = new ArrayList<>();
//		File folder = new File(this.PATH_TO);
//
//		if (folder.exists()) {
//			for (File f : folder.listFiles()) {
//				if(checkFileType(f.getName())) {
//					images.add(f);
//				}
//			}
//		}
//		
//		return images;
//	}
//
//}
