package com.example.ybl.network;

import android.content.Context;

import com.example.ybl.util.SessionManager;

import android.content.Intent;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final SessionManager sessionManager;
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
        this.sessionManager = SessionManager.getInstance(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();

        // Add headers
        requestBuilder.header("Accept", "application/json");
        requestBuilder.header("Content-Type", "application/json");

        String token = sessionManager.getToken();
        if (token != null) {
            requestBuilder.header("Authorization", "Bearer " + token);
        }

        Request request = requestBuilder.build();
        Response response = chain.proceed(request);

        if (response.code() == 401) {
            // Unauthorized - trigger logout
            Intent intent = new Intent(SessionManager.ACTION_LOGOUT);
            intent.setPackage(context.getPackageName()); // Restrict to this app
            context.sendBroadcast(intent);
        }

        return response;
    }
}
