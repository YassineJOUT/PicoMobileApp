package ma.fstm.ilisi.pico.picomobile.Directions.Repository;

import ma.fstm.ilisi.pico.picomobile.Directions.Model.Direction;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DirectionServiceApi {

    @GET("directions/json")
    Call<Direction> getDirectionTo(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key);

}

