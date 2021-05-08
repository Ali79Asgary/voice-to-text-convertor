package sample;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ir.deepmine.speech.v1.*;
//import ir.sample.*;
import ir.deepmine.speech.v1.StreamingRecognizeResponse;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SpeechClient extends Task {

    static File audioFile;
    static AudioInputStream ais;
    Label lblSpeechToText;
    String text = "";

    static ManagedChannel managedChannel;
    static SpeechGrpc.SpeechStub speechStub;
    static StreamObserver toServer;

    public SpeechClient(File audioFile) {
        this.audioFile = audioFile;
    }

    public SpeechClient(AudioInputStream ais) {
        this.ais = ais;
    }

    public SpeechClient(AudioInputStream ais, Label lblSpeechToText) {
        this.ais = ais;
        this.lblSpeechToText = lblSpeechToText;
    }

    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("deepmine.ir", 8443).usePlaintext(true).build();
        SpeechGrpc.SpeechStub speechStub = SpeechGrpc.newStub(managedChannel);
        StreamObserver toServer = speechStub.bidiStreamingRecognize(new StreamObserver<StreamingRecognizeResponse>() {
            @Override
            public void onNext(StreamingRecognizeResponse streamingRecognizeResponse) {
                System.out.println("Hello GRPC");
            }

            @Override
            public void onError(Throwable throwable) {

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

        toServer.onNext(StreamingRecognizeRequest.newBuilder().setStreamingConfig(streamingRecognitionConfig));

        InputStream inputStream = new FileInputStream(audioFile);

        ByteString byteString = ByteString.readFrom(inputStream);
        toServer.onNext(StreamingRecognizeRequest.newBuilder().setAudioContent(byteString));
    }

    @Override
    protected Object call() throws Exception {
        try {
            managedChannel = ManagedChannelBuilder.forAddress("deepmine.ir", 8443).usePlaintext().build();
            speechStub = SpeechGrpc.newStub(managedChannel);
            toServer = speechStub.bidiStreamingRecognize(new StreamObserver<StreamingRecognizeResponse>() {
                @Override
                public void onNext(StreamingRecognizeResponse streamingRecognizeResponse) {
                    System.out.println("Hello GRPC");
                    System.out.println("in onNext");
                    StreamingRecognitionResult result = streamingRecognizeResponse.getResultsList().get(0);
                    // There can be several alternative transcripts for a given chunk of speech.
                    // Just use the first (most likely) one here.
                    SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);

                    System.out.printf("%s\n", alternative.getTranscript());
                    if (result.getIsFinal()) {
                        System.out.printf("alternative_confidence: %.4f", alternative.getConfidence());
                        for(WordInfo wordInfo: alternative.getWordsList()) {
                            System.out.printf("%.4f  %.4f  %.3f  %s\n", wordInfo.getStartTime(), wordInfo.getEndTime(),
                                    wordInfo.getConfidence(), wordInfo.getWord());
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    text = text.concat(wordInfo.getWord()).concat(" ");
                                    lblSpeechToText.setText(text);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("error grpc");
                    throwable.printStackTrace();
                }

                @Override
                public void onCompleted() {

                }
            });
            sendDataToServer();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void sendDataToServer(){
        try {
            RecognitionConfig.AudioEncoding audioEncoding = RecognitionConfig.AudioEncoding.LINEAR16;

            RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder().setEncoding(audioEncoding).
                    setLanguageCode("en-EN").setEnableWordConfidence(true).setSampleRateHertz(16000).build();

            StreamingRecognitionConfig streamingRecognitionConfig = StreamingRecognitionConfig.newBuilder().
                    setConfig(recognitionConfig).setAccessToken(UtilAccessToken.accessToken).setInterimResults(true).
                    setInterimResultsIntervalMilliseconds(500).build();
//            System.out.println("123");
//            StreamingRecognizeRequest.Builder str = StreamingRecognizeRequest.newBuilder();
//            System.out.println("125");
//            StreamingRecognizeRequest.Builder str5 = str.setStreamingConfig(streamingRecognitionConfig);
//            System.out.println("127");
//            toServer.onNext(str5);
            System.out.println("129");
            toServer.onNext(StreamingRecognizeRequest.newBuilder().setStreamingConfig(streamingRecognitionConfig).build());

//            InputStream inputStream = new FileInputStream(audioFile);

            ByteString byteString = ByteString.readFrom(ais);
            System.out.println("135");
            toServer.onNext(StreamingRecognizeRequest.newBuilder().setAudioContent(byteString).build());

            System.out.println("138");
            toServer.onCompleted();

//            for (StreamObserver res : toServer){
//
//            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
