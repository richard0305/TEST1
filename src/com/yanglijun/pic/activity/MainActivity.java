package com.yanglijun.pic.activity;

import java.util.List;

import com.nineoldandroids.view.ViewHelper;
import com.yanglijun.pic.R;
import com.yanglijun.pic.adapter.PhotoAdapter;
import com.yanglijun.pic.adapter.PhotoNameAdapter;
import com.yanglijun.pic.adapter.recyclerViewAdapter;
import com.yanglijun.pic.adapter.recyclerViewAdapter.RecyclerViewOnItemClickListener;
import com.yanglijun.pic.entity.Photo;
import com.yanglijun.pic.entity.PhotoName;
import com.yanglijun.pic.presenter.IPhotoNamePresenter;
import com.yanglijun.pic.presenter.IPhotoPresenter;
import com.yanglijun.pic.presenter.PhotoNamePresenter;
import com.yanglijun.pic.presenter.PhotoPresenter;
import com.yanglijun.pic.utils.DragLayout;
import com.yanglijun.pic.utils.DragLayout.DragListener;
import com.yanglijun.pic.view.IPhotoNameView;
import com.yanglijun.pic.view.IPhotoView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements IPhotoView, IPhotoNameView {
	DragLayout dl;
	private RecyclerView recyclerview;
	private GridView gvPic;
	private ListView lvContent;
	private ImageView ivbottom, icon;
	private int position;
	private List<Photo> photos;
	private int page = 1;
	private List<PhotoName> photoNames;
	IPhotoPresenter presenter;
	IPhotoNamePresenter photoNamePresenter;
	private PhotoAdapter adapter;
	private PhotoNameAdapter photoNameAdapter;
	private TextView tvPhotoTitle;
	private PhotoName photoName;
	private recyclerViewAdapter recyclerviewAdapter;
	private SpacesItemDecoration decoration = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		presenter = new PhotoPresenter(this);
		photoNamePresenter = new PhotoNamePresenter(this);
		photoNamePresenter.loadPhotoName();
		tvPhotoTitle.setText("性感美女");

		presenter.loadData(position + 1, page);

		// 设置item之间的间隔
		decoration = new SpacesItemDecoration(5);
		recyclerview.addItemDecoration(decoration);
		setListener();

	}

	// 侧滑
	private void setListener() {

		dl.setDragListener(new DragListener() {

			@Override
			public void onOpen() {
			}

			// 主界面缩小
			@Override
			public void onDrag(float percent) {
				ViewHelper.setAlpha(icon, 1 - percent);
			}

			@Override
			public void onClose() {
			}
		});
		icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dl.open();
			}
		});

		lvContent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MainActivity.this.position = position;
				photoName = photoNames.get(position);
				tvPhotoTitle.setText(photoName.getTitle());
				presenter.loadData(position + 1, page);

			}

		});


	}

	private void initView() {
		dl = (DragLayout) findViewById(R.id.dl);
		// gvPic=(GridView) findViewById(R.id.gv_img);
		recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
		lvContent = (ListView) findViewById(R.id.lv);
		icon = (ImageView) findViewById(R.id.iv_icon);
		ivbottom = (ImageView) findViewById(R.id.iv_bottom);
		tvPhotoTitle = (TextView) findViewById(R.id.tvPhotoTitle);
	}

	// 图片列表
	@Override
	public void showPhoto(final List<Photo> photos) {
		this.photos = photos;

		recyclerviewAdapter = new recyclerViewAdapter(photos, this);
		recyclerview.setAdapter(recyclerviewAdapter);
		recyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

		// 设置 监听
		recyclerviewAdapter.setRecyclerViewOnItemClickListener(new RecyclerViewOnItemClickListener() {

			@Override
			public void onItemLongClick(View view, int position) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onItemClick(View view, int position) {
				// TODO Auto-generated method stub
				Log.i("------------------", "----------------------------------vvvvvvv" + position);
				Photo photo = photos.get(position);
				int ID = photo.getId();
				String title = photo.getTitle();
				Intent intent = new Intent(MainActivity.this, PictureListActivity.class);
				intent.putExtra("ID", ID);
				intent.putExtra("title", title);
				startActivity(intent);
			}
		});

	}

	// 图片类别
	@Override
	public void showPhotoName(List<PhotoName> photoNames) {

		this.photoNames = photoNames;
		photoNameAdapter = new PhotoNameAdapter(MainActivity.this, photoNames);
		lvContent.setAdapter(photoNameAdapter);

	}

	public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

		private int space;

		public SpacesItemDecoration(int space) {
			this.space = space;
		}

		// 自定义了一个SpacesItemDecoration
		@Override
		public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
			outRect.left = space;
			outRect.right = space;
			outRect.bottom = space;
			if (parent.getChildAdapterPosition(view) == 0) {
				outRect.top = space;
			}
		}
	}

}
