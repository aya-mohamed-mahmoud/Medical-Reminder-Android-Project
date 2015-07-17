package com.project.classes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.project.beans.Medicine;
import com.project.medicalreminder.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ahmedrashad on 3/10/15.
 */
public class MyArrayAdapter extends ArrayAdapter<Medicine> {

	Context context;
	Medicine[] objects;

	public MyArrayAdapter(Context context, Medicine[] objects) {
		super(context, R.layout.medicine_row, R.id.LinearLayout1, objects);

		this.context = context;
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup listView) {

		ViewCache viewCache;
		//final int posistionCopy = position;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.medicine_row, listView, false);
			viewCache = new ViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (ViewCache) convertView.getTag();
		}

		// View view = inflater.inflate(R.layout.row, listView, false); //get
		// the views on the layout

		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.imageView);
		TextView headerTextView = (TextView) convertView
				.findViewById(R.id.headerTextView);
		TextView descriptionTextView = (TextView) convertView
				.findViewById(R.id.descriptionTextView);

		if (objects[position].getImage().equals("default")) {
			imageView.setImageResource(R.drawable.medecine);
		} else {			
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
			Bitmap bitmap = BitmapFactory.decodeFile(objects[position].getImage(),options);
			imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 100, 70, true));
//			imageView.setImageBitmap(decodeSampledBitmapFromResource(
//					imageView.getResources(), objects[position].getImage(),
//					100, 100));
		}
		headerTextView.setText(objects[position].getName());
		
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		if (objects[position].getEndDate() >= calendar
				.getTimeInMillis()) {
		
		Date date = new Date(objects[position].getStartTime());
		SimpleDateFormat formatter = new SimpleDateFormat(
				"MM-dd HH:mm");
		String startTime = formatter.format(date);
		
		descriptionTextView.setText("Next dose: "+startTime);
		
		} else{
			descriptionTextView.setText(" No next doses(medicine duration ended)");
		}
		return convertView;
	}}

//	// image decoding for utilizing memory bitmap heap
//	public static Bitmap decodeSampledBitmapFromResource(Resources res,
//			String imagePath, int reqWidth, int reqHeight) {
//
//		// First decode with inJustDecodeBounds=true to check dimensions
//		final BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//
//		FileInputStream fis = null;
//		try {
//			fis = new FileInputStream(new File(imagePath));
//			// Bitmap img=BitmapFactory.decodeFile(imagePath);
//			Bitmap img = BitmapFactory.decodeStream(fis);
//			// If no file on external storage, look in internal storage
//			if (img == null) {
//				try {
//					// File filePath = .getFileStreamPath(imagePath);
//					FileInputStream fi = new FileInputStream(imagePath);
//					img = BitmapFactory.decodeStream(fi);
//				} catch (Exception ex) {
//					Log.e("getThumbnail() on internal storage", ex.getMessage());
//				}
//			}
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} finally {
//			try {
//				fis.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		// BitmapFactory.decodeResource(res, resId, options);
//
//		// Calculate inSampleSize
//		options.inSampleSize = calculateInSampleSize(options, reqWidth,
//				reqHeight);
//
//		// Decode bitmap with inSampleSize set
//		options.inJustDecodeBounds = false;
//		return BitmapFactory.decodeStream(fis, null, options);
//	}
//
//	public static int calculateInSampleSize(BitmapFactory.Options options,
//			int reqWidth, int reqHeight) {
//		// Raw height and width of image
//		final int height = options.outHeight;
//		final int width = options.outWidth;
//		int inSampleSize = 1;
//
//		if (height > reqHeight || width > reqWidth) {
//
//			final int halfHeight = height / 2;
//			final int halfWidth = width / 2;
//
//			// Calculate the largest inSampleSize value that is a power of 2 and
//			// keeps both
//			// height and width larger than the requested height and width.
//			while ((halfHeight / inSampleSize) > reqHeight
//					&& (halfWidth / inSampleSize) > reqWidth) {
//				inSampleSize *= 2;
//			}
//		}
//
//		return inSampleSize;
//	}
//
//}
