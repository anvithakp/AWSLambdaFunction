package chatbot;


import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lexruntime.AmazonLexRuntime;
import com.amazonaws.services.lexruntime.AmazonLexRuntimeClientBuilder;
import com.amazonaws.services.lexruntime.model.PostTextRequest;
import com.amazonaws.services.lexruntime.model.PostTextResult;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Request, Object> {

    public Object handleRequest(final Request input, final Context context) {
        context.getLogger().log("handle request is :   " + input.getMessage());
        System.out.println("handleRequest : " + input);

        AmazonLexRuntime client = AmazonLexRuntimeClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        PostTextRequest text = new PostTextRequest();
        text.setBotName("carrentconversations");
        text.setBotAlias("carrentchatbot");
        text.setUserId("mssechargers");
        text.setInputText(input.getMessage());
        PostTextResult result = client.postText(text);
        System.out.println("handleRequest : response from lex :" + result.getMessage());

//        return result.getMessage();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        System.out.println("INput : " + input);
            String output = result.getMessage();
            return new GatewayResponse(output, headers, 200);
    }


}
