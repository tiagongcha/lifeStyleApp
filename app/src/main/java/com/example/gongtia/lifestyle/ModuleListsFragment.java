package com.example.gongtia.lifestyle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ModuleListsFragment extends Fragment {
    private RecyclerView m_RecyclerView;
    private RecyclerView.Adapter m_Adaptor;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_module_lists, container, false);

        m_RecyclerView = view.findViewById(R.id.rcycV_List);
        m_RecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        m_RecyclerView.setLayoutManager(layoutManager);

        ArrayList<ModuleButton> buttons = new ArrayList<>();
        buttons.add(new ModuleButton(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_goal_all_black_24dp, null), "FITNESS"));
        buttons.add(new ModuleButton(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_goal_all_black_24dp, null), "HIKING"));
        buttons.add(new ModuleButton(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_goal_all_black_24dp, null), "Weather"));
        buttons.add(new ModuleButton(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_goal_all_black_24dp, null), "Placeholder"));


        m_Adaptor = new MyRVAdapter(buttons);

        if(!getResources().getBoolean(R.bool.isTablet)){
            m_RecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        }
        m_RecyclerView.setAdapter(m_Adaptor);
        return view;
    }
}
