package com.rahul.simpplr.utility;

public class Listeners {

   public interface ItemClickListener{
         void onItemClick(Object object, Object object2);
    }

    public interface MainListener {
         void onLogout(Object object);
         void loadFragment(Object object);
    }
}
