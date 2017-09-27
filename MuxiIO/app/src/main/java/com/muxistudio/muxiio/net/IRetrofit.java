package com.muxistudio.muxiio.net;

import com.muxistudio.muxiio.model.AddComments;
import com.muxistudio.muxiio.model.AddCommentsSuccessful;
import com.muxistudio.muxiio.model.AddShare;
import com.muxistudio.muxiio.model.AfterChangeAvatar;
import com.muxistudio.muxiio.model.AfterEditShare;
import com.muxistudio.muxiio.model.AllUsers;
import com.muxistudio.muxiio.model.AvatarUrl;
import com.muxistudio.muxiio.model.Comments;
import com.muxistudio.muxiio.model.CreateId;
import com.muxistudio.muxiio.model.DeleteInfo;
import com.muxistudio.muxiio.model.EditShare;
import com.muxistudio.muxiio.model.LoginInfo;
import com.muxistudio.muxiio.model.ProfileEdited;
import com.muxistudio.muxiio.model.ProfileInfo;
import com.muxistudio.muxiio.model.RegisterInfo;
import com.muxistudio.muxiio.model.ShareList;
import com.muxistudio.muxiio.model.Token;
import com.muxistudio.muxiio.model.UpLoadResponse;
import com.muxistudio.muxiio.model.UserProfile;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kolibreath on 17-8-2.
 */

public interface IRetrofit {

    /**
     * auth
     */
    //Login auth
    @POST("login/")
    Observable<Token> postLogin(@Body LoginInfo info);

    //register auth
    @POST("signup/")
    Observable<CreateId> postRegister(@Body RegisterInfo info);


    /**
     *share
     *
     *
     */
    @GET("get_all_id/")
    Observable<AllUsers> getUsersname2Id();

   @GET("get_some/")
   Observable<ShareList> getShareByNumber(@Query("num") int number);

    @GET("./")
    Observable<ShareList> getShareBySort(@Query("page")int page,
                                    @Query("sort")String sort);
    //get share by  (get all shares)
    @GET("all/")
    Observable<ShareList> getShareByPage(@Query("page")int page);

    //login share
    @POST("login/")
    Observable
            <Token> postShareLogin(@Body LoginInfo info);

    //register share; register share used in RegisterActivity
    @POST("signup/")
    Observable<Response<CreateId>>postShareRegister(@Body RegisterInfo info);

    //add share
    @POST("send/")
    Observable<AddShare> postShareAdd(@Header("token") String token,
                                @Body AddShare addShare);

    //delete share
    @DELETE("{id}/delete/")
    Observable<DeleteInfo> deleteShareDelete(@Path("id") int id,
                                       @Header("token")String token);

    @POST("upload/")
    @Multipart
    Observable<UpLoadResponse> postUpLoadPic(@Part MultipartBody.Part part);


    //send comments about a share
    @POST("{id}/add_comment/")
    Observable<Response<AddCommentsSuccessful>> postCommentsAdd(@Header("token")String token,
                                                @Path("id") int id,
                                                @Body AddComments comments);

    @GET("{id}/comments")
    Observable<Comments> getCommentList(@Header("token") String token,
                           @Path("id") int id);


    @PUT("{id}/edit/")
    Observable<AfterEditShare> putEditShare(@Header("token") String token,
                                            @Body EditShare editShare,
                                            @Path("id") int id);

    //no use of return data, replace it with string
    @POST("change_avatar/")
    Observable<AfterChangeAvatar> postChangeAvatar(@Header("token")String shareToken,
                                                   @Body AvatarUrl url);


    @GET("/get_all_id/")
    Observable<Token> getAllId();

    /**
     * message
     */

    //get return all shares about a user
    @GET("get_one_all/{id}")
    Call<ShareList> getOneAllShare(@Path("id") int id);

   @GET("get_one_all/{id}")
   Observable<ShareList> getUserAllShare(@Path("id") int id);

    //the return data is null
    @POST("{id}/read_comment/")
    Call<String> postSetMessageRead(@Header("token")String token,
            @Path("id") int id);

    //get comment using url
    @GET("{id}/comments/")
    Call<Comments> getMessageComment(@Path("id")int id);




    /**
     * profile
     */
    @GET("show_profile/{id}/")
    Observable<UserProfile> getUserProfile(@Header("token") String authToken,
                                     @Path("id") int id);
    
    @POST("/edit_profile/")
    Observable<ProfileEdited> postUserProfile(@Header("token")String token, @Body ProfileInfo profileInfo);

}

