/** 
 * Filename: BMP_Displayer.java
 * Author: Nicholas Hoekstra
 * 
 * Created: October 16, 2012
 * Last Updated: October 18, 2012
 * 
 */

package app;

import java.io.*;
import java.util.Scanner;
import javax.swing.*;
//import java.awt.*;
import java.awt.image.BufferedImage;

public class BMP_Displayer {

	public static void main(String[] args) {
		BMP bitmap = null;
		boolean importSuccess = true;
		boolean tryAgain = false;
		Scanner scan = new Scanner(System.in);
		
		do {
			try {
				bitmap = importBMP();

				System.out.println("The bitmap was successfully imported.");
				importSuccess = true;
				
			} catch (IOException e) {
				System.out.println("Exception caught, there was an error reading the file. The file was not a bitmap. Bitmap import aborted.");
				System.out.println("Would you like to try another image? (y/n): ");
				String response = scan.next();
			
				if (response.equalsIgnoreCase("y"))
					tryAgain = true;
				else
					tryAgain = false;
				
				importSuccess = false;	
			}
		} while (tryAgain);
		
		// Display the images if the bitmap was successfully read.
		if (importSuccess) {
			displayImage(bitmap);
		}
		
		System.out.println("Program execution finished.");
		
		System.exit(0);
			
	} // end main
	
	// USED TO IMPORT THE BMP DATA
	public static BMP importBMP() throws IOException {
		boolean problem = false;
		File bmpFile = null;
		FileInputStream input = null;
		DataInputStream data = null;
		
		// Find the bitmap file to read.
		do {
			try {
				bmpFile = new File(getFileName());
				input = new FileInputStream(bmpFile);
				data = new DataInputStream(input);
				problem = false;
			}
			catch (FileNotFoundException e) {
				System.out.println("The file name entered was not found. Please try again.");
				problem = true;			
			}
		} while (problem);
		
		// Create a bitmap object.
		BMP bitmap = new BMP();
		
		// Read bitmap header and store data (data is all read as little-endian format)
		bitmap.setType(Short.reverseBytes(data.readShort()));
		bitmap.setSize(Integer.reverseBytes(data.readInt()));
		bitmap.setBlank1(Short.reverseBytes(data.readShort()));
		bitmap.setBlank2(Short.reverseBytes(data.readShort()));
		bitmap.setOffset(Integer.reverseBytes(data.readInt()));
		
		// Check the bitmap header type 19778 = BM
		if (bitmap.getType() != 19778)
			throw new IOException();
		
		// Read bitmap header info and store data
		bitmap.setHeadSize(Integer.reverseBytes(data.readInt()));
		bitmap.setWidth(Integer.reverseBytes(data.readInt()));
		bitmap.setHeight(Integer.reverseBytes(data.readInt()));
		bitmap.setPlanes(Short.reverseBytes(data.readShort()));
		bitmap.setBits(Short.reverseBytes(data.readShort()));
		bitmap.setCompression(Integer.reverseBytes(data.readInt()));
		bitmap.setImageSize(Integer.reverseBytes(data.readInt()));
		bitmap.setXres(Integer.reverseBytes(data.readInt()));
		bitmap.setYres(Integer.reverseBytes(data.readInt()));
		bitmap.setNumColours(Integer.reverseBytes(data.readInt()));
		bitmap.setImptColours(Integer.reverseBytes(data.readInt()));
		
		// Read pixel data into a byte array
		byte[] pixelArray = new byte[bitmap.getWidth()*3*bitmap.getHeight()]; // Possible missing 2 bytes?
		for (int index = 0; index < bitmap.getWidth()*3*bitmap.getHeight(); index++) {
			pixelArray[index] = data.readByte();
		}
		
		bitmap.setPixelArray(pixelArray);
		
		return bitmap;
	} // end importBMP
	
	// USED TO GET THE FILE NAME OF THE BITMAP
	public static String getFileName() {
		System.out.print("Enter the full path for the Bitmap file you wish to open: ");
		Scanner scan = new Scanner(System.in);
		String fileName = scan.nextLine();
		return fileName;
	} // end getFileName
	
