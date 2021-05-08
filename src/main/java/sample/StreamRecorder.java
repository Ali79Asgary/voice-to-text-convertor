package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StreamRecorder extends Task {

    int filesCount = 0;
    boolean isStrip = false;
    public static boolean stillRecord = false;
    Thread speechClientThread;

    // path of the wav file
    File wavFile;

    // format of audio file
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    // the line from which audio data is captured
    TargetDataLine line;

    AnchorPane anchorPane;
    Label lblSpeechToText;

    public StreamRecorder() {
    }

    public StreamRecorder(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    public StreamRecorder(boolean isStrip) {
        this.isStrip = isStrip;
    }

    public StreamRecorder(boolean isStrip, AnchorPane anchorPane) {
        this.isStrip = isStrip;
        this.anchorPane = anchorPane;
    }

    public StreamRecorder(boolean isStrip, boolean stillRecord, AnchorPane anchorPane) {
        this.isStrip = isStrip;
        this.stillRecord = stillRecord;
        this.anchorPane = anchorPane;
    }

    public StreamRecorder(boolean isStrip, Label lblSpeechToText) {
        this.isStrip = isStrip;
        this.lblSpeechToText = lblSpeechToText;
    }

    public static void setStillRecord(boolean stillRecord) {
        StreamRecorder.stillRecord = stillRecord;
    }

    @Override
    protected Void call() throws Exception
    {
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

//            byte[] buf = new byte[bufferByteSize];
//            float[] samples = new float[bufferByteSize / 2];
//            float lastPeak = 0f;

            line.start();   // start capturing
//            int b = line.read(buf, 0, buf.length);
            System.out.println("Start capturing...");

            AudioInputStream ais = new AudioInputStream(line);

            System.out.println("Start recording...");

//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    byte[] buf = new byte[bufferByteSize];
//                    float[] samples = new float[bufferByteSize / 2];
//                    float lastPeak = 0f;
//                    for(int b; (b = line.read(buf, 0, buf.length)) > -1;) {
////            while (b == 2048){
//                        // convert bytes to samples here
//                        if (b != 2048){
//                            break;
//                        }
//                        for (int i = 0, s = 0; i < b; ) {
//                            int sample = 0;
//
//                            sample |= buf[i++] & 0xFF; // (reverse these two lines
//                            sample |= buf[i++] << 8;   //  if the format is big endian)
//
//                            // normalize to range of +/-1.0f
//                            samples[s++] = sample / 32768f;
//                        }
//
//                        float rms = 0f;
//                        float peak = 0f;
//                        for (float sample : samples) {
//
//                            float abs = Math.abs(sample);
//                            if (abs > peak) {
//                                peak = abs;
//                            }
//
//                            rms += sample * sample;
//                        }
//
//                        rms = (float) Math.sqrt(rms / samples.length);
//
//                        if (lastPeak > peak) {
//                            peak = lastPeak * 0.875f;
//                        }
//
//                        lastPeak = peak;
//                        System.out.println(peak);
//                        if (isStrip){
//                            setMeterOnEDTStrip(rms, peak);
//                        } else {
//                            setMeterOnEDT(rms, peak);
//                        }
//                        System.out.println(b);
//                    }
//
//                }
//            });
//            thread.start();

//            System.out.println("Start capturing...");
//
//            AudioInputStream ais = new AudioInputStream(line);
//
//            System.out.println("Start recording...");

            speechClientThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("speechClientThread1");
                    while (UtilStillRecord.stillRecord){
                        System.out.println("speechClientThread");
                        try {
                            TimeUnit.MILLISECONDS.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        SpeechClient speechClient = new SpeechClient(ais, lblSpeechToText);
                        speechClient.setOnSucceeded((succeededEvent) ->{
                            System.out.println("SpeechClient On succeed");
                        });
                        ExecutorService executorService = Executors.newFixedThreadPool(1);
                        executorService.execute(speechClient);
                        executorService.shutdown();
                    }
                }
            });
            speechClientThread.start();

            // start recording
            AudioSystem.write(ais, fileType, wavFile);



        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Defines an audio format
     */
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

    /**
     * Closes the target data line to finish capturing and recording
     */
    public void finish() {
        line.stop();
        line.close();
        try {
//            speechClientThread.interrupt();
//            speechClientThread.stop();
            System.out.println("Finished");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    void setMeterOnEDT(final float rms, final float peak) {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (!isStrip){
                        float newPeak = (float) ((peak) * 70);
                        float newRms = rms * 500;
                        System.out.println(newPeak);
                        System.out.println("rms : "+newRms);
                        anchorPane.setPrefWidth(newRms);
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    void setMeterOnEDTStrip(final float rms, final float peak) {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (isStrip){
                        float newPeak = (float) ((peak) * 70);
                        float newRms = rms * 500;
                        System.out.println(newPeak);
                        System.out.println("rms : "+newRms);
                        if (newRms < 70){
                            anchorPane.setPrefHeight(newRms);
                        }
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
