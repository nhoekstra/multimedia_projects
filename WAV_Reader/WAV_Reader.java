/** 
 * Filename: WAV_Reader.java
 * Author: Nicholas Hoekstra
 * 
 * Created: October 18, 2012
 * Last Updated: October 22, 2012
 * 
 */

package app;

import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;

public class WAV_Reader extends JFrame implements ActionListener {
	static JFrame jf;
	static Container display;
	static JFileChooser fc;
	static JLabel statusLabel;
	
	static JButton fadeButton;
	static JLabel sampleLabel;
	static JFrame frame;
	static boolean faded = false;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		WAV_Reader wavreader = new WAV_Reader();

	} // end main

	// Constructs the GUI
	public WAV_Reader() {
		// Create the JFrame window
		jf = new JFrame();
		display = jf.getContentPane();
		display.setLayout(new FlowLayout(FlowLayout.LEFT));

		jf.setTitle("WAV File Reader");
		jf.setSize(450, 100);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add read file button to display
		JButton readButton = new JButton("Open File");
		readButton.addActionListener(this);
		display.add(readButton);

		// Add user feedback label
		statusLabel = new JLabel();
		display.add(statusLabel);

		// Create the file chooser
		fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);	
		FileFilter filter = new FileNameExtensionFilter("WAV Files", "wav");
		fc.addChoosableFileFilter(filter);

		validate();
		jf.setVisible(true);
	} // end WAV_Reader

	// Action listener for the Open File button
	public void actionPerformed(ActionEvent event) {
		int userSelect = fc.showOpenDialog(this);

		if (userSelect == JFileChooser.APPROVE_OPTION) {
			File wavFile = fc.getSelectedFile();

			WAV audio = null;

			try {
				audio = readFile(wavFile);

				displayWaveform(audio);
				statusLabel.setText("The WAV file was successfully read.");
			} 
			catch (IOException e) {
				statusLabel.setText("There was an error reading the file. Please select another file and try again.");
			}

		} // end if user selects approve

	} // end actionPerformed

	// Reads the WAV File
	public static WAV readFile(File wavFile) throws IOException{
		DataInputStream data = null;

		// Creates data stream from file
		try {
			data = new DataInputStream(new FileInputStream(wavFile));
		} 
		catch (FileNotFoundException e) {
			System.out.println("This error should not occur.");
		}

		// Create new WAV object
		WAV audio = new WAV();

		// RIFF chunk descriptor
		audio.setChunkID(data.readInt());
		audio.setChunkSize(Integer.reverseBytes(data.readInt()));
		audio.setFormat(data.readInt());

		if (audio.getFormat() != 1463899717)
			throw new IOException();

		// fmt sub-chunk
		audio.setSubChunk1ID(data.readInt());
		audio.setSubChunk1Size(Integer.reverseBytes(data.readInt()));
		audio.setAudioFormat(Short.reverseBytes(data.readShort()));
		audio.setNumChannels(Short.reverseBytes(data.readShort()));
		audio.setSampleRate(Integer.reverseBytes(data.readInt()));
		audio.setByteRate(Integer.reverseBytes(data.readInt()));
		audio.setBlockAlign(Short.reverseBytes(data.readShort()));
		audio.setBitsPerSample(Short.reverseBytes(data.readShort()));

		// data sub-chunk
		audio.setSubChunk2ID(data.readInt());
		audio.setSubChunk2Size(Integer.reverseBytes(data.readInt()));

		byte[] audioBytes = new byte[audio.getChunkSize()];

		for (int i = 0; i < audio.getSubChunk2Size(); i++) {
			audioBytes[i] = data.readByte();
		}

		audio.setData(audioBytes);

		return audio;
	} // end readFile

	// Displays the faded in/faded out waveform and number of samples
	public static void displayWaveform(WAV audio) {
		
		// For Testing purposes
		System.out.println("Format: " + audio.getFormat());
		System.out.println("Audio Format: " + audio.getAudioFormat());
		System.out.println("Number of Channels: " + audio.getNumChannels());
		System.out.println("Sample Rate: " + audio.getSampleRate());
		System.out.println("Byte Rate: " + audio.getByteRate());
		System.out.println("Block Align: " + audio.getBlockAlign());
		System.out.println("Bits Per Sample: " + audio.getBitsPerSample());
		System.out.println("Chunk Size: " +audio.getSubChunk2Size());

		// Convert bytes to amplitudes and normalize
		byte[] audioInfo = audio.getData();
		final double[] normInfo = new double[audioInfo.length / 2]; // final to make code work
		int normCount = 0;

		for (int i = 0; i < audioInfo.length; i = i+2) {
			short amplitude = (short) ((audioInfo[i+1] << 8) | audioInfo[i]);
			double normAmplitude = amplitude / 32768.0;
			normInfo[normCount] = normAmplitude;
			normCount++;	
		}
		
		// Add sample count label
		sampleLabel = new JLabel("Number of Samples: " + (audio.getSubChunk2Size() * 8 / audio.getBitsPerSample() / audio.getNumChannels()));
		
		// The fadedButton switches between the original waveform and the faded waveform
		fadeButton = new JButton("Toggle Fade Audio In/Out");
		fadeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (faded == false) {
					frame.getContentPane().add(new DrawWaveform2(sampleLabel, fadeButton, normInfo));
					frame.validate();
					faded = true;
				}
				else if (faded == true) {
					frame.getContentPane().add(new DrawWaveform(sampleLabel, fadeButton, normInfo));
					frame.validate();
					faded = false;
				}
			}
			});

		// Waveform and stats will show in a pop up window
		frame = new JFrame();
		frame.getContentPane().add(new DrawWaveform(sampleLabel, fadeButton, normInfo));
		
		frame.setTitle("WAV File Output");
		frame.setSize(1200, 400);
		
		frame.setVisible(true);

	} // end displayWAV

} // end class WAV_Reader


