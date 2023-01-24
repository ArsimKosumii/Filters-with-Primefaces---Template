package controller;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Dao;
import model.User;

@Named
@SessionScoped
public class LoginController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private User user;

	@Inject
	private Dao dao;

	String userName = "";
	String password = "";

	public void logout() throws Exception {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		request.logout();
		request.getSession().invalidate();
		FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/login.xhtml");
	}

	public void login() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();

		if (userName != null && !userName.isBlank() && password != null && !password.isBlank()) {
			try {
				if (request.getRemoteUser() != null) {
					request.logout();
					request.getSession().invalidate();
				}
				request.login(userName, password);
				password = encryptPasswordSHA(password);
				user = dao.findUserByUsernameAndPassword(userName, password);

				if (user != null) {
					if (request.isUserInRole("admin")) {
						FacesContext.getCurrentInstance().getExternalContext()
								.redirect(request.getContextPath() + "/admin/index.xhtml");
					} else {
						FacesContext.getCurrentInstance().getExternalContext()
								.redirect(request.getContextPath() + "/user/index.xhtml");
					}
				}
			} catch (Exception e) {
				showError();
			}
		} else {
			showError();
		}
	}

	public void showError() {
		addMessage(FacesMessage.SEVERITY_ERROR, "", "Username or Password is incorrect!");
	}

	public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	private String encryptPasswordSHA(String txt) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] hashbytes = md.digest(txt.getBytes(StandardCharsets.UTF_8));

		Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(hashbytes);
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}