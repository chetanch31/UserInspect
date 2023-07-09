package visual.camp.sample.app.activity;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class BitmapArrayListSingleton {
    private static BitmapArrayListSingleton instance;
    private ArrayList<Bitmap> bitmapArrayList;

    private BitmapArrayListSingleton() {
        bitmapArrayList = new ArrayList<>();
    }

    public static BitmapArrayListSingleton getInstance() {
        if (instance == null) {
            instance = new BitmapArrayListSingleton();
        }
        return instance;
    }

    public ArrayList<Bitmap> getBitmapArrayList() {
        return bitmapArrayList;
    }

    public void setBitmapArrayList(ArrayList<Bitmap> bitmapArrayList) {
        this.bitmapArrayList = bitmapArrayList;
    }
}
