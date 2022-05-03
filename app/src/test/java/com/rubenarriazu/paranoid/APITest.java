package com.rubenarriazu.paranoid;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.rubenarriazu.paranoid.api.Endpoints;
import com.rubenarriazu.paranoid.api.APIClient;
import com.rubenarriazu.paranoid.api.requests.*;
import com.rubenarriazu.paranoid.api.responses.*;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// https://www.baeldung.com/junit-5-test-order
// https://mkyong.com/junit5/junit-5-test-execution-order/

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class APITest {

    private static String martinToken;
    private static int martinID;
    private static String sofiaToken;
    private static int sofiaID;
    private static int petitionID;

    @Test
    @Order(1)
    public void registration() {
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "martin", "1234", "Martin", "M");

        ArrayList<Integer> httpCodes = new ArrayList<>();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<ResponseBody> call = endpoints.registration(registrationRequest);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                httpCodes.add(response.code());
                conciseNotify(APITest.this);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                conciseNotify(APITest.this);
            }

        });

        conciseWait(this);

        if (httpCodes.size() == 1) {
            assertEquals(201, httpCodes.get(0));
        } else {
            fail();
        }
    }

    @Test
    @Order(2)
    public void login() {
        LoginRequest loginRequest = new LoginRequest("martin", "1234");

        ArrayList<Integer> httpCodes = new ArrayList<>();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<LoginResponse> call = endpoints.login(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                httpCodes.add(response.code());
                martinToken = response.body().getToken();
                conciseNotify(APITest.this);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                conciseNotify(APITest.this);
            }

        });

        conciseWait(this);

        if (httpCodes.size() == 1) {
            assertEquals(200, httpCodes.get(0));
        } else {
            fail();
        }
    }

    @Test
    @Order(3)
    public void getUser() {
        ArrayList<Integer> httpCodes = new ArrayList<>();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<UserResponse> call = endpoints.getUser("Token " + martinToken);
        call.enqueue(new Callback<UserResponse>() {

            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                httpCodes.add(response.code());
                UserResponse userResponse = response.body();
                martinID = userResponse.getId();
                conciseNotify(APITest.this);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                conciseNotify(APITest.this);
            }

        });

        conciseWait(this);

        if (httpCodes.size() == 1) {
            assertEquals(200, httpCodes.get(0));
        } else {
            fail();
        }
    }

    @Test
    @Order(4)
    public void getUserById() {
        ArrayList<Integer> httpCodes = new ArrayList<>();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<UserResponse> call = endpoints.getUser("Token " + martinToken, martinID);
        call.enqueue(new Callback<UserResponse>() {

            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                httpCodes.add(response.code());
                conciseNotify(APITest.this);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                conciseNotify(APITest.this);
            }

        });

        conciseWait(this);

        if (httpCodes.size() == 1) {
            assertEquals(200, httpCodes.get(0));
        } else {
            fail();
        }
    }

    @Test
    @Order(5)
    public void accounts() {
        ArrayList<Integer> httpCodes = new ArrayList<>();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<List<AccountResponse>> call = endpoints.getAccounts("Token " + martinToken);
        call.enqueue(new Callback<List<AccountResponse>>() {

            @Override
            public void onResponse(Call<List<AccountResponse>> call, Response<List<AccountResponse>> response) {
                httpCodes.add(response.code());
                conciseNotify(APITest.this);
            }

            @Override
            public void onFailure(Call<List<AccountResponse>> call, Throwable t) {
                conciseNotify(APITest.this);
            }

        });

        conciseWait(this);

        if (httpCodes.size() == 1) {
            assertEquals(200, httpCodes.get(0));
        } else {
            fail();
        }
    }

    @Test
    @Order(6)
    public void account() {
        ArrayList<Integer> httpCodes = new ArrayList<>();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<AccountResponse> call = endpoints.getAccount("Token " + martinToken, martinID);
        call.enqueue(new Callback<AccountResponse>() {

            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                httpCodes.add(response.code());
                conciseNotify(APITest.this);
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                conciseNotify(APITest.this);
            }

        });

        conciseWait(this);

        if (httpCodes.size() == 1) {
            assertEquals(200, httpCodes.get(0));
        } else {
            fail();
        }
    }

    @Test
    @Order(7)
    public void search() {
        SearchUserRequest searchUserRequest = new SearchUserRequest("m");
        ArrayList<Integer> httpCodes = new ArrayList<>();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<List<UserResponse>> call = endpoints.searchUser("Token " + martinToken, searchUserRequest);
        call.enqueue(new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                httpCodes.add(response.code());
                conciseNotify(APITest.this);
            }

            @Override
            public void onFailure(Call<List<UserResponse>> call, Throwable t) {
                conciseNotify(APITest.this);
            }
        });

        conciseWait(this);

        if (httpCodes.size() == 1) {
            assertEquals(200, httpCodes.get(0));
        } else {
            fail();
        }
    }

    @Test
    @Order(8)
    public void createASecondUser() {
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);

        RegistrationRequest registrationRequest = new RegistrationRequest(
                "sofia", "1234", "Sofia", "S");
        Call<ResponseBody> callRegistration = endpoints.registration(registrationRequest);
        callRegistration.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                conciseNotify(APITest.this);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                conciseNotify(APITest.this);
            }

        });
        conciseWait(this);

        LoginRequest loginRequest = new LoginRequest("sofia", "1234");
        Call<LoginResponse> callLogin = endpoints.login(loginRequest);
        callLogin.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                sofiaToken = response.body().getToken();
                conciseNotify(APITest.this);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                conciseNotify(APITest.this);
            }

        });
        conciseWait(this);

        Call<UserResponse> callUR = endpoints.getUser("Token " + sofiaToken);
        callUR.enqueue(new Callback<UserResponse>() {

            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                sofiaID = userResponse.getId();
                conciseNotify(APITest.this);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                conciseNotify(APITest.this);
            }

        });

        conciseWait(this);
    }

    @Test
    @Order(9)
    public void sendFollowerPetition() {
        SendPetitionRequest sendPetitionRequest = new SendPetitionRequest(sofiaID);
        ArrayList<Integer> httpCodes = new ArrayList<>();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<PetitionResponse> call = endpoints.sendPetition("Token " + martinToken, sendPetitionRequest);
        call.enqueue(new Callback<PetitionResponse>() {
            @Override
            public void onResponse(Call<PetitionResponse> call, Response<PetitionResponse> response) {
                httpCodes.add(response.code());
                petitionID = response.body().getId();
                conciseNotify(APITest.this);
            }

            @Override
            public void onFailure(Call<PetitionResponse> call, Throwable t) {
                conciseNotify(APITest.this);
            }
        });

        conciseWait(this);

        if (httpCodes.size() == 1) {
            assertEquals(200, httpCodes.get(0));
        } else {
            fail();
        }
    }

    @Test
    @Order(10)
    public void getPetition() {
        ArrayList<Integer> httpCodes = new ArrayList<>();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<PetitionResponse> call = endpoints.getPetition("Token " + sofiaToken, petitionID);
        call.enqueue(new Callback<PetitionResponse>() {
            @Override
            public void onResponse(Call<PetitionResponse> call, Response<PetitionResponse> response) {
                httpCodes.add(response.code());
                conciseNotify(APITest.this);
            }

            @Override
            public void onFailure(Call<PetitionResponse> call, Throwable t) {
                conciseNotify(APITest.this);
            }
        });
        conciseWait(this);
        if (httpCodes.size() == 1) {
            assertEquals(200, httpCodes.get(0));
        } else {
            fail();
        }
    }

    @Test
    @Order(11)
    public void discardPetition() {
        ArrayList<Integer> httpCodes = new ArrayList<>();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<PetitionResponse> call = endpoints.discardPetition("Token " + sofiaToken, petitionID);
        call.enqueue(new Callback<PetitionResponse>() {
            @Override
            public void onResponse(Call<PetitionResponse> call, Response<PetitionResponse> response) {
                httpCodes.add(response.code());
                conciseNotify(APITest.this);
            }

            @Override
            public void onFailure(Call<PetitionResponse> call, Throwable t) {
                conciseNotify(APITest.this);
            }
        });
        conciseWait(this);
        if (httpCodes.size() == 1) {
            assertEquals(204, httpCodes.get(0));
        } else {
            fail();
        }
    }

    @Test
    @Order(12)
    public void sendNewPetition() {
        SendPetitionRequest sendPetitionRequest = new SendPetitionRequest(sofiaID);
        ArrayList<Integer> httpCodes = new ArrayList<>();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<PetitionResponse> call = endpoints.sendPetition("Token " + martinToken, sendPetitionRequest);
        call.enqueue(new Callback<PetitionResponse>() {
            @Override
            public void onResponse(Call<PetitionResponse> call, Response<PetitionResponse> response) {
                httpCodes.add(response.code());
                conciseNotify(APITest.this);
            }

            @Override
            public void onFailure(Call<PetitionResponse> call, Throwable t) {
                conciseNotify(APITest.this);
            }
        });

        conciseWait(this);

        if (httpCodes.size() == 1) {
            assertEquals(200, httpCodes.get(0));
        } else {
            fail();
        }
    }

    @Test
    @Order(13)
    public void getPetitions() {
        ArrayList<Integer> httpCodes = new ArrayList<>();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<List<PetitionResponse>> call = endpoints.getPetitions("Token " + sofiaToken);
        call.enqueue(new Callback<List<PetitionResponse>>() {
            @Override
            public void onResponse(Call<List<PetitionResponse>> call, Response<List<PetitionResponse>> response) {
                httpCodes.add(response.code());
                petitionID = response.body().get(0).getId();
                conciseNotify(APITest.this);
            }

            @Override
            public void onFailure(Call<List<PetitionResponse>> call, Throwable t) {
                conciseNotify(APITest.this);
            }
        });
        conciseWait(this);
        if (httpCodes.size() == 1) {
            assertEquals(200, httpCodes.get(0));
        } else {
            fail();
        }
    }

    @Test
    @Order(14)
    public void acceptPetition() {
        AcceptPetitionRequest acceptPetitionRequest = new AcceptPetitionRequest(martinID);
        ArrayList<Integer> httpCodes = new ArrayList<>();
        Endpoints endpoints = APIClient.retrofit.create(Endpoints.class);
        Call<PetitionResponse> call = endpoints.acceptPetition("Token " + sofiaToken, acceptPetitionRequest);
        call.enqueue(new Callback<PetitionResponse>() {
            @Override
            public void onResponse(Call<PetitionResponse> call, Response<PetitionResponse> response) {
                httpCodes.add(response.code());
                conciseNotify(APITest.this);
            }

            @Override
            public void onFailure(Call<PetitionResponse> call, Throwable t) {
                conciseNotify(APITest.this);
            }
        });
        conciseWait(this);
        if (httpCodes.size() == 0) {
            assertEquals(200, httpCodes.get(0));
        }
    }

    private void conciseNotify(Object monitor) {
        synchronized (monitor) {
            APITest.this.notify();
        }
    }

    private void conciseWait(Object monitor) {
        synchronized (monitor) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}