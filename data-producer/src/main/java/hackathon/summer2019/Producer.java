package hackathon.summer2019;

import com.github.javafaker.Faker;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Producer {

  private static final Logger LOGGER = Logger.getLogger(Producer.class.getSimpleName());

  private static final int GRCP_PORT = 50051;

  public static void main(String[] args) throws Exception {

    List<GrpcClient> clients = args.length == 0 ?
      Lists.newArrayList(new GrpcClient("localhost", GRCP_PORT)) :
      Arrays.stream(args)
          .map(host -> new GrpcClient(host, GRCP_PORT))
          .collect(Collectors.toList());

    Faker faker = new Faker();

    while (true) {
      String data = faker.chuckNorris().fact();
      LOGGER.info("sending data: " + data);
      clients.forEach(client -> client.sendData(data));
    }
  }
}
