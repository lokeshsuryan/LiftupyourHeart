package com.liftupmyheart.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.liftupmyheart.model.GroupDetailDao;
import com.liftupmyheart.model.ServerResponseHeart;
import com.liftupmyheart.rest.ProjectRepository;

import java.util.List;

public class ViewGroupModel extends AndroidViewModel {

    public ViewGroupModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ServerResponseHeart<List<GroupDetailDao>>> getGroupList(String userId){
        return ProjectRepository.getInstance().getGroupRespone(userId);
    }
}
