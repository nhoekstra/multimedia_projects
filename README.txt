The source code for the bitmap displayer and waveform reader were done to learn how BMP and WAV file structures are formatted and read. The code available here has not been optimized and could use some extensive refactoring. 

Since only a minimal amount of API's were used the applications can only read very basic files.

BMP - 24-colour uncompressed mode, with a max dimensions of 384x384. (must be a multiple of 8)

WAV - PCM encoded, mono and < 9000 samples