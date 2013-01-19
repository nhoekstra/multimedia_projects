/** 
 * Filename: WAV.java
 * Author: Nicholas Hoekstra
 * 
 * Created: October 18, 2012
 * Last Updated: October 19, 2012
 * 
 */

package app;

public class WAV {
	
	// RIFF chunk descriptor
	int chunkID; // "RIFF"
	int chunkSize; // Size of file
	int format; // File Type "WAVE"
	
	// fmt sub-chunk
	int subChunk1ID; // "fmt"
	int subChunk1Size; // 16 bytes
	short audioFormat; // This will be uncompressed (PCM)
	short numChannels; // Mono = 1, Stereo = 2, etc.
	int sampleRate; // Number of samples per second
	int byteRate; // sampleRate * numChannels * bitsPerSample / 8
	short BlockAlign; // Number of bytes for one sample numChannels * bitsPerSample / 8
	short bitsPerSample; // 8 = 8bits, 16 = 16bits, etc
	
	// data sub-chunk	
	int subChunk2ID; // "data"
	int subChunk2Size; // bitsPerSample / 8 * numChannels * numSamples
	byte[] data; // Sound data
	// padding byte? (only if bitsPerSample / 8 * numChannels * numSamples is odd)
	
	// GETTERS AND SETTERS FOR WAV DATA
	public int getChunkID() {
		return chunkID;
	}
	
	public void setChunkID(int chunkID) {
		this.chunkID = chunkID;
	}
	
	public int getChunkSize() {
		return chunkSize;
	}
	
	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}
	
	public int getFormat() {
		return format;
	}
	
	public void setFormat(int format) {
		this.format = format;
	}
	
	public int getSubChunk1ID() {
		return subChunk1ID;
	}
	
	public void setSubChunk1ID(int subChunk1ID) {
		this.subChunk1ID = subChunk1ID;
	}
	
	public int getSubChunk1Size() {
		return subChunk1Size;
	}
	
	public void setSubChunk1Size(int subChunk1Size) {
		this.subChunk1Size = subChunk1Size;
	}
	
	public short getAudioFormat() {
		return audioFormat;
	}
	
	public void setAudioFormat(short audioFormat) {
		this.audioFormat = audioFormat;
	}
	
	public short getNumChannels() {
		return numChannels;
	}
	
	public void setNumChannels(short numChannels) {
		this.numChannels = numChannels;
	}
	
	public int getSampleRate() {
		return sampleRate;
	}
	
	public void setSampleRate(int sampleRate) {
		this.sampleRate = sampleRate;
	}
	
	public int getByteRate() {
		return byteRate;
	}
	
	public void setByteRate(int byteRate) {
		this.byteRate = byteRate;
	}
	
	public short getBlockAlign() {
		return BlockAlign;
	}
	
	public void setBlockAlign(short blockAlign) {
		BlockAlign = blockAlign;
	}
	
	public short getBitsPerSample() {
		return bitsPerSample;
	}
	
	public void setBitsPerSample(short bitsPerSample) {
		this.bitsPerSample = bitsPerSample;
	}
	
	public int getSubChunk2ID() {
		return subChunk2ID;
	}
	
	public void setSubChunk2ID(int subChunk2ID) {
		this.subChunk2ID = subChunk2ID;
	}
	
	public int getSubChunk2Size() {
		return subChunk2Size;
	}
	
	public void setSubChunk2Size(int subChunk2Size) {
		this.subChunk2Size = subChunk2Size;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}
	
} // end class WAV
