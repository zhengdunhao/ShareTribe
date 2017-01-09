package com.atm.chatonline.bbs.adapter;

import java.util.List;





import com.atm.chatonline.bbs.util.GridViewItem;
import com.atm.chatonline.bbs.util.PhotoItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.AbsListView.LayoutParams;

public class PhotoAdapter extends BaseAdapter {
	private List<PhotoItem> photoItem;// 全部图片
	private List<PhotoItem> selectedPic;// 选中的图片
	private Context context;

	
	public PhotoAdapter() {
		// TODO Auto-generated constructor stub
	}

	public PhotoAdapter(Context context, List<PhotoItem> photoItem,
			List<PhotoItem> selectedPic) {
		this.context = context;
		this.photoItem = photoItem;
		this.selectedPic = selectedPic;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (photoItem != null)
			return photoItem.size();
		else
			return selectedPic.size();
	}

	@Override
	public PhotoItem getItem(int arg0) {
		// TODO Auto-generated method stub
		if (photoItem != null)
			return photoItem.get(arg0);
		else
			return selectedPic.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		GridViewItem grid = null;
		if (arg1 == null) {
			grid = new GridViewItem(context);
			grid.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			// arg1.setTag(grid);
		} else {
			grid = (GridViewItem) arg1;
		}
		if (selectedPic == null) {
			Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context
					.getContentResolver(), photoItem.get(arg0).getPhotoID(),
					Thumbnails.MICRO_KIND, null);
			grid.mySetBitmap(bitmap);
			boolean flag = photoItem.get(arg0).isSelect();
			grid.setChecked(flag);
		} else {
			Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context
					.getContentResolver(), selectedPic.get(arg0).getPhotoID(),
					Thumbnails.MICRO_KIND, null);
			grid.mySetBitmap(bitmap);
		}
		if(photoItem == null){
			grid.mSelect.setVisibility(View.INVISIBLE);
		}
		return grid;
	}

}
