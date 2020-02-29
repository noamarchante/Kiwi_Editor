package editor;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import javax.swing.text.rtf.*;
import editor.MenuScroller;

public class KiwiEditor {
	//MAIN-------------------------------------------------------------
	public static void main(String[] args) {
		/**
		 * El main solo tiene que iniciar la ventana de bienvenida y la del editor
		 */
		new VentanaIntroductoria();
		new MarcoPrincipalEditor();
		}
}
@SuppressWarnings("serial")
class MarcoPrincipalEditor extends JFrame{
	public MarcoPrincipalEditor(){
		//MARCO PRINCIPAL-----------------------------------------------
		setTitle("Kiwi Editor v1.1.5");
		//TAMAÑO Y POSICIÓN DEL MARCO PRINCIPAL-------------------------
		Toolkit Pantalla= Toolkit.getDefaultToolkit();
		Dimension tamañoPantalla = Pantalla.getScreenSize();
		int alturaPantalla = tamañoPantalla.height;
		int anchoPantalla = tamañoPantalla.width;
		setSize(anchoPantalla/2, alturaPantalla/2);
		setLocation(anchoPantalla/4, alturaPantalla/4);
		//ICONO DEL MARCO PRINCIPAL--------------------------------------
		Image icono = new ImageIcon(KiwiEditor.class.getResource("/images/kiwi.png")).getImage();
		setIconImage(icono);
		//LÁMINA PRINCIPAL-----------------------------------------------
		LaminaPrincipalEditor lamina = new LaminaPrincipalEditor();
		add(lamina);
		//ULTIMOS DETALLES-----------------------------------------------
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
@SuppressWarnings("serial")
class LaminaPrincipalEditor extends JPanel{

	public LaminaPrincipalEditor(){
		
		//LAYOUT LÁMINA PRINCIPAL--------------------------------------------------------------------
		setLayout(new BorderLayout());
		//---------------------------------------------------BARRA DE HERRAMIENTAS-------------------------------------------------------------------------
		barraHerramientas = new JToolBar();
		//ESTILOS BARRA------------------------------------------------------------------------------------------------------------------------------------
		/**
		 * A cada boton se le agrega una imagen en su posicion normal, un escuchador para que ejecute la accion que debe
		 * y un escuchador del raton para que sepa cuando y como modificar la imagen del boton.
		 * 
		 * Para cambiar el estilo del texto se uso la clase StyledEditorKit y sus diferentes metodos y acciones
		 */
		//NEGRITA
		b_negrita = new JButton(iconoNegrita = new ImageIcon(KiwiEditor.class.getResource("/images/BotonNegritaPEQUE.png")));
		b_negrita.addActionListener(new StyledEditorKit.BoldAction());
		b_negrita.addMouseListener(new Accionador());
		barraHerramientas.add(b_negrita);
		//CURSIVA
		b_cursiva = new JButton(iconoCursiva = new ImageIcon(KiwiEditor.class.getResource("/images/BotonCursivaPEQUE.png")));
		b_cursiva.addActionListener(new StyledEditorKit.ItalicAction());
		b_cursiva.addMouseListener(new Accionador());
		barraHerramientas.add(b_cursiva);
		//UNDERLINE
		b_underline = new JButton(iconoUnderline = new ImageIcon(KiwiEditor.class.getResource("/images/BotonSubrayadoPEQUE.png")));
		b_underline.addActionListener(new StyledEditorKit.UnderlineAction());
		b_underline.addMouseListener(new Accionador());
		barraHerramientas.add(b_underline);
		barraHerramientas.addSeparator();
		//COLOR BARRA------------------------------------------------------------------------------------------------------------------------------------
		Action accionColor = new AbstractAction("Color", new ImageIcon(KiwiEditor.class.getResource("/images/pantone.png"))){
			public void actionPerformed(ActionEvent e) {
				//------ACCIÓN COLOR BARRA DE HERRAMIENTAS---------------------------------------------------------------------------------------------------
				/**
				 * El siguiente metodo permite cambiar el color del txto seleccionado usando el JColorChooser para escoger el color
				 */
				Color c = JColorChooser.showDialog(null, "Selecciona Color", Color.WHITE);
				if (c!=null){
					/**
					 * Se cambian los atributos del texto para que el color de primer plano o fore-ground
					 * sea del mismo color que el seleccionado
					 */
					MutableAttributeSet estilo = new SimpleAttributeSet();
					StyleConstants.setForeground(estilo, c);
					documento.setCharacterAttributes(estilo, false);
				}
			}
		};
		//accionColor.putValue(Action.SHORT_DESCRIPTION, "Color");
		/**
		 * Agregacion de los demas botones de la barra de herramientas:
		 */
		barraHerramientas.add(accionColor);
		barraHerramientas.addSeparator();
		//ALINEACION BARRA------------------------------------------------------------------------------------------------------------------------------------
		/**
		 * La alineacion tambien se modifica usando los metodos de la clase StyledEditorKit que ya habia sido mencionada
		 */
		JButton izquierda = new JButton(new ImageIcon(KiwiEditor.class.getResource("/images/izquierda.png")));
		izquierda.addActionListener(new StyledEditorKit.AlignmentAction("IZQUIEDA", 0));
		barraHerramientas.add(izquierda);
		JButton derecha = new JButton(new ImageIcon(KiwiEditor.class.getResource("/images/derecha.png")));
		derecha.addActionListener(new StyledEditorKit.AlignmentAction("DERECHA", 2));
		barraHerramientas.add(derecha);
		JButton centrado = new JButton(new ImageIcon(KiwiEditor.class.getResource("/images/centrado.png")));
		centrado.addActionListener(new StyledEditorKit.AlignmentAction("CENTRADO", 1));
		barraHerramientas.add(centrado);
		JButton justificado = new JButton(new ImageIcon(KiwiEditor.class.getResource("/images/justificado.png")));
		justificado.addActionListener(new StyledEditorKit.AlignmentAction("JUSTIFICADO", 3));
		barraHerramientas.add(justificado);
		//--------------------------------------------------
		barraHerramientas.setOrientation(JToolBar.VERTICAL);
		add(barraHerramientas,BorderLayout.WEST);
		//JLABEL RUTA DOCUMENTO------
		ruta = new JLabel("Abra o guarde un archivo");
		//------------------------------------------------------MENÚ BÁSICO----------------------------------------------------------
		/**
		 * Construccion de los menus
		 */
		JMenuBar barra = new JMenuBar();
		JMenu archivo = new JMenu("Archivo");
		abrir = new JMenuItem("Abrir");
		guardar_como = new JMenuItem("Guardar Como"); guardar = new JMenuItem("Guardar") ;
		salir = new JMenuItem ("Salir");
		//CREACIÓN DE JMENU ESTILO & TAMAÑO---------------------------------------------------------------------------------------------------
		estilo = new JMenu("Estilo"); tamaño = new JMenu("Tamaño");
		//--------------------------------------------------JMENU & JMENUITEM FUENTE----------------------------------------------------------
		/**
		 * Uso de la clase "ambiente grafico" para obtener los nombres de las fuentes disponibles en el ordenador
		 * para que luego las recoja en un array de String que luego es deglosado por la clase MenuScroller
		 * que es una clase descargada del internet
		 */
		String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (int i=0; i<fonts.length-1; i++){
			fuente = new JMenu("Fuentes");
			new MenuScroller(fuente, 8, 125, 5, 5);
			for (int d = 0; d<fonts.length-1; d++){
				fuente.add(new JMenuItem(fonts[d]));
				/**
				 * Metodo automatizado que llama al metodo "AccionFuente" que se encarga de generar y acoplar
				 * el escuchador a cada una de las fuentes del array de String usando un metodo parecido al usado
				 * para cambiar de color el texto pero con una "Constante de Estilo" distinta.
				 */
				fuente.getItem(d).addActionListener(new AccionFuente(fuente.getItem(d)));
			}
		}
		//--------------------------------------------------JMENUITEM ESTILOS------------------------------------------------------------------
		/**
		 * Menus del panel que se encuentra en la posicion norte de la ventana,
		 * No solo tienen sus propios escuchadores si que tambien se les puso un "acelerador"
		 * para que se ejecute la accion usando una combinacion de teclas
		 */
		JMenuItem negrita = new JMenuItem("Negrita");
		negrita.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		JMenuItem cursiva = new JMenuItem("Cursiva");
		cursiva.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_DOWN_MASK));
		JMenuItem subrayado = new JMenuItem("Subrayado");
		subrayado.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		negrita.addActionListener(new StyledEditorKit.BoldAction());
		cursiva.addActionListener(new StyledEditorKit.ItalicAction());
		subrayado.addActionListener(new StyledEditorKit.UnderlineAction());
		estilo.add(negrita);
		estilo.add(cursiva);
		estilo.add(subrayado);
		//--------------------------------------------------JMENUITEM TAMAÑO--------------------------------------------------------------------
		/**
		 * El tamaño de la fuente es modificado por un grupo de radio buttons:
		 */
		ButtonGroup tamañogroup = new ButtonGroup();
		JRadioButtonMenuItem tamaño12 = new JRadioButtonMenuItem("12");
		JRadioButtonMenuItem tamaño16 = new JRadioButtonMenuItem("16");
		JRadioButtonMenuItem tamaño20 = new JRadioButtonMenuItem("20");
		JRadioButtonMenuItem tamaño24 = new JRadioButtonMenuItem("24");
		tamañogroup.add(tamaño12);
		tamañogroup.add(tamaño16);
		tamañogroup.add(tamaño20);
		tamañogroup.add(tamaño24);
		tamaño12.addActionListener(new StyledEditorKit.FontSizeAction("cambiarT,", 12));
		tamaño16.addActionListener(new StyledEditorKit.FontSizeAction("cambiarT,", 16));
		tamaño20.addActionListener(new StyledEditorKit.FontSizeAction("cambiarT,", 20));
		tamaño24.addActionListener(new StyledEditorKit.FontSizeAction("cambiarT,", 24));
		tamaño.add(tamaño12);
		tamaño.add(tamaño16);
		tamaño.add(tamaño20);
		tamaño.add(tamaño24);
		//-----------------------------------------------------------------------------------------------------------------------------------------------
		archivo.add(abrir);
		archivo.add(guardar);
		archivo.add(guardar_como);
		archivo.add(salir);
		abrir.addActionListener(new Accionador());
		guardar_como.addActionListener(new Accionador()); guardar.addActionListener(new Accionador());
		salir.addActionListener(new Accionador());
		barra.add(archivo);
		barra.add(fuente);
		barra.add(estilo);
		barra.add(tamaño);
		//------------------------------------------------------------LÁMINA SECUNDARIA-----------------------------------------------------------
		JPanel laminaSecundaria = new JPanel();
		laminaSecundaria.setLayout(new GridLayout(1,2));
			
		laminaSecundaria.add(barra);
		laminaSecundaria.add(ruta);
		add(laminaSecundaria,BorderLayout.NORTH);
		//-----------------------------------------------------------DOCUMENTO EN EL QUE ESCRIBIR-----------------------------------------------------
		documento = new JTextPane();
		add(documento,BorderLayout.CENTER);	
	}
	//DECLARACIÓN DE VARIABLES------------------------------------------------------------------------------------------------------------------------------------
	private String nombrefuente;
	private ImageIcon iconoNegrita, iconoCursiva, iconoUnderline;
	private JButton b_negrita, b_cursiva, b_underline;
	private JMenu fuente, estilo, tamaño;
	private JMenuItem abrir, guardar, guardar_como, salir;
	private JLabel ruta;
	private JTextPane documento;
	private JToolBar barraHerramientas;
	//MÉTODO BARRA HERRAMIENTAS----------------------------------------
	/*public JButton ConfiguraBarra(String icono){
		JButton miboton = new JButton(new ImageIcon(icono));
		barraHerramientas.add(miboton);
		return miboton;
	}*/
	//------------------------------------------------------CLASE QUE REALIZA LA ACCION DE LA FUENTE--------------------------------------------------------
	private class AccionFuente implements ActionListener{
		
