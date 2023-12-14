package com.example.bookmart;
import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import com.example.manager.CoinManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class SaleFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final int GALLERY_REQUEST_CODE = 1000;
    private Uri imageUri;
    private ImageButton imgGallery;
    private Button upload_Data, f_post;
    private StorageReference mStorageReference;
    private DatabaseReference mDataBaseReference;

    private EditText bookNameEditText;
    private EditText bookPriceEditText;
    private EditText bookAuthor;
    private EditText bookDescription;

    private RadioGroup radioGroup;
    private RadioButton radioButtonNew;
    private RadioButton radioButtonUsed;

    String condition;
    private Spinner spinner;

    private Enums.BookCategory selectedBookCategory;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale, container, false);

        ContentResolver contentResolver = requireContext().getContentResolver();

        imgGallery = view.findViewById(R.id.imageButton);
        upload_Data = view.findViewById(R.id.uploadData);

        bookNameEditText = view.findViewById(R.id.bookNameEditText);
        bookPriceEditText = view.findViewById(R.id.price);
        bookAuthor = view.findViewById(R.id.bookAuthorEditText);
        bookDescription = view.findViewById(R.id.DescriptionEditText);


        radioGroup = view.findViewById(R.id.radioGroup);
        radioButtonNew = view.findViewById(R.id.radioButtonNew);
        radioButtonUsed = view.findViewById(R.id.radioButtonUsed);
        f_post = view.findViewById(R.id.featurePost);

        spinner = view.findViewById(R.id.category_book);
        if (getActivity() != null) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.book_category, android.R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(this);
        }

        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
        mDataBaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonNew) {
                condition = "New";
            } else if (checkedId == R.id.radioButtonUsed) {
                condition = "Used";
            }
        });

        imgGallery.setOnClickListener(v -> pickImageFromGallery());
        upload_Data.setOnClickListener(v -> uploadPost(false));
        f_post.setOnClickListener(v -> uploadPost(true));



        return view;
    }

    private void pickImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            imageUri = data.getData();
            imgGallery.setImageURI(imageUri);
        }
    }

    public void uploadPost(boolean isFeatured) {
        if (imageUri != null) {
            StorageReference fileReference = mStorageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            String bookName = bookNameEditText.getText().toString();
                            String bookPrice = bookPriceEditText.getText().toString();
                            String author = bookAuthor.getText().toString();
                            String description = bookDescription.getText().toString();
                            String old_new_condition = condition;

                            // Get the current date and time
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                            String uploadDate = sdf.format(new Date());

                            // Verify if any field is empty
                            if (!bookName.isEmpty() && !bookPrice.isEmpty() && !author.isEmpty() && !description.isEmpty() && !uploadDate.isEmpty()) {
                                // If it's a featured post, show the confirmation dialog
                                if (isFeatured) {
                                    showFeaturedDialog();
                                } else {
                                    // If not a featured post, proceed with normal post
                                    postNormal(uploadDate, bookName, bookPrice, author,old_new_condition, description, downloadUrl);
                                }
                            } else {
                                if (getActivity() != null) {

                                    Toast.makeText(getActivity(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        if (getActivity() != null) {

                            Toast.makeText(getActivity(), "Fail to Upload Image", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            if (getActivity() != null) {

                Toast.makeText(getActivity(), "Please fill in all the Fields", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void postNormal(String uploadDate, String bookName, String bookPrice, String author,
                            String old_new_condition, String description, String downloadUrl) {
        ImageUpload upload = new ImageUpload(bookName, bookPrice, downloadUrl, author, description, old_new_condition, uploadDate, selectedBookCategory);
        String uploadId = mDataBaseReference.push().getKey();
        mDataBaseReference.child(uploadId).setValue(upload);
        if (getActivity() != null) {

            Toast.makeText(getActivity(), "Post Added Successfully", Toast.LENGTH_SHORT).show();
        }
        // Clear fields after posting
        clearFields();
    }

    private void clearFields() {
        // Clear all the input fields
        bookNameEditText.setText("");
        bookPriceEditText.setText("");
        bookAuthor.setText("");
        bookDescription.setText("");
        // Clear other fields as needed
    }
    private String getFileExtension(Uri uri) {

        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

//    AdapterView.OnitemSelected class Methods
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedItem = adapterView.getItemAtPosition(i).toString();

        // Set the selectedBookCategory based on the spinner item
        if (selectedItem.equals("Academic")) {
            Toast.makeText(getActivity(), "Academic is selected", Toast.LENGTH_SHORT).show();
            selectedBookCategory = Enums.BookCategory.ACADEMIC;
        } else if (selectedItem.equals("General")) {
            Toast.makeText(getActivity(), "General is selected", Toast.LENGTH_SHORT).show();
            selectedBookCategory = Enums.BookCategory.GENERAL;
        }
    }

//    Featured Post:


    private AlertDialog alertDialog;

    private void showFeaturedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false); // Set the dialog to be non-cancelable

        // Inflate your custom layout for the dialog
        View view = getLayoutInflater().inflate(R.layout.dialogue_confirm_feature, null);

        // Find views by ID
        TextView dialogTxt = view.findViewById(R.id.dialogtxt);
        Button btnConfirm = view.findViewById(R.id.btnconfirm);
        Button btnReject = view.findViewById(R.id.btnreject);

        dialogTxt.setText("Do you want to spend 5 coins to feature your post?");
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }

            // Set actions for the positive button
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoinManager coinManager = AppController.getInstance().getManager(CoinManager.class);
                if (coinManager.getTotalCoins() >= 5) {
                    coinManager.deductCoins(5);
                    if (getActivity() != null) {

                        Toast.makeText(getActivity(), "coinManager.deductCoins(5)", Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();
uploadPost(false);                    }
                } else {
                    if (getActivity() != null) {

                        alertDialog.dismiss();
                        Toast.makeText(getActivity(), "Not Enough Coins", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        // Set actions for the negative button
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        // Create the dialog
        builder.setView(view);
        alertDialog = builder.create();

        // Show the dialog
        alertDialog.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Dismiss the dialog if it is showing
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
