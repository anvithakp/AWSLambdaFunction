package rekognition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.rekognition.model.*;
import com.google.gson.Gson;


/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<Object, GatewayResponse> {

    private static final String LABEL_DL = "Driving License";
    private static final float CONFIDENCE_LEVEL = 85F;
    private static final int MAX_LEVELS = 5;


    @Override
    public GatewayResponse handleRequest(Object request, Context context) {
        System.out.println("Inside lambda : " + request);
        Gson gson = new Gson();
        Request input = gson.fromJson((String) request, Request.class);
        System.out.println("Inside lambda after gson : " + input);

        AmazonRekognition amazonRekognition = AmazonRekognitionClientBuilder.defaultClient();
        String isDL = "invalid";
        DetectLabelsRequest labelsRequest = new DetectLabelsRequest()
                .withImage(new Image().withS3Object(new S3Object().withName(input.getFileName()).withBucket("mssecarrental")))
                .withMaxLabels(MAX_LEVELS)
                .withMinConfidence(CONFIDENCE_LEVEL);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        try {
            DetectLabelsResult result = amazonRekognition.detectLabels(labelsRequest);
            List<Label> labels = result.getLabels();

            for (Label label : labels) {

                if (label.getName().equalsIgnoreCase(LABEL_DL)) {
                    System.out.println("label is : " + label);
                    isDL = "valid";

                    // Success case return back 200
                    return new GatewayResponse(isDL, headers, 200);

                }
            }
        }
            catch(AmazonRekognitionException e){
                    e.printStackTrace();
                }

                // Error case 404
        return new GatewayResponse(isDL, headers, 404 );

    }

        }

