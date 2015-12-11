package itmir.numberbaseball;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by whdghks913 on 2015-12-09.
 */
class ViewHolder {
    public TextView mInputList;
    public TextView status;
}

class Data {
    public String input, status;
}

public class Adapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<Data> mListData = new ArrayList<>();

    public Adapter(Context mContext) {
        super();

        this.mContext = mContext;
    }

    public void addItem(String mText, String status) {
        Data mData = new Data();
        mData.input = mText;
        mData.status = status;
        mListData.add(0, mData);
    }

    public void clearData() {
        mListData.clear();
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Data getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder mHolder;

        if (convertView == null) {
            mHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_2, null);

            mHolder.mInputList = (TextView) convertView.findViewById(android.R.id.text1);
            mHolder.status = (TextView) convertView.findViewById(android.R.id.text2);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        Data mData = mListData.get(position);
        mHolder.mInputList.setText(mData.input);
        mHolder.status.setText(mData.status);

        return convertView;
    }
}
