package controller;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import dao.Dao;
import model.User;

@Named
@SessionScoped
public class AdminController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Dao dao;

	private User user = new User();
	private String selectedRole;

	private List<User> listUsers;
	private String[] roles = { "user", "admin" };

	@PostConstruct
	public void init() {
		setListUsers(dao.getUsers());
	}

	public void insertRow() throws NoSuchAlgorithmException {
		user.setRole(selectedRole);
		user.setPassword(encryptPasswordSHA("admin"));
		dao.persist(user);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("User Added"));
		PrimeFaces.current().ajax().update("form:messages");
		user = new User();
		PrimeFaces.current().executeScript("PF('add').hide()");
		this.init();
	}
	
	public String encryptPasswordSHA(String txt) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] hashbytes = md.digest(txt.getBytes(StandardCharsets.UTF_8));

		Encoder encoder = Base64.getEncoder();
		
		System.out.println(encoder.encodeToString(hashbytes));
		
		return encoder.encodeToString(hashbytes);
	}
	
	public static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	
	public void delete(User u) {
		dao.remove(u);
	}

	public List<User> getListUsers() {
		return listUsers;
	}

	public void setListUsers(List<User> listUsers) {
		this.listUsers = listUsers;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(String selectedRole) {
		this.selectedRole = selectedRole;
	}

	public String[] getRoles() {
		return roles;
	}

	public void setRoles(String[] roles) {
		this.roles = roles;
	}

}