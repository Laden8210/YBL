package com.example.ybl.repository;

import android.content.Context;

import com.example.ybl.interfaces.ApiCallback;
import com.example.ybl.model.BaseResponse;
import com.example.ybl.model.ErrorResponse;
import com.example.ybl.model.LoginRequest;
import com.example.ybl.model.LoginResponse;
import com.example.ybl.model.UpdateProfileRequest;
import com.example.ybl.model.User;
import com.example.ybl.model.RegisterRequest;
import com.example.ybl.util.SessionManager;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository extends BaseRepository {

    private final SessionManager sessionManager;

    public AuthRepository(Context context) {
        super(context);
        this.sessionManager = SessionManager.getInstance(context);
    }

    public void login(LoginRequest loginRequest, ApiCallback<LoginResponse> callback) {
        apiService.login(loginRequest).enqueue(new Callback<BaseResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<LoginResponse>> call, Response<BaseResponse<LoginResponse>> response) {
                handleAuthResponse(response, callback);
            }

            @Override
            public void onFailure(Call<BaseResponse<LoginResponse>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void register(RegisterRequest registerRequest, ApiCallback<LoginResponse> callback) {
        apiService.register(registerRequest).enqueue(new Callback<BaseResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<LoginResponse>> call, Response<BaseResponse<LoginResponse>> response) {
                handleAuthResponse(response, callback);
            }

            @Override
            public void onFailure(Call<BaseResponse<LoginResponse>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    private void handleAuthResponse(Response<BaseResponse<LoginResponse>> response, ApiCallback<LoginResponse> callback) {
        if (response.isSuccessful() && response.body() != null) {
            BaseResponse<LoginResponse> baseResponse = response.body();
            LoginResponse loginResponse = baseResponse.getData();
            if (loginResponse != null && loginResponse.getUser() != null && loginResponse.getToken() != null) {
                sessionManager.createLoginSession(loginResponse.getUser(), loginResponse.getToken());
                callback.onSuccess(loginResponse);
            } else {
                callback.onError("Invalid response from server");
            }
        } else {
            if (response.errorBody() != null) {
                try {
                    Gson gson = new Gson();
                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), ErrorResponse.class);
                    callback.onError(errorResponse.getFirstErrorMessage());
                } catch (Exception e) {
                    callback.onError("Request failed with error: " + e.getMessage());
                }
            } else {
                callback.onError("Request failed with no error details");
            }
        }
    }

    public void getProfile(ApiCallback<User> callback) {
        executeCall(apiService.getProfile(), callback);
    }

    public void updateProfile(UpdateProfileRequest request, ApiCallback<User> callback) {
        executeCall(apiService.updateProfile(request), callback);
    }

    public void logout(ApiCallback<Void> callback) {
        apiService.logout().enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                if (response.isSuccessful()) {
                    sessionManager.logoutUser();
                    callback.onSuccess(null);
                } else {
                    callback.onError("Logout failed");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
