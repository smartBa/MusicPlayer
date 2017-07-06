package c.musicplayer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import c.musicplayer.R;
import c.musicplayer.Service.MusicService;

/**
 * Created by hasee on 2017/3/16.
 */

public class LikeListAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    Context context;
    List list=new ArrayList();
    ArrayList<HashMap<String, Object>> musicList = new ArrayList<HashMap<String, Object>>();
    public LikeListAdapter(Context context){
        this.list= MusicService.getLikeList();
        this.context=context;
        this.mInflater=LayoutInflater.from(context);
        this.musicList=MusicService.getMusicList();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LikeListAdapter.viewHolder holder=new LikeListAdapter.viewHolder();
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.music_list,null);
            holder.num=(TextView)convertView.findViewById(R.id.num_count);
            holder.name=(TextView)convertView.findViewById(R.id.name);
            holder.info=(TextView)convertView.findViewById(R.id.info);
            holder.album=(TextView)convertView.findViewById(R.id.album);
            holder.more=(ImageView) convertView.findViewById(R.id.list_more);
            convertView.setTag(holder);
        }
        else{
            holder=(LikeListAdapter.viewHolder) convertView.getTag();
        }
        holder.num.setText(String.valueOf(position+1));
        holder.name.setText( musicList.get((int)list.get(position)).get("music_file_name").toString());
        holder.info.setText(musicList.get((int)list.get(position)).get("music_author").toString()+"--");
        holder.album.setText(musicList.get((int)list.get(position)).get("music_album").toString());
        holder.more.setVisibility(View.GONE);
        return convertView;
    }

    class viewHolder{
        TextView name;
        TextView info;
        TextView num;
        TextView album;
        ImageView more;
    }
}
