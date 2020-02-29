package editor;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class VentanaIntroductoria{
	//CONSTRUCTOR-------------------------------------------------------------
	public VentanaIntroductoria(){
	    SwingUtilities.invokeLater(new Runnable()
	    {
	    	/**
	    	 * El siguiente metodo sirve para implementar el metodo de la interfaz "Runnable"
	    	 * esta interfaz permite crear varios "hilos" de ejecucion permitiendo que el programa
	    	 * se ejecute en dos "objetivos" al mismo tiempo, esto permite que un programa adquiera 
	    	 * la capacidad de hacer tareas multiples.
	    	 */
	        public void run() 
	        {
	            try
	            {
	                JDialog bienvenida = new JDialog();
	                Dimension dp = Toolkit.getDefaultToolkit().getScreenSize();
	                bienvenida.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	                bienvenida.setTitle("Welcome!");
	                /**
	                 * La ventana de bienvenida solo tiene un JLabel el cual solo contiene la imagen.
	                 */
	                bienvenida.add(new JLabel(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/images/IntroImage1PEQUE.png")))));
	                bienvenida.pack();
	                bienvenida.setLocation(dp.width/2-bienvenida.getSize().width/2, dp.height/2-bienvenida.getSize().height/2);
	                //bienvenida.setLocationByPlatform(true);
	                //TEMPORIZADOR DE AUTOCIERRE----------------------------------------------------------------------------------------------
	                /**
	                 * Este es un temporizador con su propio escuchador que se encarga de cerrar la ventana en 1.5s
	                 */
	                Timer auto_cierre = new Timer(1500, new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							bienvenida.dispatchEvent(new WindowEvent(bienvenida, WindowEvent.WINDOW_CLOSING));
						}
					});
	                bienvenida.setVisible(true);
	                auto_cierre.start();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    });
	}
}
