package ba.sum.fsre.habittracker.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupabaseClient {


    public static final String BASE_URL = "https://jyzdhvwfgpdtykwpexvt.supabase.co/rest/v1/";


    public static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imp5emRodndmZ3BkdHlrd3BleHZ0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3Njc0Njc5MDcsImV4cCI6MjA4MzA0MzkwN30.unmtlMBxAarYKWzTiwLM7P4c8pyLTQOz8s13seTv6so";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("apikey", API_KEY)
                                .header("Content-Type", "application/json");

                        if (original.header("Authorization") == null) {
                            requestBuilder.header("Authorization", "Bearer " + API_KEY);
                        }

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    })
                    .build();


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}