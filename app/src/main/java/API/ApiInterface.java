package API;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by winhtaikaung on 2/27/16.
 */

public interface ApiInterface {

    @GET(APIRoutes.CAR_PARKS)
    void getParkings(Callback<String> callBack);

}
