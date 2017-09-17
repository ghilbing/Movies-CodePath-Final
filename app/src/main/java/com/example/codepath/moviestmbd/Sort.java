package com.example.codepath.moviestmbd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gretel on 9/16/17.
 */

public class Sort {

    private int id;
    private String text;

    public static final int POP = 1;
    public static final int RAT = 2;

    static List<Sort> options = new ArrayList<>();

    public Sort(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public static List<Sort> getOptions(){
        if(options.isEmpty()){
            options.add(new Sort(POP, "Popular"));
            options.add(new Sort(RAT, "Rated"));
        }
        return options;
    }

    public static int getSortMethod(int position) {
        return options.get(position).id;
    }

    public int getId() {
        return id;
    }


    public String getText() {
        return text;
    }


}
