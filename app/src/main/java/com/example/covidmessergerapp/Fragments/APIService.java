package com.example.covidmessergerapp.Fragments;

import com.example.covidmessergerapp.Notifications.MyResponse;
import com.example.covidmessergerapp.Notifications.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface APIService{
        @Headers({
                "Content-Type:application/json",
                "Authorization:key=AAAA4Rs5QvY:APA91bGK1Zp1LyOofWHwwX83BZyiAVUM3LCe9P-9OZmFk6faI4su5Qp2uwNT_k0HBJ6TMKYccqcR3CTEdnHY-oNj-oy54rSiWPIinz0qEJsi_EVnCHq7MFWx6VcQ-GxYVdxlDxroRb7E"
        })
        @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
    }

