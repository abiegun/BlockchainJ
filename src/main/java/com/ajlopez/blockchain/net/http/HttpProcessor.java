package com.ajlopez.blockchain.net.http;

import com.ajlopez.blockchain.json.*;
import com.ajlopez.blockchain.jsonrpc.JsonRpcException;
import com.ajlopez.blockchain.jsonrpc.JsonRpcProcessor;
import com.ajlopez.blockchain.jsonrpc.JsonRpcRequest;
import com.ajlopez.blockchain.jsonrpc.JsonRpcResponse;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

/**
 * Created by ajlopez on 04/12/2018.
 */
public class HttpProcessor {
    private final JsonRpcProcessor jsonRpcProcessor;
    private final Reader reader;
    private final Writer writer;

    public HttpProcessor(JsonRpcProcessor jsonRpcProcessor, Reader reader, Writer writer) {
        this.jsonRpcProcessor = jsonRpcProcessor;
        this.reader = reader;
        this.writer = writer;
    }

    public void process() throws JsonLexerException, JsonParserException, IOException, JsonRpcException {
        HttpRequestParser parser = new HttpRequestParser();

        HttpRequest request = parser.parse(this.reader);

        if (request.getMethod().equals("POST")) {
            JsonParser jparser = new JsonParser(request.getReader());
            JsonValue jvalue = jparser.parseValue();

            if (jvalue.getType() == JsonValueType.OBJECT) {
                JsonObjectValue jovalue = (JsonObjectValue) jvalue;

                if (jovalue.hasProperty("method") && jovalue.hasProperty("id") && jovalue.hasProperty("version") && jovalue.hasProperty("params") && jovalue.getProperty("params").getType() == JsonValueType.ARRAY) {
                    String id = jovalue.getProperty("id").getValue().toString();
                    String version = jovalue.getProperty("version").getValue().toString();
                    String method = jovalue.getProperty("method").getValue().toString();

                    JsonArrayValue avalue = (JsonArrayValue)jovalue.getProperty("params");
                    List<JsonValue> params = avalue.getValues();

                    JsonRpcRequest jsonrequest = new JsonRpcRequest(id, version, method, params);

                    JsonRpcResponse jsonresponse = this.jsonRpcProcessor.processRequest(jsonrequest);

                    JsonBuilder builder = new JsonBuilder();
                    JsonValue response = builder.object()
                            .name("id")
                            .value(jsonresponse.getId())
                            .name("version")
                            .value(jsonresponse.getVersion())
                            .name("result")
                            .value(jsonresponse.getResult())
                            .build();

                    this.writer.write(response.toString());
                    this.writer.flush();
                }
            }
        }
    }
}

