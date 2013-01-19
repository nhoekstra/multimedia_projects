/** 
 * Filename: BMP.java
 * Author: Nicholas Hoekstra
 * 
 * Created: October 16, 2012
 * Last Updated: October 18, 2012
 * 
 */

package app;

class BMP {

	// HEADER DATA	
	short type; // BMP Identifier
	int size; // File size in bytes
	short blank1, blank2; // Empty holders (Should both be 0)
	int offset; // Offset to pixel data after header and info header
	
	// HEADER INFO DATA
	int HeadSize; // Header size in bytes
	int width, height; // Width and height of image
	short planes; // Number of colour planes
	short bits; // Number of bits per pixel
	int compression; // Compression type (This program will not work with compressed 
					 // images though and thus will have compression = 0)
	int imageSize; // Image size in bytes
	int xres, yres; // Pixels per meter
	int numColours; // Number of colours
	int imptColours; // Important colours
	
	// IMAGE DATA
	byte[] pixelArray; // Holds pixel data
	
	// GETTERS AND SETTERS FOR BITMAP DATA
	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public short getBlank1() {
		return blank1;
	}

	public void setBlank1(short blank1) {
		this.blank1 = blank1;
	}

	public short getBlank2() {
		return blank2;
	}

	public void setBlank2(short blank2) {
		this.blank2 = blank2;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getHeadSize() {
		return HeadSize;
	}

	public void setHeadSize(int headSize) {
		HeadSize = headSize;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public short getPlanes() {
		return planes;
	}

	public void setPlanes(short planes) {
		this.planes = planes;
	}

	public short getBits() {
		return bits;
	}

	public void setBits(short bits) {
		this.bits = bits;
	}

	public int getCompression() {
		return compression;
	}

	public void setCompression(int compression) {
		this.compression = compression;
	}

	public int getImageSize() {
		return imageSize;
	}

	public void setImageSize(int imageSize) {
		this.imageSize = imageSize;
	}

	public int getXres() {
		return xres;
	}

	public void setXres(int xres) {
		this.xres = xres;
	}

	public int getYres() {
		return yres;
	}

	public void setYres(int yres) {
		this.yres = yres;
	}

	public int getNumColours() {
		return numColours;
	}

	public void setNumColours(int numColours) {
		this.numColours = numColours;
	}

	public int getImptColours() {
		return imptColours;
	}

	public void setImptColours(int imptColours) {
		this.imptColours = imptColours;
	}

	public byte[] getPixelArray() {
		return pixelArray;
	}

	public void setPixelArray(byte[] pixelArray) {
		this.pixelArray = pixelArray;
	}
	
} // end class BMP