// Draws the original waveform
class DrawWaveform extends JPanel {

	double[] normInfo;
	
	int zero = 200;
	
	public DrawWaveform(JLabel sampleLabel, JButton fadeButton, double[] normInfo) {
		super();
		this.add(sampleLabel);
		this.add(fadeButton);
		this.normInfo = normInfo;
	} // end DrawWaveform

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black); 
		for (int i = 0; i < normInfo.length-1; i++) {
			g.drawLine(i/8+20, zero + (int) (normInfo[i]*100), (i+1)/8+20, zero + (int) (normInfo[i+1]*100));
		}
	} // end paintComponent
	
} // end class DrawWaveform


// Draws the waveform fading in and out
class DrawWaveform2 extends JPanel {

	double[] normInfo;
	
	int zero = 200;
	
	public DrawWaveform2(JLabel sampleLabel, JButton fadeButton, double[] normInfo) {
		super();
		this.add(sampleLabel);
		this.add(fadeButton);
		this.normInfo = normInfo;
	} // end DrawWaveform2

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.blue);
		int samples = normInfo.length-1;
		for (int i = 0; i < samples; i++) {
			
			if (i < samples/3.0) {
				g.drawLine(i/8+20, zero + (int) ((normInfo[i]*100)*i*1.5/samples), (i+1)/8+20, zero + (int) ((normInfo[i+1]*100)*i*1.5/samples));
			}
			else if ( i > samples-samples/3.0) {
				g.drawLine(i/8+20, zero + (int) ((normInfo[i]*100)*(samples-i)*1.5/samples), (i+1)/8+20, zero + (int) ((normInfo[i+1]*100)*(samples-i)*1.5/samples));
			}
			else {
				g.drawLine(i/8+20, zero + (int) (normInfo[i]*100), (i+1)/8+20, zero + (int) (normInfo[i+1]*100));
			}
			
		} // end for
	} // end paintComponent
	
} // end class DrawWaveform2