package com.impakter.impakter.network;


import com.impakter.impakter.object.ActionFollowRespond;
import com.impakter.impakter.object.AddProToCollectionRespond;
import com.impakter.impakter.object.AddressRespond;
import com.impakter.impakter.object.BaseMessageRespond;
import com.impakter.impakter.object.BrandRespond;
import com.impakter.impakter.object.CartIdRespond;
import com.impakter.impakter.object.CartItemRespond;
import com.impakter.impakter.object.CartSellerRespond;
import com.impakter.impakter.object.CollectionRespond;
import com.impakter.impakter.object.CommentRespond;
import com.impakter.impakter.object.ContactRespond;
import com.impakter.impakter.object.ConversationRespond;
import com.impakter.impakter.object.CountryRespond;
import com.impakter.impakter.object.CourierRespond;
import com.impakter.impakter.object.CreateCollectionRespond;
import com.impakter.impakter.object.CreateRoomRespond;
import com.impakter.impakter.object.HomeCategoryRespond;
import com.impakter.impakter.object.HomeLatestRespond;
import com.impakter.impakter.object.HomeTrendingRespond;
import com.impakter.impakter.object.MenuCategoryRespond;
import com.impakter.impakter.object.MessageRespond;
import com.impakter.impakter.object.NotificationRespond;
import com.impakter.impakter.object.OrderDetailRespond;
import com.impakter.impakter.object.OrderRespond;
import com.impakter.impakter.object.ProOfCollectionRespond;
import com.impakter.impakter.object.ProductByCategoryRespond;
import com.impakter.impakter.object.ProductDetailRespond;
import com.impakter.impakter.object.ResultSearchRespond;
import com.impakter.impakter.object.ReviewOrderRespond;
import com.impakter.impakter.object.ReviewRespond;
import com.impakter.impakter.object.SellerProfileRespond;
import com.impakter.impakter.object.SendMessageRespond;
import com.impakter.impakter.object.UserRespond;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ServerAPI {

    // TODO: 16/08/2018 Register Action
    @FormUrlEncoded
    @POST("register")
    Call<UserRespond> register(@Field("firstName") String firstName,
                               @Field("lastName") String lastName,
                               @Field("email") String email,
                               @Field("password") String password);

    // TODO: 16/08/2018 Login Action
    @GET("login")
    Call<UserRespond> login(@Query("email") String email,
                            @Query("password") String password,
                            @Query("deviceToken") String deviceToken,
                            @Query("typeDevice") int typeDevice);

    // TODO: 16/08/2018 Forgot Password Action
    @GET("forgetPassword")
    Call<BaseMessageRespond> forgotPassword(@Query("email") String email);

    // TODO: 16/08/2018 Change password Action
    @FormUrlEncoded
    @POST("changePassword")
    Call<BaseMessageRespond> changePassword(@Field("id") int id,
                                            @Field("oldPassword") String oldPassword,
                                            @Field("newPassword") String newPassword);

    // TODO: 27/08/2018 updateProfile
    @Multipart
    @POST("updateProfile")
    Call<UserRespond> updateProfile(@Part("id") RequestBody id,
                                    @Part("firstName") RequestBody firstName,
                                    @Part("lastName") RequestBody lastName,
                                    @Part("email") RequestBody email,
                                    @Part("address") RequestBody address,
                                    @Part MultipartBody.Part avatar,
                                    @Part MultipartBody.Part cover);

    // TODO: 17/08/2018 Login facebook Action
    @FormUrlEncoded
    @POST("loginFacebook")
    Call<UserRespond> loginFacebook(@Field("socialId") String socialId,
                                    @Field("firstName") String firstName,
                                    @Field("lastName") String lastName,
                                    @Field("email") String email);

    // TODO: 20/08/2018 Get HomeCategory
    @GET("showHomeCategory")
    Call<HomeCategoryRespond> getHomeCategory();

    // TODO: 20/08/2018 GetHomeLatest
    @GET("showHomeLatest")
    Call<HomeLatestRespond> getHomeLatest(@Query("categoryId") String categoryId,
                                          @Query("typeTime") int typeTime,
                                          @Query("page") int page);

    @GET("showHomeTrending")
    Call<HomeTrendingRespond> getHomeTrending(@Query("categoryId") String categoryId,
                                              @Query("page") int page);

    // TODO: 20/08/2018 showHomeBrand
    @GET("showHomeBrand")
    Call<BrandRespond> getHomeBrand(@Query("userId") int userId,
                                    @Query("filter") String filter);

    // TODO: 20/08/2018 showHomeBrand
    @GET("productDetails")
    Call<ProductDetailRespond> getProductDetail(@Query("id") int id);

    // TODO: 20/08/2018 showMenuCategory
    @GET("showMenuCategory")
    Call<MenuCategoryRespond> getMenuCategory();

    // TODO: 20/08/2018 get seller profile
    @GET("sellerProfile")
    Call<SellerProfileRespond> getSellerProfile(@Query("sellerId") int sellerId,
                                                @Query("userId") String userId,
                                                @Query("categoryId") String categoryId,
                                                @Query("page") int page);

    // TODO: 20/08/2018 get cart list of seller
    @GET("listCatOfSeller")
    Call<CartSellerRespond> getListCartOfSeller(@Query("sellerId") int sellerId);

    // TODO: 22/08/2018 Get Personal Collection
    @GET("getCollection")
    Call<CollectionRespond> getCollection(@Query("userId") int userId,
                                          @Query("exceptCollectId") String exceptCollectId);

    // TODO: 22/08/2018 Comment
    @GET("comment")
    Call<CommentRespond> comment(@Query("productId") int productId,
                                 @Query("user_id") int userId,
                                 @Query("content") String content,
                                 @Query("page") int page);

    // TODO: 24/08/2018 showProductByCat
    @GET("showProByCat")
    Call<ProductByCategoryRespond> getProductByCategory(@Query("catId") int categoryId,
                                                        @Query("subCatId") int subCatId,
                                                        @Query("page") int page);

    @GET("getProOfCollection")
    Call<ProOfCollectionRespond> getProductOfCollection(@Query("collectId") int collectionId);

    @GET("userInfo")
    Call<UserRespond> getUserInfo(@Query("id") String otherId,
                                  @Query("userId") String userId);

    // TODO: 06/09/2018 create collection
    @FormUrlEncoded
    @POST("createCollection")
    Call<CreateCollectionRespond> createCollection(@Field("name") String name,
                                                   @Field("userId") int userId);

    // TODO: 06/09/2018 Delete collection
    @FormUrlEncoded
    @POST("deleteCollection")
    Call<BaseMessageRespond> deleteCollection(@Field("collectId") int collectId,
                                              @Field("userId") int userId);

    // TODO: 06/09/2018 Add product to collection
    @GET("addProInCollection")
    Call<AddProToCollectionRespond> addProductToCollection(@Query("collectId") int collectId,
                                                           @Query("proId") int proId);

    // TODO: 06/09/2018 Delete product from collection
    @FormUrlEncoded
    @POST("deleteProInCollection")
    Call<BaseMessageRespond> deleteProductFromCollection(@Field("collectId") int collectId,
                                                         @Field("proId") int proId,
                                                         @Field("userId") int userId);

    // TODO: 06/09/2018 get list of notification
    @GET("showActivity")
    Call<NotificationRespond> getNotification(@Query("userId") int userId,
                                              @Query("page") int page);

    // TODO: 06/09/2018 action follow and unFollow (action 1: follow, 2: unFollow)
    @GET("follow")
    Call<ActionFollowRespond> follow(@Query("userId") int userId,
                                     @Query("receiverId") int receiverId,
                                     @Query("action") int action);

    // TODO: 07/09/2018 get list order
    @GET("listOrder")
    Call<OrderRespond> getOrders(@Query("userId") int userId,
                                 @Query("status") String status,
                                 @Query("sortTime") String sortTime);

    // TODO: 07/09/2018 get order detail
    @GET("orderDetail")
    Call<OrderDetailRespond> getOrderDetail(@Query("orderId") int orderId);

    // TODO: 07/09/2018 Rename Collection
    @GET("renameCollection")
    Call<BaseMessageRespond> renameCollection(@Query("collectId") int collectId,
                                              @Query("name") String name);

    // TODO: 07/09/2018 Move a Product from a collection to another collection
    @GET("moveProInCollection")
    Call<BaseMessageRespond> moveProductToCollection(@Query("fromCollectId") int fromCollectId,
                                                     @Query("toCollectId") int toCollectId,
                                                     @Query("proId") int productId);

    // TODO: 10/09/2018 Get list of message
    @GET("listMessage")
    Call<MessageRespond> getListMessages(@Query("userId") int userId,
                                         @Query("conversationId") int conversationId,
                                         @Query("page") int page);

    // TODO: 10/09/2018 Get list of conversation
    @GET("listMessage")
    Call<ConversationRespond> getListConversation(@Query("userId") int userId,
                                                  @Query("page") int page);

    // TODO: 27/08/2018 sendMessage
    @Multipart
    @POST("sendMessage")
    Call<SendMessageRespond> sendMessage(@Part("senderId") int senderId,
                                         @Part("receiverId") int receiverId,
                                         @Part("roomId") int roomId,
                                         @Part("content") RequestBody content,
                                         @Part MultipartBody.Part file);

    // TODO: 11/09/2018 get list of follower
    @GET("listFollower")
    Call<ContactRespond> getListFollower(@Query("userId") int userId);

    // TODO: 11/09/2018 get list of following
    @GET("listFollowing")
    Call<ContactRespond> getListFollowing(@Query("userId") int userId,
                                          @Query("yourId") int yourId);

    // TODO: 11/09/2018 review order
    @GET("review")
    Call<ReviewOrderRespond> reviewOrder(@Query("orderId") int orderId,
                                         @Query("productId") int productId,
                                         @Query("userId") int userId,
                                         @Query("content") String content,
                                         @Query("rate") float rate);

    // TODO: 12/09/2018 get list review
    @GET("listReview")
    Call<ReviewRespond> getListReview(@Query("sellerId") int sellerId,
                                      @Query("page") int page);

    // TODO: 17/09/2018 get cartId
    @GET("getCartId")
    Call<CartIdRespond> getCartId(@Query("userId") String userId,
                                  @Query("deviceToken") String deviceToken);

    // TODO: 17/09/2018 get list product in my cart
    @GET("getCartItem")
    Call<CartItemRespond> getCartItem(@Query("cartId") int cartId);

    // TODO: 17/09/2018 Delete cart item
    @GET("deleteCartItem")
    Call<CartItemRespond> deleteCartItem(@Query("id") int cartItemId,
                                         @Query("cartId") int cartId,
                                         @Query("action") int action);


    // TODO: 17/09/2018 action add product to cart
    @GET("addToCart")
    Call<BaseMessageRespond> addToCart(@Query("userId") String userId,
                                       @Query("productId") int productId,
                                       @Query("cartId") int cartId,
                                       @Query("size") String size,
                                       @Query("color") String color,
                                       @Query("quantity") int quantity);

    @GET("createRoomMessage")
    Call<CreateRoomRespond> createConversation(@Query("senderId") int senderId,
                                               @Query("receiverId") int receiverId);

    // TODO: 18/09/2018 get list address
    @GET("showListAddress")
    Call<AddressRespond> getListAddress(@Query("userId") int userId);

    // TODO: 18/09/2018 get list country
    @GET("showListCountry")
    Call<CountryRespond> getListCountry();

    // TODO: 19/09/2018 Edit address
    @GET("address")
    Call<BaseMessageRespond> updateAddress(@Query("userId") int userId,
                                           @Query("address") String address,
                                           @Query("cityTown") String cityTown,
                                           @Query("state") String state,
                                           @Query("countryId") int countryId,
                                           @Query("zipcode") String zipcode,
                                           @Query("phoneNumber") String phoneNumber,
                                           @Query("action") int action);

    // TODO: 19/09/2018 search
    @GET("search")
    Call<ResultSearchRespond> search(@Query("keyword") String keyword,
                                     @Query("page") int page);

    // TODO: 20/09/2018 edit credit card
    @GET("editCreditCard")
    Call<BaseMessageRespond> editCreditCard(@Query("userId") int userId,
                                            @Query("holderName") String holderName,
                                            @Query("cardNumber") String cardNumber,
                                            @Query("expirationDate") String expirationDate,
                                            @Query("numberCvv") String numberCvv);

    // TODO: 21/09/2018 get courier
    @GET("getCourier")
    Call<CourierRespond> getCouriers(@Query("cartId") int cartId,
                                     @Query("zipcode") String zipCode);

    // TODO: 25/09/2018 send order
    @FormUrlEncoded
    @POST("saveOrder")
    Call<BaseMessageRespond> sendOrder(@Field("dataOrder") String dataOrder,
                                       @Field("dataShipping") String dataShipping,
                                       @Field("dataPayment") String dataPayment);

    // TODO: 25/09/2018
    @FormUrlEncoded
    @POST("updateCart")
    Call<BaseMessageRespond> updateCart(@Field("updateData") String updateData);

    // TODO: 25/09/2018 save cart to order: is used in case user don't login
    @GET("saveCartToOrder")
    Call<BaseMessageRespond> saveCartToOrder(@Query("cartId") int cartId,
                                             @Query("userId") int userId);

    @GET("sendStripeRequest")
    Call<BaseMessageRespond> sendStripeRequest(@Query("token") String token,
                                               @Query("totalCost") float totalCost);

    @GET("saveProductToCart")
    Call<BaseMessageRespond> saveProductToCart(@Query("cartData") String cartData);
}
