package sample;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ir.deepmine.speech.v1.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicBoolean;

//کلاسی که صدا را توسط میکروفون از کاربر دریافت میکند
//و از طریق grpc برای سرور میفرستد (استریم میکند)
//و متن را در لحظه دریافت میکند
public class StreamDirectly extends Task {

    String accessToken = "";
    Label lblVoiceToText;
    String text = "";
    File wavFile;
    TargetDataLine targetDataLine;
    TargetDataLine targetDataLine1;
    long estimatedTime = 0;
    boolean stillRun = true;
    boolean isStrip = false;
    AnchorPane anchorPane;
    final int bufferByteSize = 2048;

    RecognitionConfig config = RecognitionConfig.newBuilder()
            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
            .setLanguageCode("fa-IR")
            .setSampleRateHertz(16000)
            .setModel("default")
            .setEnableWordConfidence(true)
            .build();

    StreamingRecognitionConfig streamingConfig = StreamingRecognitionConfig.newBuilder()
            .setConfig(config)
            .setAccessToken(UtilAccessToken.accessToken)
            .setInterimResults(true)
            .setInterimResultsIntervalMilliseconds(500)
            .build();

    final AtomicBoolean finished = new AtomicBoolean(false);

    public StreamDirectly() {
    }

    @Override
    protected Object call() throws Exception {
        try {
            runBidiTest();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public StreamDirectly(boolean isStrip) {
        this.isStrip = isStrip;
    }

    public StreamDirectly(String accessToken) {
        this.accessToken = accessToken;
    }

    public StreamDirectly(Label lblVoiceToText) {
        this.lblVoiceToText = lblVoiceToText;
    }

    public StreamDirectly(Label lblVoiceToText, boolean isStrip) {
        this.lblVoiceToText = lblVoiceToText;
        this.isStrip = isStrip;
    }

    public StreamDirectly(boolean isStrip, AnchorPane anchorPane) {
        this.isStrip = isStrip;
        this.anchorPane = anchorPane;
    }

    public StreamDirectly(Label lblVoiceToText, boolean isStrip, AnchorPane anchorPane) {
        this.lblVoiceToText = lblVoiceToText;
        this.isStrip = isStrip;
        this.anchorPane = anchorPane;
    }

    public StreamDirectly(Label lblVoiceToText, File wavFile) {
        this.lblVoiceToText = lblVoiceToText;
        this.wavFile = wavFile;
    }

    class ResponseApiStreamingObserver implements StreamObserver<StreamingRecognizeResponse> {

        @Override
        public void onNext(StreamingRecognizeResponse streamingRecognizeResponse) {
            StreamingRecognitionResult result = streamingRecognizeResponse.getResultsList().get(0);
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            System.out.printf("%s\n", alternative.getTranscript());
            if (result.getIsFinal()) {
                System.out.printf("alternative_confidence: %.4f", alternative.getConfidence());
                for(WordInfo wordInfo: alternative.getWordsList()) {
                    System.out.printf("%.4f  %.4f  %.3f  %s\n", wordInfo.getStartTime(), wordInfo.getEndTime(), wordInfo.getConfidence(), wordInfo.getWord());
                    System.out.println("Stream Text : "+wordInfo.getWord());

                    //بخشی که مربوط به نمایش متن صدای استریم شده است
                    //که اگر درخواست از صفحه نوار باشد آن را کپی و پیست میکند
                    //و اگر درخواست از صفحه اصلی باشد آن را مستقیما در مکان مشخص شده در صفحه اصلی نمایش میدهد
                    if (!isStrip){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        UtilStreamText.text = UtilStreamText.text.concat(wordInfo.getWord()).concat(" ");
                                        lblVoiceToText.setText(UtilStreamText.text);
                                    }
                                });
                            }
                        }).start();
                    } else {
                        System.out.println("Strip");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                text = "";
                                text = text.concat(" ").concat(wordInfo.getWord()).concat(" ");
                                StringSelection stringSelection = new StringSelection(text);
                                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                                clipboard.setContents(stringSelection, null);
                                try {
                                    Robot robot = new Robot();
                                    robot.keyPress(KeyEvent.VK_CONTROL);
                                    robot.keyPress(KeyEvent.VK_V);
                                    robot.keyRelease(KeyEvent.VK_CONTROL);
                                    robot.keyRelease(KeyEvent.VK_V);
                                } catch (AWTException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            }
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
            finished.set(true);
        }

