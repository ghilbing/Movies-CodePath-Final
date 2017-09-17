package com.example.codepath.moviestmbd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gretel on 9/16/17.
 */

public class Sort {

    private int id;
    private int itemDrawable;
    private int dropdownDrawable;
    private String text;

    public static final int POP = 0;
    public static final int RAT = 1;

    static List<Sort> options = new ArrayList<>();

    public Sort(int id, String text, int itemDrawable, int dropdownDrawable) {
        this.id = id;
        this.text = text;
        this.itemDrawable = itemDrawable;
        this.dropdownDrawable = dropdownDrawable;
    }

    public static List<Sort> getOptions(){
        if(options.isEmpty()){
            options.add(new Sort(POP, "Popular", R.drawable.ic_public_black_24dp, R.drawable.ic_public_black_24dp));
            options.add(new Sort(RAT, "Rated", R.drawable.ic_stars_black_24dp, R.drawable.ic_stars_black_24dp));
        }
        return options;
    }

    public static int getSortMethod(int position) {
        return options.get(position).id;
    }

    public int getId() {
        return id;
    }

    public int getItemDrawable() {
        return itemDrawable;
    }

    public int getDropdownDrawable() {
        return dropdownDrawable;
    }

    public String getText() {
        return text;
    }


}
