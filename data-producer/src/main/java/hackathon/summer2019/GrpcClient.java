package hackathon.summer2019;

import hackathon.summer2019.grpc.DataRequest;
import hackathon.summer2019.grpc.DataResponse;
import hackathon.summer2019.grpc.DataServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrpcClient {

  private static final Logger LOGGER = Logger.getLogger(GrpcClient.class.getName());

  private final ManagedChannel channel;
  private final DataServiceGrpc.DataServiceBlockingStub blockingStub;

  public GrpcClient(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build());
  }

  GrpcClient(ManagedChannel channel) {
    this.channel = channel;
    blockingStub = DataServiceGrpc.newBlockingStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public void sendData(String data) {
    DataRequest request = DataRequest.newBuilder().setData(data).build();
    DataResponse response;
    try {
      response = blockingStub.sendData(request);
    } catch (StatusRuntimeException e) {
      LOGGER.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return;
    }
    LOGGER.info("Greeting: " + response.getMessage());
  }
}
