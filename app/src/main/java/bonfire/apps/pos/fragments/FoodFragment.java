package bonfire.apps.pos.fragments;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import bonfire.apps.pos.R;
import bonfire.apps.pos.activities.MainActivity;
import bonfire.apps.pos.adapter.MenuItemAdapter;
import bonfire.apps.pos.database.ServerManager;
import bonfire.apps.pos.modules.DataModule;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 * Use the {@link FoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    boolean _areLecturesLoaded = false;
    boolean is_visible = false;

    int orientation;
    public static RecyclerView recyclerView;
    public List<DataModule> dataModules = new ArrayList<>();
    List<DataModule> list = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static String food = "/GetCategory";
    ProgressDialog pb;
    MenuItemAdapter adapter;
    private static String categoryType = "1";



    public FoodFragment() {
        // Required empty public constructor
    }

//    public FoodFragment(String categoryType){
//        this.categoryType = categoryType;
//            }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodFragment newInstance(String param1, String param2) {
        FoodFragment fragment = new FoodFragment();
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
        return inflater.inflate(R.layout.fragment_food, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.foodListRecycler);

        pb = new ProgressDialog(getContext());
        pb.setTitle("Loading Menu");
        pb.setMessage("Please Wait......");
        pb.setCancelable(false);

        //     Toast.makeText(getActivity(), categoryType, Toast.LENGTH_SHORT).show();

        recyclerView.hasFixedSize();
        orientation = getResources().getConfiguration().orientation;

        Display display = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        if (width > 750) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
                dataModules.clear();
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 6));
                dataModules.clear();
            }

        } else {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                dataModules.clear();
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
                dataModules.clear();
            }

        }


        //drink.clear();
        //   if (isInternetOn())
        //     if (is_visible && _areLecturesLoaded) {


        foodList();
        MainActivity.searchView.setQueryHint("Search");
        //MainActivity.searchView.setIconified(false);
//        MainActivity.searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                dataModules.clear();
//                foodList();
//
//                return true;
//            }
//        });

        MainActivity.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if (s.isEmpty()) {
                    dataModules.clear();
                    foodList();
                    //drink.clear();
                    drinkList();
                } else {
                    //     Toast.makeText(getContext(), "food" + s, Toast.LENGTH_SHORT).show();
                    //dataModules.clear();
                    List<DataModule> searchList = new ArrayList<>();
                    for (int j = 0; j < dataModules.size(); j++) {
                        if (dataModules.get(j).getItemName().toUpperCase().contains(s.toUpperCase())) {
                            searchList.add(dataModules.get(j));
                        }
                        MenuItemAdapter adapter = new MenuItemAdapter(getContext(), searchList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    dataModules.clear();
                    foodList(s);

                }

                return false;
            }
        });
        //      }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areLecturesLoaded) {
            is_visible = true;
            _areLecturesLoaded = true;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void drinkList() {
        dataModules.clear();
        RequestQueue rq;
        StringRequest sr;

     final List<DataModule> drink = new ArrayList<>();
        drink.clear();
        try{
        rq = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        sr = new StringRequest(Request.Method.POST, new ServerManager(getContext()).getServer() + food,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);

                            JSONArray jAry = object.getJSONArray("CategoryDetails");

                            for (int i = 0; i < jAry.length(); i++) {
                                JSONObject jo = jAry.getJSONObject(i);

                                DataModule m = new DataModule();
                                m.setItemID(jo.getInt("category_id"));
                                m.setItemImage(jo.getString("product_image"));
                                m.setItemName(jo.getString("categoryname"));
                                drink.add(m);

                            }

                            MenuItemAdapter adapter = new MenuItemAdapter(getContext(), drink);
                            DrinksFragment.drinksView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
              //  Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
//

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("CategoryType", "2");
                return map;
            }
        };
        rq.add(sr); } catch (Exception e) {
        e.printStackTrace();
    }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void foodList() {
        dataModules.clear();
        RequestQueue rq;
        StringRequest sr;

        //pb.show();
        try{
        rq = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        sr = new StringRequest(Request.Method.POST, new ServerManager(getContext()).getServer() + food,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        //  pb.dismiss();
                        //   Log.e("Toast",response);

                        try {
                            JSONObject object = new JSONObject(response);

                            JSONArray jAry = object.getJSONArray("CategoryDetails");

                            for (int i = 0; i < jAry.length(); i++) {
                                JSONObject jo = jAry.getJSONObject(i);

                                DataModule m = new DataModule();
                                m.setItemID(jo.getInt("category_id"));
                                m.setItemImage(jo.getString("product_image"));
                                m.setItemName(jo.getString("categoryname"));
                                dataModules.add(m);

                            }

                            adapter = new MenuItemAdapter(getContext(), dataModules);
                            recyclerView.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                //  pb.dismiss();
                //   Toast.makeText(getActivity(),error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
//

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("CategoryType", categoryType);
                return map;
            }
        };
        rq.add(sr);
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void foodList(final String s) {
        //pb.show();
        dataModules.clear();
        RequestQueue rq;
        StringRequest sr;

        try {
            rq = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
            sr = new StringRequest(Request.Method.POST, new ServerManager(getContext()).getServer() + food,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            //  pb.dismiss();
                            // Log.e("Toast",response);

                            try {
                                JSONObject object = new JSONObject(response);

                                JSONArray jAry = object.getJSONArray("CategoryDetails");

                                for (int i = 0; i < jAry.length(); i++) {
                                    JSONObject jo = jAry.getJSONObject(i);

                                    DataModule m = new DataModule();
                                    m.setItemID(jo.getInt("category_id"));
                                    m.setItemImage(jo.getString("product_image"));
                                    m.setItemName(jo.getString("categoryname"));
                                    String s1 = jo.getString("categoryname");

                                    if (s1.toUpperCase().contains(s.toUpperCase()))
                                        dataModules.add(m);

                                }

                                adapter = new MenuItemAdapter(getContext(), dataModules);
                                //    recyclerView.setAdapter(adapter);
                                DrinksFragment.drinksView.setAdapter(adapter);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", error.toString());
                    //  pb.dismiss();
                    //   Toast.makeText(getActivity(),error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
//

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("CategoryType", "2");
                    return map;
                }
            };
            rq.add(sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isInternetOn() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null)
            networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

}