		private JMenuItem nombre;
	
		public AccionFuente(JMenuItem nombre){
			this.nombre=nombre;
		}
		public void actionPerformed(ActionEvent e) {
			//---------------ACCIÓN FUENTE MENÚ BÁSICO--------------------------
			MutableAttributeSet attributeSet = new SimpleAttributeSet();
			nombrefuente=nombre.getText();
			StyleConstants.setFontFamily(attributeSet, nombrefuente);
			documento.setCharacterAttributes(attributeSet, false);
		}
	}
	//--------------------------------------CLASE QUE REALIZA LA ACCIÓN DE GUARDAR, ABRIR, SALIR & CAMBIO DE ICONOS---------------------------------------------
	private class Accionador implements ActionListener, MouseListener{
		
		public Accionador(){};
		
		public void actionPerformed(ActionEvent e) {
			//---------------ACCIÓN GUARDAR COMO------------------------------------------------------------------
			/**
			 * Metodos de guardar como y abrir usan todos un JFileChooser ya sea para escoger el archivo que
			 * se quiere abrir o para escoger el archivo donde se guardara el nuevo documento rtf
			 */
			if (e.getSource()==guardar_como){
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Guardar como...");
				int resultfileChooser = fileChooser.showSaveDialog(null);
				if (resultfileChooser==JFileChooser.APPROVE_OPTION){
					/**
					 * Tanto guardar como guardar como usan lienas en comun que fueron reagrupadas en un
					 * unico metodo privado "Guardar"
					 */
					Guardar(fileChooser.getSelectedFile().getAbsoluteFile()+".rtf");
				} else {
					JOptionPane.showMessageDialog(null, "Se ha abortado la elección del directorio");
				}
				//---------------ACCIÓN GUARDAR------------------------------------------------------------------
			} else if (e.getSource()==guardar){
				/**
				 * El metodo guardar coje la ruta del ultimo archivo modificado la cual ha sido almacenada en un JLabel
				 */
				if (ruta.getText().compareTo("Abra o guarde un archivo")==0){
					JOptionPane.showMessageDialog(null, "\"Abra\" o \"Guarde Como\" un archivo para poder guardar");
				} else {
					Guardar(ruta.getText());
				}
				//---------------ACCIÓN ABRIR------------------------------------------------------------------
			} else if (e.getSource()==abrir){
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Seleccione el documento");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("DOCUMENTOS DE TEXTO", "txt", "rtf");
				fileChooser.setFileFilter(filter);
				int resultfileChooser = fileChooser.showOpenDialog(null);
				if (resultfileChooser==JFileChooser.APPROVE_OPTION){
					documento.setContentType("text/rtf");
					EditorKit rtf_kit = documento.getEditorKitForContentType("text/rtf");
					try {
						rtf_kit.read(new FileReader(fileChooser.getSelectedFile()), documento.getDocument(), 0);
						rtf_kit=null;
						ruta.setText(fileChooser.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Se ha abortado la elección del documento");
				}
				//---------------ACCIÓN SALIR------------------------------------------------------------------
			} else if (e.getSource()==salir) {
				System.exit(0);
			}
		}
		private void Guardar(String rutaSeleccionada){
			/**
			 * Este metodo solo coge una ruta y guarda el texto en esa ruta
			 */
			StyledDocument document = (StyledDocument)documento.getDocument();
			RTFEditorKit rtf_kit = new RTFEditorKit();
			FileOutputStream outputStream;
			try{
				outputStream = new FileOutputStream(rutaSeleccionada);
				rtf_kit.write(outputStream, document, 0, document.getLength());
				ruta.setText(rutaSeleccionada);
			} catch (Exception e1){JOptionPane.showMessageDialog(null, "Ha ocurrido un error al guardar");}
		}
		//---------------CAMBIADORES DE ICONOS------------------------------------------------------------------
		/**
		 * Escuchadores del raton que realizan los cambios de imagenes
		 */
		public void mouseClicked(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			if (e.getSource()== b_negrita){
				iconoNegrita.setImage(new ImageIcon(KiwiEditor.class.getResource("/images/BotonNegritaPresionadoPEQUE.png")).getImage());
			} else if (e.getSource()== b_cursiva){
				iconoCursiva.setImage(new ImageIcon(KiwiEditor.class.getResource("/images/BotonCursivaPresionadoPEQUE.png")).getImage());
			}  else if (e.getSource()== b_underline){
				iconoUnderline.setImage(new ImageIcon(KiwiEditor.class.getResource("/images/BotonSubrayadoPresionadoPEQUE.png")).getImage());
			}
		}
		public void mouseReleased(MouseEvent e) {
			if (e.getSource()== b_negrita){
				iconoNegrita.setImage(new ImageIcon(KiwiEditor.class.getResource("/images/BotonNegritaSeleccionadoPEQUE.png")).getImage());
			} else if (e.getSource()== b_cursiva){
				iconoCursiva.setImage(new ImageIcon(KiwiEditor.class.getResource("/images/BotonCursivaSeleccionadoPEQUE.png")).getImage());
			} else if (e.getSource()== b_underline){
				iconoUnderline.setImage(new ImageIcon(KiwiEditor.class.getResource("/images/BotonSubrayadoSeleccionadoPEQUE.png")).getImage());
			}
		}
		public void mouseEntered(MouseEvent e) {
			if (e.getSource()== b_negrita){
				iconoNegrita.setImage(new ImageIcon(KiwiEditor.class.getResource("/images/BotonNegritaSeleccionadoPEQUE.png")).getImage());
			} else if (e.getSource()== b_cursiva){
				iconoCursiva.setImage(new ImageIcon(KiwiEditor.class.getResource("/images/BotonCursivaSeleccionadoPEQUE.png")).getImage());
			} else if (e.getSource()== b_underline){
				iconoUnderline.setImage(new ImageIcon(KiwiEditor.class.getResource("/images/BotonSubrayadoSeleccionadoPEQUE.png")).getImage());
			}
		}
		public void mouseExited(MouseEvent e) {
			if (e.getSource()== b_negrita){
				iconoNegrita.setImage(new ImageIcon(KiwiEditor.class.getResource("/images/BotonNegritaPEQUE.png")).getImage());
			} else if (e.getSource()== b_cursiva){
				iconoCursiva.setImage(new ImageIcon(KiwiEditor.class.getResource("/images/BotonCursivaPEQUE.png")).getImage());
			} else if (e.getSource()== b_underline){
				iconoUnderline.setImage(new ImageIcon(KiwiEditor.class.getResource("/images/BotonSubrayadoPEQUE.png")).getImage());
			}
		}
	}
}