        @Override
        public void onCompleted() {
            System.out.println("Finished");
            finished.set(true);
        }
    }

    //تابعی که صدا را از طریق grpc برای سرور استریم میکند
    void runBidiTest() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("deepmine.ir", 8443).usePlaintext().build();

        SpeechGrpc.SpeechStub asyncSpeechStub = SpeechGrpc.newStub(channel);

        StreamDirectly.ResponseApiStreamingObserver responseObserver = new StreamDirectly.ResponseApiStreamingObserver();

        StreamObserver<StreamingRecognizeRequest> requestObserver = asyncSpeechStub.bidiStreamingRecognize(responseObserver);

        finished.set(false);

        System.out.println("Start sending the data ...");
        requestObserver.onNext(StreamingRecognizeRequest.newBuilder().setStreamingConfig(streamingConfig).build());

        try {
            AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
            DataLine.Info targetInfo =
                    new DataLine.Info(
                            TargetDataLine.class,
                            audioFormat); // Set the system information to read from the microphone audio stream
            if (!AudioSystem.isLineSupported(targetInfo)) {
                System.out.println("Microphone not supported");
                System.exit(0);
            }
            // Target data line captures the audio stream the microphone produces.
            targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            System.out.println("Start speaking");
            long startTime = System.currentTimeMillis();
            // Audio Input Stream
            AudioInputStream audio = new AudioInputStream(targetDataLine);
            int i = 0;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                showSound(targetDataLine);
                            }
                        }).start();
                        while (stillRun) {
                            byte[] data = new byte[6400];
                            audio.read(data);
                            requestObserver.onNext(StreamingRecognizeRequest
                                .newBuilder()
                                .setAudioContent(ByteString.copyFrom(data))
                                .build());
                        }
                        requestObserver.onCompleted();
                        System.out.println("All data was sent. Waiting for the results ...");
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
        System.out.println(e);
        }
        while (!finished.get()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }

    byte[] readWavFile(String filePath) {
        File fid = new File(filePath);
        int fileSize;
        RandomAccessFile fReader;
        try {
            fileSize = (int) fid.length();
            fReader = new RandomAccessFile(fid, "r");
            fReader.seek(44);
            if (fileSize < 44){
                return new byte[0];
            }
            byte[] data = new byte[fileSize-44];
            fReader.readFully(data);
            return data;
        } catch (Exception e){
            e.printStackTrace();
            return new byte[0];
        }
    }

    //تابعی که بخش استریم صدا را به پایان میرساند
    public void finish() {
        try {
            targetDataLine.stop();
            targetDataLine.close();
            estimatedTime = 60000;
            stillRun = false;
            System.out.println("Finished in finish method!");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //تابعی که بلندی صدای استریم شده را هر لحظه برای کاربر نمایش میدهد
    public void showSound(TargetDataLine targetDataLine){
        try {
            byte[] buf = new byte[bufferByteSize];
            float[] samples = new float[bufferByteSize / 2];
            float lastPeak = 0f;
            for(int b; (b = targetDataLine.read(buf, 0, buf.length)) > -1;) {
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
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //تابعی که بلندی صدای استریم شده را هر لحظه برای کاربر نمایش میدهد
    void setMeterOnEDT(final float rms, final float peak) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (!isStrip){
                                float newPeak = (float) ((peak) * 70);
                                float newRms = rms * 500;
                                anchorPane.setPrefHeight(newRms);
                            }
                        }
                    });
                }
            }).start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //تابعی که بلندی صدای استریم شده را هر لحظه برای کاربر نمایش میدهد
    void setMeterOnEDTStrip(final float rms, final float peak) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
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
                }
            }).start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
