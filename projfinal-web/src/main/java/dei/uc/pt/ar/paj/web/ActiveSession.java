package dei.uc.pt.ar.paj.web;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dei.uc.pt.ar.paj.Entities.UserEntity;
import dei.uc.pt.ar.paj.ejb.UserEJB;

@Named
@SessionScoped
public class ActiveSession implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = LoggerFactory.getLogger(ActiveSession.class);


	private HttpSession session;

	private UserEntity activeUser;

	private String newPlayListName;
	private String search;

	private Part file;


	private String mensagem;


	public void init(UserEntity user) {
		startSession();

		this.newPlayListName = "";

		this.activeUser = user;

		this.mensagem = "";

		UserEJB.increaseUserCount(activeUser);
	}


	public Part getFile() {
		return file;
	}

	public void setFile(Part file) {
		this.file = file;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getNewPlayListName() {
		return newPlayListName;
	}

	public void setNewPlayListName(String newPlayListName) {
		this.newPlayListName = newPlayListName;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public UserEntity getActiveUser() {
		return activeUser;
	}

	public void setActiveUser(UserEntity activeUser) {
		this.activeUser = activeUser;
	}

	// Logout
	public void logout() {
		UserEJB.decreaseUserCount(activeUser);

		logger.info("ActiveSession.logout() - antes de fazlogout()");
		fazlogout();

		this.activeUser = null;

		logger.info("No ActiveSession.logout() antes do endSession()");

		endSession();
		redirect();
	}

	public String fazlogout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();

		try {
			request.logout();

			logger.info("No ActiveSession.fazlogout() depois de 'request.logout()'");
			return "/login.xhtml";
		} catch (ServletException e) {

		}
		logger.info("No ActiveSession.fazlogout() antes do return '/login'");
		return "/login.xhtml?faces-redirect=true";
	}

	private void redirect() {
		String redirect = "/projfinal-web/login.xhtml";
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();
		try {
			response.sendRedirect(redirect);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Logout

	// In�cio e Fim da sess�o http
	public void startSession() {

		this.session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		this.session.setAttribute("sessionLoggedIn", true);
	}

	public void endSession() {

		if (this.session != null) {
			this.session.invalidate();
		}

		logger.info("Em ActiveSession.endSession() no final da endSession()");
	}
	// In�cio e Fim da sess�o http
}
