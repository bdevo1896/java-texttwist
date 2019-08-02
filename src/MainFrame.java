import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.BoxLayout;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.util.concurrent.TimeoutException;

import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.Color;

/**
 * This class will display the GUI for the game.
 * @author User
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame{
	private static final int MAX_Y = 800;//The max y length for the window
	private static final int MAX_X = 1000;//The max x length for the window
	private JTextField txtGuess;//The text box containing the user's guess
	private JLabel lblScoreDisplay;//Displays the score
	private JLabel lblTimeDisplay;//Displays the count down timer
	private JLabel lblLevelDisplay;//Displays the level the game is on
	private Timer gameTimer;//Used to count down the time every second
	private JButton[] letterButtons;//List containing references to the buttons the user presses to input a letter
	private WordGame controller;//The class delegates to the WordDictionary object.
	private DefaultListModel<String> model;//Used to display used words
	private JButton btnEnter;//Used to input the user guess for checking
	private JButton btnRefresh;//Used to display new buttons to be clicked on

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		letterButtons = new JButton[10];
		controller = new WordGame();
		
		JPanel mainPanel = new JPanel();//The main container for the panels.
		mainPanel.setBackground(Color.BLUE);
		mainPanel.setForeground(Color.WHITE);
		mainPanel.setMaximumSize(new Dimension(MAX_X, MAX_Y));
		mainPanel.setMinimumSize(new Dimension(MAX_X, MAX_Y));
		mainPanel.setPreferredSize(new Dimension(MAX_X, MAX_Y));
		mainPanel.setSize(MAX_X,MAX_Y);
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout(0, 0));
		getContentPane().add(mainPanel);
		
		/**
		 * Creating the main GUI element for the window. This panel will include all of the button panels
		 * , the dashboard, and the decorations to the panel.
		 */
		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(new Color(135, 206, 250));
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		JPanel pnlDecoration = new JPanel();
		pnlDecoration.setBackground(new Color(135, 206, 250));
		centerPanel.add(pnlDecoration);
		/*
		 * Loading a reference to the image file for the Text Twist game icon to a ImageIcon object
		 */
		ImageIcon icon = null;
		try {
			icon = new ImageIcon(ImageIO.read(getClass().getResource("textTwistI.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel lblTitle = new JLabel(icon);
		pnlDecoration.add(lblTitle);
		
		JPanel pnlWordDisplay = new JPanel();
		pnlWordDisplay.setBackground(new Color(135, 206, 250));
		centerPanel.add(pnlWordDisplay);
		
		txtGuess = new JTextField();
		txtGuess.setText("");
		pnlWordDisplay.add(txtGuess);
		txtGuess.setColumns(10);
		
		JPanel pnlLetterSet = new JPanel();
		pnlLetterSet.setBackground(new Color(135, 206, 250));
		centerPanel.add(pnlLetterSet);
		FlowLayout fl_pnlLetterSet = new FlowLayout(FlowLayout.CENTER, 5, 5);
		pnlLetterSet.setLayout(fl_pnlLetterSet);
		InputButtonListener inputAction = new InputButtonListener();
		for(int i = 0; i < letterButtons.length; i++){
			letterButtons[i]=makeButton("X");
			letterButtons[i].addActionListener(inputAction);
			pnlLetterSet.add(letterButtons[i]);
		}
		
		JPanel pnlMainButtons = new JPanel();
		pnlMainButtons.setBackground(new Color(135, 206, 250));
		centerPanel.add(pnlMainButtons);
		
		JButton btnTwist = makeButton("Twist");
		btnTwist.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				onTwist();
			}
		});
		pnlMainButtons.add(btnTwist);
		
		btnEnter = makeButton("Enter");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onEnter();
			}
		});
		pnlMainButtons.add(btnEnter);
		
		btnRefresh = makeButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onRefresh();
			}
		});
		pnlMainButtons.add(btnRefresh);
		
		JButton btnLastWord = makeButton("Last Word");
		btnLastWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onLastWord();
			}
		});
		pnlMainButtons.add(btnLastWord);
		
		JButton btnClear = makeButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onClear();
			}
		});
		pnlMainButtons.add(btnClear);
		
		JButton btnLookUp = makeButton("Look Up");
		btnLookUp.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				onLookUp();
			}
		});
		pnlMainButtons.add(btnLookUp);
		
		JPanel pnlDashBoard = new JPanel();
		pnlDashBoard.setBackground(new Color(135, 206, 250));
		FlowLayout flowLayout_1 = (FlowLayout) pnlDashBoard.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		centerPanel.add(pnlDashBoard);
		
		JLabel lblScore = makeLabel("Score");
		pnlDashBoard.add(lblScore);
		
		lblScoreDisplay = makeLabel("0");
		pnlDashBoard.add(lblScoreDisplay);
		
		JLabel lblTime = makeLabel("Time");
		pnlDashBoard.add(lblTime);
		
		lblTimeDisplay = makeLabel("0:00");
		pnlDashBoard.add(lblTimeDisplay);
		
		JLabel lblLevel = makeLabel("Level");
		pnlDashBoard.add(lblLevel);
		
		lblLevelDisplay = makeLabel(controller.getLevel()+"");
		pnlDashBoard.add(lblLevelDisplay);
		
		Component verticalGlue = Box.createVerticalGlue();
		centerPanel.add(verticalGlue);
		
		JPanel pnlQuit = new JPanel();
		pnlQuit.setBackground(new Color(135, 206, 250));
		FlowLayout flowLayout = (FlowLayout) pnlQuit.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		centerPanel.add(pnlQuit);
		
		JButton btnQuit = makeButton("Quit");
		btnQuit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		pnlQuit.add(btnQuit);
		
		model = new DefaultListModel<String>();
		JList<String> list = new JList<String>(model);
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setMaximumSize(new Dimension(258, 130));
		scrollPane.setMinimumSize(new Dimension(258, 130));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		mainPanel.add(scrollPane, BorderLayout.WEST);
		
		scrollPane.setViewportView(list);
		this.pack();
		
		TimeDisplay tD = new TimeDisplay();
		tD.setTime();
		gameTimer = new Timer(1000,tD);
		gameTimer.start();
		changeButtons(this.convertList(controller.newLetters(controller.getLevel()+2)));
	}
	
	/**
	 * This method makes a handle for a typical button look.
	 * @param args
	 */
	public JButton makeButton(String lbl){
		JButton rtnBtn = new JButton(lbl);
		rtnBtn.setFont(new Font("Arial",Font.BOLD,12));
		return rtnBtn;
	}
	
	/**
	 * This method makes a handle for a typical label look.
	 * @param args
	 */
	public JLabel makeLabel(String lbl){
		JLabel rtnLbl = new JLabel(lbl);
		rtnLbl.setFont(new Font("Arial",Font.PLAIN,15));
		return rtnLbl;
	}
	
	/**
	 * This method converts a char array to a String array.
	 */
	public String[] convertList(char[] list){
		String[] rtnList = new String[list.length];
		
		for(int i = 0; i < rtnList.length; i++){
			rtnList[i] = list[i]+"";
		}
		
		return rtnList;
	}
	
	/**
	 * This method will set the specific amount of buttons to the specific characters provided by the controller.
	 */
	public void resetLetterButtons(){
		for(JButton btn: letterButtons){
			btn.setVisible(false);
		}
		
		for(int i = 0; i < controller.getLevel()+2; i++){
			JButton btn = letterButtons[i];
			//btn.setText(letterButtons[i]+"");
			btn.setVisible(true);
		}
	}
	
	/**
	 *This method will be called when the user presses the Enter button.
	 */
	public void onEnter(){
		try {
			if(controller.testWord(txtGuess.getText())){
				lblScoreDisplay.setText(controller.getPoints()+"");
				model.addElement(txtGuess.getText());
				txtGuess.setText("");
				changeButtons(this.convertList(controller.newLetters(controller.getLevel()+2)));
				lblLevelDisplay.setText(controller.getLevel()+"");
				if(controller.isGameOver()){
					endGame();
				}
				
			}else{
				onClear();
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this,"Error","File Not Found",JOptionPane.ERROR_MESSAGE);
		} catch (UsedWordException e) {
			JOptionPane.showMessageDialog(this,"Already used that word.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method will display new characters to use.
	 */
	public void onRefresh(){
		changeButtons(this.convertList(controller.newLetters(controller.getLevel()+2)));
	}
	
	/**
	 * This method will run the game over sequence.
	 */
	public void endGame(){
		gameTimer.stop();
		btnEnter.setEnabled(false);
		btnRefresh.setEnabled(false);
		JOptionPane.showMessageDialog(this, "Game Over","Final Score: "+(controller.getPoints()+controller.getLvlPoints()),JOptionPane.PLAIN_MESSAGE);
		//boolean newGame = JOptionPane.sh
	}
	
	/**
	 * This method will be called when the user presses the Twist Button.
	 */
	public void onTwist(){
		controller.shuffleLetters();
		changeButtons(controller.getLetters());
		lblScoreDisplay.setText(controller.getPoints()+"");
	}
	
	/**
	 * This method will be called when the user presses the Last Word Button.
	 */
	public void onLastWord(){
		txtGuess.setText(controller.getWordList().get(controller.getWordList().size()-1));
		repaint();
	}
	
	/**
	 * This method will be called when the user presses the Clear Button.
	 */
	public void onClear(){
		txtGuess.setText("");
		for(int i = 0; i < controller.getLevel()+2;i++){
			letterButtons[i].setVisible(true);
		}
	}
	
	/**
	 * This method will show a pane for the user to enter a phrase to see a list of words.
	 */
	public void onLookUp(){
		String str = JOptionPane.showInputDialog("Enter a phrase to find words.");
		System.out.println("-----WORDS FOUND FOR: "+str+"-----");
		controller.printUsableWords(str);
	}
	
	/**
	 * This method will input the buttons character into the guess box.
	 */
	public void onInput(JButton btn){
		txtGuess.setText(txtGuess.getText()+btn.getText());
		btn.setVisible(false);
		repaint();
	}
	
	/**
	 * This method will remove all the letter set buttons and replace them with new ones.
	 */
	public void changeButtons(String[] list){
		for(JButton btn: letterButtons){
			btn.setVisible(false);
		}
		
		for(int i = 0; i < list.length; i++){
			letterButtons[i].setText(list[i]);
			letterButtons[i].setVisible(true);
		}
	}
	
	/**
	 *This class will allow for the buttons to input a letter into the guessBox.
	 */
	private class InputButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			onInput((JButton)arg0.getSource());
		}
		
	}
	
	/**
	 * This class will decrement the timer and 
	 * @author User
	 *
	 */
	private class TimeDisplay implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			setTime();
		}
		
		public void setTime(){
			try {
				controller.decrementTimer();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
			if(controller.isGameOver()){
				endGame();
			}
			int time = controller.getTimer();
			int minutes = time / 60;
			int seconds = time - minutes*60;
			if(seconds > 9)
				lblTimeDisplay.setText(minutes+":"+seconds);
			else
				lblTimeDisplay.setText(minutes+":0"+seconds);

		}
		
	}
	
	public static void main(String[] args){
		MainFrame mf = new MainFrame();
	}
}
