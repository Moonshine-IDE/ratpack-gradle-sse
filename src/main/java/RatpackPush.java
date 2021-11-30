import org.reactivestreams.Publisher;
import ratpack.sse.ServerSentEvents;
import java.time.Duration;
import static ratpack.stream.Streams.periodically;
import ratpack.server.RatpackServer;
import ratpack.server.BaseDir;

import service.FileUtilService;

public class RatpackPush {

  public static void main(String[] args) throws Exception {

    RatpackServer.start(server -> server
      .serverConfig(c -> c.baseDir(BaseDir.find()))
      .handlers(chain -> chain
        .all(ctx -> {
          ctx.header("Access-Control-Allow-Origin", "*")
             .header("Access-Control-Allow-Headers", "*")
             .next();
        })
        .files(f -> f.dir("public").indexFiles("index.html"))
        .get("jsonData", ctx -> ctx.render(FileUtilService.getCSVDataFileAsJSONString()))
        .get("dataGrid", ctx -> {

            FileUtilService fileUtilService = new FileUtilService();
            // We check the datasource once per minute, if there is a change the event is streamed to the
            // to the browser. If the file is unchanged (eventData="skip") no events are sent to the
            // browser
            Publisher<String> stream = periodically(ctx, Duration.ofMinutes(1), i -> fileUtilService.getEventData())
              .filter(r -> ( r == null || !r.equals("skip") ));

            ServerSentEvents events = ServerSentEvents.serverSentEvents(stream, dataGridEvent -> {
                dataGridEvent.event("message");
                dataGridEvent.id(Long.toString(System.currentTimeMillis()));
                dataGridEvent.data(theData -> theData);
            });

            ctx.render(events);
        })
      )
    );
  }
}
