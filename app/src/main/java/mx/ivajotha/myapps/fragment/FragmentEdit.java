package mx.ivajotha.myapps.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import mx.ivajotha.myapps.Model.ModelAppList;
import mx.ivajotha.myapps.R;
import mx.ivajotha.myapps.sql.ItemDataSource;

/**
 * Created by jonathan on 14/07/16.
 */
public class FragmentEdit extends Fragment implements View.OnClickListener{

    private EditText editnameApp;
    private EditText editnameDev;
    private EditText editDetailsApp;
    private CheckBox editIsUpdate;
    private Integer idAppEdit;
    private Integer resourceId;
    private ItemDataSource itemDataSource;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit, container,false);

        /* Assign values to the fields*/
        itemDataSource = new ItemDataSource(getActivity());

        idAppEdit = getArguments().getInt("key_Id");

        resourceId = getArguments().getInt("key_resourceId");

        editnameApp = (EditText) view.findViewById(R.id.actEdit_appName);
        editnameApp.setText(getArguments().getString("key_nameApp"));

        editnameDev = (EditText) view.findViewById(R.id.actEdit_appNameDev);
        editnameDev.setText(getArguments().getString("key_nameDev"));

        editDetailsApp = (EditText) view.findViewById(R.id.actEdit_appDetails);
        editDetailsApp.setText(getArguments().getString("key_detailsApp"));

        editIsUpdate = (CheckBox) view.findViewById(R.id.actEdit_appUpdate);
        Boolean checked_  = getArguments().getInt("key_updateApp") == 0 ? false : true;
        editIsUpdate.setChecked(checked_);

        view.findViewById(R.id.actEdit_btnSaveEdit).setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {
        /* Validate empty fields */
        if (!TextUtils.isEmpty(editnameApp.getText().toString()) &&
                !TextUtils.isEmpty(editnameDev.getText().toString()) &&
                !TextUtils.isEmpty(editDetailsApp.getText().toString())){

            Integer appId_ = idAppEdit;
            String appName_ = editnameApp.getText().toString();
            String appNameDev_ = editnameDev.getText().toString();
            String appDetails_ = editDetailsApp.getText().toString();
            Integer appUpdated_ = editIsUpdate.isChecked() ? 1 : 0;

            ModelAppList modelAppList = new ModelAppList();
            modelAppList.id = appId_;
            modelAppList.name = appName_;
            modelAppList.nameDeveloper = appNameDev_;
            modelAppList.details = appDetails_;
            modelAppList.resourceId = resourceId;
            modelAppList.updated = appUpdated_;

            /* Save Changes BD */
            long respEditApp = itemDataSource.updateDataApp(modelAppList);
            if(respEditApp != -1){
                fragmentDetails fragmentDetails = mx.ivajotha.myapps.fragment.fragmentDetails.newInstance(modelAppList);
                getFragmentManager().beginTransaction().replace(R.id.fragmentApp, fragmentDetails).commit();
                Toast.makeText(getActivity(),R.string.msgOk_EditApp, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(),R.string.msgFail_EditApp, Toast.LENGTH_SHORT).show();
            }


        }else{
            Toast.makeText(getActivity(),R.string.msgEmptyViews, Toast.LENGTH_SHORT).show();
        }

    }

    /* Method set views */
    public static FragmentEdit newInstance(ModelAppList modelAppList) {
        
        Bundle args = new Bundle();
        FragmentEdit fragment = new FragmentEdit();

        args.putInt("key_Id", modelAppList.id);
        args.putString("key_nameApp",modelAppList.name);
        args.putString("key_nameDev",modelAppList.nameDeveloper);
        args.putString("key_detailsApp",modelAppList.details);
        args.putInt("key_updateApp",modelAppList.updated);
        args.putInt("key_resourceId",modelAppList.resourceId);
        fragment.setArguments(args);
        return fragment;
    }
}
