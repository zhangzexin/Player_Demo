package voole.example.com.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import voole.example.com.myapplication.R;
import voole.example.com.myapplication.activity.PlayerActivity;
import voole.example.com.myapplication.data.liveData;

/**
 * Created by zhangzexin on 16/1/29.
 */
public class newAdapter extends BaseAdapter {
    String url = "http://pl3.live.panda.tv/live_panda/";
    Context context;
    ArrayList<liveData> data;
    public newAdapter(Context context,ArrayList<liveData> data) {
        super();
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public liveData getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final liveData item = getItem(position);
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            holder.layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.content_main,null);
            holder.imagerview = (SimpleDraweeView) holder.layout.findViewById(R.id.my_image_view);
            holder.textView = (TextView) holder.layout.findViewById(R.id.text);
            convertView = holder.layout;
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(item.getName());
        Uri uri = Uri.parse(item.getImgerURl());
        holder.imagerview.setImageURI(uri);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context,PlayerActivity.class);
                String flv = item.getFlv();
//                intent.putExtra("url",url+flv+".flv?sign=sign&time=ts");
                PlayerActivity.intentTo(context,url+flv+".flv?sign=sign&time=ts");
//                context.startActivity(intent);
                Log.d("Adapter","点几了第"+position);
            }
        });
        return  holder.layout;
    }
    static class ViewHolder{
        public SimpleDraweeView imagerview;
        public RelativeLayout layout;
        public TextView textView;
    }



}
