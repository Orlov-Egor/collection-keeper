package common.interaction;

import common.data.SpaceMarine;

import java.io.Serializable;
import java.util.NavigableSet;

/**
 * Class for get response value.
 */
public class Response implements Serializable {
    private ResponseCode responseCode;
    private String responseBody;
    NavigableSet<SpaceMarine> marinesCollection;

    public Response(ResponseCode responseCode, String responseBody, NavigableSet<SpaceMarine> marinesCollection) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.marinesCollection = marinesCollection;
    }

    /**
     * @return Response сode.
     */
    public ResponseCode getResponseCode() {
        return responseCode;
    }

    /**
     * @return Response body.
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * @return Marines collection last save.
     */
    public NavigableSet<SpaceMarine> getMarinesCollection() {
        return marinesCollection;
    }

    @Override
    public String toString() {
        return "Response[" + responseCode + ", " + responseBody + "]";
    }
}
