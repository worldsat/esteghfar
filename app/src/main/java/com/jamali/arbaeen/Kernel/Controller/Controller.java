package com.jamali.arbaeen.Kernel.Controller;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.jamali.arbaeen.Kernel.Controller.Bll.SettingsBll;
import com.jamali.arbaeen.Kernel.Controller.Domain.ApiResponse;
import com.jamali.arbaeen.Kernel.Controller.Domain.DomainInfo;
import com.jamali.arbaeen.Kernel.Controller.Domain.Filter;
import com.jamali.arbaeen.Kernel.Controller.Domain.SpinnerDomain;
import com.jamali.arbaeen.Kernel.Controller.Interface.CallBackSpinner;
import com.jamali.arbaeen.Kernel.Controller.Interface.CallbackGet;
import com.jamali.arbaeen.Kernel.Controller.Interface.CallbackOperation;
import com.jamali.arbaeen.Kernel.Controller.Module.SnakBar.SnakBar;
import com.jamali.arbaeen.Kernel.Controller.Module.Volley.VolleyCall;
import com.jamali.arbaeen.Kernel.Helper.DataBaseHelper;
import com.jamali.arbaeen.Kernel.Helper.DateConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Controller {
    private static String PORT;
    private static String URL;
    private final Context context;
    private final SettingsBll settingsBll;
    private boolean isOnline;
    private Gson gson;
    private ProgressDialog dialog;
    private int count = -1;

    public Controller(Context context) {
        this.context = context;

        //get all settings from SettingsBll which works with sharedPreferences
        settingsBll = new SettingsBll(context);
        URL = settingsBll.getUrlAddress();
        PORT = settingsBll.getPort();
        gson = new Gson();
        isOnline = settingsBll.isOnline();
    }

    public <T> void Get(Class domain, ArrayList<Filter> filter, int List, int position, boolean allData, CallbackGet callbackGet) {


        try {


            Constructor constructor = domain.getConstructor();
            Object instance = constructor.newInstance();
            Class superClass = domain.getSuperclass();

            Method getTableName;
            String tableName;
            if (List == 0) {
                getTableName = superClass.getDeclaredMethod("getTableName");
                tableName = (String) getTableName.invoke(instance);
            } else {
                tableName = String.valueOf(List) + "_" + String.valueOf(position);
            }

            Method getApiAddress = superClass.getDeclaredMethod("getApiAddress");
            String apiName = (String) getApiAddress.invoke(instance);

            if (isOnline) {
                GetFromApi(domain, apiName, filter, List, position, allData, callbackGet);
            } else {
                GetFromDatabase(tableName, filter, domain, callbackGet);

            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // invokes the method at runtime


    }


    private <T> void GetFromApi(Class domain, String ApiAddress, ArrayList<Filter> filters,
                                int take, int skip, boolean allData, CallbackGet callbackGet) throws JSONException {


//        if (filters != null && filters.size() > 0) {
//            for (Filter filter : filters) {
//
//                if (filter.getField().endsWith("Date") || filter.getField().endsWith("DateTime")) {
//                    DateConverter converter = new DateConverter();
//                    if (filter.getApiAddress().contains("-")) {
//                        String[] dates = filter.getApiAddress().split("-");
//                        if (dates.length == 2) {
//                            String[] startDate = dates[0].split("/");
//
//                            int year = Integer.parseInt(startDate[0]);
//                            int month = Integer.parseInt(startDate[1]);
//                            int day = Integer.parseInt(startDate[2]);
//                            converter.persianToGregorian(year, month, day);
//                            String startDateEn =
//                                    String.format("%02d", converter.getYear()) +
//                                            "/" + String.format("%02d", converter.getMonth()) +
//                                            "/" + String.format("%02d", converter.getDay());
//
//
//                            String[] endDate = dates[1].split("/");
//
//                            converter.persianToGregorian(
//                                    Integer.parseInt(endDate[0]),
//                                    Integer.parseInt(endDate[1]),
//                                    Integer.parseInt(endDate[2])
//                            );
//                            String endDateEn =
//                                    String.format("%02d", converter.getYear()) +
//                                            "/" + String.format("%02d", converter.getMonth()) +
//                                            "/" + String.format("%02d", converter.getDay());
//
//                            filter.setApiAddress(startDateEn + "-" + endDateEn);
//
//                        }
//                    } else {
//                        String[] startDate = filter.getApiAddress().split("/");
//
//                        converter.persianToGregorian(
//                                Integer.parseInt(startDate[0]),
//                                Integer.parseInt(startDate[1]),
//                                Integer.parseInt(startDate[2])
//                        );
//
//
//                        String startDateEn =
//                                String.format("%02d", converter.getYear()) +
//                                        "/" + String.format("%02d", converter.getMonth()) +
//                                        "/" + String.format("%02d", converter.getDay());
//
//
//                        filter.setApiAddress(startDateEn);
//                    }
//
//                }
//            }
//        }

        Gson gson = new Gson();
        String filterStr = "[]";

        filterStr = gson.toJson(filters);

        VolleyCall volleyCall = new VolleyCall(context);
//        String Address = URL + ":" + PORT + "/" + ApiAddress;
        String Address = URL + "/" + ApiAddress;

        if (allData) {
            if (filters != null) {
                Address = Address + "?filter=" + filterStr;
            }
        } else {
            Address = Address + "?skip=" + String.valueOf(skip) + "&take=" + String.valueOf(take) + "&filter=" + filterStr;
        }

        Log.i("ApiCall", Address);

        volleyCall.Get(Address, new VolleyCall.VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                Log.i("response", response);
                ArrayList<T> result = new ArrayList<>();

                JSONObject jsonRootObject = null;
                try {
                    jsonRootObject = new JSONObject(response);
                    count = jsonRootObject.getInt("Count");

                    if (jsonRootObject.getString("IsError").equals("true")) {
//                        Intent intent = new Intent(context, LoginActivity.class);
//                        context.startActivity(intent);
                    }

                    JSONArray array = jsonRootObject.optJSONArray("Data");
                    Method[] declaredMethods = domain.getDeclaredMethods();

                    if (array.length() > 0) {

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject obj = array.getJSONObject(i);
                            int columnCount = obj.length();
                            Constructor constructor = domain.getConstructor();
                            Iterator<String> keys = obj.keys();
                            ArrayList<String> keysList = Lists.newArrayList(keys);

                            Object item = constructor.newInstance();
                            for (int j = 0; j < columnCount; j++) {
                                String columnName = keysList.get(j);
                                // Log.i("columnName", columnName);
                                String setColumnName = "set" + columnName;
                                for (int m = 0; m < declaredMethods.length; m++) {
                                    if (setColumnName.equals(declaredMethods[m].getName())) {
                                        if (setColumnName.equals("setLat1")) {
                                            Log.i("Log", "l");
                                        }
                                        declaredMethods[m].invoke(item, obj.getString(keysList.get(j)));
                                        //Log.i("added Item", item.toString());
                                        break;
                                    }
                                }
                            }

                            result.add((T) item);
                        }
                    }


                } catch (JSONException e) {

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                callbackGet.onSuccess(result, count);
            }

            @Override
            public void onError(String error) {
                callbackGet.onError(error);
            }
        });
    }


    public void Operation(String Operation, Class domain, Context context, String params, CallbackOperation callbackOperation) {
        try {
            Constructor constructor = domain.getConstructor();
            Object instance = constructor.newInstance();
            Class superClass = domain.getSuperclass();

            Method getTableName = superClass.getDeclaredMethod("getTableName");
            Method getApiAddress = superClass.getDeclaredMethod("getApiAddress");

            String tableName = (String) getTableName.invoke(instance);
            String apiName = (String) getApiAddress.invoke(instance);

            if (isOnline) {
                OperationApi(context, Operation, apiName, params, callbackOperation);
            } else {
                //    OperationDatabase(context, params, callbackOperation);
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }


    private void OperationApi(Context context, String Operation, String ApiAddress, String params, CallbackOperation callback) {

        String Address = "";
        if (Operation.equals("Manage")) {
//            Address = URL + ":" + PORT + "/" + ApiAddress + "/Manage";
            Address = URL + "/" + ApiAddress + "/Manage";
        } else if (Operation.equals("Delete")) {
//            Address = URL + ":" + PORT + "/" + ApiAddress + "/Delete";
            Address = URL + "/" + ApiAddress + "/Delete";
        } else {
//            Address = URL + ":" + PORT + "/" + ApiAddress;
            Address = URL + "/" + ApiAddress;
        }

        VolleyCall volleyCall = new VolleyCall(context);
        volleyCall.Post(params, Address, new VolleyCall.VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {

                Gson gson = new Gson();
                ApiResponse apiResponse = gson.fromJson(response, ApiResponse.class);

                if (!apiResponse.IsError) {

                    try {

                        callback.onSuccess(apiResponse.Message);
                        Log.i("moh3n", "Message: " + apiResponse.Message);
                    } catch (Exception e) {

                        Log.i("moh3n", e.toString());
                    }

                } else callback.onError(apiResponse.Message);

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private <T> void GetFromDatabase(String tableName, ArrayList<Filter> filters, Class domain, CallbackGet callbackGet) {
        ArrayList<T> result = new ArrayList<>();
        try {
            StringBuilder filterStr = new StringBuilder();
            filterStr.append(" where 1=1 ");
            if (filters != null && filters.size() > 0) {
                for (Filter filter : filters) {
                    if (filter.getField().endsWith("Id")) {
                        filterStr.append(String.format(" and %s like '%s' ", filter.getField(), filter.getValue()));
                    } else if (filter.getField().endsWith("Date") || filter.getField().endsWith("DateTime")) {
                        DateConverter converter = new DateConverter();
                        if (filter.getValue().contains("-")) {
                            String[] dates = filter.getValue().split("-");
                            if (dates.length == 2) {
                                String[] startDate = dates[0].split("/");

                                int year = Integer.parseInt(startDate[0]);
                                int month = Integer.parseInt(startDate[1]);
                                int day = Integer.parseInt(startDate[2]);
                                converter.persianToGregorian(year, month, day);
                                String startDateEn =
                                        String.format("%02d", converter.getYear()) +
                                                "-" + String.format("%02d", converter.getMonth()) +
                                                "-" + String.format("%02d", converter.getDay());


                                String[] endDate = dates[1].split("/");

                                converter.persianToGregorian(
                                        Integer.parseInt(endDate[0]),
                                        Integer.parseInt(endDate[1]),
                                        Integer.parseInt(endDate[2])
                                );
                                String endDateEn =
                                        String.format("%02d", converter.getYear()) +
                                                "-" + String.format("%02d", converter.getMonth()) +
                                                "-" + String.format("%02d", converter.getDay());

                                filterStr.append(
                                        String.format(" and (%s >= '%s' and %s <= '%s')",
                                                filter.getField(),
                                                startDateEn,
                                                filter.getField(),
                                                endDateEn)
                                );
                            }
                        } else {
                            String[] startDate = filter.getValue().split("/");

                            converter.persianToGregorian(
                                    Integer.parseInt(startDate[0]),
                                    Integer.parseInt(startDate[1]),
                                    Integer.parseInt(startDate[2])
                            );


                            String startDateEn =
                                    String.format("%02d", converter.getYear()) +
                                            "-" + String.format("%02d", converter.getMonth()) +
                                            "-" + String.format("%02d", converter.getDay());


                            filterStr.append(
                                    String.format(" and (%s >= '%s' and %s <= '%s')",
                                            filter.getField(),
                                            startDateEn,
                                            filter.getField(),
                                            startDate)
                            );

                        }

                    } else {
                        filterStr.append(String.format(" and %s like '%%%s%%'", filter.getField(), filter.getValue()));
                    }
                }
            }
            String filterString = filterStr.toString();


            DataBaseHelper dbHelper = new DataBaseHelper(context);
            SQLiteDatabase database = dbHelper.openDataBase();

            String query = "Select * from '" + tableName +"'"+ filterString;
            Log.i("moh3n", "GetFromDatabase: " + query);
            Cursor cursor = database.rawQuery(query, null);
            int columnCount = cursor.getColumnCount();

            Constructor constructor = domain.getConstructor();

            if (cursor.moveToFirst()) {
                do {
                    Object item = constructor.newInstance();
                    for (int i = 0; i < columnCount; i++) {
                        String columnName = cursor.getColumnName(i);
                        Method setMethod = domain.getDeclaredMethod("set" + columnName, String.class);
                        if (cursor.getString(i) == null) {
                            setMethod.invoke(item, "null");
                        } else {
                            setMethod.invoke(item, cursor.getString(i));
                        }
                    }

                    result.add((T) item);
                } while (cursor.moveToNext());
            }
            cursor.close();
            database.close();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        callbackGet.onSuccess(result, result.size());
    }

    public void OperationPostBodyApi(Context context, String Operation, String ApiAddress, Map<String, String> params, CallbackOperation callback) {

        String Address = "";
        if (Operation.equals("Manage")) {
//            Address = URL + ":" + PORT + "/" + ApiAddress + "/Manage";
            Address = URL + "/" + ApiAddress + "/Manage";
        } else if (Operation.equals("Delete")) {
//            Address = URL + ":" + PORT + "/" + ApiAddress + "/Delete";
            Address = URL + "/" + ApiAddress + "/Delete";
        } else {
//            Address = URL + ":" + PORT + "/" + ApiAddress;
            Address = URL + "/" + ApiAddress + Operation;
        }

        VolleyCall volleyCall = new VolleyCall(context);
        volleyCall.Volley_POSTBody(params, Address, new VolleyCall.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                Log.i("moh3n", "onSuccessResponse: " + result);
                try {
                    Gson gson = new Gson();
                    ApiResponse apiResponse = gson.fromJson(result, ApiResponse.class);

                    if (!apiResponse.IsError) {

                        try {
                            callback.onSuccess(apiResponse.Message);
                            Log.i("moh3n", "Message: " + apiResponse.Message);
                        } catch (Exception e) {

                            Log.i("moh3n", e.toString());
                        }

                    } else callback.onError(apiResponse.Message);
                } catch (Exception e) {

                    Log.i("moh3n", "errorPostBody: " + e.toString());
                }

            }

            @Override
            public void onError(String error) {

                SnakBar snakBar = new SnakBar();
                snakBar.snakShow(context, "خطا در دریافت اطلاعات");

                Log.i("moh3n", "errorPostBody: " + error);
            }
        });


    }

    public void PopulateFilterSpinner(String field, String entryName, String apiAddress, CallBackSpinner callback) {
        VolleyCall volleyCall = new VolleyCall(context);
//        String Address = URL + ":" + PORT + "/" + apiAddress;
        String Address = URL + "/" + apiAddress;
        volleyCall.Get(Address, new VolleyCall.VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                ArrayList<SpinnerDomain> result = new ArrayList<>();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.optJSONArray("Data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String id = jsonArray.getJSONObject(i).getString(field);
                        String title = jsonArray.getJSONObject(i).getString(entryName);
                        result.add(new SpinnerDomain(field, title, id));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onSuccess(result);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    public ArrayList<DomainInfo> GetDomainInfo(Class domain) {
        ArrayList<DomainInfo> result = new ArrayList<>();
        Constructor constructor;
        try {
            constructor = domain.getConstructor();
            Object instance = constructor.newInstance();
            Class superclass = domain.getSuperclass();
            Method getDomainInfo = superclass.getDeclaredMethod("getDomainInfo");
            result = (ArrayList<DomainInfo>) getDomainInfo.invoke(instance);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void LoginApi(Context context, Class IntentClass, String ApiAddress, String params, CallbackOperation callbackOperation) {
//        String Address = URL + ":" + PORT + "/" + ApiAddress;
        String Address = URL + "/" + ApiAddress;
        VolleyCall volleyCall = new VolleyCall(context);

        volleyCall.Post(params, Address, new VolleyCall.VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {

                Gson gson = new Gson();
                ApiResponse apiResponse = gson.fromJson(response, ApiResponse.class);

                if (!apiResponse.IsError && apiResponse.getMessage() == null) {


                    try {

                        String token = new JSONObject(response).getJSONObject("Data").getString("Ticket");
                        String UserPostId = new JSONObject(response).getJSONObject("Data").getString("UserPostIds");
                        Log.i("moh3n", "onSuccessResponse: " + token);
                        settingsBll.setTicket(token);
                        settingsBll.setUserPostId(UserPostId);

                        context.startActivity(new Intent(context, IntentClass));

                    } catch (JSONException e) {

                        Log.i("moh3n", e.toString());
                    }


                } else callbackOperation.onError(apiResponse.Message);


            }

            @Override
            public void onError(String error) {
                callbackOperation.onError(error);
            }
        });
    }
}
