package ir.deepmine.grpc;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import ir.deepmine.speech.v1.*;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicBoolean;

public class GrpcClient {
    RecognitionConfig config = RecognitionConfig.newBuilder()
            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
            .setLanguageCode("fa-IR")
            .setSampleRateHertz(16000)
            .setModel("default")
            .setEnableWordConfidence(true)
            .build();

    StreamingRecognitionConfig streamingConfig = StreamingRecognitionConfig.newBuilder()
            .setConfig(config)
            .setAccessToken("")
            .setInterimResults(true)
            .setInterimResultsIntervalMilliseconds(500)
            .build();

    final AtomicBoolean finished = new AtomicBoolean(false);

    class ResponseApiStreamingObserver implements StreamObserver<StreamingRecognizeResponse> {

        @Override
        public void onNext(StreamingRecognizeResponse response) {
            // For streaming recognize, the results list has one is_final result (if
            // available) followed by a number of in-progress results (if iterim_results
            // is true) for subsequent utterances.
            // Just print the first result here.
            System.out.println("in onNext");
            StreamingRecognitionResult result = response.getResultsList().get(0);
            // There can be several alternative transcripts for a given chunk of speech.
            // Just use the first (most likely) one here.
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);

            System.out.printf("%s\n", alternative.getTranscript());
            if (result.getIsFinal()) {
                System.out.printf("alternative_confidence: %.4f", alternative.getConfidence());
                for(WordInfo wordInfo: alternative.getWordsList()) {
                    System.out.printf("%.4f  %.4f  %.3f  %s\n", wordInfo.getStartTime(), wordInfo.getEndTime(),
                            wordInfo.getConfidence(), wordInfo.getWord());
                }
            }

        }

        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
            finished.set(true);
        }

        @Override
        public void onCompleted() {
            System.out.println("Finished");
            finished.set(true);
        }
    }

    public static void main(String[] argv) {
        GrpcClient client = new GrpcClient();
        client.runBidiTest();
    }

    private void runBidiTest() {
        byte[] wavArray = readWavFile("test_fa.wav");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("deepmine.ir", 8443)
                .usePlaintext()
                .build();

        SpeechGrpc.SpeechStub asyncSpeechStub = SpeechGrpc.newStub(channel);

        ResponseApiStreamingObserver responseObserver = new ResponseApiStreamingObserver();

        StreamObserver<StreamingRecognizeRequest> requestObserver =
                asyncSpeechStub.bidiStreamingRecognize(responseObserver);

        // The first request must **only** contain the audio configuration:
        finished.set(false);
        System.out.println("Start sending the data ...");
        requestObserver.onNext(StreamingRecognizeRequest.newBuilder().setStreamingConfig(streamingConfig).build());

        int chunkSize = 64 * 1024;
        int offset = 0;
        int dataLength = wavArray.length;
        while (true) {
            int size = chunkSize;
            if (offset + size > dataLength)
                size = dataLength - offset;

            // Subsequent requests must **only** contain the audio data.
            requestObserver.onNext(StreamingRecognizeRequest
                    .newBuilder()
                    .setAudioContent(ByteString.copyFrom(wavArray, offset, size))
                    .build());
            if (offset + size >= dataLength)
                break;
            offset += size;
        }
        // Mark transmission as completed after sending the data.
        requestObserver.onCompleted();
        System.out.println("All data was sent. Waiting for the results ...");
        while (!finished.get()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private byte[] readWavFile(String filePath) {
        File fid = new File(filePath);
        int fileSize;
        RandomAccessFile fReader;
        try {
            fileSize = (int) fid.length();
            fReader = new RandomAccessFile(fid, "r");
            fReader.seek(44);
            if (fileSize < 44)
                return new byte[0];
            byte[] data = new byte[fileSize - 44];
            fReader.readFully(data);
            return data;
        } catch (Exception exp) {
            exp.printStackTrace();
            return new byte[0];
        }
    }
}
