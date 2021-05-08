package sample;

import com.google.protobuf.ByteString;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
//import ir.sample.*;
import ir.deepmine.speech.v1.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

import javax.sound.sampled.*;

public class StreamingRecognize extends Task {

    static ManagedChannel managedChannel;
    static SpeechGrpc.SpeechStub speechStub;
    static StreamObserver toServer;

    Label lblSpeechToText;

    public StreamingRecognize(Label lblSpeechToText) {
        this.lblSpeechToText = lblSpeechToText;
    }

    @Override
    protected Object call() throws Exception {
        try {
            managedChannel = ManagedChannelBuilder.forTarget("deepmine.ir:8443").usePlaintext(true).build();
            speechStub = SpeechGrpc.newStub(managedChannel);
            toServer = speechStub.bidiStreamingRecognize(new StreamObserver<StreamingRecognizeResponse>() {
                @Override
                public void onNext(StreamingRecognizeResponse streamingRecognizeResponse) {
                    System.out.println("Hello GRPC");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lblSpeechToText.setText(streamingRecognizeResponse.toString());
                        }
                    });
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("error grpc1");
                    throwable.printStackTrace();
                }

                @Override
                public void onCompleted() {

                }
            });

            RecognitionConfig.AudioEncoding audioEncoding = RecognitionConfig.AudioEncoding.LINEAR16;

            RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder().setEncoding(audioEncoding).
                    setLanguageCode("en-EN").setEnableWordConfidence(true).setSampleRateHertz(16000).build();

            StreamingRecognitionConfig streamingRecognitionConfig = StreamingRecognitionConfig.newBuilder().
                    setConfig(recognitionConfig).setAccessToken(UtilAccessToken.accessToken).setInterimResults(true).
                    setInterimResultsIntervalMilliseconds(500).build();

            StreamingRecognizeRequest request = StreamingRecognizeRequest.newBuilder().
                    setStreamingConfig(streamingRecognitionConfig).build();

            toServer.onNext(request);

            AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
            DataLine.Info targetInfo = new DataLine.Info(
                            TargetDataLine.class,
                            audioFormat); // Set the system information to read from the microphone audio stream

            if (!AudioSystem.isLineSupported(targetInfo)) {
                System.out.println("Microphone not supported");
                System.exit(0);
            }
            // Target data line captures the audio stream the microphone produces.
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            System.out.println("Start speaking");
            long startTime = System.currentTimeMillis();
            // Audio Input Stream
            AudioInputStream audio = new AudioInputStream(targetDataLine);
            while (true) {
                long estimatedTime = System.currentTimeMillis() - startTime;
                byte[] data = new byte[6400];
                audio.read(data);
                if (estimatedTime > 10000) { // 60 seconds
                    System.out.println("Stop speaking.");
                    targetDataLine.stop();
                    targetDataLine.close();
                    break;
                }
                request = StreamingRecognizeRequest.newBuilder()
                                .setAudioContent(ByteString.copyFrom(data))
                                .build();

                toServer.onNext(request);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        toServer.onCompleted();
        return null;
    }
}
