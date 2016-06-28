package com.codepath.apps.restclienttemplate.adapters;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.FlickrPhoto;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;

public class PhotoArrayAdapter extends ArrayAdapter<FlickrPhoto> {

	public static final String SEEN = "SEEN";
	public static final String UNSEEN = "UNSEEN";
	HashMap<Integer, String> m_cChanges;

	public PhotoArrayAdapter(Context context, List<FlickrPhoto> photoList) {
		super(context, R.layout.photo_item, photoList);
		m_cChanges = new HashMap<>();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		FlickrPhoto photo = this.getItem(position);
		View view = convertView;
		final ViewHolder holder;
		ImageLoader imageLoader = ImageLoader.getInstance();
		if (view == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.photo_item,
					parent, false);
			holder = new ViewHolder();
			holder.imageview = (ImageView) view.findViewById(R.id.ivPhoto);
			holder.layout = (LinearLayout) view.findViewById(R.id.lvMeta);
			holder.name = (TextView) view.findViewById(R.id.tvName);
			holder.size = (TextView) view.findViewById(R.id.tvSize);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.imageview.setImageResource(android.R.color.transparent);
		imageLoader.displayImage(photo.getUrl(), holder.imageview);
		holder.name.setText(photo.getName());
		holder.layout.setVisibility(View.GONE);
		holder.imageview.setVisibility(View.VISIBLE);

		if (m_cChanges.get(position) != null) {
			if(m_cChanges.get(position).equals(SEEN)) {
				holder.layout.setVisibility(View.GONE);
				holder.imageview.setVisibility(View.VISIBLE);
				holder.imageview.setTag(R.id.IMG_OPEN_TAG, SEEN);
				holder.imageview.setScaleX(1);
			}
			else  if (m_cChanges.get(position).equals(UNSEEN)){
				holder.imageview.setScaleX(-1);
				holder.imageview.setTag(R.id.IMG_OPEN_TAG, UNSEEN);
				holder.imageview.setVisibility(View.GONE);
				holder.layout.setVisibility(View.VISIBLE);
			}
		}

		holder.imageview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playAnimation(holder.imageview, holder.layout);
				m_cChanges.put(position, UNSEEN);
			}
		});
		holder.layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				removeAnimation(holder.layout, holder.imageview);
				m_cChanges.put(position, SEEN);
			}
		});

		return view;
	}

	private void playAnimation(final ImageView imageview, final LinearLayout layout) {
		ObjectAnimator animation = ObjectAnimator.ofFloat(imageview, "rotationY", 0.0f, 180f);
		animation.setDuration(600);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.start();
		animation.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				imageview.setVisibility(View.GONE);
				layout.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});

	}

	private void removeAnimation(final LinearLayout layout, final ImageView imageview) {
		ObjectAnimator animation = ObjectAnimator.ofFloat(layout, "rotationY", 180f, 0.0f);
		animation.setDuration(600);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.start();
		animation.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				layout.setVisibility(View.GONE);
				imageview.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});

	}

	public class ViewHolder {
		ImageView imageview;
		LinearLayout layout;
		TextView name;
		TextView size;
	}
}
