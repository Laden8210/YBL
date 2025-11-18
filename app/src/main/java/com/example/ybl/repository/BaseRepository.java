package com.example.ybl.repository;


import android.content.Context;
import com.example.ybl.interfaces.ApiCallback;
import com.example.ybl.model.BaseResponse;
import com.example.ybl.network.ApiClient;
import com.example.ybl.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BaseRepository {
    protected ApiService apiService;
    protected Context context;

    public BaseRepository(Context context) {
        this.context = context;
        this.apiService = ApiClient.getInstance(context).createService(ApiService.class);
    }

    protected <T> void executeCall(Call<BaseResponse<T>> call, ApiCallback<T> callback) {
        call.enqueue(new Callback<BaseResponse<T>>() {
            @Override
            public void onResponse(Call<BaseResponse<T>> call, Response<BaseResponse<T>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getData());
                } else {
                    String errorMessage = "Request failed";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            errorMessage = "Unknown error occurred";
                        }
                    }
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<T>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

}