package com.schwergsy.kentstate;

public class SortType {

    public String type;

    public SortType(String type) {

        if (type.equals("proximity"))
            this.type = type;
        else if (type.equals("rating"))
            this.type = type;
        else if (type.equals("payment"))
            this.type = type;
        else if (type.equals("time"))
            this.type = type;
        else
            this.type = "INVALID";
    }

    public boolean equals(String t) {

        if (this.type.equals(t))
            return true;
        return false;
    }
}