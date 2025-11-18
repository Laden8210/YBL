package com.example.ybl.repository;

import android.content.Context;

import com.example.ybl.interfaces.ApiCallback;
import com.example.ybl.model.BaseResponse;
import com.example.ybl.model.DriverDashboard;
import com.example.ybl.model.Schedule;
import com.example.ybl.model.TodayScheduleResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverRepository extends BaseRepository {

    public static final String TAG = DriverRepository.class.getSimpleName();

    public DriverRepository(Context context) {
        super(context);
    }

    public void getDashboard(ApiCallback<DriverDashboard> callback) {
        executeCall(apiService.getDriverDashboard(), callback);
    }

    public void getTodaySchedule(ApiCallback<Schedule> callback) {
        apiService.getTodaySchedule().enqueue(new Callback<BaseResponse<TodayScheduleResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<TodayScheduleResponse>> call, Response<BaseResponse<TodayScheduleResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getData().getSchedule());
                } else {
                    callback.onError("Failed to get today's schedule");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<TodayScheduleResponse>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
