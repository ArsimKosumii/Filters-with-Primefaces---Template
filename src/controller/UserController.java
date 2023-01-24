package controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dao.Dao;
import model.User;

@Named(value = "userController")
@SessionScoped
public class UserController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private Dao dao;

	public void createTables() {
		dao.findById(User.class, 0);
	}

}
