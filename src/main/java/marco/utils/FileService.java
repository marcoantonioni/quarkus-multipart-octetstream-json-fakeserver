package marco.utils;

import java.io.BufferedReader;
import java.io.IOException;
import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.vertx.ConsumeEvent;

@ApplicationScoped
public class FileService {

  @ConsumeEvent(blocking = true, value = "file-service")
  public void processFile(BufferedReader br) throws InterruptedException {

      System.out.println("===>> processFile() begin");

      try (br) {
          String currentLine = null;
          while ((currentLine = br.readLine()) != null) {
            System.out.println("=====>>> line: " + currentLine);
          }
      } catch (IOException e) {
          System.err.println("===>> Error"); 
          e.printStackTrace(System.err);
      }

      System.out.println("===>> processFile() end");

  }

}
