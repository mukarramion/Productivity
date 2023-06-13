package com.example.productivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubjectFragment extends Fragment implements PdfAdapter.PdfItemClickListener {
    private TextView textView;
    private ImageView imageView;
    private String title;
    private static final String TAG = "PdfFragment";
    private static final String SHARED_PREFS_KEY = "MyAppPrefs";
    private static final String LAST_VERSION_KEY = "last_version";

    private RecyclerView recyclerView;
    private PdfAdapter pdfAdapter;

    private FirebaseStorage storage;
    private SharedPreferences sharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubjectFragment newInstance(String param1, String param2) {
        SubjectFragment fragment = new SubjectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subject, container, false);

        textView = view.findViewById(R.id.subject_header);
        imageView = view.findViewById(R.id.subject_back);
        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("title");

            // Use the received text in your fragment as needed
            textView.setText(title);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesFragment notesFragment = new NotesFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,notesFragment);
                fragmentTransaction.commit();
            }
        });
        recyclerView = view.findViewById(R.id.subject_RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pdfAdapter = new PdfAdapter(this);
        recyclerView.setAdapter(pdfAdapter);

        storage = FirebaseStorage.getInstance();
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS_KEY, getActivity().MODE_PRIVATE);

        String lastVersion = sharedPreferences.getString(LAST_VERSION_KEY, "");
        String currentVersion = getAppVersion();

        // Check if the app version has changed since the last update
        if (!currentVersion.equals(lastVersion)) {
            // Clear the existing PDF files from the storage directory
            clearPdfFilesDirectory();
            // Update the last version in shared preferences
            sharedPreferences.edit().putString(LAST_VERSION_KEY, currentVersion).apply();
        }

        loadPdfList(title);

        return view;
    }

    private String getAppVersion() {
        try {
            return getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void clearPdfFilesDirectory() {
        File pdfFilesDirectory = getActivity().getFilesDir();
        File[] files = pdfFilesDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    private void loadPdfList(String folderName) {
        StorageReference folderRef = storage.getReference().child(folderName);

        folderRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        List<PdfItem> pdfItems = new ArrayList<>();
                        for (StorageReference item : listResult.getItems()) {
                            String fileName = item.getName();
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Create a PdfItem object with the file name and download URL
                                    PdfItem pdfItem = new PdfItem(fileName, uri.toString());
                                    pdfItems.add(pdfItem);
                                    pdfAdapter.setPdfList(pdfItems);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Log.e(TAG, "Failed to get download URL", e);
                                    Toast.makeText(getActivity(), "Failed to get download URL", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e(TAG, "Failed to fetch PDF list", e);
                        Toast.makeText(getActivity(), "Failed to fetch PDF list", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onItemClick(PdfItem pdfItem) {
        String fileName = pdfItem.getFileName();
        File localFile = new File(getActivity().getFilesDir(), fileName);

        if (localFile.exists()) {
            // If the PDF file already exists locally, open it directly
            displayPdf(localFile);
        } else {
            // If the PDF file does not exist locally, download it from Firebase Storage
            downloadPdf(pdfItem, localFile);
        }
    }

    private void downloadPdf(PdfItem pdfItem, File localFile) {
        StorageReference pdfRef = storage.getReferenceFromUrl(pdfItem.getDownloadUrl());

        pdfRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // File download success, display the PDF
                        displayPdf(localFile);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e(TAG, "Failed to download PDF", e);
                        Toast.makeText(getActivity(), "Failed to download PDF", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayPdf(File pdfFile) {
        Uri pdfUri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", pdfFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "No PDF viewer app found", Toast.LENGTH_SHORT).show();
        }
    }

}