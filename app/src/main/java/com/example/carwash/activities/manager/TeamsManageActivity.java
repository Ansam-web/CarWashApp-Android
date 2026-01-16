package com.example.carwash.activities.manager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carwash.R;
import com.example.carwash.activities.BaseActivity;
import com.example.carwash.adapters.UniversalAdapter;
import com.example.carwash.database.TeamRepository;
import com.example.carwash.models.Team;
import com.example.carwash.utils.Helpers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TeamsManageActivity extends BaseActivity {

    private RecyclerView recyclerTeams;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    private FloatingActionButton fabAddTeam;
    private View layoutTeamForm;

    // Form fields
    private EditText etTeamName, etCarNumber, etCarPlate;
    private Button btnSaveTeam, btnCancelTeam;

    private UniversalAdapter<Team> adapter;
    private TeamRepository teamRepository;
    private Team currentTeam;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_manage);

        setupToolbar();
        initViews();
        setupRecyclerView();
        loadTeams();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.manage_teams);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        recyclerTeams = findViewById(R.id.recyclerTeams);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        fabAddTeam = findViewById(R.id.fabAddTeam);
        layoutTeamForm = findViewById(R.id.layoutTeamForm);

        // Form fields
        etTeamName = findViewById(R.id.etTeamName);
        etCarNumber = findViewById(R.id.etCarNumber);
        etCarPlate = findViewById(R.id.etCarPlate);
        btnSaveTeam = findViewById(R.id.btnSaveTeam);
        btnCancelTeam = findViewById(R.id.btnCancelTeam);

        teamRepository = new TeamRepository(this);

        // Listeners
        fabAddTeam.setOnClickListener(v -> showAddTeamForm());
        btnSaveTeam.setOnClickListener(v -> saveTeam());
        btnCancelTeam.setOnClickListener(v -> hideTeamForm());
    }

    private void setupRecyclerView() {
        // 1. Create a DiffUtil.ItemCallback for the Team class
        DiffUtil.ItemCallback<Team> teamDiffCallback = new DiffUtil.ItemCallback<Team>() {
            @Override
            public boolean areItemsTheSame(@NonNull Team oldItem, @NonNull Team newItem) {
                return oldItem.getTeamId().equals(newItem.getTeamId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Team oldItem, @NonNull Team newItem) {
                return oldItem.equals(newItem);
            }
        };

        // 2. Create the binder
        UniversalAdapter.ItemBinder<Team> teamBinder = (team, title, subtitle, extra) -> {
            title.setText(team.getName());
            subtitle.setText(team.getCarNumber());
            extra.setText(team.isAvailable() ? "Available" : "Busy");
        };

        // 3. Create the adapter instance
        adapter = new UniversalAdapter<>(teamBinder, teamDiffCallback);

        recyclerTeams.setLayoutManager(new LinearLayoutManager(this));
        recyclerTeams.setAdapter(adapter);

        adapter.setOnItemClickListener(team -> {
            showEditTeamForm(team);
        });
    }

    private void loadTeams() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerTeams.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.GONE);

        teamRepository.getAllTeams(new TeamRepository.OnTeamsLoadedListener() {
            @Override
            public void onTeamsLoaded(List<Team> teams) {
                progressBar.setVisibility(View.GONE);

                if (teams.isEmpty()) {
                    tvEmptyState.setVisibility(View.VISIBLE);
                } else {
                    recyclerTeams.setVisibility(View.VISIBLE);
                    adapter.submitList(teams);
                }
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                tvEmptyState.setVisibility(View.VISIBLE);
                showError(error);
            }
        });
    }

    private void showAddTeamForm() {
        isEditMode = false;
        currentTeam = null;
        clearForm();
        layoutTeamForm.setVisibility(View.VISIBLE);
        fabAddTeam.hide();
        btnSaveTeam.setText(R.string.add);
    }

    private void showEditTeamForm(Team team) {
        isEditMode = true;
        currentTeam = team;
        fillForm(team);
        layoutTeamForm.setVisibility(View.VISIBLE);
        fabAddTeam.hide();
        btnSaveTeam.setText(R.string.update);
    }

    private void hideTeamForm() {
        layoutTeamForm.setVisibility(View.GONE);
        fabAddTeam.show();
        clearForm();
    }

    private void clearForm() {
        etTeamName.setText("");
        etCarNumber.setText("");
        etCarPlate.setText("");
    }

    private void fillForm(Team team) {
        etTeamName.setText(team.getName());
        etCarNumber.setText(team.getCarNumber());
        etCarPlate.setText(team.getCarPlate());
    }

    private void saveTeam() {
        if (!validateTeam()) {
            return;
        }

        showLoading(isEditMode ? "Updating team..." : "Adding team...");

        String name = etTeamName.getText().toString().trim();
        String carNumber = etCarNumber.getText().toString().trim();
        String carPlate = etCarPlate.getText().toString().trim();

        if (isEditMode) {
            currentTeam.setName(name);
            currentTeam.setCarNumber(carNumber);
            currentTeam.setCarPlate(carPlate);

            teamRepository.updateTeam(currentTeam.getTeamId(), currentTeam,
                    new TeamRepository.OnTeamUpdatedListener() {
                        @Override
                        public void onTeamUpdated() {
                            hideLoading();
                            showToast("Team updated successfully");
                            hideTeamForm();
                            loadTeams();
                        }

                        @Override
                        public void onError(String error) {
                            hideLoading();
                            showError(error);
                        }
                    });
        } else {
            Team newTeam = new Team(name, carNumber, carPlate);

            teamRepository.addTeam(newTeam, new TeamRepository.OnTeamAddedListener() {
                @Override
                public void onTeamAdded(Team team) {
                    hideLoading();
                    showToast("Team added successfully");
                    hideTeamForm();
                    loadTeams();
                }

                @Override
                public void onError(String error) {
                    hideLoading();
                    showError(error);
                }
            });
        }
    }

    private boolean validateTeam() {
        if (Helpers.isEmpty(etTeamName.getText().toString())) {
            etTeamName.setError("Required");
            return false;
        }

        if (Helpers.isEmpty(etCarNumber.getText().toString())) {
            etCarNumber.setError("Required");
            return false;
        }

        if (Helpers.isEmpty(etCarPlate.getText().toString())) {
            etCarPlate.setError("Required");
            return false;
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
