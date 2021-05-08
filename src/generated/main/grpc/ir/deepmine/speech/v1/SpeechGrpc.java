package ir.deepmine.speech.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * Service that implements DeepMine Cloud Speech API.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.12.0)",
    comments = "Source: deepmine/speech/v1/speech.proto")
public final class SpeechGrpc {

  private SpeechGrpc() {}

  public static final String SERVICE_NAME = "deepmine.speech.v1.Speech";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getRecognizeMethod()} instead. 
  public static final io.grpc.MethodDescriptor<ir.deepmine.speech.v1.RecognizeRequest,
      ir.deepmine.speech.v1.RecognizeResponse> METHOD_RECOGNIZE = getRecognizeMethodHelper();

  private static volatile io.grpc.MethodDescriptor<ir.deepmine.speech.v1.RecognizeRequest,
      ir.deepmine.speech.v1.RecognizeResponse> getRecognizeMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<ir.deepmine.speech.v1.RecognizeRequest,
      ir.deepmine.speech.v1.RecognizeResponse> getRecognizeMethod() {
    return getRecognizeMethodHelper();
  }

  private static io.grpc.MethodDescriptor<ir.deepmine.speech.v1.RecognizeRequest,
      ir.deepmine.speech.v1.RecognizeResponse> getRecognizeMethodHelper() {
    io.grpc.MethodDescriptor<ir.deepmine.speech.v1.RecognizeRequest, ir.deepmine.speech.v1.RecognizeResponse> getRecognizeMethod;
    if ((getRecognizeMethod = SpeechGrpc.getRecognizeMethod) == null) {
      synchronized (SpeechGrpc.class) {
        if ((getRecognizeMethod = SpeechGrpc.getRecognizeMethod) == null) {
          SpeechGrpc.getRecognizeMethod = getRecognizeMethod = 
              io.grpc.MethodDescriptor.<ir.deepmine.speech.v1.RecognizeRequest, ir.deepmine.speech.v1.RecognizeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "deepmine.speech.v1.Speech", "Recognize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ir.deepmine.speech.v1.RecognizeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ir.deepmine.speech.v1.RecognizeResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new SpeechMethodDescriptorSupplier("Recognize"))
                  .build();
          }
        }
     }
     return getRecognizeMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getStreamingRecognizeMethod()} instead. 
  public static final io.grpc.MethodDescriptor<ir.deepmine.speech.v1.StreamingRecognizeRequest,
      ir.deepmine.speech.v1.RecognizeResponse> METHOD_STREAMING_RECOGNIZE = getStreamingRecognizeMethodHelper();

  private static volatile io.grpc.MethodDescriptor<ir.deepmine.speech.v1.StreamingRecognizeRequest,
      ir.deepmine.speech.v1.RecognizeResponse> getStreamingRecognizeMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<ir.deepmine.speech.v1.StreamingRecognizeRequest,
      ir.deepmine.speech.v1.RecognizeResponse> getStreamingRecognizeMethod() {
    return getStreamingRecognizeMethodHelper();
  }

  private static io.grpc.MethodDescriptor<ir.deepmine.speech.v1.StreamingRecognizeRequest,
      ir.deepmine.speech.v1.RecognizeResponse> getStreamingRecognizeMethodHelper() {
    io.grpc.MethodDescriptor<ir.deepmine.speech.v1.StreamingRecognizeRequest, ir.deepmine.speech.v1.RecognizeResponse> getStreamingRecognizeMethod;
    if ((getStreamingRecognizeMethod = SpeechGrpc.getStreamingRecognizeMethod) == null) {
      synchronized (SpeechGrpc.class) {
        if ((getStreamingRecognizeMethod = SpeechGrpc.getStreamingRecognizeMethod) == null) {
          SpeechGrpc.getStreamingRecognizeMethod = getStreamingRecognizeMethod = 
              io.grpc.MethodDescriptor.<ir.deepmine.speech.v1.StreamingRecognizeRequest, ir.deepmine.speech.v1.RecognizeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "deepmine.speech.v1.Speech", "StreamingRecognize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ir.deepmine.speech.v1.StreamingRecognizeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ir.deepmine.speech.v1.RecognizeResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new SpeechMethodDescriptorSupplier("StreamingRecognize"))
                  .build();
          }
        }
     }
     return getStreamingRecognizeMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getBidiStreamingRecognizeMethod()} instead. 
  public static final io.grpc.MethodDescriptor<ir.deepmine.speech.v1.StreamingRecognizeRequest,
      ir.deepmine.speech.v1.StreamingRecognizeResponse> METHOD_BIDI_STREAMING_RECOGNIZE = getBidiStreamingRecognizeMethodHelper();

  private static volatile io.grpc.MethodDescriptor<ir.deepmine.speech.v1.StreamingRecognizeRequest,
      ir.deepmine.speech.v1.StreamingRecognizeResponse> getBidiStreamingRecognizeMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<ir.deepmine.speech.v1.StreamingRecognizeRequest,
      ir.deepmine.speech.v1.StreamingRecognizeResponse> getBidiStreamingRecognizeMethod() {
    return getBidiStreamingRecognizeMethodHelper();
  }

  private static io.grpc.MethodDescriptor<ir.deepmine.speech.v1.StreamingRecognizeRequest,
      ir.deepmine.speech.v1.StreamingRecognizeResponse> getBidiStreamingRecognizeMethodHelper() {
    io.grpc.MethodDescriptor<ir.deepmine.speech.v1.StreamingRecognizeRequest, ir.deepmine.speech.v1.StreamingRecognizeResponse> getBidiStreamingRecognizeMethod;
    if ((getBidiStreamingRecognizeMethod = SpeechGrpc.getBidiStreamingRecognizeMethod) == null) {
      synchronized (SpeechGrpc.class) {
        if ((getBidiStreamingRecognizeMethod = SpeechGrpc.getBidiStreamingRecognizeMethod) == null) {
          SpeechGrpc.getBidiStreamingRecognizeMethod = getBidiStreamingRecognizeMethod = 
              io.grpc.MethodDescriptor.<ir.deepmine.speech.v1.StreamingRecognizeRequest, ir.deepmine.speech.v1.StreamingRecognizeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "deepmine.speech.v1.Speech", "BidiStreamingRecognize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ir.deepmine.speech.v1.StreamingRecognizeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ir.deepmine.speech.v1.StreamingRecognizeResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new SpeechMethodDescriptorSupplier("BidiStreamingRecognize"))
                  .build();
          }
        }
     }
     return getBidiStreamingRecognizeMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getAlignMethod()} instead. 
  public static final io.grpc.MethodDescriptor<ir.deepmine.speech.v1.AlignmentRequest,
      ir.deepmine.speech.v1.AlignmentResponse> METHOD_ALIGN = getAlignMethodHelper();

  private static volatile io.grpc.MethodDescriptor<ir.deepmine.speech.v1.AlignmentRequest,
      ir.deepmine.speech.v1.AlignmentResponse> getAlignMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<ir.deepmine.speech.v1.AlignmentRequest,
      ir.deepmine.speech.v1.AlignmentResponse> getAlignMethod() {
    return getAlignMethodHelper();
  }

  private static io.grpc.MethodDescriptor<ir.deepmine.speech.v1.AlignmentRequest,
      ir.deepmine.speech.v1.AlignmentResponse> getAlignMethodHelper() {
    io.grpc.MethodDescriptor<ir.deepmine.speech.v1.AlignmentRequest, ir.deepmine.speech.v1.AlignmentResponse> getAlignMethod;
    if ((getAlignMethod = SpeechGrpc.getAlignMethod) == null) {
      synchronized (SpeechGrpc.class) {
        if ((getAlignMethod = SpeechGrpc.getAlignMethod) == null) {
          SpeechGrpc.getAlignMethod = getAlignMethod = 
              io.grpc.MethodDescriptor.<ir.deepmine.speech.v1.AlignmentRequest, ir.deepmine.speech.v1.AlignmentResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "deepmine.speech.v1.Speech", "Align"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ir.deepmine.speech.v1.AlignmentRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ir.deepmine.speech.v1.AlignmentResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new SpeechMethodDescriptorSupplier("Align"))
                  .build();
          }
        }
     }
     return getAlignMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SpeechStub newStub(io.grpc.Channel channel) {
    return new SpeechStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SpeechBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new SpeechBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SpeechFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new SpeechFutureStub(channel);
  }

  /**
   * <pre>
   * Service that implements DeepMine Cloud Speech API.
   * </pre>
   */
  public static abstract class SpeechImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Performs synchronous non-streaming speech recognition:
     *     receive results after all audio has been sent and processed.
     * </pre>
     */
    public void recognize(ir.deepmine.speech.v1.RecognizeRequest request,
        io.grpc.stub.StreamObserver<ir.deepmine.speech.v1.RecognizeResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRecognizeMethodHelper(), responseObserver);
    }

    /**
     * <pre>
     * Performs synchronous client-to-server streaming speech recognition:
     *     receive results after all audio has been streamed and processed.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<ir.deepmine.speech.v1.StreamingRecognizeRequest> streamingRecognize(
        io.grpc.stub.StreamObserver<ir.deepmine.speech.v1.RecognizeResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(getStreamingRecognizeMethodHelper(), responseObserver);
    }

    /**
     * <pre>
     * Performs synchronous bidirectional streaming speech recognition:
     *     receive results while sending audio.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<ir.deepmine.speech.v1.StreamingRecognizeRequest> bidiStreamingRecognize(
        io.grpc.stub.StreamObserver<ir.deepmine.speech.v1.StreamingRecognizeResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(getBidiStreamingRecognizeMethodHelper(), responseObserver);
    }

    /**
     * <pre>
     * Performs synchronous non-streaming speech alignment:
     *     receive results after all audio has been sent and processed.
     * </pre>
     */
    public void align(ir.deepmine.speech.v1.AlignmentRequest request,
        io.grpc.stub.StreamObserver<ir.deepmine.speech.v1.AlignmentResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAlignMethodHelper(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRecognizeMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                ir.deepmine.speech.v1.RecognizeRequest,
                ir.deepmine.speech.v1.RecognizeResponse>(
                  this, METHODID_RECOGNIZE)))
          .addMethod(
            getStreamingRecognizeMethodHelper(),
            asyncClientStreamingCall(
              new MethodHandlers<
                ir.deepmine.speech.v1.StreamingRecognizeRequest,
                ir.deepmine.speech.v1.RecognizeResponse>(
                  this, METHODID_STREAMING_RECOGNIZE)))
          .addMethod(
            getBidiStreamingRecognizeMethodHelper(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                ir.deepmine.speech.v1.StreamingRecognizeRequest,
                ir.deepmine.speech.v1.StreamingRecognizeResponse>(
                  this, METHODID_BIDI_STREAMING_RECOGNIZE)))
          .addMethod(
            getAlignMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                ir.deepmine.speech.v1.AlignmentRequest,
                ir.deepmine.speech.v1.AlignmentResponse>(
                  this, METHODID_ALIGN)))
          .build();
    }
  }

  /**
   * <pre>
   * Service that implements DeepMine Cloud Speech API.
   * </pre>
   */
  public static final class SpeechStub extends io.grpc.stub.AbstractStub<SpeechStub> {
    private SpeechStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SpeechStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SpeechStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SpeechStub(channel, callOptions);
    }

    /**
     * <pre>
     * Performs synchronous non-streaming speech recognition:
     *     receive results after all audio has been sent and processed.
     * </pre>
     */
    public void recognize(ir.deepmine.speech.v1.RecognizeRequest request,
        io.grpc.stub.StreamObserver<ir.deepmine.speech.v1.RecognizeResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRecognizeMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Performs synchronous client-to-server streaming speech recognition:
     *     receive results after all audio has been streamed and processed.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<ir.deepmine.speech.v1.StreamingRecognizeRequest> streamingRecognize(
        io.grpc.stub.StreamObserver<ir.deepmine.speech.v1.RecognizeResponse> responseObserver) {
      return asyncClientStreamingCall(
          getChannel().newCall(getStreamingRecognizeMethodHelper(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * Performs synchronous bidirectional streaming speech recognition:
     *     receive results while sending audio.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<ir.deepmine.speech.v1.StreamingRecognizeRequest> bidiStreamingRecognize(
        io.grpc.stub.StreamObserver<ir.deepmine.speech.v1.StreamingRecognizeResponse> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getBidiStreamingRecognizeMethodHelper(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * Performs synchronous non-streaming speech alignment:
     *     receive results after all audio has been sent and processed.
     * </pre>
     */
    public void align(ir.deepmine.speech.v1.AlignmentRequest request,
        io.grpc.stub.StreamObserver<ir.deepmine.speech.v1.AlignmentResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAlignMethodHelper(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * Service that implements DeepMine Cloud Speech API.
   * </pre>
   */
  public static final class SpeechBlockingStub extends io.grpc.stub.AbstractStub<SpeechBlockingStub> {
    private SpeechBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SpeechBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SpeechBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SpeechBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Performs synchronous non-streaming speech recognition:
     *     receive results after all audio has been sent and processed.
     * </pre>
     */
    public ir.deepmine.speech.v1.RecognizeResponse recognize(ir.deepmine.speech.v1.RecognizeRequest request) {
      return blockingUnaryCall(
          getChannel(), getRecognizeMethodHelper(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Performs synchronous non-streaming speech alignment:
     *     receive results after all audio has been sent and processed.
     * </pre>
     */
    public ir.deepmine.speech.v1.AlignmentResponse align(ir.deepmine.speech.v1.AlignmentRequest request) {
      return blockingUnaryCall(
          getChannel(), getAlignMethodHelper(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * Service that implements DeepMine Cloud Speech API.
   * </pre>
   */
  public static final class SpeechFutureStub extends io.grpc.stub.AbstractStub<SpeechFutureStub> {
    private SpeechFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SpeechFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SpeechFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SpeechFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Performs synchronous non-streaming speech recognition:
     *     receive results after all audio has been sent and processed.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<ir.deepmine.speech.v1.RecognizeResponse> recognize(
        ir.deepmine.speech.v1.RecognizeRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRecognizeMethodHelper(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Performs synchronous non-streaming speech alignment:
     *     receive results after all audio has been sent and processed.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<ir.deepmine.speech.v1.AlignmentResponse> align(
        ir.deepmine.speech.v1.AlignmentRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAlignMethodHelper(), getCallOptions()), request);
    }
  }

  private static final int METHODID_RECOGNIZE = 0;
  private static final int METHODID_ALIGN = 1;
  private static final int METHODID_STREAMING_RECOGNIZE = 2;
  private static final int METHODID_BIDI_STREAMING_RECOGNIZE = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SpeechImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(SpeechImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RECOGNIZE:
          serviceImpl.recognize((ir.deepmine.speech.v1.RecognizeRequest) request,
              (io.grpc.stub.StreamObserver<ir.deepmine.speech.v1.RecognizeResponse>) responseObserver);
          break;
        case METHODID_ALIGN:
          serviceImpl.align((ir.deepmine.speech.v1.AlignmentRequest) request,
              (io.grpc.stub.StreamObserver<ir.deepmine.speech.v1.AlignmentResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_STREAMING_RECOGNIZE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.streamingRecognize(
              (io.grpc.stub.StreamObserver<ir.deepmine.speech.v1.RecognizeResponse>) responseObserver);
        case METHODID_BIDI_STREAMING_RECOGNIZE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.bidiStreamingRecognize(
              (io.grpc.stub.StreamObserver<ir.deepmine.speech.v1.StreamingRecognizeResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class SpeechBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SpeechBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ir.deepmine.speech.v1.SpeechProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Speech");
    }
  }

  private static final class SpeechFileDescriptorSupplier
      extends SpeechBaseDescriptorSupplier {
    SpeechFileDescriptorSupplier() {}
  }

  private static final class SpeechMethodDescriptorSupplier
      extends SpeechBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    SpeechMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (SpeechGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SpeechFileDescriptorSupplier())
              .addMethod(getRecognizeMethodHelper())
              .addMethod(getStreamingRecognizeMethodHelper())
              .addMethod(getBidiStreamingRecognizeMethodHelper())
              .addMethod(getAlignMethodHelper())
              .build();
        }
      }
    }
    return result;
  }
}
