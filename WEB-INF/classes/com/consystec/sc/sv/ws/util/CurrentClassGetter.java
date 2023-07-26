package com.consystec.sc.sv.ws.util;

public class CurrentClassGetter extends SecurityManager {
    public String getClassName() {
        return getClassContext()[1].getSimpleName();
    }
}