package com.sathishkumar.mywardrobe.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.sathishkumar.mywardrobe.Adapters.ViewPagerAdapter_P;
import com.sathishkumar.mywardrobe.Events.ShuffleEvent;
import com.sathishkumar.mywardrobe.OnImageChangeListener;
import com.sathishkumar.mywardrobe.R;
import com.sathishkumar.mywardrobe.Utils;
import com.sathishkumar.mywardrobe.WardrobeApp;
import com.sathishkumar.mywardrobe.WardrobeItem;
import com.squareup.sqlbrite.BriteDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/*Created by Sathish 30-07-2020*/

public class PantFragment extends Fragment {

    @BindView(R.id.pant_view_pager)
    ViewPager viewPager;

    @BindView(R.id.btn_add_pant)
    ImageButton addPantBtn;

    @Inject
    BriteDatabase db;

    @BindView(R.id.addPantText)
    TextView textView;

    private int imageTask = 0;
    private String type = null;

    private ViewPagerAdapter_P adapter;
    private Subscription subscription;
    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;
    private Uri mImageCaptureUri;

    public static PantFragment newInstance() {
        return new PantFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        WardrobeApp.getComponent(getActivity()).inject(this);
        adapter = new ViewPagerAdapter_P(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pant, container, false);
    }

    @Subscribe
    public void onEvent(ShuffleEvent event) {
        if (adapter.getCount() > 1) {
            Random random = new Random();
            int i = random.nextInt(adapter.getCount()) + 1;
            while (i == adapter.getCurrentPosition()) {
                i = random.nextInt(adapter.getCount()) + 1;
            }
            viewPager.setCurrentItem(i);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ((OnImageChangeListener)getActivity()).onImageChange("pant",adapter.getCurrentItemId(viewPager.getCurrentItem()));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
        String query = WardrobeItem.QUERY_PANT;
        String table = WardrobeItem.TABLE;
        subscription = db.createQuery(table, query)
                .mapToList(WardrobeItem.MAPPER)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter);

        new Handler().postDelayed(() -> {
            if (adapter.getCount() == 0) {
                textView.setText(R.string.add_pant_image);
                textView.setVisibility(View.VISIBLE);
            } else {
                textView.setVisibility(View.GONE);
            }
        },100);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        subscription.unsubscribe();
    }

    @OnClick(R.id.btn_add_pant)
    public void addPant(View view) {
        type = getString(R.string.pant);
        selectImage(getString(R.string.add_pant));
    }

    private void selectImage(String msg) {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(msg);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utils.checkPermission(getContext());
                switch (item) {
                    case REQUEST_CAMERA:
                        imageTask = item;
                        if (result)
                            cameraIntent();
                        break;

                    case SELECT_FILE:
                        imageTask = item;
                        if (result)
                            galleryIntent();
                        break;
                    default:
                        dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                File photoFile = createImageFile();
                // Continue only if the File was successfully created
                if (photoFile != null) {

                    mImageCaptureUri = FileProvider.getUriForFile(getContext(), "com.sathishkumar.mywardrobe.fileprovider", photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
            } catch (Exception ex) {
                Log.i("Reg-Activity", "ex");
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = System.currentTimeMillis() + "";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",    /* suffix */
                storageDir      /* directory */
        );
    }

    private void performCrop() {
        CropImage.activity(mImageCaptureUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start((Activity) getContext(), this);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType(getString(R.string.set_image_type));
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Utils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (imageTask == REQUEST_CAMERA)
                    cameraIntent();
                else if (imageTask == SELECT_FILE)
                    galleryIntent();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK) {
            String realPath = "";
            try {
                if (requestCode == SELECT_FILE) {
                    realPath = Utils.getRealPathFromURI(getContext(), data.getData());
                    insertData(realPath);
                } else if (requestCode == REQUEST_CAMERA) {
                    performCrop();
                } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
                    try {
                        if (resultUri.getPath() != null) {
                            insertData(Utils.compressBitmap(new File(resultUri.getPath()), getContext()).getPath());
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error loading image", Toast.LENGTH_SHORT).show();
                    }
                }
                setCurrentItem();
            } catch (Exception e) {
                //Log.d("ShirtFrag",e.getMessage());
            }
        }
    }

    public void insertData(String data) throws UnsupportedEncodingException {

        db.insert(WardrobeItem.TABLE, new WardrobeItem.Builder()
                .id(System.currentTimeMillis())
                .type(type)
                .data(data)
                .build());
    }

    private void setCurrentItem(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(adapter.getCount());
            }
        }, 100);
    }
}
