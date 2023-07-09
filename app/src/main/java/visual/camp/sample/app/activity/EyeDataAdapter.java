package visual.camp.sample.app.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Map;

public class EyeDataAdapter extends BaseAdapter {

    private Context context;
    private Map<Long, Map<String, Float>> eyeData;
    private Long[] keys;

    public EyeDataAdapter(Context context, Map<Long, Map<String, Float>> eyeData) {
        this.context = context;
        this.eyeData = eyeData;
        this.keys = eyeData.keySet().toArray(new Long[0]);
    }

    @Override
    public int getCount() {
        return keys.length;
    }

    @Override
    public Object getItem(int position) {
        return eyeData.get(keys[position]);
    }

    @Override
    public long getItemId(int position) {
        return keys[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(keys[position] + " ms : " + eyeData.get(keys[position]));

        return convertView;
    }
}
