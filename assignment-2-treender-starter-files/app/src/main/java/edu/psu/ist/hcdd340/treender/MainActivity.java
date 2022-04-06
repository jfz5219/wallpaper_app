package edu.psu.ist.hcdd340.treender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.imageview.ShapeableImageView;

/**
 * Data class representing a tree profile hi
 */
final class TreeProfile{
    private final String profileName;
    private final String profileID;
    private final String location;
    private final int profileImageID;

    TreeProfile(String profileName, String profileID, String location, int profileImageID) {
        this.profileName = profileName;
        this.profileID = profileID;
        this.location = location;
        this.profileImageID = profileImageID;
    }

    public int getProfileImageID() {
        return profileImageID;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getProfileID() {
        return profileID;
    }

    public String getLocation() {
        return location;
    }
}

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "TREENDER_ACTIVITY";

    private static final int[] ACTION_ICON_IDS = {
            R.id.rewindIcon,
            R.id.nopeIcon,
            R.id.boostIcon,
            R.id.likeIcon,
            R.id.superLikeIcon
    };

    private static final TreeProfile[] TREE_PROFILES = {
            new TreeProfile("Hosler Oak", "7863", "Arboretum", R.drawable.hosler_tree),
            new TreeProfile("Banana", "7864", "Esplanade in Arboretum", R.drawable.banana_tree),
            new TreeProfile("Hemlock", "7865", "Entry Walk in Arboretum", R.drawable.hemlock_tree),
            /*
            new TreeProfile("Santa Cruz lily", "7866", "Oasis Garden & Lotus Pool", R.drawable.santa_cruiz_tree),
            new TreeProfile("Flapjack", "7867", "Arboretum", R.drawable.flapjack_tree),
            new TreeProfile("Japanese Peony", "7868", "Rose & Fragrance Garden", R.drawable.japanese_peony_tree),
            new TreeProfile("Crab Apple", "7869", "Strolling Garden", R.drawable.crabapple_tree),
            new TreeProfile("Black Pine", "7870", "Strolling Garden", R.drawable.blackpine_tree),
            new TreeProfile("Hellebore", "7871", "Strolling Garden", R.drawable.hellebore_tree),
            new TreeProfile("False Spirea", "7872", "Strolling Garden", R.drawable.spirea_tree), */
    };

    private static int index = 0;

    private SharedPreferences sharedPreferences;
    private final static int DEFAULT_ICON_UNSELECTED = Integer.MIN_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Set event handler for icons
         */
        for (int i = 0; i < ACTION_ICON_IDS.length; i++) {
            ShapeableImageView icon = findViewById(ACTION_ICON_IDS[i]);
            icon.setOnClickListener(this);
        }
        /*
        Set up shared preferences
         */

        sharedPreferences = getSharedPreferences("Tree App", MODE_PRIVATE);

        showProfile(getCurrentProfile());

    }

    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (view.getId() == R.id.rewindIcon){
            // Must remove current icon preference before moving to previous profile.
            editor.remove(TREE_PROFILES[index].getProfileID());
            editor.commit();
            showProfile(moveToPreviousProfile());

        }
        else {
            // Must save icon preference before moving onto next profile.
            editor.putInt(TREE_PROFILES[index].getProfileID(), view.getId());
            editor.apply();
            showProfile(moveToNextProfile());

        }
    }


    private void showProfile(TreeProfile profile) {
        // update profile image
        ShapeableImageView idHolderImage = findViewById(R.id.imageTree);
        idHolderImage.setImageResource(profile.getProfileImageID());

        TextView name = findViewById(R.id.treeName);
        name.setText(profile.getProfileName());

        TextView id = findViewById(R.id.treeID);
        id.setText(profile.getProfileID());

        TextView location = findViewById(R.id.locationText);
        location.setText(profile.getLocation());

        //First, clear all background colors
        for (int i = 0; i < ACTION_ICON_IDS.length; i++) {
            ShapeableImageView icon = findViewById(ACTION_ICON_IDS[i]);
            icon.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        }

        //Retrieving saved preference, if none, then default will show
        final int defaultState = 0;
        int icon_id = sharedPreferences.getInt(profile.getProfileID(),defaultState);

        if(icon_id != 0){
            ShapeableImageView icon_image = findViewById(icon_id);
            icon_image.setBackgroundColor(ContextCompat.getColor(this, R.color.selected_icon));
        }
    }
    /**
     * Gets next tree profile by increasing the index by 1. Also, saves the index.
     */
    private TreeProfile moveToNextProfile() {
        index = (index + 1) % TREE_PROFILES.length;
        return TREE_PROFILES[index];
    }

    /**
     * Gets previous tree profile by decreasing the index by 1. Also, saves the index.
     */
    private TreeProfile moveToPreviousProfile() {
        index = index - 1;
        if (index < 0)
            index = TREE_PROFILES.length - 1;
        return TREE_PROFILES[index];
    }


    /**
     * Gets current profile
     */
    private TreeProfile getCurrentProfile() {
        return TREE_PROFILES[index];
    }


}