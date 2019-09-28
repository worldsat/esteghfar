package com.jamali.arbaeen.Kernel.Controller;

import android.content.Context;

import com.jamali.arbaeen.Kernel.Controller.Domain.DomainInfo;
import com.jamali.arbaeen.Kernel.Controller.Domain.ViewMode;
import com.jamali.arbaeen.Kernel.Controller.Interface.CallBackSpinner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class GenericListBll {
    private final Controller controller;

    public GenericListBll(Context context) {
        this.controller = new Controller(context);
    }


    public void populateSpinner(String id, String viewType, String apiAddress, CallBackSpinner callBackSpinner) {
        controller.PopulateFilterSpinner(id, viewType, apiAddress, callBackSpinner);
    }

    public ArrayList<DomainInfo> getDomainInfo(Class domain) {
        return controller.GetDomainInfo(domain);
    }

    public HashMap<String, String> getIds(Object instance, ArrayList<DomainInfo> metaData) {
        HashMap<String, String> result = new HashMap<>();

        try {
            Class<?> aClass = instance.getClass();
            Method[] declaredMethods = aClass.getDeclaredMethods();

            for (int i = 0; i < declaredMethods.length; i++) {
                if (declaredMethods[i].getName().contains("get")) {

                    Object value = declaredMethods[i].invoke(instance);
                    String methodName = declaredMethods[i].getName();
                    String field = methodName.substring(3);
                    for (int j = 0; j < metaData.size(); j++) {
                        if (field.equalsIgnoreCase(metaData.get(j).getId())) {
                            result.put(field, value.toString());
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public HashMap<String, String> parseObject(Object instance) {
        HashMap<String, String> result = new HashMap<>();

        try {
            Class<?> aClass = instance.getClass();
            Method[] declaredMethods = aClass.getDeclaredMethods();

            for (int i = 0; i < declaredMethods.length; i++) {
                if (declaredMethods[i].getName().contains("get")) {

                    Object value = declaredMethods[i].invoke(instance);
                    String methodName = declaredMethods[i].getName();
                    String field = methodName.substring(3);
                    if (value == null) {
                        result.put(field, "null");
                    } else result.put(field, value.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public ArrayList<DomainInfo> getEditDomains(ArrayList<DomainInfo> metaData) {
        ArrayList<DomainInfo> filteredByMode = new ArrayList<>();
        for (DomainInfo item : metaData) {
            String viewMode = item.getViewMode();
            if (viewMode != null) {
                if (viewMode.equals(ViewMode.EDIT.name()) ||
                        viewMode.equals(ViewMode.ALL.name()) ||
                        viewMode.equals(ViewMode.CHART_EDIT.name()) ||
                        viewMode.equals(ViewMode.FILTER_EDIT.name())
                ) {
                    filteredByMode.add(item);
                }
            }
        }
        return filteredByMode;
    }

    public ArrayList<DomainInfo> getFilterDomains(ArrayList<DomainInfo> metaData) {
        ArrayList<DomainInfo> filteredByMode = new ArrayList<>();
        for (DomainInfo item : metaData) {
            String viewMode = item.getViewMode();
            if (viewMode != null) {
                if (viewMode.equals(ViewMode.FILTER.name()) ||
                        viewMode.equals(ViewMode.ALL.name()) ||
                        viewMode.equals(ViewMode.FILTER_EDIT.name()) ||
                        viewMode.equals(ViewMode.CHART_FILTER.name())
                ) {
                    filteredByMode.add(item);
                }
            }
        }
        return filteredByMode;
    }

}
