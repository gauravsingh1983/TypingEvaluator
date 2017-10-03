package com.typingtest;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class TypingEvaluator
{

	// ============================================================== fields
	private static JLabel				timeField;
	private static JLabel				timeLbl				= new JLabel();
	private static javax.swing.Timer	t					= new javax.swing.Timer(100, new StopWatchListener());
	private static javax.swing.Timer	t1					= new javax.swing.Timer(100, new ClockListener());
	private static JTextArea			area				= null;
	private static JLabel				filenameLabel		= null;
	private static JTextField			filenameTxtfield	= null;
	private static JLabel				showOutputLabel		= null;
	private static JPanel				resultPanel			= null;

	private static long					processingStart;
	private static long					processingEnd;

	private static int					clockTick			= 0;
	private static double				clockTime;
	private static String				clockTimeString;
	private static String[]				resultArr			= null;

	private static String				text				= null;

	private static class StopWatchListener implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			clockTick++;
			clockTime = ((double) clockTick) / 10.0;
			clockTimeString = new Double(clockTime).toString();
			timeLbl.setFont(new Font("Serif", Font.PLAIN, 16));
			timeLbl.setText("Time elapsed : " + clockTimeString);
		}
	}

	private static class ClockListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Calendar now = Calendar.getInstance();
			int h = now.get(Calendar.HOUR_OF_DAY);
			int m = now.get(Calendar.MINUTE);
			int s = now.get(Calendar.SECOND);
			timeField.setText("" + h + ":" + m + ":" + s);
		}
	}

	private static class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("Exiting System");
			System.exit(0);
		}
	}

	private static class WindowHandler implements WindowListener
	{

		@Override
		public void windowOpened(WindowEvent e)
		{
		}

		@Override
		public void windowClosing(WindowEvent e)
		{
			System.out.println("Exiting System");
			System.exit(0);
		}

		@Override
		public void windowClosed(WindowEvent e)
		{
			System.out.println("Exiting System");
			System.exit(0);
		}

		@Override
		public void windowIconified(WindowEvent e)
		{
		}

		@Override
		public void windowDeiconified(WindowEvent e)
		{
		}

		@Override
		public void windowActivated(WindowEvent e)
		{
		}

		@Override
		public void windowDeactivated(WindowEvent e)
		{
		}

	}

	private static class ButtonHandlerStart implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			clockTick = 0;
			showOutputLabel.setText("");
			filenameTxtfield.setEditable(false);
			resultPanel.setVisible(false);
			processingStart = System.currentTimeMillis();
			area.setText("");
			area.setEditable(true);
			t.restart();
			t1.restart();
		}
	}

	private static class ButtonHandlerStop implements ActionListener
	{
		String resultText = null;
		public void actionPerformed(ActionEvent e)
		{
			processingEnd = System.currentTimeMillis();
			area.setEditable(false);

			if (area.getText() != null)
			{

				try
				{
					text = area.getText();
					resultArr = SpellChecker.evalTyping(text, filenameTxtfield.getText());
					resultText = "Result:	[ Speed (CPM) : " + resultArr[0] + ";	 Total time in minutes : "
							+ TimeUnit.MILLISECONDS.toMinutes(processingEnd - processingStart) + ";	 No. of misspelled words : " + resultArr[1] + " ]";
				}
				catch (IOException e1)
				{
					resultText = "Please specify a valid source file !!!";
					System.out.println("Please specify a valid source file to validate the text typed !!");;
				}
			}

			
			System.out.println(resultText);
			showOutputLabel.setText(resultText);
			showOutputLabel.setFont(new Font("Serif", Font.PLAIN, 16));
			showOutputLabel.setForeground(Color.RED);
			resultPanel.setVisible(true);
			t.stop();
			t1.stop();

			// System.out.println(area.getText());
		}
	}

	public static void main(String[] args)
	{
		JButton okButton = new JButton("Exit");
		// okButton.sets
		JButton startButton = new JButton("Start Test");
		JButton stopButton = new JButton("Stop Test");

		ButtonHandler listener = new ButtonHandler();
		okButton.addActionListener(listener);

		ButtonHandlerStart startListener = new ButtonHandlerStart();
		startButton.addActionListener(startListener);

		ButtonHandlerStop stopListener = new ButtonHandlerStop();
		stopButton.addActionListener(stopListener);

		JPanel contentText = new JPanel();
		GridLayout layout = new GridLayout(1, 1);
		contentText.setLayout(layout);
		area = new MyTextArea(31, 10);
		JScrollPane scroll = new JScrollPane(area);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		area.setEditable(false);
		contentText.add(scroll);

		JPanel contentButton = new JPanel();
		GridLayout layout1 = new GridLayout(1, 3);
		contentButton.setLayout(layout1);
		contentButton.add(okButton);
		contentButton.add(startButton);
		contentButton.add(stopButton);

		timeField = new JLabel();
		timeField.setFont(new Font("sansserif", Font.PLAIN, 12));

		JPanel contentField = new JPanel();
		GridLayout layout3 = new GridLayout(1, 4);
		contentField.setLayout(layout3);
		filenameLabel = new JLabel();
		filenameLabel.setText("Enter source filename: ");
		filenameTxtfield = new JTextField(1);
		contentField.add(timeField);
		contentField.add(timeLbl);
		contentField.add(filenameLabel);
		contentField.add(filenameTxtfield);

		resultPanel = new JPanel();
		contentField.setLayout(layout);
		showOutputLabel = new JLabel();
		resultPanel.add(showOutputLabel);
		resultPanel.setVisible(false);

		JPanel content = new JPanel();
		BoxLayout layout2 = new BoxLayout(content, BoxLayout.Y_AXIS);
		content.setLayout(layout2);
		content.add(contentText, BorderLayout.PAGE_START);
		content.add(resultPanel);
		content.add(contentField);
		content.add(contentButton, BorderLayout.PAGE_END);
		JFrame window = new JFrame("TypingEvaluator v1.0");
		window.setContentPane(content);
		window.setSize(600, 600);
		window.setLocation(100, 100);
		window.setVisible(true);
		window.addWindowListener(new WindowHandler());
	}
}