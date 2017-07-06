package c.musicplayer.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import c.musicplayer.R;
import c.musicplayer.Service.MusicService;

/**
 * Created by hasee on 2017/3/16.
 */

public class LocalListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    Context context;
    ArrayList<HashMap<String, Object>> musicList = new ArrayList<HashMap<String, Object>>();
    public LocalListAdapter(Context context){
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.musicList= MusicService.getMusicList();
    }
    @Override
    public int getCount() {
        return musicList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        viewHolder holder=new viewHolder();
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
            holder=(viewHolder) convertView.getTag();
        }
        holder.num.setText(String.valueOf(position+1));
        holder.name.setText( musicList.get(position).get("music_file_name").toString());
        holder.info.setText(musicList.get(position).get("music_author").toString()+"--");
        holder.album.setText(musicList.get(position).get("music_album").toString());
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isLike=false;
                for(int i=0;i<MusicService.getLikeList().size();i++) {
                    if ((int) MusicService.getLikeList().get(i) == position) {
                        isLike = true;
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("提示");
                        builder.setMessage("确定不再喜欢这首歌吗");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("adapter","removeAdapterDislike");
                                MusicService.setLikeList(position);
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("点错了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        break;
                    }
                }
                if (isLike==false){
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("提示");
                    builder.setMessage("确认喜欢这首音乐吗");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("adapter","removeAdapterLike");
                            MusicService.setLikeList(position);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("点错了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }});
                    builder.create().show();
                }
            }

        });
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
