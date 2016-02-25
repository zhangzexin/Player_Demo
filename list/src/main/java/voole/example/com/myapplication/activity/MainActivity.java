package voole.example.com.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import voole.example.com.myapplication.network.HttpHelp;
import voole.example.com.myapplication.R;
import voole.example.com.myapplication.data.liveData;
import voole.example.com.myapplication.adapter.newAdapter;


public class MainActivity extends AppCompatActivity implements AbsListView.OnScrollListener {
    String url = "http://pl3.live.panda.tv/live_panda/";
    int pageno = 1;
    ArrayList<liveData> arrayList = new ArrayList();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(MainActivity.this,(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    ArrayList<liveData> arrayList = (ArrayList<liveData>) msg.obj;
                    adapter = new newAdapter(MainActivity.this, arrayList);
                    view.setAdapter(adapter);
                    view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            liveData item = adapter.getItem(position);
                            Intent intent = new Intent(MainActivity.this,PlayerActivity.class);
                            String flv = item.getFlv();
                            intent.putExtra("url",url+flv+".flv?sign=sign&time=ts");
                            MainActivity.this.startActivity(intent);
                        }
                    });
                    break;
                case 2:
                    adapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    List<Map<String, Object>> data;
    GridView view;
    String Http = "Http";
    private newAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
//        data = getData();
        view = (GridView) findViewById(R.id.gridview);
        view.setOnScrollListener(this);
        loadData();

//        view.setItemsCanFocus(true);
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String time = System.currentTimeMillis()+"";
                Log.d(Http,"http====>Time======>   "+time);
//                String url = "http://capi.douyucdn.cn/api/v1/live?aid=ios&client_sys=ios&limit=20&offset=0&time="+time+"&auth=20125743bcc2114d1abcbca5afac55c7";
                String url = "http://api.m.panda.tv/ajax_get_live_list_by_cate";
                Map<String, String> map = new LinkedHashMap<String, String>();
                map.put("plat","android");
                map.put("version","1.0.5.109");
                map.put("cate","lol");
                map.put("order","person_num");
                map.put("pageno",String.valueOf(pageno));
                map.put("pagenum","10");
                map.put("status","2");
                HttpHelp help = new HttpHelp();
                Log.d(Http,"Url ===>"+url);

                try {
                    String json = help.Post(url,map,"UTF-8");
                    Log.d(Http,""+json);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray items = data.getJSONArray("items");
                        int length = items.length();
                        Log.d("JsonObject","length====>JSONObject======>   "+length);
                        Log.d("JsonObject","items====>JSONObject======>   "+items.toString());

                        for (int i = 0;i<items.length();i++){
                            liveData mData =  new liveData();
                            JSONObject jsonObject1 = items.optJSONObject(i);
                            mData.setName(jsonObject1.getString("name"));
                            mData.setFlv(jsonObject1.getString("room_key"));
                            mData.setId(jsonObject1.getString("id"));
                            JSONObject pictures = jsonObject1.getJSONObject("pictures");
                            mData.setImgerURl(pictures.getString("img"));
                            Log.d("JsonObject", "room_key====>JSONObject======>   " + mData.getFlv());
                            Log.d("JsonObject", "imgUrl====>JSONObject======>   " + mData.getImgerURl());
                            Log.d("JsonObject", "name====>JSONObject======>   " + mData.getName());
                            Log.d("JsonObject", "Id====>JSONObject======>   " + mData.getId());
                            arrayList.add(mData);
                        }
                        Message message = new Message();
                        if(pageno == 1) {
                            message.what = 1;
                            message.obj = arrayList;
                        }else{
                            message.what = 2;
                        }
                        pageno++;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = "解析出错";
                        handler.sendMessage(message);
//                        Toast.makeText(MainActivity.this,"解析出错",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    Message message = new Message();
                    message.what = 0;
                    message.obj = "服务器繁忙，请稍后再试";
                    handler.sendMessage(message);

                    e.printStackTrace();
                }

            }
        }).start();
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
            if(view.getLastVisiblePosition() == view.getCount() -1){
                loadData();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
