package com.ruzz.butilordering.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ruzz.butilordering.Model.DealsModel;
import com.ruzz.butilordering.Repository.DealsRepository;

import java.util.List;

public class DealsViewModel extends ViewModel {
    private final MutableLiveData<List<DealsModel>> dealsItems = new MutableLiveData<>();
    private final DealsRepository repository = new DealsRepository();

    public void setDeals() {
        List<DealsModel> deals = repository.getDealsFromFirebase();
       dealsItems.setValue(deals);
    }

    public LiveData<List<DealsModel>> getDeals() {
        return dealsItems;
    }
}
