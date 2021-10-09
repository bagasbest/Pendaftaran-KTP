package com.pendaftaran.ktp.admin;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HomepageViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<HomepageModel>> listHomepage = new MutableLiveData<>();
    final ArrayList<HomepageModel> homepageModelArrayList = new ArrayList<>();

    private static final String TAG = HomepageViewModel.class.getSimpleName();

    public void setListHomepage() {
        homepageModelArrayList.clear();

        try {
            FirebaseFirestore
                    .getInstance()
                    .collection("registration")
                    .orderBy("status", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                HomepageModel model = new HomepageModel();

                                model.setAddress("" + document.get("address"));
                                model.setAge("" + document.get("age"));
                                model.setDocument((ArrayList<String>) document.get("document"));
                                model.setDp("" + document.get("dp"));
                                model.setEmail("" + document.get("email"));
                                model.setName("" + document.get("name"));
                                model.setNik("" + document.get("nik"));
                                model.setNoKK("" + document.get("noKK"));
                                model.setPhone("" + document.get("phone"));
                                model.setUid("" + document.get("uid"));
                                model.setStatus("" + document.get("status"));
                                model.setPos("" + document.get("pos"));
                                model.setSign("" + document.get("sign"));

                                homepageModelArrayList.add(model);
                            }
                            listHomepage.postValue(homepageModelArrayList);
                        } else {
                            Log.e(TAG, task.toString());
                        }
                    });
        } catch (Exception error) {
            error.printStackTrace();
        }
    }


    public LiveData<ArrayList<HomepageModel>> getHomepage() {
        return listHomepage;
    }


}
