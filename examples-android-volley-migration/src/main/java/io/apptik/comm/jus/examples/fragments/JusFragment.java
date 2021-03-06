package io.apptik.comm.jus.examples.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import io.apptik.comm.jus.ALog;
import io.apptik.comm.jus.AndroidJus;
import io.apptik.comm.jus.JusLog;
import io.apptik.comm.jus.Request;
import io.apptik.comm.jus.RequestListener;
import io.apptik.comm.jus.RequestQueue;
import io.apptik.comm.jus.error.JusError;
import io.apptik.comm.jus.examples.R;
import io.apptik.comm.jus.examples.TestObjectForGson;
import io.apptik.comm.jus.examples.jus.JusHelper;
import io.apptik.comm.jus.request.GsonRequest;
import io.apptik.comm.jus.request.ImageRequest;
import io.apptik.comm.jus.request.JSONArrayRequest;
import io.apptik.comm.jus.request.StringRequest;
import io.apptik.comm.jus.ui.ImageLoader;
import io.apptik.comm.jus.ui.NetworkImageView;

public class JusFragment extends Fragment {
	RequestQueue requestQueue;
	public static final String TAG = "MyTag";

	public JusFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment Volley.
	 */
	// TODO: Rename and change types and number of parameters
	public static JusFragment newInstance() {
		JusFragment fragment = new JusFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_jus, container, false);

		JusLog.MarkerLog.on();
		standardQueueStringRequest(v);
		customQueueImageRequest(v);
		networkImageViewRequest(v);
		jsonRequest(v);
		gsonRequest(v);

		// Inflate the layout for this fragment
		return v;
	}

	private void standardQueueStringRequest(View v) {

		final TextView mTextView = (TextView) v.findViewById(R.id.tv_string_request);

		// StringRequest with JUS with Standard RequestQueue
		// Instantiate the RequestQueue.
		requestQueue = AndroidJus.newRequestQueue(getContext());
		String url = "http://www.google.com";

		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url);
		stringRequest.setTag(TAG);

		// Add to RequestQueue
		requestQueue.add(stringRequest
				.addResponseListener(new RequestListener.ResponseListener<String>() {
					@Override
					public void onResponse(String response) {
						mTextView.setText("Response is: " + response.substring(0, 500));
					}
				})
				.addErrorListener(new RequestListener.ErrorListener() {
					@Override
					public void onError(JusError error) {
						mTextView.setText("That didn't work!");
					}
				}));

	}

	private void customQueueImageRequest(View v) {
		// ImageRequest with JUS for Android
		final ImageView imageView;
		String url = "http://i.imgur.com/7spzG.png";
		imageView = (ImageView) v.findViewById(R.id.iv_image_request);

		ImageRequest request = new ImageRequest(url, 0, 0, ImageView.ScaleType.CENTER, null);

		JusHelper.getInstance(v.getContext()).addToRequestQueue(request.addResponseListener(new RequestListener.ResponseListener<Bitmap>
				() {
			@Override
			public void onResponse(Bitmap response) {
				imageView.setImageBitmap(response);

			}
		}).addErrorListener(new RequestListener.ErrorListener() {
			@Override
			public void onError(JusError error) {
				imageView.setImageResource(R.drawable.ic_error);

			}
		}));

	}

	private void networkImageViewRequest(View v) {
		ImageLoader imageLoader;
		NetworkImageView networkImageView;
		String url = "https://developer.android.com/images/training/system-ui.png";
		// Get the NetworkImageView that will display the image.
		networkImageView = (NetworkImageView) v.findViewById(R.id.networkImageView);


		//set option request tag
		networkImageView.setRequestTag("ImageTag2");

		// Get the ImageLoader through your custom Helper.
		imageLoader = JusHelper.getInstance(v.getContext()).getImageLoader();

		// Set error image
		networkImageView.setErrorImageResId(R.drawable.ic_error);

		// Set the URL of the image that should be loaded into this view, and
		// specify the ImageLoader that will be used to make the request.
		networkImageView.setImageUrl(url, imageLoader);
	}


	private void jsonRequest(View v) {
		final TextView txtDisplay;
		txtDisplay = (TextView) v.findViewById(R.id.tv_jsonRequest);
		String url = "https://api.github.com/users/mralexgray/repos";

		JSONArrayRequest jsonArrayRequest = new JSONArrayRequest(Request.Method.GET, url);
		JusHelper.getInstance(v.getContext()).addToRequestQueue(jsonArrayRequest.addResponseListener(
				new RequestListener.ResponseListener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						try {
							txtDisplay.setText("Response: " + response.getJSONObject(0).toString()
									.substring(0, 50));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
		)).addErrorListener(new RequestListener.ErrorListener() {
			@Override
			public void onError(JusError error) {
				txtDisplay.setText("Error while loading Json: " + error.toString());

			}
		});
	}

	private void gsonRequest(View v) {
		final TextView txtDisplay;
		txtDisplay = (TextView) v.findViewById(R.id.tv_gsonRequest);

		String url = "https://api.github.com/users/mralexgray/repos";
		GsonRequest gsonRequest = new GsonRequest(Request.Method.GET, url, TestObjectForGson[]
				.class);

		JusHelper.getInstance(v.getContext()).addToRequestQueue(gsonRequest.addResponseListener(
				new RequestListener.ResponseListener<TestObjectForGson[]>() {
					@Override
					public void onResponse(TestObjectForGson[] response) {
						txtDisplay.setText("Response: " + response[0].getId());

					}
				}
		)).addErrorListener(new RequestListener.ErrorListener() {
			@Override
			public void onError(JusError error) {
				txtDisplay.setText("Error while loading Gson: " + error.toString());

			}
		});
	}

	@Override
	public void onStop() {
		super.onStop();
		if (requestQueue != null) {
			requestQueue.cancelAll(TAG);
		}
	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

	}

	@Override
	public void onDetach() {
		super.onDetach();
	}


}
