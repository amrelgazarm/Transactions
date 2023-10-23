package com.qceccenter.qcec.utils.retrofit;

import com.qceccenter.qcec.models.Authentication;
import com.qceccenter.qcec.models.CompanyInfo;
import com.qceccenter.qcec.models.ContactUs;
import com.qceccenter.qcec.models.Profile;
import com.qceccenter.qcec.models.SearchResult;
import com.qceccenter.qcec.models.TransactionDetails;
import com.qceccenter.qcec.models.Transactions;
import com.qceccenter.qcec.models.VisitDetails;
import com.qceccenter.qcec.models.VisitSubmission;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface QCECService {
    @FormUrlEncoded
    @POST("sendSignIn")
    Call<Authentication> signIn(@Field("email") String email, @Field("password") String password);

    @GET("getAllTransactions")
    Call<Transactions> getAllTransactions(@Header("Authorization") String accessToken);

    @GET("getTransactionById/{Id}")
    Call<TransactionDetails> getTransactionByID(@Path("Id") int transactionID, @Header("Authorization") String accessToken);

    @GET("getVisitById/{Id}")
    Call<VisitDetails> getVisitByID(@Path("Id") int visitID, @Header("Authorization") String accessToken);

    @FormUrlEncoded
    @POST("auth/addVisit")
    Call<VisitSubmission> addVisit(@Header("Authorization") String accessToken, @Field("transaction_id") int transactionID, @Field("works_description") String workDescription, @Field("notes") String notes, @Field("latitude") double latitude, @Field("longitude") double longitude, @Field("sms_to_owner") int shouldSendToOwner, @Field("sms_to_contractor") int shouldSendToCont);

    @Multipart
    @POST("addImagesToVisit/{Id}")
    Call<VisitSubmission> submitImages(@Header("Authorization") String accessToken, @Path("Id") int visitID, @Part List<MultipartBody.Part> attachmentsList);

    @GET("about-company")
    Call<CompanyInfo> getCompanyInfo();

    @FormUrlEncoded
    @POST("search")
    Call<SearchResult> search(@Header("Authorization") String accessToken, @Field("data") String searchStr);

    @GET("contacts_info")
    Call<ContactUs> getContactInfo(@Header("Authorization") String accessToken);

    @GET("getTransactionsNotifications")
    Call<Transactions> getNewTransactions(@Header("Authorization") String accessToken);

    @FormUrlEncoded
    @POST("sendSignUp")
    Call<VisitSubmission> signup(@Field("user_name") String username, @Field("password") String password, @Field("phone") String phone, @Field("national_id") String nationalID);

    @POST("auth/me")
    Call<Profile> getUserInfo(@Header("Authorization") String accessToken);
}
