package sample;

import java.io.*;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.AnchorPane;

import javax.sound.sampled.*;

/**
 * A sample program is to demonstrate how to record sound in Java author:
 * www.codejava.net
 * http://www.codejava.net/coding/capture-and-record-sound-into-wav-file-with-java-sound-api
 */

//کلاس مربوط به ضبط صدا و ذخیره آن بر روی سیستم کاربر در پوشه AudioFiles
//و فرستادن فایل ضبط شده برای کلاس JsonPostVoiceFile
public class MainRecorder extends Task<Void> {

    int filesCount = 0;
    boolean isStrip = false;

    // path of the wav file
    File wavFile;

    // format of audio file
    AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

    // the line from which audio data is captured
    TargetDataLine line;

    AnchorPane anchorPane;

    public MainRecorder() {
    }

    public MainRecorder(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    public MainRecorder(boolean isStrip) {
        this.isStrip = isStrip;
    }

    public MainRecorder(boolean isStrip, AnchorPane anchorPane) {
        this.isStrip = isStrip;
        this.anchorPane = anchorPane;
    }

    @Override
    protected Void call() throws Exception
    {
        try {
            filesCount = new File("./AudioFiles").listFiles().length;
            wavFile = new File("./AudioFiles/RecordAudio"+filesCount+".wav");
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

            //بخش مربوط به نشانگر بلندی صدا که بلندی صدا را از میکروفون
            //دریافت میکند و آن را بر روی صفحه اصلی نمایش میدهد.
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    byte[] buf = new byte[bufferByteSize];
                    float[] samples = new float[bufferByteSize / 2];
                    float lastPeak = 0f;
                    for(int b; (b = line.read(buf, 0, buf.length)) > -1;) {
                        // convert bytes to samples here
                        if (b != 2048){
                            break;
                        }
                        for (int i = 0, s = 0; i < b; ) {
                            int sample = 0;
                            sample |= buf[i++] & 0xFF; // (reverse these two lines
                            sample |= buf[i++] << 8;   //  if the format is big endian)
                            // normalize to range of +/-1.0f
                            samples[s++] = sample / 32768f;
                        }
                        float rms = 0f;
                        float peak = 0f;
                        for (float sample : samples) {
                            float abs = Math.abs(sample);
                            if (abs > peak) {
                                peak = abs;
                            }
                            rms += sample * sample;
                        }
                        rms = (float) Math.sqrt(rms / samples.length);
                        if (lastPeak > peak) {
                            peak = lastPeak * 0.875f;
                        }
                        lastPeak = peak;
                        if (isStrip){
                            setMeterOnEDTStrip(rms, peak);
                        } else {
                            setMeterOnEDT(rms, peak);
                        }
                    }
                }
            });
            thread.start();

//            System.out.println("Start capturing...");
//
//            AudioInputStream ais = new AudioInputStream(line);
//
//            System.out.println("Start recording...");

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
    //تابعی که فرمت صدای ضبط شده را مشخص میکند
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
    //تابعی که ضبط صدا را به پایان میرساند
    public void finish() {
        line.stop();
        line.close();
        System.out.println("Finished");
    }

    //تابعی که بلندی صدا را بر روی بخش گرافیکی برنامه نمایش میدهد
    void setMeterOnEDT(final float rms, final float peak) {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (!isStrip){
                        float newPeak = (float) ((peak) * 70);
                        float newRms = rms * 500;
                        anchorPane.setPrefWidth(newRms);
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //تابعی که بلندی صدا را بر روی بخش گرافیکی برنامه نمایش میدهد
    void setMeterOnEDTStrip(final float rms, final float peak) {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (isStrip){
                        float newPeak = (float) ((peak) * 70);
                        float newRms = rms * 500;
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
