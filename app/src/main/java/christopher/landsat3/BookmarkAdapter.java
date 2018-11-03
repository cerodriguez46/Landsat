package christopher.landsat3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import christopher.landsat3.Networking.LandsatModel;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private Context mContext;

    private List<LandsatModel> satelliteList;


    String poster;

    LandsatModel model;


    public BookmarkAdapter(Context mContext, List<LandsatModel> satelliteList) {

        this.mContext = mContext;
        this.satelliteList = satelliteList;
    }


    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ViewHolder viewHolder = new ViewHolder(view);



        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final BookmarkAdapter.ViewHolder holder, int position) {

        LandsatModel recordEntries = satelliteList.get(position);

        String poster = recordEntries.url;

        RequestOptions options = new RequestOptions()
                .override(Target.SIZE_ORIGINAL)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .priority(Priority.HIGH);

        Glide.with(mContext)
                .load(poster)
                .transition(withCrossFade())
                .apply(options)
                .into(holder.posterthumbnail);
    }


    @Override
    public int getItemCount() {
        if (satelliteList == null) {
            return 0;
        }
        return satelliteList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView posterthumbnail;

        public ViewHolder(View itemView) {
            super(itemView);

            posterthumbnail = (ImageView) itemView.findViewById(R.id.rv_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int clickedPosition = getAdapterPosition();
                    LandsatModel clickedItem = satelliteList.get(clickedPosition);

                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("passedLat", clickedItem.getLatitude());
                    intent.putExtra("passedLong", clickedItem.getLongitude());
                    intent.putExtra("passedDate", clickedItem.getDate());
                    intent.putExtra("passedImage", clickedItem.getUrl());
                    intent.putExtra("passedTitle", clickedItem.getTitle());
                    intent.putExtra("landsatParcel", clickedItem);



                    mContext.startActivity(intent);
                }
            });
        }
    }

    public List<LandsatModel> getRecords() {
        return satelliteList;
    }

    public void setRecords(List<LandsatModel> recordEntries) {
        satelliteList = recordEntries;
        notifyDataSetChanged();
    }


}
