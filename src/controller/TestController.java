package controller;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dao.Dao;
import model.Country;
import model.Employee;
import model.User;

@Named(value = "testController")
@SessionScoped
public class TestController  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<Country> countries;
	
	@Inject
	private Dao dao;
	
	@PostConstruct
	public void init() {
		countries = dao.getCountries();
	}
	
	
	public void insertHundredRows() {
		Random random = new Random();
		for (int i = 0; i < 150; i++) {
			Employee emp = new Employee();
			emp.setName("Emp_" + i);
			emp.setLastname("Loyee_" + (i + 20));
			if (i <= 75)
				emp.setAge(i + 20);
			else
				emp.setAge(i / 2);
			emp.setDateofbirth("10/10/1990");
			emp.setDateofemployment("10/10/2021");
			emp.setCountry(countries.get(random.nextInt(countries.size() - 1)));
			dao.persist(emp);
		}
	}
}
