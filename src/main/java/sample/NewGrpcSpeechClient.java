package sample;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ir.deepmine.speech.v1.*;
import javafx.application.Platform;
import javafx.scene.control.Label;
//import ir.sample.*;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicBoolean;

public class NewGrpcSpeechClient {

    String accessToken = "";
    Label lblVoiceToText;
    String text = "";
    File wavFile;

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

    public NewGrpcSpeechClient() {
    }

    public NewGrpcSpeechClient(String accessToken) {
        this.accessToken = accessToken;
    }

    public NewGrpcSpeechClient(Label lblVoiceToText) {
        this.lblVoiceToText = lblVoiceToText;
    }

    public NewGrpcSpeechClient(Label lblVoiceToText, File wavFile) {
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
                    System.out.printf("%.4f  %.4f  %.3f  %s\n", wordInfo.getStartTime(), wordInfo.getEndTime(),
                            wordInfo.getConfidence(), wordInfo.getWord());

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            UtilStreamText.text = UtilStreamText.text.concat(wordInfo.getWord()).concat(" ");
                            lblVoiceToText.setText(UtilStreamText.text);
                        }
                    });
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

    public static void main(String[] args) {
        NewGrpcSpeechClient newGrpcSpeechClient = new NewGrpcSpeechClient();
        newGrpcSpeechClient.runBidiTest();
    }

    void runBidiTest() {
        byte[] wavArray = readWavFile(wavFile.getPath());

        ManagedChannel channel = ManagedChannelBuilder.forAddress("deepmine.ir", 8443).usePlaintext().build();

        SpeechGrpc.SpeechStub asyncSpeechStub = SpeechGrpc.newStub(channel);

        ResponseApiStreamingObserver responseObserver = new ResponseApiStreamingObserver();

        StreamObserver<StreamingRecognizeRequest> requestObserver = asyncSpeechStub.bidiStreamingRecognize(responseObserver);

        finished.set(false);

        System.out.println("Start sending the data ...");
        requestObserver.onNext(StreamingRecognizeRequest.newBuilder().setStreamingConfig(streamingConfig).build());

        int chunkSize = 64 * 1024;
        int offset = 0;
        int dataLength = wavArray.length;
        while (true) {
            int size = chunkSize;
            if (offset + size > dataLength) {
                size = dataLength - offset;
            }
            requestObserver.onNext(StreamingRecognizeRequest
                    .newBuilder()
                    .setAudioContent(ByteString.copyFrom(wavArray, offset, size))
                    .build());
            if (offset + size >= dataLength) {
                break;
            }
            offset += size;
        }
        requestObserver.onCompleted();
        System.out.println("All data was sent. Waiting for the results ...");
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
}
