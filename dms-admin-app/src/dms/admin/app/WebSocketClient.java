package dms.admin.app;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

public class WebSocketClient implements WebSocket.Listener {
  private final Consumer<String> onMessage;
  private WebSocket ws;

  private WebSocketClient(Consumer<String> onMessage) { this.onMessage = onMessage; }

  public static void subscribe(String wsUrl, String topic, Consumer<String> consumer) {
    WebSocketClient listener = new WebSocketClient(consumer);
    HttpClient.newHttpClient()
      .newWebSocketBuilder()
      .buildAsync(URI.create(wsUrl), listener)
      .thenAccept(webSocket -> {
        listener.ws = webSocket;
        // STOMP CONNECT + SUBSCRIBE
        listener.send("CONNECT\naccept-version:1.2\nhost:localhost\n\n\u0000");
        listener.send("SUBSCRIBE\nid:sub-0\ndestination:" + topic + "\n\n\u0000");
      })
      .exceptionally(ex -> { ex.printStackTrace(); return null; });
  }

  private void send(String s) { ws.sendText(s, true); }

  @Override public void onOpen(WebSocket webSocket) { webSocket.request(1); }

  @Override public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
    String frame = data.toString();
    int sep = frame.indexOf("\n\n");
    if (sep >= 0) {
      String body = frame.substring(sep + 2).replace("\u0000", "");
      if (onMessage != null && !body.isBlank()) onMessage.accept(body);
    }
    webSocket.request(1);
    return null;
  }

  @Override public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
    webSocket.request(1);
    return null;
  }

  @Override public void onError(WebSocket webSocket, Throwable error) {
    error.printStackTrace(); // <-- no return
  }
}
