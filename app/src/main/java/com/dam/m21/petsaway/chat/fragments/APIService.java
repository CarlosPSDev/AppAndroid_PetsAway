package com.dam.m21.petsaway.chat.fragments;

import com.dam.m21.petsaway.chat.notificationes.MyResponse;
import com.dam.m21.petsaway.chat.notificationes.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAwhd2nik:APA91bGxOwSyu-3BJasbbNYgxPJW5r7B_4toxkymonVcdQcqzgohTi4fQMFV9SvV2Sv7mRmSSLnQ7SY6_cMkRy-KOVPLj98x4jXhbZaMZXhhToWhRkU-OksGVobcvpyMyL-hy95gEZIl"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
