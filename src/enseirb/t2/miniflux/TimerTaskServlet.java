package enseirb.t2.miniflux;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TimerTaskServlet
 */
@WebServlet("/TimerTaskServlet")
public class TimerTaskServlet extends HttpServlet implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		new RepetAction();
	}

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TimerTaskServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	
	class RepetAction {
		  Timer t;

		  public RepetAction() {
		    t = new Timer();
		    t.schedule(new MonAction(), 0, 1*1000);
		    }

		  class MonAction extends TimerTask {
		    int nbrRepetitions = 3;

		    public void run() {
		      if (nbrRepetitions > 0) {
		        System.out.println("Ca bosse dur!");
		        nbrRepetitions--;
		      } else {
		        System.out.println("Terminé!");
		        t.cancel();
		        }
		      }
		    }
		  }
}
