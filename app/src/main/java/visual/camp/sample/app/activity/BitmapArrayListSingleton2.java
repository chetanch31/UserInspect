package visual.camp.sample.app.activity;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class BitmapArrayListSingleton2 {
    private static BitmapArrayListSingleton2 instance;
    private ArrayList<Bitmap> bitmapArrayList;

    private BitmapArrayListSingleton2() {
        bitmapArrayList = new ArrayList<>();
    }

    public static BitmapArrayListSingleton2 getInstance() {
        if (instance == null) {
            instance = new BitmapArrayListSingleton2();
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