	// USED TO DISPLAY ALL THE DIFFERENT BITMAP IMAGES
	public static void displayImage(BMP bitmap) {
		BufferedImage bmp = new BufferedImage(bitmap.getWidth(), bitmap.getHeight(), BufferedImage.TYPE_INT_RGB);
		int byteIndex = 0; // Used to track the index of the bitmap byte array

// DISPLAY THE ORIGINAL IMAGE		
		for (int row = 0; row < bitmap.getHeight(); row++) {
			for (int col = 0; col < bitmap.getWidth(); col++) {
				
				// Get RGB values and change into one int of form 0xRRGGBB
				int rgb = (bitmap.getPixelArray()[byteIndex+2] & 0xFF); // add red
				rgb = (rgb << 8) + (bitmap.getPixelArray()[byteIndex+1] & 0xFF); // add green
				rgb = (rgb << 8) + (bitmap.getPixelArray()[byteIndex] & 0xFF); // add blue
				
				byteIndex = byteIndex+3; // skip to start of next pixel RGB index
				bmp.setRGB(col, bitmap.getHeight()-1 - row, rgb); // pixel data starts from lower left corner
																  // and goes up left to right
			}
		}
		
		// Create the GUI used to display the images
		JFrame jf = new JFrame();
		
		jf.setTitle("Bitmap Displayer");
		jf.setSize(500, 500);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
		
		JLabel label = new JLabel(new ImageIcon(bmp));
		jf.getContentPane().add(label);
		SwingUtilities.updateComponentTreeUI(jf);
		
		continuePrompt();
		byteIndex = 0;

// DISPLAY THE 0.5U / 0.5V IMAGE
		for (int row = 0; row < bitmap.getHeight(); row++) {
			for (int col = 0; col < bitmap.getWidth(); col++) {
				
				// Get RGB values
				int r = (bitmap.getPixelArray()[byteIndex+2] & 0xFF);
				int g = (bitmap.getPixelArray()[byteIndex+1] & 0xFF);
				int b = (bitmap.getPixelArray()[byteIndex] & 0xFF);
				
				// Convert to YUV set U=>0.5U V=>0.5V
				double y = 0.299*r + 0.587*g + 0.114*b;
				double u = (-0.14713*r - 0.28886*g + 0.436*b)*0.5;
				double v = (0.615*r - 0.51499*g - 0.10001*b)*0.5;
				
				// Convert back to RGB
				r = (int) (y + 1.13983*v);
				g = (int) (y - 0.39465*u - 0.58060*v);
				b = (int) (y + 2.03211*u);
				
				// Change into one int of form 0xRRGGBB
				int rgb =  r; // add red
				rgb = (rgb << 8) +  g; // add green
				rgb = (rgb << 8) + b; // add blue
				
				byteIndex = byteIndex+3;
				bmp.setRGB(col, bitmap.getHeight()-1 - row, rgb);
			}
		}
		
		jf.getContentPane().remove(label);
		JLabel label2 = new JLabel(new ImageIcon(bmp));
		jf.getContentPane().add(label2);
		SwingUtilities.updateComponentTreeUI(jf);
		
		continuePrompt();
		byteIndex = 0;

// DISPLAY THE GREYSCALE IMAGE
		
		for (int row = 0; row < bitmap.getHeight(); row++) {
			for (int col = 0; col < bitmap.getWidth(); col++) {
				
				// Get RGB values
				int r = (bitmap.getPixelArray()[byteIndex+2] & 0xFF);
				int g = (bitmap.getPixelArray()[byteIndex+1] & 0xFF);
				int b = (bitmap.getPixelArray()[byteIndex] & 0xFF);
				
				// Find the Y (Luminance) value
				double y = 0.299*r + 0.587*g + 0.114*b;
				
				// Convert back to RGB (Grayscale only uses Y)
				r = (int) y;
				g = (int) y;
				b = (int) y;
				
				// Change into one int of form 0xRRGGBB
				int rgb =  r; // add red
				rgb = (rgb << 8) +  g; // add green
				rgb = (rgb << 8) + b; // add blue
				
				byteIndex = byteIndex+3;
				bmp.setRGB(col, bitmap.getHeight()-1 - row, rgb);
			}
		}
		
		jf.getContentPane().remove(label2);
		JLabel label3 = new JLabel(new ImageIcon(bmp));
		jf.getContentPane().add(label3);
		SwingUtilities.updateComponentTreeUI(jf);
		
		continuePrompt();
		byteIndex = 0;

// DISPLAY THE ORDERED DITHERED IMAGE
		//int[][] matrix = {{0,8,2,10}, {12,4,14,6}, {3,11,1,9}, {15,7,13,5}};
		int[][] matrix = {{7,12,1,4},{2,13,8,11},{16,3,10,5},{9,6,15,4}};
		int matrixRow = 0;
		int matrixCol = 0;
		
		for (int row = 0; row < bitmap.getHeight(); row++) {
			for (int col = 0; col < bitmap.getWidth(); col++) {
				
				// Get RGB values
				int r = (bitmap.getPixelArray()[byteIndex+2] & 0xFF);
				int g = (bitmap.getPixelArray()[byteIndex+1] & 0xFF);
				int b = (bitmap.getPixelArray()[byteIndex] & 0xFF);
				
				// Find the Y (Luminance) value
				double y = 0.299*r + 0.587*g + 0.114*b;
				
				// Normalize Y
				y = y/16;
				
				// Dither the value
				if ((int) y > matrix[matrixRow][matrixCol])
					y = 255;
				else
					y = 0;
				
				// Convert back to RGB (Grayscale only uses Y)
				r = (int) y;
				g = (int) y;
				b = (int) y;
				
				// Change into one int of form 0xRRGGBB
				int rgb =  r; // add red
				rgb = (rgb << 8) +  g; // add green
				rgb = (rgb << 8) + b; // add blue
				
				byteIndex = byteIndex+3;
				bmp.setRGB(col, bitmap.getHeight()-1 - row, rgb);
				
				matrixCol++;
				if (matrixCol == 4)
					matrixCol = 0;
			}
			matrixRow++;
			if (matrixRow == 4)
				matrixRow = 0;
		}
		
		jf.getContentPane().remove(label3);
		JLabel label4 = new JLabel(new ImageIcon(bmp));
		jf.getContentPane().add(label4);
		SwingUtilities.updateComponentTreeUI(jf);
		
		continuePrompt();
		
	} // end displayImage
	
	// USED TO WAIT FOR USER PROMPT BEFORE CONTINUING
	public static void continuePrompt() {
		System.out.print("Press enter in console to continue.");
		Scanner scan = new Scanner(System.in);
		scan.nextLine();
	} // end continuePrompt
	
} // end class BMP_Displayer

