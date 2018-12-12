package com.example.cuong.discover_ahihikara.controller;

import org.json.JSONObject;

public class APIController {
    private JSONObject jsonObject;
    private static final String HOST_NAME = "https://ahihikara.herokuapp.com";
    private static final String API_LOGIN = "/api/auth/login";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_PASSWORD = "password";
//    public void callPostAuthLogin(final String userName, final String password, final Context context) {
//            StringRequest requestLogin = new StringRequest(Request.Method.POST, HOST_NAME+API_LOGIN,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                System.out.print(response);
//                                jsonObject = new JSONObject(response);
//                                jsonObject.put("status", 200);
//                                LoginActivity.jsonResponse = jsonObject;
//                                System.out.print(jsonObject);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            AuthController auth = new AuthController();
//                            auth.saveData(response, context);
//                            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
//                            LoginActivity.pDialog.dismiss();
//                        }
//                    },
//
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            System.out.print(error);
//                            jsonObject = new JSONObject();
//                            try {
//                                jsonObject.put("status", error.networkResponse.statusCode);
//                                LoginActivity.jsonResponse = jsonObject;
//                                System.out.print(jsonObject);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            LoginActivity.jsonResponse = jsonObject;
////                            LoginActivity.pDialog.dismiss();
////                            Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
//                        }
//                    }) {
//                /**
//                 * set paramater
//                 * */
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<>();
//                    params.put(KEY_USERNAME, userName);
//                    params.put(KEY_PASSWORD, password);
//                    return params;
//                }
//
//                /**
//                 * set header
//                 * */
//                @Override
//                public Map<String, String> getHeaders() {
//                    Map<String, String> headers = new HashMap<String, String>();
//                    headers.put("Authorization", "testvvvvvvv");
//                    return headers;
//                }
//            };
//            RequestQueue queue = Volley.newRequestQueue(context);
//            queue.add(requestLogin);
//    }
}
