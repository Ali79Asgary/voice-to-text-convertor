package sample;

import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javax.sound.sampled.*;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StreamRecorder1 extends Task {

    int filesCount = 0;
    boolean isStrip = false;
    public static boolean stillRecord = false;
    File wavFile;
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
    TargetDataLine line;
    AnchorPane anchorPane;
    Label lblSpeechToText;
    Thread voiceThread;

    public StreamRecorder1(Label lblSpeechToText) {
        this.lblSpeechToText = lblSpeechToText;
    }

    @Override
    protected Object call() throws Exception {
        try {
            filesCount = new File("./StreamAudioFiles").listFiles().length;
            wavFile = new File("./StreamAudioFiles/RecordAudio"+filesCount+".wav");
            AudioFormat format = getAudioFormat();
            final int bufferByteSize = 2048;
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);

            line.start();   // start capturing
            System.out.println("Start capturing... StreamRecorder1");

            AudioInputStream ais = new AudioInputStream(line);

            System.out.println("Start recording... StreamRecorder1");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0 ; i < 10 ; i++){
                            System.out.println("Delay 1 Second");
                            TimeUnit.MILLISECONDS.sleep(400);
//                            finish();
//                            NewGrpcSpeechClient speechClient = new NewGrpcSpeechClient(lblSpeechToText, wavFile);
//                            speechClient.runBidiTest();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    NewGrpcSpeechClient speechClient = new NewGrpcSpeechClient(lblSpeechToText, wavFile);
                                    speechClient.runBidiTest();
                                }
                            }).start();
                            copyAudio(wavFile.getPath(), wavFile.getPath(), (long) i/2, (long) 0.5);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
//            voiceThread.start();

            // start recording
            AudioSystem.write(ais, fileType, wavFile);
            System.out.println("Hello StreamRecorder1");
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    AudioFormat getAudioFormat() {
//        float sampleRate = 16000;
        float sampleRate = 16000;
//        int sampleSizeInBits = 8;
        int sampleSizeInBits = 16;
//        int channels = 2;
        int channels = 1;
//        boolean signed = true;
        boolean signed = true;
//        boolean bigEndian = true;
        boolean bigEndian = false;
        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        return format;
    }

    public void finish() {
        try {
            line.stop();
            line.close();
//            voiceThread.interrupt();
//            voiceThread.stop();
//              speechClientThread.interrupt();
//              speechClientThread.stop();
            System.out.println("Finished");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void copyAudio(String sourceFileName, String destinationFileName, long startSecond, long secondsToCopy) {
        AudioInputStream inputStream = null;
        AudioInputStream shortenedStream = null;
        try {
            File file = new File(sourceFileName);
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
            AudioFormat format = fileFormat.getFormat();
            inputStream = AudioSystem.getAudioInputStream(file);
            int bytesPerSecond = format.getFrameSize() * (int)format.getFrameRate();
            inputStream.skip(startSecond * bytesPerSecond);
            long framesOfAudioToCopy = secondsToCopy * (int)format.getFrameRate();
            shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
            File destinationFile = new File(destinationFileName);
            AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (inputStream != null) try { inputStream.close(); } catch (Exception e) {
                System.out.println(e); }
            if (shortenedStream != null) try { shortenedStream.close(); } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
